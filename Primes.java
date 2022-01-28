// Manfred Le
// COP4520: Concepts of Parrallel and Distributed Proccessing
// Returns the execution time, the number of Primes, Sum of Primes
// Top 10 primes from smallest to largest. 
// (Optional) Add in an integer value when compiling to test for another value.
// (Optional**) After adding and integer flag to change the limit you can change the num of thread spawnned as well.
// Sieve of Eratos Algorithm from https://www.geeksforgeeks.org/sieve-of-eratosthenes/

import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.lang.Math;

public class Primes implements Runnable {

    public static boolean DEBUG = false;
    private static AtomicInteger Counter = new AtomicInteger(2);
    private static boolean [] sieve;
    private static int endCount;

    public static void main(String [] args) throws IOException {
        int limit = 100000000, numPrimes, numThread = 8;
        long startTime,endTime,executionTime,sumPrimes;
        Primes parrallel;
        // If an argument is passed in then make it the limit
        // Error will occur if it surpasess Integer limit
        if(args.length == 2) {
            if (Integer.parseInt(args[0]) < 29){
                System.out.println("Error: must enter an integer greater than 29");
                return;
            }
            limit = Integer.parseInt(args[0]);
            numThread = Integer.parseInt(args[1]);
            System.out.println("Running with N = "+limit+" and "+numThread+" Threads");
        }
        else if(args.length == 1) {
            if (Integer.parseInt(args[0]) < 29){
                System.out.println("Error: must enter an integer greater than 29");
                return;
            }
            limit = Integer.parseInt(args[0]);
            System.out.println("Running with N = "+limit);
        }
        // Start the timer before you spawn the threads
        startTime = System.nanoTime();
        parrallel = new Primes(limit);
        ExecutorService threadPool = Executors.newFixedThreadPool(numThread);
        for (int i = 2; i*i < limit; i++){
            threadPool.execute(parrallel);
        }
        threadPool.shutdown();
        try {
            threadPool.awaitTermination(2147483647, TimeUnit.SECONDS);
        }catch(InterruptedException e){
            e.printStackTrace();
        }
        // Stop the clock and convert it from ns > ms
        endTime = System.nanoTime();
        executionTime = (endTime - startTime)/1000000;
        numPrimes = getNumOfPrimes(sieve);
        sumPrimes = getSumOfPrimes(sieve);
        if (DEBUG){
            // printPrimes(sieve);
            System.out.print("Num Prime: "+numPrimes+" Runtime: "+executionTime);
        }
        // write to file primes.txt
        FileWriter writer = new FileWriter("primes.txt");
        // <execution time>  <total number of primes found>  <sum of all primes found>
        // <top ten maximum primes, listed in order from lowest to highest>
        String test = "<" + executionTime + " ms> <" + numPrimes + "> <" + sumPrimes + ">";
        writer.write(test);
        writer.write("\n");
        printLastTenPrimes(sieve, writer);
        writer.close();
    }

    // Initialized thread function that goes to limit
    // Boolean Array is initialized as false thus assume everything is true
    // False = Prime / True = Composite

    Primes(int limit) {
        // Handles the entire number
        Primes.sieve = new boolean[limit + 1];
        // Mark 0 and 1 as not prime
        Primes.sieve[0] = true;
        Primes.sieve[1] = true;
        // Get the Sqrt to improve the speed since any value > sqrt will go over the limit
        Primes.endCount = (int) Math.sqrt(limit);
    }

    // command will run and inpout every value from 2 - limit
    public void run() {
        int x = Counter.getAndIncrement();
        while(x <= endCount) {
            if (DEBUG)
                printThreadName();
            sieveOfEratosthenes(x);
            x = Counter.getAndIncrement();
        }
    }

    // Begin sieve code modified from
    // https://www.geeksforgeeks.org/sieve-of-eratosthenes/
    public static void sieveOfEratosthenes(int x){
        // Skips any even number to speed up time (Optimization) and passes 2
        if(x != 2 && x % 2 == 0) {
            return;
        }
        if(!sieve[x]) {
            // jumps to squared index and mark every multiple of it
            for(int i = x * x ; i < sieve.length ; i += x) {
                sieve[i] = true;
            }
        }
    }

    public static int getNumOfPrimes(boolean[] list) {
        int numPrime = 0;
        for(int i = 0 ; i < list.length ; i++) {
            if(!list[i]) {
                numPrime++;
            }
        }
        return numPrime;
    }

    public static long getSumOfPrimes(boolean [] list) {
        long sum = 0;
        for(int i = 2; i < list.length; i++) {
            if(!list[i]) {
                sum += i;
            }
        }
        return sum;
    }

    public static void printLastTenPrimes(boolean[] list, FileWriter writer) throws IOException{
        int[] topTen = new int[10];
        int j = 9;
        // Goes from the back of the sieve enter prime numbers backwards in the array till 10;
        for(int i = list.length - 1; j >= 0 ; i--) {
            if(!list[i]) {
                topTen[j] = i;
                j--;
            }
        }
        writer.write(Arrays.toString(topTen));
    }

    public void printThreadName() {
        String name = Thread.currentThread().getName();
        System.out.println("name=" + name + " Counter=" + Counter.get());
    }

    public static void printPrimes(boolean[] list) {
        for(int i = 0 ; i < list.length ; i++) {
            if(!list[i]) {
                System.out.print(i+" ");
            }
        }
    }
    
}
