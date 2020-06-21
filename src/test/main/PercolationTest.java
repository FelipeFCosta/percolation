 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.main;


import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collection;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import main.percolation.Percolation;

/**
 *
 * @author FelipeFcosta
 */
@RunWith(Parameterized.class)
public class PercolationTest {
    private static int N;
    private static boolean expected;
    private static Percolation p;
    
    public PercolationTest(int N, boolean expected) {
        PercolationTest.N = N;
        PercolationTest.expected = expected;
    }
    
    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {     
                 { 2, true }, { 3, false }, { 5, false }, { 10, false },
                 { 20, false }, { 30, false }, { 40, false }  
            });
    }

    @Before
    public void setUpClass() {
    }

    @After
    public void tearDownClass() {
    }

    @BeforeClass
    public static void setUp() {
    }

    @AfterClass
    public static void tearDown() {
    }

    public int pairToIdx(int x, int y) {
        return (x - 1)*N + y;
    }
    /**
    * Test of isFull method, of class Percolation.
    */
    @Test
    public void testIsFull() {
       p = new Percolation(N);
       System.out.println("isFull");

       for (int i = 1; i <= N; i++) {
           p.open(pairToIdx(i, N));
       }

       for (int i = 1; i < (N > 2 ? N - 1 : N); i++) {
           p.open(pairToIdx(N, i));
       }

       boolean result = p.isFull(pairToIdx(N, 1));
       assertEquals(expected, result);
   }

   /**
    * Test of percolates method, of class Percolation.
    */
   @Test
   public void testPercolates() {
       p = new Percolation(N);
       System.out.println("percolates");

       // opens sites forming an 'X' in the grid
       for (int i = 1; i <= N; i++) {
           p.open(pairToIdx(i, i));           // main diagonal
           p.open(pairToIdx(i, N - i + 1));   // other diagonal
       }

       boolean result = p.percolates();
       assertEquals(expected, result);
   }  
}
