package de.unifrankfurt.dbis;

import org.junit.jupiter.api.Test;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class FDKeyTest {

    /**
     * tests if powerSetWoSelfAndEmptySet works correctly.
     * it should return the powerset of itself without itself and empty set.
     */
    @Test
    public void subsetTest(){
        HashSet<FDKey> set = new HashSet<>();
        set.add(new FDKey("a"));
        set.add(new FDKey("b"));
        set.add(new FDKey("c"));
        set.add(new FDKey("d"));
        set.add(new FDKey("a","b"));
        set.add(new FDKey("a","c"));
        set.add(new FDKey("a","d"));
        set.add(new FDKey("b","c"));
        set.add(new FDKey("b","d"));
        set.add(new FDKey("c","d"));
        set.add(new FDKey("a","b","c"));
        set.add(new FDKey("a","b","d"));
        set.add(new FDKey("a","c","d"));
        set.add(new FDKey("b","c","d"));
        FDKey key = new FDKey("a","b","c","d");
        assertEquals(set,key.powerSetWoSelfAndEmptySet());

    }
}