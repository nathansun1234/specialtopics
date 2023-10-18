import java.util.Iterator;
import java.util.NoSuchElementException;
import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] queue;
    private int size;

    // construct an empty randomized queue
    public RandomizedQueue() {
        queue = (Item[]) new Object[2];
        size = 0;
    }

    private void resize(int newSize) {
        Item[] temp = (Item[]) new Object[newSize];
        for (int i = 0; i < size; i++) {
            temp[i] = queue[i];
        }
        queue = temp;
    }

    private class IterateQueue implements Iterator<Item> {
        private int current;
        private int[] indxs;

        public IterateQueue() {
            current = 0;
            indxs = new int[size];
            for (int i = 0; i < size; i++) {
                indxs[i] = i;
            }
            StdRandom.shuffle(indxs);
        }

        public boolean hasNext() {
            return current < size;
        }

        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            Item result = queue[indxs[current]];
            current++;
            return result;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return size;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        if (size == queue.length) {
            resize(queue.length * 2);
        }
        if (size == 0) {
            queue[size] = item;
            size++;
            return;
        }
        int rand = StdRandom.uniformInt(size);
        Item temp = queue[rand];
        queue[rand] = item;
        queue[size] = temp;
        size++;
    }

    // remove and return a random item
    public Item dequeue() {
        if (size == 0) {
            throw new NoSuchElementException();
        }

        if (size == queue.length / 4) {
            resize(queue.length / 2);
        }
        int rand = StdRandom.uniformInt(size);
        Item item = queue[rand];
        size--;
        queue[rand] = queue[size];
        queue[size] = null;
        return item;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        else {
            return queue[StdRandom.uniformInt(size)];
        }
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new IterateQueue();
    }

    // unit testing (required)
    public static void main(String[] args) {
    }
}