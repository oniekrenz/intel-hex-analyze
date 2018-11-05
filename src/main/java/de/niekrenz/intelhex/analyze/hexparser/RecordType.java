package de.niekrenz.intelhex.analyze.hexparser;

import java.util.Arrays;
import java.util.Optional;

public enum RecordType {
    DATA(0, false, null),
    EOF(1, true, 0),
    EXTENDED_SEGMENT_ADDRESS(2, true, 2),
    START_SEGMENT_ADDRESS(3, true, 4),
    EXTENDED_LINEAR_ADDRESS(4, true, 2),
    START_LINEAR_ADDRESS(5, true, 4);

    private final int value;
    private final boolean nullAddress;
    private final Optional<Integer> expectedDataLength;

    RecordType(int value, boolean expectNullAddress, Integer expectedDataLength) {
        this.value = value;
        this.nullAddress = expectNullAddress;
        this.expectedDataLength = Optional.ofNullable(expectedDataLength);
    }

    public static RecordType from(int value) {
        return Arrays.stream(RecordType.values())
                .filter(rt -> rt.value == value)
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    public boolean isNullAddress() {
        return nullAddress;
    }

    public Optional<Integer> getExpectedDataLength() {
        return expectedDataLength;
    }
}
