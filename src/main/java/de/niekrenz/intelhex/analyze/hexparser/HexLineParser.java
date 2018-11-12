package de.niekrenz.intelhex.analyze.hexparser;

import java.text.ParseException;
import java.util.Arrays;
import java.util.regex.Pattern;

import static de.niekrenz.intelhex.analyze.hexparser.RecordType.from;
import static java.lang.String.format;

public class HexLineParser {

    private static final Pattern LINE_PATTERN = Pattern.compile("^:(\\p{XDigit}\\p{XDigit}){5,260}$");

    public void parseLine(String line, ParseContext context) throws ParseException {
        String trimmed = line.trim();
        if (!LINE_PATTERN.matcher(trimmed).matches()) {
            throw new ParseException("Syntax error", 0);
        }
        LineContext lineContext = new LineContext();
        Arrays.stream(splitByEverySecondCharacter(trimmed))
                .map(hexValue -> Integer.parseInt(hexValue, 16))
                .forEach(value -> nextValue(value, context, lineContext));
    }

    private String[] splitByEverySecondCharacter(String trimmed) {
        return trimmed.substring(1).split("(?<=\\G..)"); // black regex magic
    }

    private void nextValue(int value, ParseContext context, LineContext lineContext) {
        lineContext.checksum.add(value);
        switch (lineContext.state) {
            case RECLEN:
                lineContext.dataLeft = value;
                lineContext.dataIndex = 0;
                lineContext.data = new int[value];
                lineContext.state = LineState.LOAD_OFFSET_HIGH;
                break;
            case LOAD_OFFSET_HIGH:
                lineContext.loadOffset = ((long) value) << 8;
                lineContext.state = LineState.LOAD_OFFSET_LOW;
                break;
            case LOAD_OFFSET_LOW:
                lineContext.loadOffset += (long) value;
                lineContext.state = LineState.RECTYPE;
                break;
            case RECTYPE:
                lineContext.recordType = from(value);
                if (lineContext.recordType.isNullAddress() && (lineContext.loadOffset != 0)) {
                    throw new IllegalStateException("Address was not 0");
                }
                lineContext.recordType.getExpectedDataLength().ifPresent(length -> {
                    if (length != lineContext.dataLeft) {
                        throw new IllegalStateException("Expected data length mismatch");
                    }
                });
                lineContext.state = (lineContext.dataLeft-- <= 0) ? LineState.CHKSUM : LineState.DATA;
                break;
            case DATA:
                lineContext.data[lineContext.dataIndex++] = value;
                lineContext.state = (lineContext.dataLeft-- <= 0) ? LineState.CHKSUM : LineState.DATA;
                break;
            case CHKSUM:
                if (!lineContext.checksum.isValid()) {
                    throw new IllegalStateException("Checksum error");
                }
                switch (lineContext.recordType) {
                    case DATA:
                        context.addData(context.address + lineContext.loadOffset, lineContext.data);
                        break;
                    case EOF:
                        break;
                    case EXTENDED_SEGMENT_ADDRESS:
                        context.address = (((long) lineContext.data[0]) << 12) | (((long) lineContext.data[1]) << 4);
                        break;
                    case START_SEGMENT_ADDRESS:
                        context.address = ((((long) lineContext.data[0]) << 12) | (((long) lineContext.data[1]) << 4))
                                | ((((long) lineContext.data[2]) << 8) | ((long) lineContext.data[3]));
                        break;
                    case EXTENDED_LINEAR_ADDRESS:
                        context.address = (((long) lineContext.data[0]) << 24) | (((long) lineContext.data[1]) << 16);
                        break;
                    case START_LINEAR_ADDRESS:
                        break;
                }
                break;
            default:
                throw new IllegalStateException(format("Illegal line context state '%s'", lineContext.state));
        }
    }

}
