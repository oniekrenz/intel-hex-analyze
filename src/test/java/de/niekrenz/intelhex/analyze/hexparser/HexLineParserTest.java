package de.niekrenz.intelhex.analyze.hexparser;

import java.text.ParseException;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(JUnitParamsRunner.class)
public class HexLineParserTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @InjectMocks
    private HexLineParser hexLineParser;

    private ParseContext ctx = new ParseContext();

    @Test
    @Parameters({
            "",
            ":000000000",
            ":00000000",
            ":000000000g",
    })
    public void shouldThrowParseErrorOnInvalidLine(String line) throws ParseException {
        // when
        ParseContext parseContextNotNeeded = null;

        // then
        thrown.expect(ParseException.class);

        // when
        hexLineParser.parseLine(line, parseContextNotNeeded);
    }

    @Test
    public void shouldParseLine() throws ParseException {
        //hexLineParser.parseLine(":00010230a0ff", null);
    }

    @Test
    @Parameters({
            ":020000040000FA, 0",
            ":020000040001F9, 65536",
            ":020000047EEF8D, 2129592320"
    })
    public void shouldParseExtendedLinearAddress(String line, long expAddress) throws ParseException {
        // given
        ParseContext ctx = new ParseContext();

        // when
        hexLineParser.parseLine(line, ctx);

        // then
        assertThat(ctx.address, is(expAddress));
    }

    @Test
    public void shouldParseData() throws ParseException {
        hexLineParser.parseLine(":1000000058080020D5000000EF000000F1000000BB", ctx);
    }

    @Test
    public void shouldParseData2() throws ParseException {
        hexLineParser.parseLine(":04000005000000C136", ctx);
    }

    @Test
    public void shouldParseEof() throws ParseException {
        hexLineParser.parseLine(":00000001FF", ctx);
    }
}