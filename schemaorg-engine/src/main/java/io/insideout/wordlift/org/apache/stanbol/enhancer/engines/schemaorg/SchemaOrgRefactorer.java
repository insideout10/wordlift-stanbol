package io.insideout.wordlift.org.apache.stanbol.enhancer.engines.schemaorg;

import com.hp.hpl.jena.graph.Graph;

public interface SchemaOrgRefactorer {

	public Graph processGraph(Graph graph, String languageTwoLetterCode,
			Boolean filterLanguage);

}
