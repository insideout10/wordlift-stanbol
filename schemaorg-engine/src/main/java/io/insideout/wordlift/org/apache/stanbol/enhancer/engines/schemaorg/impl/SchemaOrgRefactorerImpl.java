package io.insideout.wordlift.org.apache.stanbol.enhancer.engines.schemaorg.impl;

import io.insideout.wordlift.org.apache.stanbol.enhancer.engines.schemaorg.SchemaOrgRefactorer;

import java.util.Map;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Service;

@Component
@Service
public class SchemaOrgRefactorerImpl implements SchemaOrgRefactorer {

    @Property(value = "")
    private final static String CUSTOM_MAPPINGS_PATH = "io.insideout.wordlift.org.apache.stanbol.enhancer.engines.schemaorg.mappings.custom.path";

    @Property(value = "")
    private final static String CUSTOM_RULES_PATH = "io.insideout.wordlift.org.apache.stanbol.enhancer.engines.schemaorg.rules.custom.path";

    @Property(value = "")
    private final static String DBPEDIA_MAPPINGS_PATH = "io.insideout.wordlift.org.apache.stanbol.enhancer.engines.schemaorg.mappings.dbpedia.path";

    private final static String DEFAULT_CUSTOM_MAPPINGS_PATH = "/custom-mappings.rdf";
    private final static String DEFAULT_CUSTOM_RULES_PATH = "/custom-rules.rules";
    private final static String DEFAULT_DBPEDIA_MAPPINGS_PATH = "/dbpedia-mappings.rdf";

    private String customMappingsPath;
    private String customRulesPath;
    private String dbpediaMappingsPath;

    @Activate
    private void activate(Map<String,Object> properties) {

    }
}
