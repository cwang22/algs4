import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    private Node<Item> first;
    private Node<Item> last;
    private int size;

    private static class Node<Item> {
        private Item item;
        private Node<Item> previous;
        private Node<Item> next;

        Node(Item item) {
            this.item = item;
        }
    }

    public Deque() {
        first = null;
        last = null;
        size = 0;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void addFirst(Item item) {
        if (item == null) throw new NullPointerException();
        Node<Item> n = new Node<>(item);

        if (isEmpty()) {
            first = n;
            last = n;
        } else {
            first.previous = n;
            n.next = first;
            first = n;
        }

        size++;
    }

    public void addLast(Item item) {
        if (item == null) throw new NullPointerException();
        Node<Item> n = new Node<>(item);

        if (isEmpty()) {
            first = n;
            last = n;
        } else {
            last.next = n;
            n.previous = last;
            last = n;
        }

        size++;
    }

    public Item removeFirst() {
        if (isEmpty()) throw new NoSuchElementException();
        Node<Item> n = first;
        first = first.next;
        size--;

        if (isEmpty()) last = null;
        else first.previous = null;

        return n.item;
    }

    public Item removeLast() {
        if (isEmpty()) throw new NoSuchElementException();
        Node<Item> n = last;
        last = last.previous;
        size--;

        if (isEmpty()) first = null;
        else last.next = null;
        
        return n.item;
    }

    public Iterator<Item> iterator() {
        return new DequeIterator<>(first);
    }

    private class DequeIterator<Item> implements Iterator<Item> {
        private Node<Item> current;

        DequeIterator(Node<Item> n) {
            current = n;
        }

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            Item i = current.item;
            current = current.next;
            return i;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    public static void main(String[] args) {
    }
}
