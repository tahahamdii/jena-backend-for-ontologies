package com.taha.payment;

import org.apache.jena.query.Dataset;
import org.apache.jena.query.ReadWrite;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.tdb.TDBFactory;
import org.apache.jena.util.FileManager;
import org.springframework.stereotype.Component;

import javax.xml.crypto.Data;
import java.io.FileNotFoundException;
import java.io.InputStream;

@Component // Add this annotation

public class JenaBackend {
    private Dataset dataset;

    public JenaBackend() {
        dataset = TDBFactory.createDataset();
        loadOntology("/webs.owl");
    }

    private void loadOntology(String filePath) {
        try (InputStream in = getClass().getResourceAsStream(filePath)) {
            if (in == null) {
                throw new FileNotFoundException("Ontology file not found: " + filePath);
            }
            Model model = FileManager.get().loadModel("file:src/main/resources" + filePath,null,"RDF/XML");
            dataset.begin(ReadWrite.WRITE);
            dataset.addNamedModel("ecotourismOntology", model);
            System.out.println("Number of triples loaded: " + model.size());

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
