package randomizedtest;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;
import timingtest.AList;
import timingtest.SLList;

import static org.junit.Assert.*;

/**
 * Created by hug.
 */

public class TestBuggyAList {
    // YOUR TESTS HERE

    @Test
    public void testThreeAddThreeRemove() {
        AListNoResizing<Integer> no = new AListNoResizing<>();
        BuggyAList<Integer> bu = new BuggyAList<>();

        no.addLast(3);
        no.addLast(4);
        no.addLast(5);

        bu.addLast(3);
        bu.addLast(4);
        bu.addLast(5);

        assertEquals(no.removeLast(), bu.removeLast());
        assertEquals(no.removeLast(), bu.removeLast());
        assertEquals(no.removeLast(), bu.removeLast());
    }

    @Test
    public void randomizedTes() {
        AListNoResizing<Integer> L = new AListNoResizing<>();
        BuggyAList<Integer> B = new BuggyAList<>();

        int N = 500;
        for (int i = 0; i < N; i += 1) {
            int operationNumber = StdRandom.uniform(0, 4);
            if (operationNumber == 0) {
                // addLast
                int randVal = StdRandom.uniform(0, 100);
                L.addLast(randVal);
                B.addLast(randVal);
            } else if (operationNumber == 1) {
                // size
                int size_L = L.size();
                int size_B = B.size();
                assertEquals(size_L, size_B);
            } else if (operationNumber == 2) {
                // getLast
                if (L.size() == 0) {
                    continue;
                } else {
                    int last_L = L.getLast();
                    int last_B = B.getLast();
                    assertEquals(last_L, last_B);
                }
            } else {
                // removeLast
                if (L.size() == 0) {
                    continue;
                } else {
                    int removeLast_L = L.removeLast();
                    int removeLast_B = B.removeLast();
                    assertEquals(removeLast_L, removeLast_B);
                }
            }
        }
    }
}

