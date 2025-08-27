package deque;
import static org.junit.Assert.*;
import org.junit.Test;

public class TestEmply {

    @Test
    public void testThreeAddThreeRemove() {
        ArrayDeque<Integer> no = new ArrayDeque<>();
        ArrayDeque<Integer> bu = new ArrayDeque<>();

        no.addLast(3);
        no.addLast(4);
        no.addLast(5);
        no.addLast(3);
        no.addLast(4);
        no.addLast(5);
        no.addLast(3);
        no.addLast(4);
        no.addLast(5);
        no.addLast(3);
        no.addLast(4);
        no.addLast(5);
        no.addLast(3);
        no.addLast(4);
        no.addLast(5);
        no.addLast(3);
        no.addLast(4);
        no.addLast(5);
        no.addLast(3);
        no.addLast(4);
        no.addLast(5);

        no.removeLast();
        no.removeLast();
        no.removeLast();
        no.removeLast();
        no.removeLast();
        no.removeLast();
        no.removeLast();
        no.removeLast();
        no.removeLast();
        no.removeLast();
        no.removeLast();
        no.removeLast();
        no.removeLast();
        no.removeLast();
        no.removeLast();
        no.removeLast();
        no.removeLast();
        no.removeLast();
        no.removeLast();
        no.removeLast();
        no.removeLast();



        assertTrue(no.isEmpty());
    }


}
