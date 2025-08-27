package deque;

import java.util.Arrays;

import java.util.Iterator;
import java.util.PrimitiveIterator;

public class ArrayDeque<T> implements Iterable<T> {
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

    public void addFirst(T item) {
        if (size == array.length) {
            resize(size * 2);
        }
        array[prev] = item;
        prev = (prev - 1 + array.length) % array.length;
        size += 1;
    }

    public void addLast(T item) {
        if (size == array.length) {
            resize(size * 2);
        }
        array[next] = item;
        next = (next + 1) % array.length;
        size += 1;
    }

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

    public int size() {
        return size;
    }

    public void resize(int capacity) {
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

    public boolean isEmpty() {
        return size == 0;
    }

    public T get(int index) {
        int obj = (prev + 1 + index) % array.length;
        return array[obj];
    }

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
        private int cur_pos;


        public ArrayDequeIterator() {
            count = 0;
            cur_pos = prev;
        }

        @Override
        public boolean hasNext() {
            return count < size;
        }

        @Override
        public T next() {
            cur_pos = (cur_pos + 1) % array.length;
            count += 1;
            return array[cur_pos];
        }
    }



}
