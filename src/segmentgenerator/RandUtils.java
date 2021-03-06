package segmentgenerator;

import java.util.Random;


public class RandUtils {
   private static Random random;    // pseudo-random number generator
    private static long seed;        // pseudo-random number generator seed

    // static initializer
    static {
        // this is how the seed was set in Java 1.4
        seed = System.currentTimeMillis();
        random = new Random(seed);
    }

    /**
     * Sets the seed of the pseudorandom number generator.
     * This method enables you to produce the same sequence of "random"
     * number for each execution of the program.
     * Ordinarily, you should call this method at most once per program.
     *
     * @param s the seed
     */
    public static void setSeed(long s) {
        seed   = s;
        random = new Random(seed);
    }

    /**
     * Returns the seed of the pseudorandom number generator.
     *
     * @return the seed
     */
    public static long getSeed() {
        return seed;
    }

    /**
     * Returns a random real number uniformly in [0, 1).
     *
     * @return a random real number uniformly in [0, 1)
     */
    public static double uniform() {
        return random.nextDouble();
    }

    /**
     * Returns a random integer uniformly in [0, n).
     * 
     * @param n number of possible integers
     * @return a random integer uniformly between 0 (inclusive) and <tt>N</tt> (exclusive)
     * @throws IllegalArgumentException if <tt>n <= 0</tt>
     */
    public static int uniform(int n) {
        if (n <= 0) throw new IllegalArgumentException("Parameter N must be positive");
        return random.nextInt(n);
    }
    
    /**
     * Returns a random integer uniformly in [lo, hi].
     * 
     * @param lo the lower bound of the range
     * @param hi the upper bound of the range
     * @return a random integer uniformly between lo (inclusive) and hi (inclusive)
     * @throws IllegalArgumentException if <tt>lo <= 0  or hi <= 0 or hi <= lo </tt>
     */
    public static int uniform(int lo, int hi) {
        if ((lo <= 0) || (hi <= 0)) throw new IllegalArgumentException("Parameter must be positive");
        if (hi <= lo) throw new IllegalArgumentException("Range must be >= 1");
        return random.nextInt((hi - lo) + 1) + lo;
    }
    
    /**
     * Returns a random boolean uniformly in [true, false]
     * 
     * @return a random boolean uniformly either true or false
     */
    public static boolean uniformBool() {
        int v = random.nextInt(2);
        // v is either 0 or 1
        return v != 0;
    } 
}
