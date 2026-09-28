package minermetrics.java;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.LineNumberInputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class App
{
    public static void main( String[] args )
    {

        connectionCheck(args);
        for (int index = 0; index < args.length; index++) {
            Bitaxe instanceData = fetchData(args[index]);
        }

    }

    public static HttpResponse<String> instanceConnect(String ipv4){

        HttpResponse<String> response;
        String url = "http://" + ipv4 + "/api/system/info";

        HttpClient client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofSeconds(10))
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

            if (connection.statusCode() == 200) {
                System.out.println("Connection to succesfull " + arg);
            }
        }


        // prometheus check
    }


    public static Bitaxe fetchData(String ipv4){

        HttpResponse<String> response = instanceConnect(ipv4);
        Bitaxe bitaxeData;

        ObjectMapper mapper = new ObjectMapper();
        try {
            bitaxeData = mapper.readValue(response.body(), Bitaxe.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return bitaxeData;
    }

}
