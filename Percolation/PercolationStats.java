import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private double[] x;

    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) throw new IllegalArgumentException();
        x = new double[trials];
        for (int i = 0; i < trials; i++) {
            Percolation p = new Percolation(n);
            while (!p.percolates()) {
                int row = StdRandom.uniform(1, n + 1);
                int col = StdRandom.uniform(1, n + 1);
                if (p.isOpen(row, col)) continue;
                p.open(row, col);
            }
            x[i] = (double) p.numberOfOpenSites() / (n * n);

        }
    }

    public double mean() {
        return StdStats.mean(x);
    }               // sample mean of percolation threshold

    public double stddev() {
        return StdStats.stddev(x);
    }

    public double confidenceLo() {
        return mean() - 1.96 * stddev() / Math.sqrt(x.length);

    }

    public double confidenceHi() {
        return mean() + 1.96 * stddev() / Math.sqrt(x.length);
    }

    public static void main(String[] args) {
        PercolationStats p = new PercolationStats(Integer.parseInt(args[0]), Integer.parseInt(args[1]));

        System.out.printf("mean                    = %f\n", p.mean());
        System.out.printf("stddev                  = %f\n", p.stddev());
        System.out.printf("95%% confidence interval = [%f, %f]\n",
                p.confidenceLo(), p.confidenceHi());
    }
}
