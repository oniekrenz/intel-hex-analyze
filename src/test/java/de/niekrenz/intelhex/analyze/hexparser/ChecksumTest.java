package de.niekrenz.intelhex.analyze.hexparser;

import java.util.stream.Stream;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(JUnitParamsRunner.class)
public class ChecksumTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Test
    @Parameters({
            "00, 00:00",
            "00, 02:00:00:04:00:00:FA",
            "00, 10:01:90:00:C8:60:03:49:40:39:08:60:48:60:00:F0:29:FB:10:BD:81"
    })
    public void test(String expected, String sequence) {
        Checksum checksum = new Checksum();
        Stream.of(sequence.split(":"))
                .map(bs -> Integer.parseInt(bs, 16))
                .forEach(checksum::add);

        byte exp = (byte) Integer.parseInt(expected, 16);
        assertThat(exp, is(checksum.getChecksum()));
    }
}