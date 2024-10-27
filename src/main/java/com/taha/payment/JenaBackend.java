package com.taha.payment;

import org.apache.jena.query.Dataset;
import org.apache.jena.query.ReadWrite;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.tdb.TDBFactory;
import org.apache.jena.util.FileManager;
import org.springframework.stereotype.Component;

import javax.xml.crypto.Data;
@Component // Add this annotation

public class JenaBackend {
    private Dataset dataset;

    public JenaBackend() {
        dataset = TDBFactory.createDataset();
        loadOntology("/webs.owl");
    }

    private void loadOntology(String filePath){
        Model model = FileManager.get().loadModel(filePath);
        dataset.begin(ReadWrite.WRITE);
        dataset.addNamedModel("ecotourismOntology",model);
        dataset.commit();
        dataset.end();
    }
    public Dataset getDataset() {
        return dataset;
    }
}
