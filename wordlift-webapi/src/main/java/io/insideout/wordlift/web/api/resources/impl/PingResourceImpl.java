package io.insideout.wordlift.web.api.resources.impl;

import io.insideout.wordlift.web.api.domain.impl.PingImpl;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/ping")
public class PingResourceImpl {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public PingImpl getPong() {
        return new PingImpl("PONG!");
    }

}
