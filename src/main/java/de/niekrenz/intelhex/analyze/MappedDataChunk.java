package de.niekrenz.intelhex.analyze;

public class MappedDataChunk extends DataChunk {

    private final String section;

    public MappedDataChunk(DataChunk dataChunk, String section) {
        super(dataChunk.address, dataChunk.data);
        this.section = section;
    }

    public String getSection() {
        return section;
    }
}
