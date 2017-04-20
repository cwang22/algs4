import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] items;
    private int size;

    public RandomizedQueue() {
        items = (Item[]) new Object[2];
        size = 0;
    }                // construct an empty randomized queue

    public boolean isEmpty() {
        return size == 0;
    }               // is the queue empty?

    public int size() {
        return size;
    }              // return the number of items on the queue

    public void enqueue(Item item) {
        if (item == null) throw new NullPointerException();
        items[size++] = item;
        if (size == items.length) resize(2 * items.length);

    }

    private void resize(int n) {
        Item[] temp = (Item[]) new Object[n];
        for (int i = 0; i < size; i++) {
            temp[i] = items[i];
        }
        items = temp;
    }

    public Item dequeue() {
        if (isEmpty()) throw new NoSuchElementException();
        int random = StdRandom.uniform(size);
        Item item = items[random];
        items[random] = items[size - 1];
        items[size - 1] = null;
        size--;
        if (size > 0 && size == items.length / 4) resize(items.length / 2);
        return item;
    }

    public Item sample() {
        if (isEmpty()) throw new NoSuchElementException();
        return items[StdRandom.uniform(size)];
    }

    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();
    }

    private class RandomizedQueueIterator implements Iterator<Item> {
        private Item[] r;
        private int count;

        public RandomizedQueueIterator() {
            copyQueue();
            StdRandom.shuffle(r);
        }

        private void copyQueue() {
            r = (Item[]) new Object[size];
            for (int i = 0; i < size; i++) {
                r[i] = items[i];
            }
        }

        public boolean hasNext() {
            return count < size;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            return r[count++];
        }
    }

    public static void main(String[] args) {

    }
}