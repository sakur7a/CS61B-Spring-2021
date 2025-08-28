package deque;

import java.util.Iterator;


public class ArrayDeque<T> implements Iterable<T>, Deque<T> {
    private int size;
    private T[] array;
    private int next;
    private int prev;
    private int n;

    public ArrayDeque() {
        array = (T[]) new Object[8];
        size = 0;
        next = 0;
        prev = array.length - 1;
    }

    @Override
    public void addFirst(T item) {
        if (size == array.length) {
            resize(size * 2);
        }
        array[prev] = item;
        prev = (prev - 1 + array.length) % array.length;
        size += 1;
    }

    @Override
    public void addLast(T item) {
        if (size == array.length) {
            resize(size * 2);
        }
        array[next] = item;
        next = (next + 1) % array.length;
        size += 1;
    }

    @Override
    public T removeFirst() {
        if (isEmpty()) {
            return null;
        }
        prev = (prev + 1) % array.length;
        T value = array[prev];
        array[prev] = null;
        size -= 1;
        if ((size < array.length / 4) && array.length > 4) {
            resize(array.length / 4);
        }
        return value;
    }

    @Override
    public T removeLast() {
        if (isEmpty()) {
            return null;
        }
        next = (next - 1 + array.length) % array.length;
        T value = array[next];
        array[next] = null;
        size -= 1;
        if ((size < array.length / 4) && array.length > 4) {
            resize(array.length / 4);
        }
        return value;
    }

    @Override
    public int size() {
        return size;
    }

    private void resize(int capacity) {
        T[] copy = (T[]) new Object[capacity];
        int cur = (prev + 1) % array.length;
        for (int i = 0; i < size; i++) {
            copy[i] = array[cur];
            cur = (cur + 1) % array.length;
        }
        array = copy;
        prev = array.length - 1;
        next = size;
    }

    @Override
    public T get(int index) {
        int obj = (prev + 1 + index) % array.length;
        return array[obj];
    }

    @Override
    public void printDeque() {
        if (isEmpty()) {
            return;
        }
        int i = (prev + 1) % array.length;
        int k = size;
        while (k > 0) {
            System.out.print(array[i] + " ");
            i = (i + 1) % array.length;
        }
        System.out.println();
    }

    @Override
    public Iterator<T> iterator() {
        return new ArrayDequeIterator();
    }

    private class ArrayDequeIterator implements Iterator<T> {

        private int count;
        private int currentPosition;


        public ArrayDequeIterator() {
            count = 0;
            currentPosition = prev;
        }

        @Override
        public boolean hasNext() {
            return count < size;
        }

        @Override
        public T next() {
            currentPosition = (currentPosition + 1) % array.length;
            count += 1;
            return array[currentPosition];
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ArrayDeque)) {
            return false;
        }
        ArrayDeque<T> other = (ArrayDeque<T>) o;
        if (other.size != this.size) {
            return false;
        } else {
            for (int i = 0; i < this.size; i++) {
                if (!this.get(i).equals(other.get(i))) {
                    return false;
                }
            }
        }
        return true;
    }

}
