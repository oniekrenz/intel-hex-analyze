package de.niekrenz.intelhex.analyze.hexparser;

import de.niekrenz.intelhex.analyze.DataChunk;
import java.util.LinkedList;
import java.util.List;

import static java.lang.String.format;

public class ParseContext {

    public long address = 0;
    public List<DataChunk> dataChunks = new LinkedList<>();

    public List<DataChunk> getDataChunks() {
        return dataChunks;
    }

    public void addData(long address, int[] data) {
        DataChunk curr = new DataChunk(address, data);
        DataChunk prev = null;
        DataChunk next = null;
        int idxInsert = dataChunks.size();
        while (idxInsert >= 0) {
            prev = next;
            next = (idxInsert >= 1) ? dataChunks.get(idxInsert - 1) : null;
            idxInsert--;
            if ((next != null) && curr.isHigherThan(next)) {
                break;
            }
        }
        if ((prev != null) && curr.isOverlapping(prev)) {
            throw new IllegalStateException(format("Address range overlap %s %s", curr, prev));
        }
        if ((next != null) && curr.isAdjacentTo(next)) {
            dataChunks.remove(idxInsert);
            curr = next.merge(curr);
            idxInsert--;
        }
        if ((prev != null) && curr.isAdjacentTo(prev)) {
            dataChunks.remove(idxInsert + 1);
            curr = curr.merge(prev);
        }
        dataChunks.add(idxInsert + 1, curr);
    }
}
