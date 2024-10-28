package com.taha.payment;

import org.apache.jena.query.Dataset;
import org.apache.jena.query.ReadWrite;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.tdb.TDBFactory;
import org.apache.jena.util.FileManager;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.InputStream;

@Component
public class JenaBackend {
    private final Dataset dataset;

    public JenaBackend() {
        dataset = TDBFactory.createDataset();
        loadOntology("/webs.ttl"); // Change to Turtle file
    }

    private void loadOntology(String filePath) {
        try (InputStream in = getClass().getResourceAsStream(filePath)) {
            if (in == null) {
                throw new FileNotFoundException("Ontology file not found: " + filePath);
            }

            // Load Turtle data
            Model model = FileManager.get().loadModel("file:src/main/resources" + filePath, null, "TURTLE");
            dataset.begin(ReadWrite.WRITE);
            dataset.addNamedModel("ecotourismOntology", model);
            System.out.println("Triples in the model: " + model.size());
            model.write(System.out, "TURTLE"); // Print the model in Turtle format
            dataset.commit();
        } catch (Exception e) {
            System.err.println("Error loading ontology file: " + e.getMessage());
            e.printStackTrace();
        } finally {
            dataset.end();
        }
    }

    public Dataset getDataset() {
        return dataset;
    }
}
