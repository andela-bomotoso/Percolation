import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.StdRandom;

public class PercolationStats {
    private static final double CONFIDENCE_95 = 1.96;
    private double CONFIDENCE_STDDEV;
    private final int trials;
    private double[] thresholds;

    public PercolationStats(int n, int trials) {
        validate(n, trials);
        this.trials = trials;
        this.thresholds = new double[trials];
        for (int i = 0; i < trials; i++) {
            Percolation percolation = new Percolation(n);
            while (!percolation.percolates()) {
                int randomRow = StdRandom.uniform(n) + 1;
                int randomCol = StdRandom.uniform(n) + 1;
                percolation.open(randomRow, randomCol);
            }
            thresholds[i] = ((double) percolation.numberOfOpenSites()) / (n * n);
        }
        CONFIDENCE_STDDEV = CONFIDENCE_95 * stddev();
    } // perform trials independent experiments on an n-by-n grid

    public double mean() {
        return StdStats.mean(thresholds);
    }                      // sample mean of percolation threshold

    public double stddev() {
        return StdStats.stddev(thresholds);

    }                      // sample standard deviation of percolation threshold

    public double confidenceLo() {
        return mean() - (CONFIDENCE_STDDEV / Math.sqrt(trials));
    }                // low  endpoint of 95% confidence interval

    public double confidenceHi() {
        return mean() + (CONFIDENCE_STDDEV / Math.sqrt(trials));
    } // high endpoint of 95% confidence interval

    private void validate(int n, int trials) {
        if (n <= 0) {
            throw new IllegalArgumentException("n cannot be " + n + ",it must be greater than 0 ");
        }
        if (trials <= 0) {
            throw new IllegalArgumentException("trials cannot be " + trials + ",it must be greater than 0 ");
        }
    }

    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);
        PercolationStats percolationStats = new PercolationStats(n, trials);
        System.out.printf("%-30s%s%f\n", "mean", "=", percolationStats.mean());
        System.out.printf("%-30s%s%f\n", "stddev", "=", percolationStats.stddev());
        System.out.printf("%-30s%s%s%f%s%f%s\n", "95% confidence interval", "=",
                "[", percolationStats.confidenceLo(), ",", percolationStats.confidenceHi(), "]");
    }
}
