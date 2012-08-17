package io.insideout.wordlift.org.apache.stanbol.enhancer.engines.freebase.impl.services;

import io.insideout.wordlift.org.apache.stanbol.enhancer.engines.freebase.impl.FreebaseMqlReadOptions;
import io.insideout.wordlift.org.apache.stanbol.enhancer.engines.freebase.impl.domain.FreebaseResult;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collection;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FreebaseMqlRead {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final String endPointURL = "https://www.googleapis.com/freebase/v1/mqlread";

    public Collection<FreebaseResult> read(String query) {
        return read(query, null);
    }

    public Collection<FreebaseResult> read(String query, FreebaseMqlReadOptions options) {

        if (null == query || query.isEmpty()) throw new RuntimeException("The query cannot be empty.");

        String responseBody = null;
        try {
            URIBuilder uriBuilder = new URIBuilder(endPointURL);
            uriBuilder.addParameter("query", query);
            if (null != options) options.addParametersToUriBuilder(uriBuilder);
            HttpGet httpGet = new HttpGet(uriBuilder.build());

            logger.info("Going to query Freebase [{}].", httpGet.getURI());

            HttpClient httpClient = new DefaultHttpClient();
            HttpResponse httpResponse = httpClient.execute(httpGet);
            HttpEntity httpEntity = httpResponse.getEntity();

            responseBody = EntityUtils.toString(httpEntity);
        } catch (URISyntaxException e) {
            logger.error("An exception [{}] occured:\n{}", new Object[] {e.getClass(), e.getMessage()}, e);
            throw new RuntimeException(e);
        } catch (ClientProtocolException e) {
            logger.error("An exception [{}] occured:\n{}", new Object[] {e.getClass(), e.getMessage()}, e);
            throw new RuntimeException(e);
        } catch (IOException e) {
            logger.error("An exception [{}] occured:\n{}", new Object[] {e.getClass(), e.getMessage()}, e);
            throw new RuntimeException(e);
        }

        System.out.println(responseBody);

        return null;
        // Gson gson = new Gson();
        // FreebaseResponse freebaseResponse = gson.fromJson(responseBody, FreebaseResponse.class);
        //
        // logger.info("Found [{}] result(s).", freebaseResponse.getResult().size());
        //
        // return freebaseResponse.getResult();

    }
}
