package de.niekrenz.intelhex.analyze;

import java.io.PrintStream;
import java.util.List;

import static java.lang.String.format;

public class Reporter {

    private final PrintStream out;

    public Reporter(PrintStream out) {
        this.out = out;
    }

    public void report(List<MappedDataChunk> dataChunks) {
        out.println("Data");
        for (MappedDataChunk dataChunk : dataChunks) {
            out.println(format("[%08X-%08X] %s", dataChunk.getLowAddress(), dataChunk.getHighAddress(),
                    dataChunk.getSection()));
        }
    }
}
