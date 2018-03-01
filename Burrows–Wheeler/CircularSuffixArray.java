import java.util.Arrays;

public class CircularSuffixArray {
    private final Integer[] index;

    public CircularSuffixArray(String s) {
        if (s == null) throw new IllegalArgumentException();
        index = new Integer[s.length()];

        for (int i = 0; i < s.length(); i++) {
            index[i] = i;
        }

        Arrays.sort(index, (a, b) -> {
            int indexA = a;
            int indexB = b;

            for (int i = 0; i < s.length(); i++) {
                if (indexA > s.length() - 1)
                    indexA = 0;
                if (indexB > s.length() - 1)
                    indexB = 0;

                if (s.charAt(indexA) < s.charAt(indexB))
                    return -1;
                else if (s.charAt(indexA) > s.charAt(indexB))
                    return 1;

                indexA++;
                indexB++;
            }

            return 0;
        });
    }

    public int length() {
        return index.length;
    }

    public int index(int i) {
        if (i < 0 || i > index.length - 1) throw new IllegalArgumentException();
        return index[i];
    }

    public static void main(String[] args) {
        CircularSuffixArray csa = new CircularSuffixArray("ABGC");
        for (int i = 0; i < csa.length(); i++) System.out.println(csa.index(i));
        System.out.println();
    }
}