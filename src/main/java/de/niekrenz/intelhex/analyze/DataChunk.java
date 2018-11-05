package de.niekrenz.intelhex.analyze;

import static java.lang.String.format;

public class DataChunk {

    protected long address;
    protected int[] data;

    public DataChunk(long address, int[] data) {
        this.address = address;
        this.data = data;
    }

    public int[] getData() {
        return data;
    }

    public int getLength() {
        return data.length;
    }

    public long getLowAddress() {
        return address;
    }

    public long getHighAddress() {
        return getLowAddress() + getLength() - 1;
    }

    public boolean isLowerThan(DataChunk other) {
        return getHighAddress() < other.getLowAddress();
    }

    public boolean isHigherThan(DataChunk other) {
        return getLowAddress() > other.getHighAddress();
    }

    public boolean isOverlapping(DataChunk other) {
        return !other.isHigherThan(this) && !other.isLowerThan(this);
    }

    public boolean isAdjacentToLowerAddressOf(DataChunk other) {
        return getHighAddress() == other.getLowAddress() - 1;
    }

    public boolean isAdjacentToHigherAddressOf(DataChunk other) {
        return getLowAddress() == other.getHighAddress() + 1;
    }

    public boolean isAdjacentTo(DataChunk other) {
        return isAdjacentToLowerAddressOf(other) || isAdjacentToHigherAddressOf(other);
    }

    public DataChunk merge(DataChunk other) {
        if (isAdjacentToLowerAddressOf(other)) {
            int[] newData = new int[getLength() + other.getLength()];
            System.arraycopy(data, 0, newData, 0, getLength());
            System.arraycopy(other.getData(), 0, newData, other.getLength(), other.getLength());
            return new DataChunk(getLowAddress(), newData);
        }
        if (isAdjacentToHigherAddressOf(other)) {
            return other.merge(this);
        }
        throw new IllegalArgumentException("Other data chunk is not adjacent to this");
    }

    @Override
    public String toString() {
        return format("[%08X-%08X]", getLowAddress(), getHighAddress());
    }
}
