package io.insideout.wordlift.org.apache.stanbol.enhancer.engines.schemaorg.impl;

import io.insideout.wordlift.org.apache.stanbol.enhancer.engines.schemaorg.SchemaOrgRefactorer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.ConfigurationPolicy;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hp.hpl.jena.graph.Graph;
import com.hp.hpl.jena.rdf.model.InfModel;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.ResIterator;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.SimpleSelector;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.reasoner.Reasoner;
import com.hp.hpl.jena.reasoner.ReasonerRegistry;
import com.hp.hpl.jena.reasoner.rulesys.GenericRuleReasoner;
import com.hp.hpl.jena.reasoner.rulesys.Rule;
import com.hp.hpl.jena.util.FileManager;
import com.hp.hpl.jena.vocabulary.RDF;

@Component(configurationFactory = true, policy = ConfigurationPolicy.REQUIRE, specVersion = "1.1", metatype = true, immediate = true)
@Service
public class SchemaOrgRefactorerImpl implements SchemaOrgRefactorer {

    @Property(value = "custom-mappings.rdf")
    private final static String CUSTOM_MAPPINGS_PATH = "io.insideout.wordlift.org.apache.stanbol.enhancer.engines.schemaorg.mappings.custom.path";

    @Property(value = "custom-rules.rules")
    private final static String CUSTOM_RULES_PATH = "io.insideout.wordlift.org.apache.stanbol.enhancer.engines.schemaorg.rules.custom.path";

    @Property(value = "dbpedia-mappings.rdf")
    private final static String DBPEDIA_MAPPINGS_PATH = "io.insideout.wordlift.org.apache.stanbol.enhancer.engines.schemaorg.mappings.dbpedia.path";

    @Property(value = {"http://schema.org/Place", "http://schema.org/GeoCoordinates",
                       "http://schema.org/Product", "http://schema.org/Person",
                       "http://schema.org/Organization", "http://schema.org/Event",
                       "http://schema.org/CreativeWork", "http://schema.org/MedicalEntity"})
    private final static String TYPE_URIS = "io.insideout.wordlift.org.apache.stanbol.enhancer.engines.schemaorg.type.uri";

    private final static String DEFAULT_CUSTOM_MAPPINGS_PATH = "custom-mappings.rdf";
    private final static String DEFAULT_CUSTOM_RULES_PATH = "custom-rules.rules";
    private final static String DEFAULT_DBPEDIA_MAPPINGS_PATH = "dbpedia-mappings.rdf";
    private final static String[] DEFAULT_TYPE_URIS = {"http://schema.org/Place",
                                                       "http://schema.org/GeoCoordinates",
                                                       "http://schema.org/Product",
                                                       "http://schema.org/Person",
                                                       "http://schema.org/Organization",
                                                       "http://schema.org/Event",
                                                       "http://schema.org/CreativeWork",
                                                       "http://schema.org/MedicalEntity"};

    private String customMappingsPath;
    private String customRulesPath;
    private String dbpediaMappingsPath;
    private String[] typeURIs;

    private Reasoner rulesReasoner;
    private Reasoner ontologyReasoner;

    private final static Map<String,String> prefixes;

    static {
        prefixes = new HashMap<String,String>();
        prefixes.put("sc", "http://schema.org/");
        prefixes.put("sq", "http://stanbol.apache.org/ontology/entityhub/query#");
    }

    // public SchemaOrgRefactorerImpl() {
    // Map<String,Object> properties = new HashMap<String,Object>(3);
    // properties.put(CUSTOM_MAPPINGS_PATH, DEFAULT_CUSTOM_MAPPINGS_PATH);
    // properties.put(CUSTOM_RULES_PATH, DEFAULT_CUSTOM_RULES_PATH);
    // properties.put(DBPEDIA_MAPPINGS_PATH, DEFAULT_DBPEDIA_MAPPINGS_PATH);
    // activate(properties);
    // }

    @Activate
    private void activate(Map<String,Object> properties) {
        //
        customMappingsPath = (String) properties.get(CUSTOM_MAPPINGS_PATH);
        customRulesPath = (String) properties.get(CUSTOM_RULES_PATH);
        dbpediaMappingsPath = (String) properties.get(DBPEDIA_MAPPINGS_PATH);

        logger.info(
            "The Schema.org Refactoring engine is being activated [ customMappingsPath :: {} ][ customRulesPath :: {} ][ schemaMappingsPath :: {} ].",
            new Object[] {customMappingsPath, customRulesPath, dbpediaMappingsPath});

        //
        typeURIs = (String[]) properties.get(TYPE_URIS);
        if (null == typeURIs || "".equals(typeURIs)) typeURIs = DEFAULT_TYPE_URIS;

        Model schema = FileManager.get().loadModel(dbpediaMappingsPath);
        Model customMappings = FileManager.get().loadModel(customMappingsPath);
        schema = schema.add(customMappings);

        List<Rule> rules = Rule.rulesFromURL(customRulesPath);

        rulesReasoner = new GenericRuleReasoner(rules);
        ontologyReasoner = ReasonerRegistry.getOWLMicroReasoner();
        ontologyReasoner = ontologyReasoner.bindSchema(schema);

    }

    public Graph processGraph(Graph graph, String languageTwoLetterCode) {
        Model data = ModelFactory.createModelForGraph(graph);
        Model outputModel = processModel(data, languageTwoLetterCode);
        return outputModel.getGraph();
    }

    private Model processModel(Model data, String languageTwoLetterCode) {
        InfModel rulesModel = ModelFactory.createInfModel(rulesReasoner, data);

        Model sourceModel = ModelFactory.createInfModel(ontologyReasoner, rulesModel);

        Model destinationModel = ModelFactory.createDefaultModel();
        destinationModel.setNsPrefixes(prefixes);

        for (String typeURI : typeURIs)
            addResourceOfType(sourceModel, typeURI, destinationModel, languageTwoLetterCode);

        return destinationModel;

    }

    private void addResourceOfType(Model sourceModel,
                                   String type,
                                   Model destinationModel,
                                   String languageTwoLetterCode) {

        Resource typeResource = sourceModel.createResource(type);
        ResIterator resourcesIterator = sourceModel.listSubjectsWithProperty(RDF.type, typeResource);

        while (resourcesIterator.hasNext()) {
            Resource resource = resourcesIterator.nextResource();
            destinationModel.add(resource, RDF.type, typeResource);
            addProperties(sourceModel, resource, languageTwoLetterCode, destinationModel);

        }
    }

    private void addProperties(Model sourceModel,
                               Resource subject,
                               final String languageTwoLetterCode,
                               Model destinationModel) {

        StmtIterator statementsIterator = sourceModel.listStatements(new SimpleSelector(subject, null,
                (RDFNode) null) {
            public boolean selects(Statement s) {
                if (s.getObject().isLiteral()
                    && (!languageTwoLetterCode.equals(s.getLanguage()) && !"".equals(s.getLanguage()))) {
                    return false;
                }

                String predicateURI = s.getPredicate().getURI();
                if (predicateURI.startsWith("http://schema.org/")) return true;
                if ("http://stanbol.apache.org/ontology/entityhub/query#score".equals(predicateURI)) return true;

                return false;
            }
        });

        while (statementsIterator.hasNext()) {
            Statement statement = statementsIterator.nextStatement();
            destinationModel.add(statement);
        }

    }

    @SuppressWarnings("unused")
    private final Logger logger = LoggerFactory.getLogger(getClass());
}
