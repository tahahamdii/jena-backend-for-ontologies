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

    public ResultSet getAllDestinations() {
        // Define SPARQL query
        String sparqlQuery = """
            PREFIX ex: <http://www.semanticweb.org/lenovo/ontologies/2024/9/untitled-ontology-4#>
            SELECT ?destination ?name ?popularity ?ecoRating ?season ?impactScore
            WHERE {
                ?destination a ex:Destination .
                OPTIONAL {
                    ?destination ex:name ?name .
                    ?destination ex:popularity ?popularity .
                    ?destination ex:ecoRating ?ecoRating .
                    ?destination ex:season ?season .
                    ?destination ex:impactScore ?impactScore .
                }
            }""";

        Dataset dataset = jenaBackend.getDataset();
        dataset.begin(ReadWrite.READ);

        // Debug: Print the number of triples
        System.out.println("Number of triples in the model: " + dataset.getDefaultModel().size());

        Query query = QueryFactory.create(sparqlQuery);
        try (QueryExecution qexec = QueryExecutionFactory.create(query, dataset)) {
            ResultSet results = qexec.execSelect();

            while (results.hasNext()) {
                QuerySolution soln = results.nextSolution();
                String destination = soln.getResource("destination").toString();
                String name = soln.contains("name") ? soln.getLiteral("name").getString() : "N/A";
                int popularity = soln.contains("popularity") ? soln.getLiteral("popularity").getInt() : 0;
                int ecoRating = soln.contains("ecoRating") ? soln.getLiteral("ecoRating").getInt() : 0;
                String season = soln.contains("season") ? soln.getLiteral("season").getString() : "N/A";
                float impactScore = soln.contains("impactScore") ? soln.getLiteral("impactScore").getFloat() : 0.0f;

                System.out.println("Destination: " + destination +
                        ", Name: " + name +
                        ", Popularity: " + popularity +
                        ", Eco Rating: " + ecoRating +
                        ", Season: " + season +
                        ", Impact Score: " + impactScore);
            }

            return ResultSetFactory.copyResults(results);
        } finally {
            dataset.end();
        }
    }
}
