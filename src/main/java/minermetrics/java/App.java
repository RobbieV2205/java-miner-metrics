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
        int scrapeInterval = 15000;

        connectionCheck(args);
        startExporter();

        while (true) {

            ArrayList<Bitaxe> instanceArray = new ArrayList<>();

            for (int index = 0; index < args.length; index++) {

                Bitaxe instanceData = scrapeData(args[index], index);
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

    public static void connectionCheck( String[] args ) {

        for (String arg : args) {
            HttpResponse<String> connection = instanceConnect(arg);

            if (connection.statusCode() != 200) {
                System.out.println("Error connecting to instance: " + arg);
            }
        }
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

    private static final Gauge boardVersion = Gauge.builder()
            .name("bitaxeboardVersion")
            .help("board version of the bitaxe")
            .labelNames("instance")
            .register();

    private static final Gauge frequency = Gauge.builder()
            .name("bitaxefrequency")
            .help("Frequency setting of the bitaxe")
            .labelNames("instance")
            .register();

    private static final Gauge coreVoltage = Gauge.builder()
            .name("bitaxecoreVoltage")
            .help("Voltage setting of the bitaxe")
            .labelNames("instance")
            .register();

    private static final Gauge coreVoltageActual = Gauge.builder()
            .name("bitaxecoreVoltageActual")
            .help("Actual core voltage of the bitaxe")
            .labelNames("instance")
            .register();

    private static final Gauge tempTarget = Gauge.builder()
            .name("bitaxetempTarget")
            .help("Temprature setting of the bitaxe")
            .labelNames("instance")
            .register();

    private static final Gauge fanSpeed = Gauge.builder()
            .name("bitaxefanSpeed")
            .help("fan speed setting of the bitaxe")
            .labelNames("instance")
            .register();

    private static final Gauge fanRpm = Gauge.builder()
            .name("bitaxeFanRpm")
            .help("Fan speed in RPM")
            .labelNames("instance")
            .register();

    private static final Gauge hashRate = Gauge.builder()
            .name("bitaxeHashRate")
            .help("Current hashrate")
            .labelNames("instance")
            .register();

    private static final Gauge temp = Gauge.builder()
            .name("bitaxetemp")
            .help("Temprature of the bitaxe")
            .labelNames("instance")
            .register();

    private static final Gauge power = Gauge.builder()
            .name("bitaxepower")
            .help("power consumption of the bitaxe")
            .labelNames("instance")
            .register();

    private static final Gauge uptimeSeconds = Gauge.builder()
        .name("bitaxeuptimeSeconds")
            .help("Uptime in seconds")
            .labelNames("instance")
            .register();

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
        fanRpm.clear();
        hashRate.clear();

        for (Bitaxe instance : instanceArray) {
            String id = String.valueOf(instance.id);
            boardVersion.labelValues(id).set(instance.boardVersion);
            frequency.labelValues(id).set(instance.frequency);
            coreVoltage.labelValues(id).set(instance.coreVoltage);
            coreVoltageActual.labelValues(id).set(instance.coreVoltageActual);
            tempTarget.labelValues(id).set(instance.tempTarget);
            fanSpeed.labelValues(id).set(instance.fanSpeed);
            fanRpm.labelValues(id).set(instance.fanrpm);
            hashRate.labelValues(id).set(instance.hashRate);
            temp.labelValues(id).set(instance.temp);
            power.labelValues(id).set(instance.power);
            uptimeSeconds.labelValues(id).set(instance.uptimeSeconds);
        }
    }
}

