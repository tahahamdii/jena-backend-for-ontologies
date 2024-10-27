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

    public ResultSet getEcoFriendlyDestinations(String preference, int ecoRatingThreshold) {
        // Define SPARQL query with parameters
        String sparqlQuery = """
            PREFIX ex: <http://example.org/eco-tourism#>
            SELECT ?destination ?name ?ecoRating
            WHERE {
                ?destination a ex:Destination ;
                            ex:name ?name ;
                            ex:ecoRating ?ecoRating ;
                            ex:category ?category .
                FILTER(?ecoRating >= """ + ecoRatingThreshold + """ 
                       && CONTAINS(?category, '""" + preference + """
            '))
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
