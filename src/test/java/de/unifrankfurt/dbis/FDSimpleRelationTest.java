package de.unifrankfurt.dbis;

import org.junit.jupiter.api.Test;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;


public class FDSimpleRelationTest {

    /**
     * parses "a->b"
     */
    @Test
    public void parse() {
        FDSimpleRelation rel = FDSimpleRelation.parse("a->b");
        FDKey key = new FDKey("a");
        HashSet<String> values = new HashSet<>();
        values.add("b");
        HashSet<String> attributes  = new HashSet<>();
        attributes.addAll(key.toSet());
        attributes.addAll(values);
        assertEquals(attributes,rel.getAttributes());
        assertEquals(key,rel.getKey());
        assertEquals(values,rel.getValues());
    }


    /**
     * parses ""
     */
    @Test
    public void parse2() {
        FDSimpleRelation rel = FDSimpleRelation.parse("");
        FDKey key = new FDKey();
        HashSet<String> values = new HashSet<>();
        HashSet<String> attributes  = new HashSet<>();
        attributes.addAll(key.toSet());
        attributes.addAll(values);
        assertEquals(attributes,rel.getAttributes());
        assertEquals(key,rel.getKey());
        assertEquals(values,rel.getValues());
    }

    /**
     * parses "a b -> a b"
     */
    @Test
    public void parse3() {
        FDSimpleRelation rel = FDSimpleRelation.parse("a b -> a b"," ");
        FDKey key = new FDKey("a","b");
        HashSet<String> values = new HashSet<>();
        values.add("a");
        values.add("b");
        HashSet<String> attributes  = new HashSet<>();
        attributes.addAll(key.toSet());
        attributes.addAll(values);
        assertEquals(attributes,rel.getAttributes());
        assertEquals(key,rel.getKey());
        assertEquals(values,rel.getValues());
    }

    /**
     * parses "a"
     */
    @Test
    public void parse4() {
        FDKey key = new FDKey("a");
        HashSet<String> values = new HashSet<>();
        values.add("b");
        HashSet<String> attributes  = new HashSet<>();
        FDSimpleRelation rel = new FDSimpleRelation(key.toSet(),values);
        attributes.addAll(key.toSet());
        attributes.addAll(values);
        assertEquals(attributes,rel.getAttributes());
        assertEquals(key,rel.getKey());
        assertEquals(values,rel.getValues());
    }

    /**
     * parses "ab->c"
     */
    @Test
    public void parse5() {
        FDKey key = new FDKey("a","b");
        HashSet<String> values = new HashSet<>();
        values.add("c");
        HashSet<String> attributes  = new HashSet<>();
        FDSimpleRelation rel = FDSimpleRelation.parse("ab->c");
        attributes.addAll(key.toSet());
        attributes.addAll(values);
        assertEquals(attributes,rel.getAttributes());
        assertEquals(key,rel.getKey());
        assertEquals(values,rel.getValues());
    }

    /**
     * parses "->"
     */
    @Test
    public void parseSyntaxFail1() {
        FDSimpleRelation relation = FDSimpleRelation.parse("->");
        assertNull(relation);
    }

    /**
     * parses "a->"
      */
    @Test
    public void parseSyntaxFail2() {
        FDSimpleRelation relation = FDSimpleRelation.parse("a->");
        assertNull(relation);
    }

    /**
     * parses "->b"
     */
    @Test
    public void parseSyntaxFail3() {
        FDSimpleRelation relation = FDSimpleRelation.parse("->b");
        assertNull(relation);
    }

    /**
     * parses "a->b->c"
     */
    @Test
    public void parseSyntaxFail4() {
        FDSimpleRelation relation = FDSimpleRelation.parse("a->b->c");
        assertNull(relation);
    }

    /**
     * parses "a"
     */
    @Test
    public void parseSyntaxFail5() {
        FDSimpleRelation relation = FDSimpleRelation.parse("a");
        assertNull(relation);
    }

}