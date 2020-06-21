package main.percolation;

// version of edu.princeton.cs.algs4.WeightedQuickUnionUF

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class WeightedQuickUnion {
    private int[] id;   // id[i] = parent id of i;
    private int[] size; // size[i] = number of elements in subtree rooted at i
    private int count;  // number of components

    public WeightedQuickUnion(int arraySize) {
        count = arraySize;
        id = new int[arraySize];
        size = new int[arraySize];
        for(int i = 0; i < arraySize; i++) {
            id[i] = i;              // Initially each element is the root of itself
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
        while(id[i] != i)  {
            id[i] = id[id[i]];
            i = id[i];
        }
        return i;
    }

    // O(logN)
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
        count--;
        int rootP = find(p);
        int rootQ = find(q);
        if(rootP == rootQ) return;
        if(size[rootP] < size[rootQ]) {
            id[rootP] = rootQ;
            size[rootQ] += size[rootP];
        }
        else {
            id[rootQ] = rootP;
            size[rootP] += size[rootQ];
        }
    }

    public int getComponents() {
        return count;
    }

    private void validate(int p) {
        int n = size.length;
        if (p < 0 || p >= n) {
            throw new IllegalArgumentException("index " + p + " is not between 0 and " + (n-1));  
        }
    }

    public void printRoots() {
        int n = (int) Double.parseDouble("" + Math.sqrt(id.length-2));
        System.out.println(id[0]);
        for (int i = 1; i <= id.length - 2; i++) {
            System.out.print(find(i) + "\t");
            if (i % n == 0) System.out.println();
        }
        System.out.println(id[id.length-1]);
    }

    public long randomUnionTime(int operations, File file) throws FileNotFoundException {
        long initialTime = System.currentTimeMillis();
        Scanner element = new Scanner(file);
        for(int i = 0; i < operations; i++)
            union(element.nextInt(), element.nextInt());
        element.close();
        long finalTime = System.currentTimeMillis();
        return finalTime - initialTime;
    }

    public long randomFindTime(int operations, File file) throws FileNotFoundException {
        long initialTime = System.currentTimeMillis();
        Scanner element = new Scanner(file);
        for(int i = 0; i < operations; i++)
            connected(element.nextInt(), element.nextInt());
        element.close();
        long finalTime = System.currentTimeMillis();
        return finalTime - initialTime;
    }
}