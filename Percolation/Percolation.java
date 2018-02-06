import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private int n, opens;
    private boolean[][] sites;
    private WeightedQuickUnionUF uf, buf;

    public Percolation(int n) {
        if (n <= 0) throw new IllegalArgumentException();
        this.n = n;
        sites = new boolean[n][n];

        // n * n sites plus virtual top / bottom
        uf = new WeightedQuickUnionUF(n * n + 2);

        // only virtual top to avoid backwash
        buf = new WeightedQuickUnionUF(n * n + 1);

        for (int i = 1; i <= n; i++) {
            uf.union(0, i);
            buf.union(0, i);
            uf.union(n * n - i + 1, n * n + 1);
        }
    }

    public void open(int row, int col) {
        validate(row, col);
        if (isOpen(row, col)) return;
        sites[row - 1][col - 1] = true;
        int current = index(row, col);
        opens++;

        if (row > 1 && isOpen(row - 1, col)) {
            uf.union(current - n, current);
            buf.union(current - n, current);
        }

        if (row < n && isOpen(row + 1, col)) {
            uf.union(current + n, current);
            buf.union(current + n, current);
        }

        if (col > 1 && isOpen(row, col - 1)) {
            uf.union(current - 1, current);
            buf.union(current - 1, current);
        }
        
        if (col < n && isOpen(row, col + 1)) {
            uf.union(current + 1, current);
            buf.union(current + 1, current);
        }
    }

    public boolean isOpen(int row, int col) {
        validate(row, col);
        return sites[row - 1][col - 1];
    }

    public boolean isFull(int row, int col) {
        validate(row, col);
        if (!isOpen(row, col)) return false;
        int current = index(row, col);
        return buf.connected(0, current);
    }

    public int numberOfOpenSites() {
        return opens;
    }

    public boolean percolates() {
        if (n == 1) return isOpen(1, 1);
        return uf.connected(0, n * n + 1);
    }

    private void validate(int row, int col) {
        if (row <= 0 || row > n || col <= 0 || col > n) throw new IndexOutOfBoundsException();
    }

    private int index(int row, int col) {
        return n * (row - 1) + col;
    }

    public static void main(String[] args) {

    }
}