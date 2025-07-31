package com.example.myspringboot.api.response;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WeatherResponse {

    private Current current;

    @Getter
    @Setter
    public class Current {

        @JsonProperty("observation_time")
        private String observationTime;

        private int temperature;

        private int feelslike;
    }



}
