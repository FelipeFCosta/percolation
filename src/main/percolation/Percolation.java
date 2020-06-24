package main.percolation;

/*
    N = 3;
            top
           / | \
         /   |   \
        1    2    3
        4    5    6
        7    8    9
        \    |   /
          \  |  /
           bottom
*/

public class Percolation {
    /**
     * Used to set an n-by-n percolation grid
    */
    private final int n;

    /**
     * Grid size + virtual top + virtual bottom
     */
    private final int length;

    /**
     * Number of open sites
     */
    private int countOpenSites;

    /**
     * Helps the performance by connecting it to the top row of the grid
     */
    private final int virtualTop;

    /**
     * Helps the performance by connecting it to the bottom row of the grid
     */
    private final int virtualBottom;

    /**
     * Runs the union-find algorithm in order to simulate a percolation system 
     */
    private final WeightedQuickUnion percolation;

    /**
     * Keeps track of the sites that are open.
     */
    private boolean[] openSites;
    /**
     * Keeps track of the sites that are full.
     * A site is full if its root is equal to 0.
     * 
     * This {@code WeightedQuickUnion} object prevents the virtualBottom from filling,
     * and hence causing a bug.
     */
    private final WeightedQuickUnion full;

    /** 
     * Creates n-by-n grid, with all sites initially blocked
     * @param n to set an n-by-n percolation grid
    */
    public Percolation(int n) {
        if (n < 1)
            throw new IllegalArgumentException();

        countOpenSites = 0;
        this.n = n;
        length = n*n + 2;
            
        percolation = new WeightedQuickUnion(length);
        full = new WeightedQuickUnion(length);
        virtualTop = 0;
        virtualBottom = length - 1;

        openSites = new boolean[length];

        // connect the virtual top to first row's sites and virtual bottom to the last row
        for (int i = 1; i <= n; i++) {
            full.union(virtualTop, i);
            percolation.union(virtualTop, i);
            percolation.union(virtualBottom, virtualBottom-i);
        }
    }

    /** 
     * Opens the site if it is not open already
     * @param idx the index of the site to open
    */
    public void open(int idx) {
        checkArgument(idx);
        if (!openSites[idx]) {
            openSites[idx] = true;
            countOpenSites++;

            if (idx - n >= 1)        neighborUnion(idx-n, idx); // if has up neighbor 
            if (idx + n <= length-2) neighborUnion(idx+n, idx); // if has down neighbor
            if ((idx - 1) % n != 0)  neighborUnion(idx-1, idx); // if has left neighbor
            if (idx % n != 0)        neighborUnion(idx+1, idx); // if has right neighbor

        }
    }

    /**
     * Connects a set to a neighbor, if the latter is open
     * @param neighborIdx the up, down, left or right position adjacent to the idx.
     * @param idx the index to which the neighborIdx is adjacent
    */
    private void neighborUnion(int neighborIdx, int idx) {
        if (isOpen(neighborIdx)) {
            percolation.union(neighborIdx, idx);
            full.union(neighborIdx, idx);
        }
    }

    /**
     * @param idx the index of the site to verify
     * @return {@code true} if the site is open
     */
    public boolean isOpen(int idx) {
        checkArgument(idx);
        return openSites[idx];
    }

    /**
     * @param idx is the site[idx] full ?
     * @return
     */
    public boolean isFull(int idx) {
        checkArgument(idx);
        return isOpen(idx) && full.connected(idx, virtualTop);
    }

    /**
     * Just an information
     *  @return the number of open sites
    */
    public int numberOfOpenSites() {
        return countOpenSites;
    }

    /**
     * The system percolates if the virtualBottom is connected to the virtualTop
     * @return {@code true} if the system percolates
     */
    public boolean percolates() {
        return n > 1 ? percolation.connected(virtualBottom, virtualTop) : isOpen(1);
    }

    /**
     * @param idx check if the index represents a position in the grid
     */
    private void checkArgument(int idx) {
        if (idx > length || idx < 1)
            throw new IllegalArgumentException();
    }

    public static void main(String[] args) {
        // Percolation uf = new Percolation(5);
        // uf.open(2);
        // uf.open(7);
        // uf.open(12);
        // uf.open(23);
        // uf.open(18);
        // uf.open(17);
        // uf.open(25);
        // System.out.println("number of open sites: " + IntStream.range(1, uf.openSites.length-1)
        //                                                        .filter(i -> uf.openSites[i])
        //                                                        .map(i -> 1).sum());
        // System.out.println("is 12 open? " + uf.isOpen(12));
        // System.out.println("is 7 open? " + uf.isOpen(7));
        // System.out.println("is 17 open? " + uf.isOpen(17));
        // System.out.println("is 10 open? " + uf.isOpen(10));
        // System.out.println("is 12 full? " + uf.isFull(12));
        // System.out.println("is 18 full? " + uf.isFull(8));
        // System.out.println("is 24 full? " + uf.isFull(24));
        // System.out.println("is 10 full? " + uf.isFull(10));
        // System.out.println("is 2 full? " + uf.isFull(2));
        // System.out.println("is 17 full? " + uf.isFull(17));
        // System.out.println("is 22 full? " + uf.isFull(22));
        // System.out.println("does the system percolate? " + uf.percolates());
    }
}
