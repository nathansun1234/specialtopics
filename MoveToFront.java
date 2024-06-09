import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class MoveToFront {
    
    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
        // array representing all 256 byte values corresponding to index
        char[] arr = new char[256];
        
        // populate array
        for (int i = 0; i < 256; i++) {
            arr[i] = (char) i;
        }
        
        String input = BinaryStdIn.readString();
        
        // iterate over each char in the input string
        for (int i = 0; i < input.length(); i++) {
            char in = arr[0];  // Save the first character in the array

            // iterate over the array to find  current input character
            for (int j = 0; j < 256; j++) {
                if (input.charAt(i) == arr[j]) {
                    // swap
                    arr[0] = input.charAt(i);
                    arr[j] = in;

                    // write pos of char before moving
                    BinaryStdOut.write((char) j);
                    break;  // break bc we are done
                }

                // shifts chrarcters to be able to move to front
                char temp = arr[j];
                arr[j] = in;
                in = temp;
            }
        }

        BinaryStdOut.close();      
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
        char[] arr = new char[256];
        
        for (int i = 0; i < 256; i++) {
            arr[i] = (char) i;
        }
        
        while (!BinaryStdIn.isEmpty()) {
            // int that represents position in the array
            int pos = BinaryStdIn.readInt(8);
            
            // write the character at the given position to the output
            BinaryStdOut.write(arr[pos]);
            char in = arr[0];

            // iterate over the array to move the character at the read position to the front
            for (int i = 0; i < 256; i++) {
                if (arr[pos] == arr[i]) {
                    // swap
                    arr[0] = arr[pos];
                    arr[i] = in;
                    break;
                }

                // shifts chrarcters to be able to move to front
                char temp = arr[i];
                arr[i] = in;
                in = temp;
            }
        }

        BinaryStdOut.close(); 
    }

    // if args[0] is '-', apply move-to-front encoding
    // if args[0] is '+', apply move-to-front decoding
    public static void main(String[] args) {
        if (args[0].equals("-")) {
            MoveToFront.encode();
        } 

        if (args[0].equals("+")) {
            MoveToFront.decode();
        }
    }
}
