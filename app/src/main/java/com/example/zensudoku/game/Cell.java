package com.example.zensudoku.game;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class Cell implements Serializable {

    int value;
    Set<Integer> notes = new HashSet<>();
    boolean originalValue = false;
    boolean invalidValue = false;
    boolean hintvalue = false;

    public Cell(int value) {
        this.value = value;
        if (value != 0) originalValue = true;
    }

    public boolean isInvalidValue() {
        return invalidValue;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public boolean getOriginalValue() {
        return originalValue;
    }

    public Set<Integer> getNotes() {
        Set<Integer> empty = new HashSet<>();
        if (getValue() != 0) return empty;
        return notes;
    }

    public void addNotes(int value) {
        if (value == 0) return;
        notes.add(value);
    }

    public void setHintvalue() {
        this.hintvalue = true;
    }

    public boolean isHintvalue() {
        return hintvalue;
    }

    public void removeNotes(int value) {
        notes.remove(value);
    }

    public void removeAllNotes() {
        notes = new HashSet<>();
    }
}
