package io.insideout.wordlift.org.apache.stabol.enhancer.engines.schemaorg;

import static junit.framework.Assert.assertNotNull;

import java.net.URL;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hp.hpl.jena.rdf.model.InfModel;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.ResIterator;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.SimpleSelector;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.reasoner.Reasoner;
import com.hp.hpl.jena.reasoner.ReasonerRegistry;
import com.hp.hpl.jena.util.FileManager;
import com.hp.hpl.jena.util.PrintUtil;
import com.hp.hpl.jena.vocabulary.RDF;

public class TestSchemaOrg {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Test
    public void testProperties() {
        String singleResourceURI = "http://dbpedia.org/resource/Valentino_Rossi";

        String schemaResourcePath = "/dbpedia-mappings.rdf";
        String customMappingsResourcePath = "/custom-mappings.rdf";
        String dataResourcePath = "/data.rdf";

        /**
         * http://jena.apache.org/documentation/ontology/ OWL_MEM OWL full in-memory none OWL_MEM_TRANS_INF
         * OWL full in-memory transitive class-hierarchy inference; OWL_MEM_RULE_INF OWL full in-memory
         * rule-based reasoner with OWL rules OWL_MEM_MICRO_RULE_INF OWL full in-memory optimised rule-based
         * reasoner with OWL rules OWL_MEM_MINI_RULE_INF OWL full in-memory rule-based reasoner with subset of
         * OWL rules
         */

        URL schemaURL = getClass().getResource(schemaResourcePath);
        assertNotNull(schemaURL);

        URL customMappingsURL = getClass().getResource(customMappingsResourcePath);
        assertNotNull(customMappingsURL);

        URL dataURL = getClass().getResource(dataResourcePath);
        assertNotNull(dataURL);

        Model schema = FileManager.get().loadModel(schemaURL.toString());
        Model customMappings = FileManager.get().loadModel(customMappingsURL.toString());
        schema = schema.add(customMappings);
        assertNotNull(schema);

        Model data = FileManager.get().loadModel(dataURL.toString());

        Reasoner reasoner = ReasonerRegistry.getOWLMicroReasoner();
        reasoner = reasoner.bindSchema(schema);
        assertNotNull(reasoner);

        InfModel inferredModel = ModelFactory.createInfModel(reasoner, data);

        Resource resource = inferredModel.getResource(singleResourceURI);
        assertNotNull(resource);

        printSchemaOrgStatements(inferredModel, resource);

        // Property filterProperty = inferredModel.createProperty("http://schema.org/birthDate");
        //
        // printStatements(inferredModel, resource, filterProperty, null);

    }

    @Test
    public void testByTypes() {

        String schemaResourcePath = "/dbpedia-mappings.rdf";
        String dataResourcePath = "/data.rdf";

        /**
         * http://jena.apache.org/documentation/ontology/ OWL_MEM OWL full in-memory none OWL_MEM_TRANS_INF
         * OWL full in-memory transitive class-hierarchy inference; OWL_MEM_RULE_INF OWL full in-memory
         * rule-based reasoner with OWL rules OWL_MEM_MICRO_RULE_INF OWL full in-memory optimised rule-based
         * reasoner with OWL rules OWL_MEM_MINI_RULE_INF OWL full in-memory rule-based reasoner with subset of
         * OWL rules
         */

        URL schemaURL = getClass().getResource(schemaResourcePath);
        assertNotNull(schemaURL);

        URL dataURL = getClass().getResource(dataResourcePath);
        assertNotNull(dataURL);

        Model schema = FileManager.get().loadModel(schemaURL.toString());
        Model data = FileManager.get().loadModel(dataURL.toString());

        Reasoner reasoner = ReasonerRegistry.getOWLMicroReasoner();
        reasoner = reasoner.bindSchema(schema);

        InfModel inferredModel = ModelFactory.createInfModel(reasoner, data);

        String[] typeURIs = {"http://schema.org/Place", "http://schema.org/Product",
                             "http://schema.org/Person", "http://schema.org/Organization",
                             "http://schema.org/Event", "http://schema.org/CreativeWork",
                             "http://schema.org/MedicalEntity"};

        for (String typeURI : typeURIs)
            printResourcesOfType(inferredModel, typeURI);

        // Resource louisvilleResource = inferredModel
        // .getResource("http://dbpedia.org/resource/Louisville,_Kentucky");
        // assertNotNull(louisvilleResource);
        //
        // printStatements(inferredModel, louisvilleResource, RDF.type, null);
    }

    private void printResourcesOfType(Model model, String typeURI) {
        Resource typeResource = model.getResource(typeURI);
        assertNotNull(typeResource);

        logger.info("===== {} =====", typeURI);

        ResIterator resourcesIterator = model.listSubjectsWithProperty(RDF.type, typeResource);
        while (resourcesIterator.hasNext()) {
            Resource resource = resourcesIterator.nextResource();
            logger.info("[localName :: {}][URI :: {}]", resource.getLocalName(), resource.getURI());
            printSchemaOrgStatements(model, resource);
        }

        // OntModel ontologyModel = ModelFactory.createOntologyModel();
        // // OntModel m = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
        //
        // InputStream intputStream = getClass().getClassLoader().getResourceAsStream(ontologyResourcePath);
        // assertNotNull(intputStream);
        //
        // ontologyModel.read(intputStream, null);
        //
        // try {
        // intputStream.close();
        // } catch (IOException e) {
        // logger.error("An exception occured closing the input stream.", e);
        // }

    }

    public void printSchemaOrgStatements(Model m, Resource s) {
        for (StmtIterator i = m.listStatements(new SimpleSelector(s, null, (RDFNode) null) {
            public boolean selects(Statement s) {
                return s.getPredicate().getURI().startsWith("http://schema.org/");
            }
        }); i.hasNext();) {
            Statement stmt = i.nextStatement();
            if (stmt.getObject().canAs(Literal.class)) {
                Literal literal = stmt.getObject().as(Literal.class);
                logger.info(
                    "[ predicateURI :: {} ][ lexicalForm :: {} ][ language :: {} ]",
                    new Object[] {stmt.getPredicate().getURI(), literal.getLexicalForm(),
                                  literal.getLanguage()});
            } else if (stmt.getObject().canAs(Resource.class)) {

                Resource objectResource = stmt.getObject().as(Resource.class);
                logger.info("[ predicateURI :: {} ][ objectURI :: {} ]",
                    new Object[] {stmt.getPredicate().getURI(), objectResource.getURI()});
            } else {
                logger.info("non literal/non resource");
            }

            // System.out.println(" - " + PrintUtil.print(stmt));
        }
    }

    public void printStatements(Model m, Resource s, Property p, Resource o) {
        for (StmtIterator i = m.listStatements(s, p, o); i.hasNext();) {
            Statement stmt = i.nextStatement();
            System.out.println(" - " + PrintUtil.print(stmt));
        }
    }

}
