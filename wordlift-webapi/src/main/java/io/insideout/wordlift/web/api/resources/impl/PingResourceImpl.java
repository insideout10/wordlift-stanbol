package io.insideout.wordlift.web.api.resources.impl;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/ping")
public class PingResourceImpl {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getPong() {
        return "pong";
    }

}
