package de.niekrenz.intelhex.analyze.hexparser;

class LineContext {
    public LineState state = LineState.RECLEN;
    public long loadOffset = 0;
    public int dataIndex = 0;
    public int dataLeft = 0;
    public RecordType recordType = null;
    public Checksum checksum = new Checksum();
    public long address = 0;
    public int[] data;
}
