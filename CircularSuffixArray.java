import java.util.Arrays;
import java.util.Comparator;

public class CircularSuffixArray {
    private char[] values;
    private Integer[] indices;
    
    // circular suffix array of s
    public CircularSuffixArray(String s) {
        if (s == null) {
            throw new IllegalArgumentException();
        }

        int length = s.length();

        // initialize  values and indices arrays from length of input string
        values = new char[length];
        indices = new Integer[length];

        // values  = characters from the input string
        // indices = values from 0 to length-1
        for (int i = 0; i < length; i++) {
            values[i] = s.charAt(i);
            indices[i] = i;
        }

        // sort the indices array based on the circular suffixes they represent
        Arrays.sort(indices, new Comparator<Integer>() {
            public int compare(Integer idx1, Integer idx2) {
                for (int i = 0; i < length; i++) {
                    char c1 = values[(i + idx1) % length]; // character from circular suffix starting at idx1
                    char c2 = values[(i + idx2) % length]; // character from circular suffix starting at idx2
                    if (c1 > c2) {
                        return 1; // if c1 is greater than c2 idx1 > idx2
                    }

                    if (c1 < c2) {
                        return -1; // If c1 is less than c2, idx1 < idx2
                    }
                }
                return 0; // else if all compared characters are equal, the suffixes are equal
            }
        });
    }

    // length of s
    public int length() {
        return values.length;
    }   
    
    // returns index of ith sorted suffix
    public int index(int i) {
        if (i < 0 || i >= length()) {
            throw new IllegalArgumentException();
        }

        return indices[i];
    } 

    public static void main(String[] args) {

    }
}
