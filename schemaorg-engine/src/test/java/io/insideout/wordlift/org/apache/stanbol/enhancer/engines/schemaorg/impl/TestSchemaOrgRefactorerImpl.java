package io.insideout.wordlift.org.apache.stanbol.enhancer.engines.schemaorg.impl;

import static junit.framework.Assert.assertNotNull;

import java.net.URL;

import org.junit.Test;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.util.FileManager;

public class TestSchemaOrgRefactorerImpl {

	@Test
	public void test() {
		String dataResourcePath = "/data.rdf";

		URL dataURL = getClass().getResource(dataResourcePath);
		assertNotNull(dataURL);

		Model data = FileManager.get().loadModel(dataURL.toString());
		assertNotNull(data);

		SchemaOrgRefactorerImpl schemaOrgRefactorer = new SchemaOrgRefactorerImpl();
		assertNotNull(schemaOrgRefactorer);

		schemaOrgRefactorer.processGraph(data.getGraph(), "it", true);

	}

}
