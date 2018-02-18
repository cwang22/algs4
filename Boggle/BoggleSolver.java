import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.HashSet;
import java.util.Set;

public class BoggleSolver {
    private final BoggleTrieSET dict;

    private enum Direction {
        NW(-1, -1), N(-1, 0), NE(-1, 1), E(0, 1), SE(1, 1), S(1, 0), SW(1, -1), W(0, -1);

        private final int x;
        private final int y;

        Direction(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    public BoggleSolver(String[] dictionary) {
        if (dictionary == null) throw new IllegalArgumentException();
        this.dict = new BoggleTrieSET();
        for (String word : dictionary) dict.add(word);
    }

    public Iterable<String> getAllValidWords(BoggleBoard board) {
        if (board == null) throw new IllegalArgumentException();

        Set<String> words = new HashSet<>();
        boolean[][] visited = new boolean[board.rows()][board.cols()];

        for (int i = 0; i < board.rows(); i++) {
            for (int j = 0; j < board.cols(); j++) {
                dfs(board, visited, i, j, new StringBuilder(), words);
            }
        }

        return words;
    }

    private void dfs(BoggleBoard board, boolean[][] visited, int i, int j, StringBuilder sb, Set<String> words) {
        if (i < 0 || i > visited.length - 1 || j < 0 || j > visited[0].length - 1) return;
        if (visited[i][j]) return;

        char current = board.getLetter(i, j);

        if (current == 'Q') sb.append("QU");
        else sb.append(current);

        visited[i][j] = true;
        String word = sb.toString();

        if (word.length() > 2 && dict.contains(word)) words.add(word);

        if (dict.hasPrefix(word)) {
            for (Direction d : Direction.values()) {
                dfs(board, visited, i + d.x, j + d.y, sb, words);
            }
        }

        visited[i][j] = false;

        if (current == 'Q') sb.delete(sb.length() - 2, sb.length());
        else sb.deleteCharAt(sb.length() - 1);
    }

    public int scoreOf(String word) {
        if (!dict.contains(word)) return 0;

        switch (word.length()) {
            case 0:
            case 1:
            case 2:
                return 0;
            case 3:
            case 4:
                return 1;
            case 5:
                return 2;
            case 6:
                return 3;
            case 7:
                return 5;
            default:
                return 11;
        }
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }
}
