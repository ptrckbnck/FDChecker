package de.unifrankfurt.dbis;


import java.util.*;
import java.util.function.Consumer;

/**
 * FDKey implements Iterable<String>.
 * It is basically an immutable Set<String> .
 * Has the ability to create a Set with all true subsets with size>0 of itself.
 *
 * @author Patrick Bonack
 * @version 1.1
 */
public class FDKey implements Iterable<String> {
    private final Set<String> set;

    /**
     * attributes should be unique
     *
     * @param attributes collection of String attributes
     */
    public FDKey(Collection<String> attributes) {
        this(attributes.toArray(new String[0]));
    }

    /**
     * attributes should be unique
     *
     * @param attributes String[]
     */
    public FDKey(String... attributes) {
        set = Set.of(attributes);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.set);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FDKey)) return false;
        FDKey strings = (FDKey) o;
        return set.equals(strings.set);
    }

    public int size() {
        return this.set.size();
    }


    public String[] toArray() {
        return this.set.toArray(new String[0]);
    }



    /**
     * @return null if this is empty.
     */
    public HashSet<FDKey> powerSetWoSelfAndEmptySet() {
        if (this.size() == 0) return null;
        HashSet<FDKey> set = new HashSet<>();
        if (this.size() == 1) return set;
        set.add(new FDKey());
        for (int i = 1; i < this.size(); i++) {
            HashSet<FDKey> newSet = new HashSet<>();
            for (String s : this) {
                for (FDKey key : set) {
                    Collection<String> col = new HashSet<>();
                    key.forEach(col::add);
                    col.add(s);
                    FDKey newKey = new FDKey(col);
                    newSet.add(newKey);
                }
            }
            set = newSet;
        }
        return set;
    }

    /**
     * @return a string representation of this collection.
     */
    @Override
    public String toString() {
        return Arrays.asList(this.toArray()).toString();
    }

    @Override
    public Iterator<String> iterator() {
        return this.set.iterator();
    }

    @Override
    public void forEach(Consumer<? super String> action) {
        this.set.forEach(action);
    }

    @Override
    public Spliterator<String> spliterator() {
        return this.set.spliterator();
    }

    public boolean isEmpty() {
        return this.set.isEmpty();
    }

    public Set<String> toSet() {
        return new HashSet<>(this.set);
    }

    public boolean isSuperKeyOf(FDKey lookUpKey) {
        return this.set.containsAll(lookUpKey.toSet());
    }

    /**
     * exception if empty FDKey are not excepted
     */
    static class EmptyException extends Exception {

    }
}
