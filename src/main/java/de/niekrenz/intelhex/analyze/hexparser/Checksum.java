package de.niekrenz.intelhex.analyze.hexparser;

public class Checksum {

    public int checksum = 0;

    public void add(int value) {
        checksum = (byte) (checksum + value);
    }

    public byte getChecksum() {
        return (byte) checksum;
    }

    public boolean isValid() {
        return checksum == 0;
    }
}
