package io.insideout.wordlift.org.apache.stanbol.domain;

public class Noun {

    private String word;
    private long start;
    private long end;
    private Double confidence;

    public Noun(String word, long start, long end, Double confidence) {
        this.word = word;
        this.start = start;
        this.end = end;
        this.confidence = confidence;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }

    public Double getConfidence() {
        return confidence;
    }

    public void setConfidence(Double confidence) {
        this.confidence = confidence;
    }

}
