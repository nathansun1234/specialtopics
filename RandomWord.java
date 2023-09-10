import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class RandomWord {
    public static void main(String[] args) {
        String currentHero = "";
        int index = 1;
        while (!StdIn.isEmpty()) {
            String currentString = StdIn.readString();
            if (StdRandom.bernoulli(1.0 / index)) {
                currentHero = currentString;
            }
            index++;
        }
        StdOut.println(currentHero);
    }
}