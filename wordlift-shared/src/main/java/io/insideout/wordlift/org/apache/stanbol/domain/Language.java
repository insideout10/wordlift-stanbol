package io.insideout.wordlift.org.apache.stanbol.domain;

public class Language {

    private String twoLetterCode;
    private Double rank;

    public Language(String twoLetterCode, Double rank) {
        this.twoLetterCode = twoLetterCode;
        this.rank = rank;
    }

    public String getTwoLetterCode() {
        return twoLetterCode;
    }

    public void setTwoLetterCode(String twoLetterCode) {
        this.twoLetterCode = twoLetterCode;
    }

    public Double getRank() {
        return rank;
    }

    public void setRank(Double rank) {
        this.rank = rank;
    }

}
