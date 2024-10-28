package com.taha.payment.services;

import com.taha.payment.JenaBackend;
import org.apache.jena.query.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EcoTourismService {

    private final JenaBackend jenaBackend;

    @Autowired
    public EcoTourismService(JenaBackend jenaBackend) {
        this.jenaBackend = jenaBackend;
    }

    public ResultSet getEcoFriendlyDestinations() {
        // Define SPARQL query without any filter
        String sparqlQuery = """
    PREFIX ex: <http://www.semanticweb.org/lenovo/ontologies/2024/9/untitled-ontology-4#>
    SELECT ?destination ?name ?ecoRating
    WHERE {
        ?destination a ex:Destination ;
                    ex:name ?name ;
                    ex:ecoRating ?ecoRating .
    }
    """;


        Dataset dataset = jenaBackend.getDataset();
        dataset.begin(ReadWrite.READ);

        Query query = QueryFactory.create(sparqlQuery);
        try (QueryExecution qexec = QueryExecutionFactory.create(query, dataset)) {
            ResultSet results = qexec.execSelect();
            return ResultSetFactory.copyResults(results); // Make a copy to use outside the transaction
        } finally {
            dataset.end();
        }
    }

}
