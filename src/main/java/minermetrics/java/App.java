package minermetrics.java;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;

import io.prometheus.metrics.core.metrics.Gauge;
import io.prometheus.metrics.exporter.httpserver.HTTPServer;

public class App
{
    public static void main( String[] args )
    {

        connectionCheck(args);

        ArrayList<Bitaxe> instanceArray = new ArrayList<>();

        for (int index = 0; index < args.length; index++ ){
            Bitaxe instanceData = fetchData(args[index], index);

            instanceArray.add(instanceData);

        }

        prometheusExporter(instanceArray);
    }

    public static HttpResponse<String> instanceConnect(String ipv4){

        HttpResponse<String> response;
        String url = "http://" + ipv4 + "/api/system/info";

        HttpClient client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofSeconds(6))
                .GET()
                .build();

        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        return response;
    }

    public static void connectionCheck( String[] args ) {

        for (String arg : args) {
            HttpResponse<String> connection = instanceConnect(arg);

            if (connection.statusCode() != 200) {
                System.out.println("Error connecting to instance: " + arg);
            }
        }
        System.out.println("Connection checks succesfull.");
    }


    public static Bitaxe fetchData(String ipv4, int index){

        HttpResponse<String> response = instanceConnect(ipv4);
        Bitaxe bitaxeData;

        ObjectMapper mapper = new ObjectMapper();
        try {
            bitaxeData = mapper.readValue(response.body(), Bitaxe.class);
            bitaxeData.id = index;
            bitaxeData.instanceIpv4 = ipv4;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return bitaxeData;
    }


    public static void prometheusExporter(ArrayList<Bitaxe> instanceArray){


        for (Bitaxe instance:instanceArray){

            Gauge fanRPM = Gauge.builder()
                    .name("fanRpm" + instance.id)
                    .register();
            fanRPM.set(instance.fanrpm);

            Gauge hashRate = Gauge.builder()
                    .name("hashRate" + instance.id)
                    .register();
            hashRate.set(instance.hashRate);
        }

        HTTPServer server = null;
        try {
            server = HTTPServer.builder()
                    .port(9400)
                    .buildAndStart();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


    }

}
