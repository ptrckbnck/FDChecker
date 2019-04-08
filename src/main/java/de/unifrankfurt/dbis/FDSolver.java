package de.unifrankfurt.dbis;


import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;

/**
 * FDSolver determines many normal Form related information for given relationContainer.
 *
 * @author Patrick Bonack
 * @version 1.1
 * @since 28.3.2019
 */
public class FDSolver {

    /**
     * base relation
     */
    private final FDRelation relation;


    private final FDRelation relationRaw;
    /**
     * any prim attribute
     */
    private final HashSet<String> prim;

    /**
     * any non-prim attribute
     */
    private final HashSet<String> notPrim;

    /**
     * the maximum normal form the relation is in.
     */
    private final int NF;

    /**
     * any candidate key
     */
    private final FDKeySet keyCandidates;

    protected FDSolver(FDRelation relation, FDRelation relationRaw, HashSet<String> prim, HashSet<String> notPrim, int NF, FDKeySet keyCandidates) {
        this.relation = relation;
        this.relationRaw = relationRaw;
        this.prim = prim;
        this.notPrim = notPrim;
        this.NF = NF;
        this.keyCandidates = keyCandidates;
    }

    public static FDSolver createFDSolver(FDRelation relation) {
        FDRelation transitiveClosureReflexive = relation.transitiveClosureReflexive();
        FDKeySet keyCandidates = keyCandidates(transitiveClosureReflexive);
        HashSet<String> prim = prim(keyCandidates);
        HashSet<String> notPrim = notPrim(prim, transitiveClosureReflexive.getAttributes());
        int nf  = NF(notPrim, keyCandidates, transitiveClosureReflexive);
        return new FDSolver(transitiveClosureReflexive, relation, prim, notPrim, nf, keyCandidates);
    }


    public FDRelation getRelation() {
        return relation;
    }

    public HashSet<String> getPrim() {
        return prim;
    }

    public HashSet<String> getNotPrim() {
        return notPrim;
    }

    public int getNF() {
        return NF;
    }

    public FDKeySet getKeyCandidates() {
        return keyCandidates;
    }

    private HashSet<String> getAttributes() {
        return this.relation.getAttributes();
    }

    /**
     * @return HashSet of any prim attribute
     */
    private static HashSet<String> prim(FDKeySet keyCandidates) {
        HashSet<String> _prim = new HashSet<>();
        for (FDKey key : keyCandidates) {
            key.forEach(_prim::add);
        }
        return _prim;
    }

    /**
     * @return HashSet of any non-prim attribute
     */
    private static HashSet<String> notPrim(HashSet<String> prim, HashSet<String> attributes) {
        HashSet<String> _notPrim = new HashSet<>(attributes);
        _notPrim.removeAll(prim);
        return _notPrim;
    }

    /**
     * @return max normal Form (no more than 3)
     */
    private static int NF(HashSet<String> notPrim, FDKeySet keyCandidates, FDRelation relation) {
        int nf = 1;
        if (is2NF(notPrim, keyCandidates, relation)) nf = 2;
        if (nf == 2 && is3NF(relation)) nf = 3;
        return nf;
    }

    /**
     * looks if any non-prime attribute is dependent on any proper subset of any candidate key
     *
     * @return true if in second normal Form
     */
    private static boolean is2NF(HashSet<String> notPrim, FDKeySet keyCandidates, FDRelation relation) {
        HashMap<String, FDKeySet> data = relation.getData();
        for (String att : notPrim) {
            for (FDKey key : keyCandidates) {
                for (FDKey subKey : key.powerSetWoSelfAndEmptySet()) {
                    FDKeySet keySet = data.get(att);
                    if (keySet.contains(subKey)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * looks if all the attributes are dependent only on the candidate keys
     * and not by any non-prime attributes.
     *
     * @return true if in third normal form
     */
    private static boolean is3NF(FDRelation relation) {
        HashSet<String> attributes = relation.getAttributes();
        for (String outerAtt : attributes){
            for (String innerAtt : attributes){
                if(outerAtt.equals(innerAtt)){
                    continue;
                }
                if(!relation.getDependenciesOf(outerAtt).contains(innerAtt)){
                    continue;
                }
                if(relation.getDependenciesOf(innerAtt).contains(outerAtt)){
                    continue;
                }
                final HashSet<String> innerDependencies = new HashSet<>(relation.getDependenciesOf(innerAtt));
                innerDependencies.remove(outerAtt);
                innerDependencies.remove(innerAtt);
                if(!innerDependencies.isEmpty()){
                    return false;
                }
            }
        }
        return true;
    }


    @Override
    public String toString() {
        return "FDSolver{" +
                "relation=" + relation +
                ", attributes=" + Arrays.asList(getAttributes().toArray()).toString() +
                ", prim=" + Arrays.asList(prim.toArray()).toString() +
                ", notPrim=" + Arrays.asList(notPrim.toArray()).toString() +
                ", NF=" + NF +
                ", keyCandidates=" + keyCandidates +
                '}';
    }

    public String report() {
        String ls = System.lineSeparator();
        return "Report on Relation:" + ls +
                relation + ls +
                "attributes: " + Arrays.asList(getAttributes().toArray()).toString() + ls +
                "prim attributes: " + Arrays.asList(prim.toArray()).toString() + ls +
                "non-prim attributes: " + Arrays.asList(notPrim.toArray()).toString() + ls +
                "key-candidates: " + keyCandidates + ls +
                "Highest normal form: " + NF;
    }

    /**
     * creates the product of every FDKeySet an attribute depends on
     *
     * @return FDKeySet of every candidate key
     * @param relation
     */
    public static FDKeySet keyCandidates(FDRelation relation) {
        FDKeySet product = null;
        for(FDKeySet keySet : relation.getData().values()){
            product = product(product, keySet);
        }
        return product;

    }

    public static FDKeySet product(FDKeySet keySet1, FDKeySet keySet2) {
        if (Objects.isNull(keySet1)) return keySet2;
        if (Objects.isNull(keySet2)) return keySet1;
        FDKeySet result = new FDKeySet();
        for (FDKey key1 : keySet1){
            for (FDKey key2 : keySet2){
                HashSet<String> set = new HashSet<>();
                key1.forEach(set::add);
                key2.forEach(set::add);
                result.add(new FDKey(set));
            }
        }
        return result;
    }


    public FDRelation getRelationRaw() {
        return relationRaw;
    }
}
