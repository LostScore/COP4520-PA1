Created By: Manfred Le
Purpose: Assignment 1
Course: COP4520: Concepts of Parrallel and Distributed Processing
1/28/2022

Requirements:
JDK version 8+ is required

To Compile:
Visual Studio Code with Java Extension
1. Open file with Visual Studio Code
2. Hit Run for the file.
Command Line
1. Navigate to folder containing file
2. javac Primes.java
3. java Primes.java (Optional int Flag)
Note: program checks for any commandline arguements, so if a valid Integer is inputted then it will replace the limit (currently 10^9)
Note**: Integer needs to have atleast 10 primes below it.

Algorithm Used: Sieve of Eratosthenes (https://www.geeksforgeeks.org/sieve-of-eratosthenes/)

Proof of Algorithm.
https://math.libretexts.org/Bookshelves/Combinatorics_and_Discrete_Mathematics/Elementary_Number_Theory_(Raji)/02%3A_Prime_Numbers/2.01%3A_The_Sieve_of_Eratosthenes

Proof by Contradiction:
Suppose that we have an interger N that is > 0;
then all composite numbers have prime factores <= sqrt(N)
we take a value n that has two differnt factors a and b such that
	1 < a <= b < n
Suppose that a > sqrt(n) which would mean that
	sqrt(n) < a <= b
thus
	ab > sqrt(n)^2 = n
this would mean that there is an integer that is also a prime divisor of n.

CS Algorithm

1.Creating an array of booleans N we can cross out and check which values are potentially primes.
2.Starting at 2, we cross out every number that is the squared of 2 and keep adding until we reach N
3.Move to the next uncross boolean value
4.Using the index of this boolean value move the squared value of this index
5.Cross out values moving by the index value
6.Repeat steps 3-5
7.Continue untill we reach the end of the table.

Improvements with Parralellism / Multithreading.

Using an Atomic Integer we assure that the threads will not doubleup and do the same work on tasks because it will lock and guarentte that the value will the returned
and incremented will be unique to each thread. This allows the thread to cross out multiple different values on the boolean arrays without them overlaping in resources.
For example, thread1 will be able to cross off the intial 2. Thread2 will move onto 3 and begin to cross of any values that is necessary. This will continue until we reach the end
with the atomic variable ensureing that work isn't duplicated. In the example Thread 3 will start at 4 which isn't a prime so it will return and move onto 5. There is danger
in synchronization of the boolean array fortunately however the thread marks off early value early and the distance between primes only increases.


Experimental Evaluation
-Speed
When tested at N = 10E5 that Single Thread ran faster compared to the multithreaded one
After a collection of 100 trials it was shown that the average speed for a single thread was 898.29 ms and for multi-thread was 649.44 ms. This was a 27.7% improvement
with this implementation.
-Testing with different Integers
After testing and recording down the speed of 100 trials from both single / multithreaded code with the integer ranging from 10E2 - 10E9
both values (num of primes / sum of primes / and last 10 array) show similar output meaning the the algorithm integrity was maintained.

Data Link
https://docs.google.com/spreadsheets/d/17ft9PaBDS0deXIu3G7HO1bnuPIAmf3ce/edit?usp=sharing&ouid=111049079785315663025&rtpof=true&sd=true