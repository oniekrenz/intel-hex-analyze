package de.niekrenz.intelhex.analyze;

import de.niekrenz.intelhex.analyze.memmodel.MemoryModel;
import java.util.ArrayList;
import java.util.List;

public class SectionMapper {

    public List<MappedDataChunk> map(List<DataChunk> dataChunks, MemoryModel memoryModel) {
        List<MappedDataChunk> result = new ArrayList<>();
        for (DataChunk dataChunk : dataChunks) {
            result.add(new MappedDataChunk(dataChunk, "PROGRAM"));
        }
        return result;
    }
}
