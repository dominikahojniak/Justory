package com.justory.backend.controller;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Locale;

@RestController
public class BooksLocationController {

    @GetMapping("/api/booksLocations")
    public ResponseEntity<String> getBooksLocation(
            @RequestParam double lat,
            @RequestParam double lng,
            @RequestParam int radius) {


        String overpassUrl = String.format(
                Locale.US,
                "https://overpass-api.de/api/interpreter?data=[out:json];"
                        + "("
                        + "node[\"shop\"=\"books\"](around:%d,%f,%f);"
                        + "node[\"amenity\"=\"library\"](around:%d,%f,%f);"
                        + ");"
                        + "out;",radius, lat, lng, radius, lat, lng);

        RestTemplate restTemplate = new RestTemplate();
        try {
            String response = restTemplate.getForObject(overpassUrl, String.class);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error during downloading data from Overpass API");
        }
    }
}
