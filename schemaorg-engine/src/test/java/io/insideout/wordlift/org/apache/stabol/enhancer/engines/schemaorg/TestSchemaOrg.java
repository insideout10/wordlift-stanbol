package io.insideout.wordlift.org.apache.stabol.enhancer.engines.schemaorg;

import static junit.framework.Assert.assertNotNull;

import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.ModelFactory;

public class TestSchemaOrg {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Test
    public void test() {

        String ontologyResourcePath = "dbpedia-mappings.rdf";

        /**
         * http://jena.apache.org/documentation/ontology/ OWL_MEM OWL full in-memory none OWL_MEM_TRANS_INF
         * OWL full in-memory transitive class-hierarchy inference; OWL_MEM_RULE_INF OWL full in-memory
         * rule-based reasoner with OWL rules OWL_MEM_MICRO_RULE_INF OWL full in-memory optimised rule-based
         * reasoner with OWL rules OWL_MEM_MINI_RULE_INF OWL full in-memory rule-based reasoner with subset of
         * OWL rules
         */
        OntModel ontologyModel = ModelFactory.createOntologyModel();
        // OntModel m = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);

        InputStream intputStream = getClass().getClassLoader().getResourceAsStream(ontologyResourcePath);
        assertNotNull(intputStream);

        ontologyModel.read(intputStream, null);

        try {
            intputStream.close();
        } catch (IOException e) {
            logger.error("An exception occured closing the input stream.", e);
        }

    }
}
