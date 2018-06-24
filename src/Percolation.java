import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private static final int OPENED = 1;
    private static final int CLOSED = 0;
    private boolean bottomFull;
    private final int n;
    private int opensites;
    private int[][] site;
    private final int virtualTopsite;
    private final int virtualBottomsite;
    private final WeightedQuickUnionUF weightedQuickUnionUF;

    public Percolation(int n) {
        validate(n);
        this.n = n;
        site = new int[n][n];
        bottomFull = false;
        opensites = 0;
        for (int i = 0; i < n; i++)

            for (int j = 0; j < n; j++)
                site[i][j] = CLOSED;

        this.weightedQuickUnionUF = new WeightedQuickUnionUF((n * n) + 2);
        virtualTopsite = 0;
        virtualBottomsite = n * n + 1;
    }

    // Open a closed site and connect it with all neighbouring open sites
    public void open(int row, int col) {
        validate(row, col);
        if (!isOpen(row, col)) {
            row = row - 1;
            col = col - 1;
            site[row][col] = OPENED;
            opensites++;
            connectNeigbouringSites(row, col);
        }
    }

    public boolean isOpen(int row, int col) {
        validate(row, col);
        return site[row - 1][col - 1] == OPENED;
    }

    public boolean isFull(int row, int col) {
        bottomFull = false;
        validate(row, col);
        if (isOpen(row, col)) {
            row = row - 1;
            col = col - 1;
            connectNeigbouringSites(row, col);
            bottomFull = isLastRow(row) && weightedQuickUnionUF.connected(getSiteId(row, col), 0);
        }
        return bottomFull;
    }

    public int numberOfOpenSites() {
        return opensites;
    }

    public boolean percolates() {

        return weightedQuickUnionUF.connected(virtualTopsite, virtualBottomsite);
    }

    private void validate(int row, int col) {
        if (row - 1 < 0 || row - 1 >= n) {
            throw new IllegalArgumentException("row " + row + " is not between 0 and " + (n - 1));
        }
        if (col - 1 < 0 || col - 1 >= n) {
            throw new IllegalArgumentException("column " + col + " is not between 0 and " + (n - 1));
        }
    }

    private void validate(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException(n + " must be greater than 0 ");
        }
    }

    private int getSiteId(int row, int col) {
        // Based on sites with zero indexes
        return (n * row) + col + 1;
    }

    private boolean isFirstCol(int col) {
        return col == 0;
    }

    private boolean isLastCol(int col) {
        return col == n - 1;
    }

    private boolean isFirstRow(int row) {
        return row == 0;
    }

    private boolean isLastRow(int row) {
        return row == n - 1;
    }

    private void connectNeigbouringSites(int row, int col) {
        if (isFirstRow(row)) {
            // Connect to imaginary top site
            weightedQuickUnionUF.union(virtualTopsite, getSiteId(row, col));
        }
        if (isLastRow(row)) {
            // connect to imaginary bottom site
            weightedQuickUnionUF.union(virtualBottomsite, getSiteId(row, col));
        }
        // check if left side of the curent site is opened and perform a connection
        if (!isFirstCol(col) && (site[row][col - 1]) == OPENED) {
            weightedQuickUnionUF.union(getSiteId(row, col), getSiteId(row, col - 1));
        }
        // check if right side of the curent site is opened and perform a connection
        if (!isLastCol(col) && (site[row][col + 1]) == OPENED) {
            weightedQuickUnionUF.union(getSiteId(row, col), getSiteId(row, col + 1));
        }
        // check if top side of the curent site is opened and perform a connection
        if (!isFirstRow(row) && (site[row - 1][col]) == OPENED) {
            weightedQuickUnionUF.union(getSiteId(row, col), getSiteId(row - 1, col));
        }
        // check if bottom side of the curent site is opened and perform a connection
        if (!isLastRow(row) && (site[row + 1][col]) == OPENED) {
            weightedQuickUnionUF.union(getSiteId(row, col), getSiteId(row + 1, col));
        }

    }

}

