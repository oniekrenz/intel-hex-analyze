package de.niekrenz.intelhex.analyze;

import de.niekrenz.intelhex.analyze.hexparser.HexParser;
import de.niekrenz.intelhex.analyze.memmodel.MemoryModel;
import de.niekrenz.intelhex.analyze.memmodel.MemoryModelResourceFinder;
import gnu.getopt.Getopt;
import gnu.getopt.LongOpt;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.Reader;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.util.List;

public class Main {

    private InputStream in = System.in;
    private PrintStream out = System.out;
    private MemoryModel memoryModel = null;

    private Main(Getopt options) throws FileNotFoundException {
        int c;
        while ((c = options.getopt()) != -1) {
            switch (c) {
                case 'f':
                    in = new FileInputStream(options.getOptarg());
                    break;
                case 'm':
                    memoryModel = new MemoryModelResourceFinder().open(options.getOptarg());
                    break;
                default:
                    throw new IllegalArgumentException("Unknown option " + (char) c);
            }
        }
    }

    public static void main(String[] args) {
        try {
            LongOpt[] longOpts = new LongOpt[] {
                    new LongOpt("file", LongOpt.REQUIRED_ARGUMENT, null, 'f'),
                    new LongOpt("memory map", LongOpt.REQUIRED_ARGUMENT, null, 'm')
            };
            Getopt options = new Getopt("intelHexParser", args, "f:m:", longOpts);
            new Main(options).run();
        } catch (Throwable t) {
            t.printStackTrace(System.err);
        }
    }

    private void run() throws IOException, ParseException {
        List<DataChunk> dataChunks;
        try (Reader reader = new InputStreamReader(in, Charset.forName("ASCII"))) {
            dataChunks = new HexParser(reader).parse();
        }
        List<MappedDataChunk> mappedDataChunks = new SectionMapper().map(dataChunks, memoryModel);
        new Reporter(out).report(mappedDataChunks);
    }
}
