package de.niekrenz.intelhex.analyze.hexparser;

import de.niekrenz.intelhex.analyze.DataChunk;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.text.ParseException;
import java.util.List;

import static java.lang.String.format;

public class HexParser {


    private final Reader in;

    public HexParser(Reader in) {
        this.in = in;
    }

    public List<DataChunk> parse() throws IOException, ParseException {
        int lineNumber = 0;
        try (BufferedReader lineReader = new BufferedReader(in)) {
            ParseContext context = new ParseContext();
            HexLineParser lineParser = new HexLineParser();
            String line;
            while ((((line = lineReader.readLine())) != null) && !line.isEmpty()) {
                lineNumber++;
                lineParser.parseLine(line, context);
            }
            return context.dataChunks;
        } catch (IOException ioe) {
            throw ioe;
        } catch (Exception e) {
            throw (ParseException) new ParseException(format("Line %d: %s", lineNumber, e.getMessage()), lineNumber)
                    .initCause(e);
        }
    }
}
