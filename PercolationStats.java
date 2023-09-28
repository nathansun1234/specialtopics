import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class PercolationStats {
    private static final double CONFIDENCE_CONSTANT = 1.96;

    private int trials; // number of experiments ran
    private double[] results; // results of each experiment

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n < 1 || trials < 0) {
            throw new IllegalArgumentException();
        }

        this.trials = trials;
        results = new double[trials];

        for (int i = 0; i < trials; i++) {
            Percolation perc = new Percolation(n);
            while (!perc.percolates()) {
                // chooses random row and column to open. need to add one to both bc row and column start counting at 1
                int row = StdRandom.uniformInt(0, n) + 1;
                int col = StdRandom.uniformInt(0, n) + 1;

                if (!perc.isOpen(row, col)) {
                    perc.open(row, col);
                }
            }
            double threshold = perc.numberOfOpenSites() / (double) (n * n);
            results[i] = threshold;
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(results);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(results);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean() - (CONFIDENCE_CONSTANT * stddev() / Math.sqrt(trials));
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean() + (CONFIDENCE_CONSTANT * stddev() / Math.sqrt(trials));
    }

   // test client (see below)
   public static void main(String[] args) {
    int n = Integer.parseInt(args[0]);
    int trials = Integer.parseInt(args[1]);
    PercolationStats runner = new PercolationStats(n, trials);

    String confidence = runner.confidenceLo() + ", " + runner.confidenceHi();
    StdOut.println("mean                    = " + runner.mean());
    StdOut.println("stddev                  = " + runner.stddev());
    StdOut.println("95% confidence interval = " + confidence);
   }

}
