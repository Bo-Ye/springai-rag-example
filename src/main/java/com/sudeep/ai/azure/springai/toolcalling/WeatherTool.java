package com.sudeep.ai.azure.springai.toolcalling;

import java.util.logging.Logger;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class WeatherTool {

    private RestClient restClient;
    private static final Logger LOGGER = Logger.getLogger(WeatherTool.class.getName());

    @Tool(description = "Get the weather for a given location")
    public String getWeather(String location) {
        // location is only used for logging. The coordinates are hardcoded in the URL
        LOGGER.info("getWeather tool was invoked with location: " + location);
        restClient = RestClient.builder().build();
        // URL to get the forecast for BOSTON, MA 

        String response = restClient.get().uri("https://api.weather.gov/gridpoints/BOX/71,90/forecast")
            .retrieve().body(String.class);
        LOGGER.info("Response from the weather API: " + response);
        return response;
        
    }

}
