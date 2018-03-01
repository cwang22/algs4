import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class BurrowsWheeler {
    private static final int R = 256;


    public static void transform() {
        String in = BinaryStdIn.readString();
        CircularSuffixArray csa = new CircularSuffixArray(in);
        int first = 0;
        while (first < csa.length() && csa.index(first) != 0) {
            first++;
        }
        BinaryStdOut.write(first);
        for (int i = 0; i < csa.length(); i++) {
            BinaryStdOut.write(in.charAt((csa.index(i) + in.length() - 1) % in.length()));
        }
        BinaryStdOut.close();
    }

    public static void inverseTransform() {
        int first = BinaryStdIn.readInt();
        String s = BinaryStdIn.readString();
        int n = s.length();

        int[] count = new int[R + 1];
        int[] next = new int[n];

        for (int i = 0; i < n; i++) {
            count[s.charAt(i) + 1]++;
        }

        for (int i = 1; i < R + 1; i++) {
            count[i] += count[i - 1];
        }

        for (int i = 0; i < n; i++) {
            next[count[s.charAt(i)]++] = i;
        }

        for (int i = next[first], c = 0; c < n; i = next[i], c++) {
            BinaryStdOut.write(s.charAt(i));
        }

        BinaryStdOut.close();
    }

    public static void main(String[] args) {
        if (args.length == 0)
            throw new IllegalArgumentException();
        if (args[0].equals("-"))
            transform();
        else if (args[0].equals("+"))
            inverseTransform();
        else
            throw new IllegalArgumentException();
    }
}
