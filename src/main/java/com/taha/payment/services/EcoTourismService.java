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
                    SELECT ?name
                    WHERE {
                        ex:ecoDestination1 ex:name ?name .
                    }
                    
    """;

        Dataset dataset = jenaBackend.getDataset();
        dataset.begin(ReadWrite.READ);

        Query query = QueryFactory.create(sparqlQuery);
        try (QueryExecution qexec = QueryExecutionFactory.create(query, dataset)) {
            ResultSet results = qexec.execSelect();

            while (results.hasNext()) {
                QuerySolution soln = results.nextSolution();
                String destination = soln.getResource("destination").toString();
                String name = soln.contains("name") ? soln.getLiteral("name").getString() : "N/A";

                System.out.println("Destination: " + destination + ", Name: " + name + ", Eco Rating: " );
            }

            return ResultSetFactory.copyResults(results); // Make a copy to use outside the transaction
        }

    }

}
