package com.chenxh.libroompluser.annotation;

public class AlterTable {
    public enum Action{
        Add,
        Del,
        Mod
    }

    public enum Type{
        INTEGER,
        TEXT,
        BLOB,
        REAL,
        NUMERIC
    }
}
