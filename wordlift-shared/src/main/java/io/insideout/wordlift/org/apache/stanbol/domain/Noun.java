package io.insideout.wordlift.org.apache.stanbol.domain;

/**
 * This class represents a noun found in a text with a confidence score.
 * @author David Riccitelli
 */
public class Noun {

    private String word;
    private long start;
    private long end;
    private Double confidence;

    /**
     * Creates a new Noun instance with the provided values.
     * @param word The textual word.
     * @param start The start position in the text.
     * @param end The end position in the text.
     * @param confidence The confidence score.
     */
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
