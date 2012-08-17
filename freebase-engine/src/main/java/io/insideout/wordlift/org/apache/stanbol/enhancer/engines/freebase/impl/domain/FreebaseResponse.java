package io.insideout.wordlift.org.apache.stanbol.enhancer.engines.freebase.impl.domain;

import java.util.Collection;

public class FreebaseResponse {

    private String status;
    private Collection<FreebaseResult> result;

    public FreebaseResponse() {}

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Collection<FreebaseResult> getResult() {
        return result;
    }

    public void setResult(Collection<FreebaseResult> result) {
        this.result = result;
    }

}
