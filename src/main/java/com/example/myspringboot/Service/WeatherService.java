package com.example.myspringboot.Service;

import com.example.myspringboot.api.response.WeatherResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WeatherService {

    @Value("${weather.api.key}")
    private String apiKey;

    public static final String API = "https://api.weatherstack.com/current?access_key=API_KEY&query=CITY";

    @Autowired
    private RestTemplate restTemplate;


    public WeatherResponse getWeather(String city){

        if(apiKey == null || apiKey.isEmpty())
        {
            throw new IllegalStateException("API key is not found or not put");
        }

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
