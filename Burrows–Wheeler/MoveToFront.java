import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class MoveToFront {

    private static final int R = 256;

    public static void encode() {
        char[] chars = getArray();

        while (!BinaryStdIn.isEmpty()) {
            char c = BinaryStdIn.readChar();

            char tempIn, tempOut, i;

            for (i = 0, tempOut = 0; c != chars[i]; i++) {
                tempIn = chars[i];
                chars[i] = tempOut;
                tempOut = tempIn;
            }

            chars[i] = tempOut;
            BinaryStdOut.write(i);
            chars[0] = c;

        }
        BinaryStdOut.close();
    }

    public static void decode() {
        char[] chars = getArray();

        while (!BinaryStdIn.isEmpty()) {
            char i = BinaryStdIn.readChar();
            BinaryStdOut.write(chars[i], 8);
            char index = chars[i];
            while (i > 0) {
                chars[i] = chars[--i];
            }
            chars[i] = index;
        }
        BinaryStdOut.close();
    }

    private static char[] getArray() {
        char[] chars = new char[R];
        for (char i = 0; i < R; i++) {
            chars[i] = i;
        }
        return chars;
    }

    public static void main(String[] args) {
        if (args.length == 0)
            throw new IllegalArgumentException();
        if (args[0].equals("-"))
            encode();
        else if (args[0].equals("+"))
            decode();
        else
            throw new IllegalArgumentException();
    }
}
