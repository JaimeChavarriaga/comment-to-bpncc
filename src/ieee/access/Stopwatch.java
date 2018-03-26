package ieee.access;

/**
 *  Measures the time that elapses between the start and end of a
 *  programming task (wall-clock time).
 * 
 * @author Jaime Chavarriaga
 */
public class Stopwatch { 

    private final long start1;
    private final long start2;

    /**
     * Initializes a new stopwatch.
     */
    public Stopwatch() {
    	start1 = System.nanoTime();
    	start2 = System.nanoTime();
    } 


    /**
     * Returns the elapsed CPU time (in nanoseconds) since the stopwatch was created.
     *
     * @return elapsed CPU time (in nanoseconds) since the stopwatch was created
     */
    public long elapsedTime() {
    	long now = System.nanoTime();
    	long diff = now - 2L*start2 + start1;
    	return diff;
    }

} 