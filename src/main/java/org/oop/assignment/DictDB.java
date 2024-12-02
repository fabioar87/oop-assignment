package org.oop.assignment;

public enum DictDB {
    ENG_TO_LATIN("eng-lat", "fd-eng-lat"),
    ENG_TO_IRISH("eng-irish", "fd-eng-gle"),
    ENG_TO_JAPANESE("eng-japanese", "fd-eng-jpn");

    private final String label;
    private final String dbName;

    DictDB(String label, String dbName) {
        this.label = label;
        this.dbName = dbName;
    }

    public String getLabel() {
        return label;
    }

    public String getDbName() {
        return dbName;
    }

    @Override
    public String toString() {
        return this.label;
    }
}
