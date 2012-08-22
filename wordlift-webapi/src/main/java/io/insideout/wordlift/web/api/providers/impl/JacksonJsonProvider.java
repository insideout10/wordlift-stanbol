package io.insideout.wordlift.web.api.providers.impl;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Provider;

import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;

@Provider
@Consumes({MediaType.APPLICATION_JSON, "text/json"})
@Produces({MediaType.APPLICATION_JSON, "text/json"})
public class JacksonJsonProvider extends JacksonJaxbJsonProvider {

    /**
     * Creates a new instance.
     * 
     * @throws Exception
     */
    public JacksonJsonProvider() throws Exception {
        super();
        // this.objectMapper = new ObjectMapper();
        // setMapper(this.objectMapper);
    }

}