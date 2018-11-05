package de.niekrenz.intelhex.analyze.hexparser;

enum LineState {
    RECLEN, LOAD_OFFSET_HIGH, LOAD_OFFSET_LOW, RECTYPE, DATA, CHKSUM
}
