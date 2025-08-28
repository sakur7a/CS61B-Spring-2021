package deque;
import static org.junit.Assert.*;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;

public class TestDeque {

    @Test
    public void testAddThreeRemove() {
        ArrayDeque<Integer> no = new ArrayDeque<>();
        ArrayDeque<Integer> bu = new ArrayDeque<>();

        no.addLast(3);
        no.addLast(4);
        no.addLast(3);
        no.addLast(4);
        no.addFirst(5);
        no.addLast(3);
        no.addLast(5);
        no.addFirst(3);

        no.removeLast();
        no.removeFirst();
        no.removeLast();
        no.removeLast();
        no.removeFirst();
        no.removeLast();
        no.removeLast();
        no.removeFirst();

        assertTrue(no.isEmpty());
    }


    @Test
    public void testArrayDequeEqual() {
        ArrayDeque<Integer> no = new ArrayDeque<>();
        ArrayDeque<Integer> bu = new ArrayDeque<>();

        no.addLast(3);
        no.addLast(4);
        no.addLast(5);

        bu.addLast(3);
        bu.addLast(4);
        bu.addLast(5);
        assertEquals("add same element, should be equal", no, bu);


        bu.removeLast();
        bu.removeLast();
        bu.removeLast();
        no.removeLast();
        no.removeLast();
        no.removeLast();

        assertEquals("null deque should be equal", no, bu);

        bu.addLast(20);
        assertNotEquals("has different element", no, bu);

    }

    @Test
    public void testLinkedListDequeEqual() {
        LinkedListDeque<Integer> no = new LinkedListDeque<>();
        LinkedListDeque<Integer> bu = new LinkedListDeque<>();

        no.addLast(3);
        no.addLast(4);
        no.addLast(5);

        bu.addLast(3);
        bu.addLast(4);
        bu.addLast(5);
        assertEquals(no, bu);


        bu.removeLast();
        bu.removeLast();
        bu.removeLast();
        no.removeLast();
        no.removeLast();
        no.removeLast();



        assertEquals(no, bu);

        bu.addLast(20);
        assertNotEquals("has different element", no, bu);
    }

    @Test
    public void testThreeAddThreeRemove() {
        ArrayDeque<Integer> no = new ArrayDeque<>();
        ArrayDeque<Integer> bu = new ArrayDeque<>();

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
        ArrayDeque<Integer> L = new ArrayDeque<>();
        ArrayDeque<Integer> B = new ArrayDeque<>();

        int N = 500;
        for (int i = 0; i < N; i += 1) {
            int operationNumber = StdRandom.uniform(0, 3);
            if (operationNumber == 0) {
                // addLast
                int randVal = StdRandom.uniform(0, 100);
                L.addLast(randVal);
                B.addLast(randVal);
            } else if (operationNumber == 1) {
                // size
                int sizeL = L.size();
                int sizeB = B.size();
                assertEquals(sizeL, sizeB);
            } else {
                // removeLast
                if (L.size() == 0) {
                    continue;
                } else {
                    int removeLastL = L.removeLast();
                    int removeLastB = B.removeLast();
                    assertEquals(removeLastL, removeLastB);
                }
            }
        }
    }

    @Test
    public void testEqualTwoArray() {
        ArrayDeque<Integer> Array = new ArrayDeque<>();
        LinkedListDeque<Integer> List = new LinkedListDeque<>();

        Array.addLast(3);
        Array.addLast(4);
        Array.addLast(5);

        List.addLast(3);
        List.addLast(4);
        List.addLast(5);

        assertEquals(Array, List);
    }


}
