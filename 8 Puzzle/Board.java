import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Board {
    private int[][] blocks;
    private int n;

    public Board(int[][] blocks) {
        n = blocks.length;
        this.blocks = new int[n][n];
        for (int i = 0; i < n; i++) {
            this.blocks[i] = Arrays.copyOf(blocks[i], n);
        }
    }

    public int dimension() {
        return n;
    }

    public int hamming() {
        int result = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (blocks[i][j] == 0) continue;
                if (blocks[i][j] != i * dimension() + j + 1) result++;
            }
        }
        return result;
    }

    public int manhattan() {
        int result = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (blocks[i][j] == 0) continue;
                int oi = (blocks[i][j] - 1) / n;
                int oj = (blocks[i][j] - 1) % n;
                result += Math.abs(oi - i) + Math.abs(oj - j);
            }
        }
        return result;
    }            // sum of Manhattan distances between blocks and goal

    public boolean isGoal() {
        return hamming() == 0;
    }// is this board the goal board?

    public Board twin() {
        Board twin = new Board(blocks);
        outer:
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n - 1; j++) {
                if (twin.blocks[i][j] != 0 && twin.blocks[i][j + 1] != 0) {
                    twin.swap(i, j, i, j + 1);
                    break outer;
                }
            }
        }
        return twin;

    }

    public boolean equals(Object y) {
        if (y == null) return false;
        if (y.getClass() != Board.class) return false;
        Board that = (Board) y;
        if (that.n != this.n) return false;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (that.blocks[i][j] != this.blocks[i][j]) return false;
            }
        }
        return true;
    }

    public Iterable<Board> neighbors() {
        int empty_i = -1, empty_j = -1;
        outer:
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (blocks[i][j] == 0) {
                    empty_i = i;
                    empty_j = j;
                    break outer;
                }
            }
        }

        List<Board> neighbors = new ArrayList<>();
        Board board;
        if (empty_i > 0) {
            board = new Board(blocks);
            board.swap(empty_i, empty_j, empty_i - 1, empty_j);
            neighbors.add(board);
        }
        if (empty_i < n - 1) {
            board = new Board(blocks);
            board.swap(empty_i, empty_j, empty_i + 1, empty_j);
            neighbors.add(board);
        }
        if (empty_j > 0) {
            board = new Board(blocks);
            board.swap(empty_i, empty_j, empty_i, empty_j - 1);
            neighbors.add(board);
        }
        if (empty_j < n - 1) {
            board = new Board(blocks);
            board.swap(empty_i, empty_j, empty_i, empty_j + 1);
            neighbors.add(board);
        }
        return neighbors;

    }

    private void swap(int i, int j, int k, int l) {
        int temp = blocks[i][j];
        blocks[i][j] = blocks[k][l];
        blocks[k][l] = temp;
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(n);
        s.append("\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                s.append(String.format("%2d ", blocks[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    public static void main(String[] args) {
        int[][] board = {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 0}
        };
        Board b = new Board(board);
        System.out.println(b.hamming());
    }
}