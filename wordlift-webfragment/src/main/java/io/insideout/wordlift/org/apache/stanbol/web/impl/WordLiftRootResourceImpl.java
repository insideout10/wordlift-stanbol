package io.insideout.wordlift.org.apache.stanbol.web.impl;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.stanbol.commons.web.base.resource.BaseStanbolResource;

import com.sun.jersey.api.view.Viewable;

@Path("/wordlift")
public class WordLiftRootResourceImpl extends BaseStanbolResource {

    @GET
    @Produces(MediaType.TEXT_HTML)
    public Viewable getView() {
        return new Viewable("index", this);
    }
}
