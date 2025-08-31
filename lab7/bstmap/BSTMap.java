package bstmap;

import edu.princeton.cs.algs4.BST;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.Stack;

import java.awt.*;
import java.security.Key;
import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

public class BSTMap<K extends Comparable<K>, V> implements Map61B<K,V> {

    private class BSTNode {


        private K key;
        private V value;
        private BSTNode right;
        private BSTNode left;
        private int size;


        public BSTNode(K key, V value, int size) {
            this.key = key;
            this.value = value;
            this.size = size;
        }

    }


    private BSTNode root;


    public BSTMap() {
    }


    @Override
    public void clear() {
        root = null;
    }


    @Override
    public boolean containsKey(K key) {
        if (root == null) {
            return false;
        }
        BSTNode currentNode = root;
        while (currentNode != null) {
            if (key.compareTo(currentNode.key) < 0) {
                currentNode = currentNode.left;
            } else if (key.compareTo(currentNode.key) > 0) {
                currentNode = currentNode.right;
            } else {
                return true;
            }
        }
        return false;
    }


    private V get(BSTNode r, K key) {
        if (r == null) {
            return null;
        }
        if (key.compareTo(r.key) < 0) {
            return get(r.left, key);
        } else if (key.compareTo(r.key) > 0) {
            return get(r.right, key);
        } else {
            return r.value;
        }
    }


    @Override
    public V get(K key) {
        return get(root, key);
    }


    @Override
    public int size() {
        return root == null ? 0 : root.size;
    }


    private BSTNode put(BSTNode r, K key, V value) {
        if (r == null) {
            return new BSTNode(key, value, 1);
        }
        if (key.compareTo(r.key) < 0) {
            r.left = put(r.left, key, value);
        } else if (key.compareTo(r.key) > 0) {
            r.right = put(r.right, key, value);
        } else {
            r.value = value;
        }
        int leftSize = r.left == null ? 0 : r.left.size;
        int rightSize = r.right == null ? 0 : r.right.size;
        r.size = 1 + leftSize + rightSize;
        return r;
    }


    @Override
    public void put(K key, V value) {
        root = put(root, key, value);
    }


    private void printInOrder(BSTNode r) {
        if (r == null) {
            return;
        }
        printInOrder(r.left);
        System.out.print(r.key + " ");
        printInOrder(r.right);
    }

    public void printInOrder() {
        printInOrder(root);
    }

    private void help(BSTNode r, Set<K> set) {
        if (r == null) {
            return;
        }
        help(r.left, set);
        set.add(r.key);
        help(r.right, set);
    }


    @Override
    public Set<K> keySet() {
        Set<K> emptySet = new HashSet<>();
        help(root, emptySet);
        return emptySet;
    }


    @Override
    public V remove(K key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public V remove(K key, V value) {
        throw new UnsupportedOperationException();
    }

    private class BSTMapIter implements Iterator<K> {
        private Stack<BSTNode> stack;


        public BSTMapIter() {
            stack = new Stack<>();
            pushLeft(root);
        }


        private void pushLeft(BSTNode r) {
            while (r != null) {
                stack.push(r);
                r = r.left;
            }
        }


        @Override
        public boolean hasNext() {
            return !stack.isEmpty();
        }


        @Override
        public K next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            BSTNode cur = stack.pop();
            pushLeft(cur.right);
            return cur.key;
        }
    }


    @Override
    public Iterator<K> iterator() {
        return new BSTMapIter();
    }


}
