import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation { 
    private int len;
    private int totalsize;
    private boolean[] grid;
    private int openSites;

    private int start;
    private int end;

    private WeightedQuickUnionUF uf;
    private WeightedQuickUnionUF full;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n < 1) {
            throw new IllegalArgumentException();
        }

        // initializes grid and relevant params
        len = n;
        totalsize = len * len + 2; // size of grid + start + end
        grid = new boolean[totalsize]; // true = open, false = closed
        openSites = 0;

        // initializes index of start and end sites, sets them to true (open) in the grid
        start = 0;
        end = totalsize - 1;
        grid[start] = true;
        grid[end] = true;

        // creates weightedquickunionuf objects
        uf = new WeightedQuickUnionUF(grid.length + 2);
        full = new WeightedQuickUnionUF(grid.length + 2);  
    }

    // private helper method to convert between 2d and 1d. 
    private int convertToIndex(int row, int col) {
        return (row - 1) * len + col;
    }

    private void checkRange(int row, int col) {
        if (row > len || row < 1 || col > len || col < 1) {
            throw new IllegalArgumentException();
        }
    }
    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        checkRange(row, col);
        int index = convertToIndex(row, col);

        if (!isOpen(row, col)) {
            grid[index] = true;
            openSites++;

            if (row == 1) { // if its in the top row, connect to start
                uf.union(index, start);
                full.union(index, start);
            }
            if (row == len) { // if its in the bottom row, connect to end
                uf.union(index, end);
                // we don't connect full to avoid backwash
            }

            // now we check if the neighboring sites are open, if they are we need to connect them
            if (row > 1 && isOpen(row - 1, col)) { // check top
                uf.union(index, convertToIndex(row - 1, col));
                full.union(index, convertToIndex(row - 1, col));
            }  
            if (row < len && isOpen(row + 1, col)) { // check bottom
                uf.union(index, convertToIndex(row + 1, col));
                full.union(index, convertToIndex(row + 1, col));
            }  
            if (col > 1 && isOpen(row, col - 1)) { // check left
                uf.union(index, convertToIndex(row, col - 1));
                full.union(index, convertToIndex(row, col - 1));
            }  
            if (col < len && isOpen(row, col + 1)) { // check right
                uf.union(index, convertToIndex(row, col + 1));
                full.union(index, convertToIndex(row, col + 1));
            }
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        checkRange(row, col);
        int index = convertToIndex(row, col);
        return grid[index];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        checkRange(row, col);
        int index = convertToIndex(row, col);
        return full.find(index) == full.find(start); // if the sets start and index are in are equal, they must be connected and thus the system percolates
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return openSites;
    }

    // does the system percolate?
    public boolean percolates() {
        return uf.find(start) == uf.find(end); // if the sets start and end are in are equal, they must be connected and thus the system percolates
    }
    
    public static void main(String[] args) {
        
    }
}
