package io.insideout.wordlift.org.apache.stanbol.enhancer.engines.freebase.impl.domain;

public class FreebaseNotable {

    private String name;
    private String id;

    public FreebaseNotable() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "FreebaseNotable [name=" + name + ", id=" + id + "]";
    }

}
