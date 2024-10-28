package com.taha.payment.controller;

import com.taha.payment.services.EcoTourismService;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;

@RestController
@RequestMapping("/api/eco-tourism")
public class EcoTourismController {

    @Autowired
    private EcoTourismService ecoTourismService;

        @GetMapping("/destinations")
        public String getAllDestinations(
                ) {

            ResultSet results = ecoTourismService.getAllDestinations();

            // Convert ResultSet to JSON format for API response
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ResultSetFormatter.outputAsJSON(outputStream, results);

            return outputStream.toString();
        }
}
