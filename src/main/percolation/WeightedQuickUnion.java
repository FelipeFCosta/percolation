package main.percolation;

// version of edu.princeton.cs.algs4.WeightedQuickUnionUF

public class WeightedQuickUnion {
    private int[] parent;   // parent[i] = parent of i;
    private int[] size;     // size[i] = number of elements in subtree rooted at i

    public WeightedQuickUnion(int arraySize) {
        parent = new int[arraySize];
        size = new int[arraySize];
        for(int i = 0; i < arraySize; i++) {
            parent[i] = i;              // Initially each element is the root of itself
            size[i] = 1;
        }
    }

    /**
     * Returns the canonical element of the set containing element {@code i}.
     *
     * @param  i an element
     * @return the canonical element of the set containing {@code i}
     * @throws IllegalArgumentException unless {@code 0 <= i < n}
     */
    public int find(int i) {
        validate(i);
        while(parent[i] != i)  {
            parent[i] = parent[parent[i]];   // path compression, keeps tree almost completely flat
            i = parent[i];
        }
        return i;
    }

    /**
     * Check if p and q have same root
     */
    public boolean connected(int p, int q) {
        return find(p) == find(q);
    }

    /**
     * Merges the set containing element {@code p} with the 
     * the set containing element {@code q}.
     *
     * @param  p one element
     * @param  q the other element
     * @throws IllegalArgumentException unless
     *         both {@code 0 <= p < n} and {@code 0 <= q < n}
     */
    public void union(int p, int q) { // O(logN)
        int rootP = find(p);
        int rootQ = find(q);
        if(rootP == rootQ) return;
        if(size[rootP] < size[rootQ]) {
            parent[rootP] = rootQ;
            size[rootQ] += size[rootP];
        }
        else {
            parent[rootQ] = rootP;
            size[rootP] += size[rootQ];
        }
    }

    private void validate(int p) {
        int n = size.length;
        if (p < 0 || p >= n) {
            throw new IllegalArgumentException("index " + p + " is not between 0 and " + (n-1));  
        }
    }
}
