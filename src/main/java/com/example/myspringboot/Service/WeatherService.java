package com.example.myspringboot.Service;

import com.example.myspringboot.api.response.WeatherResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class WeatherService {

    private static final String apiKey = "ef2d301f0065a38311206e78fc8756db";

    public static final String API = "https://api.weatherstack.com/current?access_key=API_KEY&query=CITY";

    @Autowired
    private RestTemplate restTemplate;


    public WeatherResponse getWeather(String city){

       String finalAPI = API.replace("CITY",city).replace("API_KEY",apiKey);


       /// Use this when send to POST call
//       String requestBody = " {\n" +
//               " \"userName\" : \"omkar\", \n" +
//               " \"password\" : \"12345\", \n" +
//               "}  ";
//
//       HttpEntity<String> httpEntity = new HttpEntity<>(requestBody);



       /// POST via java objects instead to postman, so use this httpEntity method          POST   httpEntity
       ResponseEntity<WeatherResponse> response = restTemplate.exchange(finalAPI, HttpMethod.GET, null, WeatherResponse.class);

       return response.getBody();
    }

}
