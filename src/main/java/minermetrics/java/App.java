package minermetrics.java;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.HttpRetryException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpTimeoutException;
import java.time.Duration;
import java.util.ArrayList;
import io.prometheus.metrics.exporter.httpserver.HTTPServer;

public class App
{
    public static void main( String[] args )
    {
        int scrapeInterval = 15000;

        startExporter();

        while (true) {

            ArrayList<Bitaxe> instanceArray = new ArrayList<>();
            ArrayList<String> validInstances;

            validInstances = connectionCheck(args);

            for (int index = 0; index < validInstances.toArray().length; index++) {

                Bitaxe instanceData = scrapeData(validInstances.get(index), index);
                instanceArray.add(instanceData);
            }

            updateMetrics(instanceArray);

            try {
                Thread.sleep(scrapeInterval);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static HttpResponse<String> instanceConnect(String ipv4){

        int maxTimeOut = 3;

        HttpResponse<String> response;
        String url = "http://" + ipv4 + "/api/system/info";

        HttpClient client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofSeconds(maxTimeOut))
                .GET()
                .build();

        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        return response;
    }

    public static ArrayList<String> connectionCheck( String[] args ) {

        ArrayList<String> validInstances = new ArrayList<>();

        for (int index = 0; index < args.length; index++) {
            int maxTimeOut = 3;

            String url = "http://" + args[index] + "/api/system/info";

            HttpClient client = HttpClient.newBuilder()
                    .version(HttpClient.Version.HTTP_1_1)
                    .build();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .timeout(Duration.ofSeconds(maxTimeOut))
                    .GET()
                    .build();

            try {
                client.send(request, HttpResponse.BodyHandlers.ofString());
                validInstances.add(args[index]);
            }  catch (HttpRetryException | HttpTimeoutException e) {
                System.out.println("Time-out at " + args[index]);
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        return validInstances;
    }


    public static Bitaxe scrapeData(String ipv4, int index){

        HttpResponse<String> response = instanceConnect(ipv4);
        Bitaxe bitaxeData;

        ObjectMapper mapper = new ObjectMapper();

        try {
            bitaxeData = mapper.readValue(response.body(), Bitaxe.class);
            bitaxeData.id = index;
            bitaxeData.instanceIpv4 = ipv4;
            bitaxeData.joulesPerTerahash = bitaxeData.getJoulesPerTerahash();

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return bitaxeData;
    }

    public static void startExporter() {
        try {
            HTTPServer.builder()
                    .port(9400)
                    .buildAndStart();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public static void updateMetrics(ArrayList<Bitaxe> instanceArray) {

        for (Bitaxe instance : instanceArray) {
            String id = String.valueOf(instance.id);
            BitaxeGauges.boardVersion.labelValues(id).set(instance.boardVersion);
            BitaxeGauges.frequency.labelValues(id).set(instance.frequency);
            BitaxeGauges.coreVoltage.labelValues(id).set(instance.coreVoltage);
            BitaxeGauges.coreVoltageActual.labelValues(id).set(instance.coreVoltageActual);
            BitaxeGauges.tempTarget.labelValues(id).set(instance.tempTarget);
            BitaxeGauges.fanSpeed.labelValues(id).set(instance.fanSpeed);
            BitaxeGauges.fanRpm.labelValues(id).set(instance.fanrpm);
            BitaxeGauges.hashRate.labelValues(id).set(instance.hashRate);
            BitaxeGauges.temp.labelValues(id).set(instance.temp);
            BitaxeGauges.power.labelValues(id).set(instance.power);
            BitaxeGauges.uptimeSeconds.labelValues(id).set(instance.uptimeSeconds);
            BitaxeGauges.joulesPerTerahash.labelValues(id).set(instance.joulesPerTerahash);
        }
    }
}

