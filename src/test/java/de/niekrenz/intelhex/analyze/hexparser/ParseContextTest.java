package de.niekrenz.intelhex.analyze.hexparser;

import de.niekrenz.intelhex.analyze.DataChunk;
import java.util.List;
import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

@SuppressWarnings("unchecked")
public class ParseContextTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private ParseContext ctx = new ParseContext();

    @Test
    public void addFirst() {
        ctx.addData(20, new int[] {20, 21, 22, 23});

        List<DataChunk> result = ctx.getDataChunks();
        assertThat(result, contains(isData(20, 4)));
    }

    @Test
    public void addSecondAfterFirst() {
        ctx.addData(20, new int[] {20, 21, 22, 23});

        ctx.addData(30, new int[] {30, 31});

        List<DataChunk> result = ctx.getDataChunks();
        assertThat(result, contains(isData(20, 4), isData(30, 2)));
    }

    @Test
    public void addAndMergeSecondAfterFirst() {
        ctx.addData(20, new int[] {20, 21, 22, 23});

        ctx.addData(24, new int[] {24, 25});

        List<DataChunk> result = ctx.getDataChunks();
        assertThat(result, contains(isData(20, 6)));
    }

    @Test
    public void addSecondBeforeFirst() {
        ctx.addData(30, new int[] {30, 31});

        ctx.addData(20, new int[] {20, 21, 22, 23});

        List<DataChunk> result = ctx.getDataChunks();
        assertThat(result, contains(isData(20, 4), isData(30, 2)));
    }

    @Test
    public void addAndMergeSecondBeforeFirst() {
        ctx.addData(30, new int[] {30, 31});

        ctx.addData(26, new int[] {26, 27, 28, 29});

        List<DataChunk> result = ctx.getDataChunks();
        assertThat(result, contains(isData(26, 6)));
    }

    @Test
    public void addThirdAfterSecond() {
        ctx.addData(20, new int[] {20, 21, 22, 23});
        ctx.addData(30, new int[] {30, 31});

        ctx.addData(40, new int[] {40});

        List<DataChunk> result = ctx.getDataChunks();
        assertThat(result, contains(isData(20, 4), isData(30, 2), isData(40, 1)));
    }

    @Test
    public void addThirdBeforeSecond() {
        ctx.addData(20, new int[] {20, 21, 22, 23});
        ctx.addData(40, new int[] {40});

        ctx.addData(30, new int[] {30, 31});

        List<DataChunk> result = ctx.getDataChunks();
        assertThat(result, contains(isData(20, 4), isData(30, 2), isData(40, 1)));
    }

    @Test
    public void addAndMergeThirdBeforeSecond() {
        ctx.addData(26, new int[] {26, 27, 28, 29});
        ctx.addData(32, new int[] {32});

        ctx.addData(30, new int[] {30, 31});

        List<DataChunk> result = ctx.getDataChunks();
        assertThat(result, contains(isData(26, 7)));
    }

    @Test
    public void secondOverlapsFirst1() {
        ctx.addData(20, new int[] {20, 21, 22, 23});

        thrown.expect(IllegalStateException.class);

        ctx.addData(23, new int[] {23, 24});
    }

    @Test
    public void secondOverlapsFirst2() {
        ctx.addData(23, new int[] {23, 24});

        thrown.expect(IllegalStateException.class);

        ctx.addData(20, new int[] {20, 21, 22, 23});
    }

    private static Matcher<? super DataChunk> isData(long address, int length) {
        return allOf(
                is(notNullValue()),
                hasProperty("lowAddress", is(address)),
                hasProperty("length", is(length))
        );
    }
}