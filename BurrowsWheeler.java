import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;
import edu.princeton.cs.algs4.Queue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Nathan Sun attests that this code is their original work and was written in compliance with the class Academic Integrity and Collaboration
 * Policy found in the syllabus. 
 */

// I thought it was hard to rebuild the original string in the inverse transform method because. I found it a little confusing to use a 'next' array
// to follow the indices back to the first. Using a queue made sense because it allowed me to keep track of the positions of each character, so that
// I could map the sorted characters back to their original positions in the encoded string. I also had to review Maps because I forgot how those
// functioned.

public class BurrowsWheeler {

    // apply Burrows-Wheeler encoding, reading from standard input and writing to standard output
    public static void transform() {

        String input = BinaryStdIn.readString();
        CircularSuffixArray csa = new CircularSuffixArray(input);

        // find first row in sorted suffix array that has a 0 offset
        for (int i = 0; i < csa.length(); i++) {
            if (csa.index(i) == 0) {
                // write this index to the output
                BinaryStdOut.write(i);
                break;
            }
        }

        // write last column of sorted suffix array to output
        for (int i = 0; i < csa.length(); i++) {
            int index = csa.index(i);
            if (index == 0) {
                // if equal to original string
                BinaryStdOut.write(input.charAt(input.length() - 1), 8);
            }

            else {
                // else write previous character to output
                BinaryStdOut.write(input.charAt(index - 1), 8);
            }  
        }

        BinaryStdOut.close();
    }

    // apply Burrows-Wheeler decoding, reading from standard input and writing to standard output
    public static void inverseTransform() {

        String input = BinaryStdIn.readString();

        // index of the original row
        int first = BinaryStdIn.readInt();

        char[] chars = new char[input.length()];
        int[] next = new int[input.length()];

        for (int i = 0; i < input.length(); i++) {
            chars[i] = input.charAt(i);
        }

        // map each character to a queue of positions where it appears
        Map<Character, Queue<Integer>> pos = new HashMap<Character, Queue<Integer>>();

        for (int i = 0; i < chars.length; i++) {
            // check if the char is already a key
            if (!pos.containsKey(chars[i])) {
                pos.put(chars[i], new Queue<Integer>());
            }

            pos.get(chars[i]).enqueue(i);
        }

        // sort chars array to find order of characters in the first column
        Arrays.sort(chars);

        // fill next with pos of chars in sorted array
        for (int i = 0; i < chars.length; i++) {
            next[i] = pos.get(chars[i]).dequeue();
        }

        // rebuild og string by following next array, begin with first index
        int curr = first;
        for (int i = 0; i < chars.length; i++) {
            // write the character at the current row to the output
            BinaryStdOut.write(chars[curr]);
            curr = next[curr];
        }

        BinaryStdOut.close();
    }

    public static void main(String[] args) {
        if (args[0].equals("-")) {
            transform();
        }

        else if (args[0].equals("+")) {
            inverseTransform();
        }
    }

}
