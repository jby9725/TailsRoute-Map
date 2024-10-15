package com.project.tailsroute.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api")
public class PlacesController {

    private final String API_KEY = "googleMap_apikey";

    @GetMapping("/places")
    public ResponseEntity<String> getPlaces(
            @RequestParam String location,
            @RequestParam String radius,
            @RequestParam String type)
    {
        String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + location + "&radius=" + radius + "&type=" + type + "&key=" + API_KEY;

        System.out.println(url);

        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(url, String.class);

        return ResponseEntity.ok(response);
    }
}