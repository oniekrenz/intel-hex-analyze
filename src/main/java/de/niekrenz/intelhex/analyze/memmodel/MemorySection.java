package de.niekrenz.intelhex.analyze.memmodel;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class MemorySection {

    private String name;
    @JsonDeserialize(using = AddressDeserializer.class)
    private long begin;
    @JsonDeserialize(using = AddressDeserializer.class)
    private long end;

    public MemorySection() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getBegin() {
        return begin;
    }

    public void setBegin(long begin) {
        this.begin = begin;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }
}
