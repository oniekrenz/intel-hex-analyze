package de.niekrenz.intelhex.analyze.memmodel;

import java.util.List;

public class RootMemorySection extends MemorySection {

    public enum MemoryType {
        FLASH
    }

    private MemoryType type;
    private List<MemorySection> sections;

    public RootMemorySection() {
        super();
    }

    public MemoryType getType() {
        return type;
    }

    public void setType(MemoryType type) {
        this.type = type;
    }

    public List<MemorySection> getSections() {
        return sections;
    }

    public void setSections(List<MemorySection> sections) {
        this.sections = sections;
    }
}
