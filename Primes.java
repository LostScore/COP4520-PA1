// Manfred Le
// COP4520: Concepts of Parrallel and Distributed Proccessing
// Returns the execution time, the number of Primes, Sum of Primes
// Top 10 primes from smallest to largest. 
// (Optional) Add in an integer value when compiling to test for another value.
// Sieve of Eratos Algorithm from https://www.geeksforgeeks.org/sieve-of-eratosthenes/

import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

public class Primes implements Runnable {

    public static boolean DEBUG = false;
    private static AtomicInteger Counter = new AtomicInteger(2);
    private static boolean [] sieve;
    private static int endCount;

    public static void main(String [] args) throws IOException {
        int limit = 100000000, numPrimes;
        long startTime,endTime,executionTime,sumPrimes;
        Primes parrallel;
        Thread t0,t1,t2,t3,t4,t5,t6,t7;
        // If an argument is passed in then make it the limit
        // Error will occur if it surpasess Integer limit
        if(args.length > 0) {
            limit = Integer.parseInt(args[0]);
        }

        // Start the timer before you spawn the threads
        startTime = System.nanoTime();
        parrallel = new Primes(limit);
        t0 = new Thread(parrallel);
        t1 = new Thread(parrallel);
        t2 = new Thread(parrallel);
        t3 = new Thread(parrallel);
        t4 = new Thread(parrallel);
        t5 = new Thread(parrallel);
        t6 = new Thread(parrallel);
        t7 = new Thread(parrallel);

        // Begin thread execution (their run func)
        t0.start();
        t1.start();
        t2.start();
        t3.start();
        t4.start();
        t5.start();
        t6.start();
        t7.start();

        // Join the threads together to main thread
        try {
            t0.join();
            t1.join();
            t2.join();
            t3.join();
            t4.join();
            t5.join();
            t6.join();
            t7.join();
        } catch (InterruptedException e){
            e.printStackTrace();
        }

        // Stop the clock and convert it from ns > ms
        endTime = System.nanoTime();
        executionTime = endTime - startTime;
        executionTime = executionTime/1000000;
        numPrimes = getNumOfPrimes(sieve);
        sumPrimes = getSumOfPrimes(sieve);

        if (DEBUG){
            printPrime(sieve);
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

    public static void printPrime(boolean[] list) {
        for(int i = 0 ; i < list.length ; i++) {
            if(!list[i]) {
                System.out.print(i+" ");
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

    public static void printLastTenPrimes(boolean[] list, FileWriter writer) throws IOException{
        int[] lastTen = new int[10];
        int j = 9;

        // Goes from the back of the sieve enter prime numbers backwards in the array till 10;
        for(int i = list.length - 1; j >= 0 ; i--) {
            if(!list[i]) {
                lastTen[j] = i;
                j--;
            }
        }

        writer.write(Arrays.toString(lastTen));

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

    public void printThreadName() {
        String name = Thread.currentThread().getName();
        System.out.println("name=" + name + " Counter=" + Counter.get());
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
    
    
}
