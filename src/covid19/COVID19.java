/**
 * Programmer: Chris Tralie
 * Purpose: To create a simple Monte Carlo simulation of a
 * spreading epidemic, using only arrays, methods, and loops.  A bunch
 * of people are placed uniformly at random on a square grid, and a single
 * one of them starts off infected.  Points that are moving then take
 * a random walk
 */
package covid19;

import java.util.Random;

public class COVID19 {
    // The three states a person can be in
    public static final int INFECTED = 0;
    public static final int UNINFECTED = 1;
    public static final int RECOVERED = 2;
    
    
    /**
     * Do a random walk on each point that's moving
     * 
     * @param xcoords An array of the x coordinates of all people
     * @param ycoords An array of the y coordinates of all people
     * @param isMoving An array of booleans indicating whether each person
     *                  is moving or not
     * @param pixel Drawing size of each person
     */
    public static void doRandomWalks(double[] xcoords, double[] ycoords, boolean[] isMoving, double pixel){
        int N = xcoords.length;
        Random rand = new Random();
        for (int i = 0; i < N; i++) {
            if (isMoving[i]) {
                int choice = rand.nextInt(4);
                if (choice == 0 && xcoords[i] > 0) {
                    xcoords[i] -= pixel;
                }
                else if (choice == 1 && xcoords[i] < 1) {
                    xcoords[i] += pixel;
                }
                else if (choice == 2 && ycoords[i] < 1) {
                    ycoords[i] += pixel;
                }
                else if (choice == 3 && ycoords[i] > 0) {
                    ycoords[i] -= pixel;
                }
            }
        }
    }
    
    /**
     * 
     * @param numPeople The total number of people in the simulation
     * @param numMoving The total number of people in the simulation who are moving
     * @param numHours The total number of hours in the simulation
     * @param res The resolution of the simulation
     * @param recoveryTime The number of hours it takes to recover from being infected
     */
    public static void initializePandemic(int numPeople, int numMoving, int numHours, int res, int recoveryTime) {
        double pixel = 1.0/res;
        
        // Arrays for holding the current state of all people;
        double[] xcoords = new double[numPeople];
        double[] ycoords = new double[numPeople];
        boolean[] isMoving = new boolean[numPeople];
        int[] states = new int[numPeople];
        int[] timeSick = new int[numPeople]; // The time a person has been sick
        
        Random rand = new Random();
        // Step 1: Setup initial positions of all people, initialize
        // the amount of time they've all been sick to zero, and set
        // them all to not be sick by default
        // Also set the first "numMoving" people to be moving, and the
        // rest to be stationary
        for (int i = 0; i < numPeople; i++) {
            xcoords[i] = pixel*rand.nextInt(res);
            ycoords[i] = pixel*rand.nextInt(res);
            timeSick[i] = 0;
            states[i] = UNINFECTED;
            if (i < numMoving) {
                isMoving[i] = true;
            }
            else {
                isMoving[i] = false;
            }
        }
        states[0] = INFECTED; // Setup the first infection
        
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
    }
    
}
