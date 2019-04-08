package de.unifrankfurt.dbis;

import com.google.gson.Gson;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;


public class FDSolverTest {

    /**
     * tests if keyCandidates are correctly calculated
     */
    @Test
    public void keyCandidatesTest(){
        FDRelation container = null;
        try {
            container = new FDRelation().parse("a->b").parse("bc->a");
        } catch (FDKey.EmptyException | FDRelation.UnexpectedAttributeException e) {
            fail();
        }
        FDSolver solver = FDSolver.createFDSolver(container);
        FDKeySet set = new FDKeySet();
        set.add(new FDKey("a","c"));
        set.add(new FDKey("b","c"));
        assertEquals(set,solver.getKeyCandidates());
    }

    /**
     * "a->b","bc->a" should be in Normal Form 3
     */
    @Test
    public void nf3Test(){
        FDRelation container = null;
        try {
            container = new FDRelation().parse("a->b").parse("bc->a");
        } catch (FDKey.EmptyException | FDRelation.UnexpectedAttributeException e) {
            fail();
        }
        FDSolver solver = FDSolver.createFDSolver(container);
        assertEquals(3,solver.getNF());
    }

    /**
     * "ab->c","b->c" should be in NormalForm 1
     */
    @Test
    public void nf1Test(){
        FDRelation container = null;
        try {
            container = new FDRelation().parse("ab->c").parse("b->c");
        } catch (FDKey.EmptyException | FDRelation.UnexpectedAttributeException e) {
            fail();
        }
        FDSolver solver = FDSolver.createFDSolver(container);
        assertEquals(1,solver.getNF());
    }

    /**
     * "a->b","a->c","a->d","c->d" should be in normal Form 2.
     */
    @Test
    public void nf2Test(){
        FDRelation container = null;
        try {
            container = new FDRelation().parse("a->b")
                    .parse("a->c")
                    .parse("a->d")
                    .parse("c->d");
        } catch (FDKey.EmptyException | FDRelation.UnexpectedAttributeException e) {
            fail();
        }
        FDSolver solver = FDSolver.createFDSolver(container);
        assertEquals(2,solver.getNF());
    }


    @Test
    public void nfTest() {
        FDRelation container = null;
        try {
            container = new FDRelation().parse("ac->bd")
                    .parse("be->cd")
                    .parse("bc->e");
        } catch (FDKey.EmptyException | FDRelation.UnexpectedAttributeException e) {
            fail();
        }
        FDSolver solver = FDSolver.createFDSolver(container);
        assertEquals(1,solver.getNF());
    }

    @Test
    public void nfTest2() {
        FDRelation container = null;
        try {
            container = new FDRelation().parse("a->bc")
                    .parse("be->ad")
                    .parse("bc->ae");
        } catch (FDKey.EmptyException | FDRelation.UnexpectedAttributeException e) {
            fail();
        }
        FDSolver solver = FDSolver.createFDSolver(container);
        assertEquals(3,solver.getNF());
    }

    @Test
    public void nfTest3() {
        FDSolver solved = null;
        try {
            solved = new FDRelation().parse("a->bd")
                    .parse("b->ced")
                    .parse("ed->a")
                    .solve();
        } catch (FDKey.EmptyException | FDRelation.UnexpectedAttributeException e) {
            fail();
        }
        assertEquals(3, solved.getNF());
    }

    @Test
    public void nfTest4() throws FDKey.EmptyException, FDRelation.UnexpectedAttributeException {
        FDSolver solved = new FDRelation().parse("a->bc")
                            .parse("b->c")
                            .parse("ab->c")
                            .solve();
        assertEquals(2,solved.getNF());
    }

    @Test
    public void nfTest5() {
        FDRelation container = null;
        try {
            container = new FDRelation().parse("ab->cd")
                    .parse("e->cd")
                    .parse("c->d");
        } catch (FDKey.EmptyException | FDRelation.UnexpectedAttributeException e) {
            fail();
        }
        FDSolver solver = FDSolver.createFDSolver(container);
        assertEquals(1,solver.getNF());
    }


    @Test
    public void nfTest6() throws FDKey.EmptyException, FDRelation.UnexpectedAttributeException {

        FDSolver solved = new FDRelation().parse("a->b")
                .parse("be->ad")
                .parse("bc->ad").solve();
        assertEquals(1,solved.getNF());

    }
}