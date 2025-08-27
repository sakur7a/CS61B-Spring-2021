package deque;
import static org.junit.Assert.*;
import org.junit.Test;

public class TestEmply {

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
        assertEquals("add same element, should be equal",no, bu);


        bu.removeLast();
        bu.removeLast();
        bu.removeLast();
        no.removeLast();
        no.removeLast();
        no.removeLast();

        assertEquals("null deque should be equal",no, bu);

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


}
