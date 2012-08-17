package io.insideout.wordlift.org.apache.stanbol.enhancer.engines.freebase.impl.domain;

import java.util.Collection;

import com.google.gson.annotations.SerializedName;

public class FreebaseResult {
    /*
     * "mid": "/m/06c62", "name": "Roma", "notable": { "name": "Citt\u00e0/Cittadina/Villaggio", "id":
     * "/location/citytown" }, "lang": "it", "score": 37.547695 },
     */

    private String id;
    private String mid;
    private String name;
    private String language;
    @SerializedName("relevance:score") 
    private Double score;

    private Collection<FreebaseType> type;

    private FreebaseNotable notable;

    public FreebaseResult() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public Collection<FreebaseType> getType() {
        return type;
    }

    public void setType(Collection<FreebaseType> type) {
        this.type = type;
    }

    public FreebaseNotable getNotable() {
        return notable;
    }

    public void setNotable(FreebaseNotable notable) {
        this.notable = notable;
    }

    @Override
    public String toString() {
        return "FreebaseResult [id=" + id + ", mid=" + mid + ", name=" + name + ", language=" + language
               + ", score=" + score + ", type=" + type + ", notable=" + notable + "]";
    }

}
