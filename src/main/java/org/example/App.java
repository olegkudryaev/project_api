package org.example;

import org.json.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://onlinesim.ru/api/getFreeCountryList"))
                .build();
        HttpResponse<String> response = null;
        try {
            response = client.send(request,
                    HttpResponse.BodyHandlers.ofString());

            Map<String, Integer> map = new HashMap<>();

            String jsonString = response.body();
            JSONObject obj = new JSONObject(jsonString);
            JSONArray arr = obj.getJSONArray("countries");
            for (int i = 0; i < arr.length(); i++) {
                Integer country = arr.getJSONObject(i).getInt("country");
                String country_text = arr.getJSONObject(i).getString("country_text");
                map.put(country_text, country);
            }
            for (Map.Entry<String, Integer> entry : map.entrySet()) {
                HttpRequest request2 = HttpRequest.newBuilder()
                        .uri(URI.create("https://onlinesim.ru/api/getFreePhoneList?country=" + entry.getValue()))
                        .build();
                HttpResponse<String> response2 = null;
                response2 = client.send(request2,
                        HttpResponse.BodyHandlers.ofString());
                String jsonString2 = response2.body();
                JSONObject obj2 = new JSONObject(jsonString2);

                JSONArray arr2 = obj2.getJSONArray("numbers");
                System.out.println(entry.getKey());
                for (int i = 0; i < arr2.length(); i++) {
                    String number = arr2.getJSONObject(i).getString("number");
                    System.out.println(number);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


}