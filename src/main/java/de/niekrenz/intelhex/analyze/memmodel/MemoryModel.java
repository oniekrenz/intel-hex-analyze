package de.niekrenz.intelhex.analyze.memmodel;

import java.util.List;

public class MemoryModel {

    private List<String> aliases;
    private List<RootMemorySection> memory;

    public MemoryModel() {
    }

    public List<String> getAliases() {
        return aliases;
    }

    public void setAliases(List<String> aliases) {
        this.aliases = aliases;
    }

    public List<RootMemorySection> getMemory() {
        return memory;
    }

    public void setMemory(List<RootMemorySection> memory) {
        this.memory = memory;
    }
}
