package deque;

import org.hamcrest.core.IsEqual;

import java.util.Iterator;

public class LinkedListDeque<T> implements Iterable<T>{

    private class Node {
        public T item;
        public Node next;
        public Node prev;

        public Node(T i, Node p, Node n) {
            this.item = i;
            this.prev = p;
            this.next = n;
        }
    }

    private Node sentinel;
    private int size;


    public LinkedListDeque() {
        size = 0;
        sentinel = new Node(null, null, null);
        sentinel.prev = sentinel;
        sentinel.next = sentinel;
    }

    public void addFirst(T item) {
        Node add = new Node(item, sentinel, sentinel.next);
        sentinel.next.prev = add;
        sentinel.next = add;
        size += 1;
    }

    public void addLast(T item) {
        Node add = new Node(item, sentinel.prev, sentinel);
        sentinel.prev.next = add;
        sentinel.prev = add;
        size += 1;
    }

    public T removeFirst() {
        if (isEmpty())  {
            return null;
        }
        T value = sentinel.next.item;
        sentinel.next = sentinel.next.next;
        sentinel.next.prev = sentinel;
        size -= 1;
        return value;
    }


    public T removeLast() {
        if (isEmpty())  {
            return null;
        }
        T value = sentinel.prev.item;
        sentinel.prev = sentinel.prev.prev;
        sentinel.prev.next = sentinel;
        size -= 1;
        return value;
    }


    public int size() {
        return size;
    }

    public T get(int index) {
        Node i = sentinel.next;
        while (index > 0 && i != sentinel)
        {
            index -= 1;
            i = i.next;
        }
        T value = i.item;
        return value;
    }

    public T helper(int index, Node d) {
        Node i = d.next;
        if (index == 0 || i == sentinel) {
            return i.item;
        } else {
            return helper(index - 1, i.next);
        }
    }

    public T getRecursive(int index) {
        return helper(index, sentinel);
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void printDeque() {
        if (isEmpty()) {
            return;
        }
        Node i = sentinel;
        while (i.next != sentinel) {
            i = i.next;
            T x = i.item;
            System.out.print(x + " ");
        }
        System.out.println();
    }

    @Override
    public Iterator<T> iterator() {
        return new LinkedListDequeIterator();
    }

    private class LinkedListDequeIterator implements Iterator<T> {
        private Node it;

        public LinkedListDequeIterator() {
            it = sentinel;
        }

        @Override
        public boolean hasNext() {
            return it.next != sentinel;
        }

        @Override
        public T next() {
            it = it.next;
            T value = it.item;
            return value;
        }
    }

   @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (! (o instanceof LinkedListDeque)) {
            return false;
        }

        LinkedListDeque<T> other = (LinkedListDeque<T>) o;
        if (other.size != this.size) {
            return false;
        } else {
            for (int i = 0;i < this.size; i++) {
                if (!this.get(i).equals(other.get(i))) {
                    return false;
                }
            }
        }
        return true;
    }

}
