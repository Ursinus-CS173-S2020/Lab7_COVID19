/**
 * Programmer: Chris Tralie
 * Purpose: To create a simple Monte Carlo simulation of a
 * spreading epidemic, using only arrays, methods, and loops.  A bunch
 * of people are placed uniformly at random on a square grid, and a single
 * one of them starts off infected.  Points that are moving then take
 * a random walk
 */
package covid19;

import org.knowm.xchart.*;
import java.util.Random;
import java.util.ArrayList; 

/**
 *
 * @author ctralie
 */
public class COVID19 {
    // The three states a person can be in
    public static final int INFECTED = 0;
    public static final int UNINFECTED = 1;
    public static final int RECOVERED = 2;
    
    // The amount of time to spend between frames
    public static int PAUSE_TIME = 5;
    
    /**
     * Run one hour step of the simulation, updating who's infected and
     * who's recovered based on the current spatial positions and elapsed time
     * 
     * @param dist If an infected person is within this distance of an 
     *             uninfected person in both the x and y coordinate, the
     *             uninfected person becomes infected
     * @param xcoords An array of the x coordinates of all people
     * @param ycoords An array of the y coordinates of all people
     * @param states An array of all peoples' states (INFECTED/UNINFECTED/RECOVERED)
     * @param timeSick An array holding the number of hours each person has been sick
     * @param recoveryTime The number of hours it takes to recover from being infected
     */
    public static void updateInfections(double dist, double[] xcoords, double[] ycoords,
                                        int[] states, int[] timeSick, int recoveryTime) {
        // TODO: Fill this in
    }
    
    /**
     * Draw points representing each person in the simulation, at a particular
     * point in time.  Uninfected are drawn in blue, infected are drawn in red,
     * and recovered are drawn in magenta
     * 
     * @param xcoords An array of the x coordinates of all people
     * @param ycoords An array of the y coordinates of all people
     * @param states An array of all peoples' states (INFECTED/UNINFECTED/RECOVERED)
     * @param pixel Drawing size of each person
     * @param hour The hour it is in the simulation
     */
    public static void drawPoints(double[] xcoords, double[] ycoords, int[] states, double pixel, int hour) {
        StdDraw.clear();
        StdDraw.enableDoubleBuffering();
        StdDraw.setPenColor(StdDraw.BLUE);
        int N = xcoords.length;
        for (int i = 0; i < N; i++) {
            StdDraw.setPenRadius(pixel);
            switch(states[i]) {
                case INFECTED:
                    StdDraw.setPenRadius(4*pixel);
                    StdDraw.setPenColor(StdDraw.RED);
                    break;
                case RECOVERED:
                    StdDraw.setPenRadius(2*pixel);
                    StdDraw.setPenColor(StdDraw.MAGENTA);
                    break;
                case UNINFECTED:
                    StdDraw.setPenRadius(2*pixel);
                    StdDraw.setPenColor(StdDraw.BLUE);
                    break;
            }
            // Add a little bit of noise upon drawing so overlapping points
            // both show up
            double dx = pixel*Math.random()/5;
            double dy = pixel*Math.random()/5;
            StdDraw.point(xcoords[i]+dx, ycoords[i]+dy);
        }
        StdDraw.setPenColor(StdDraw.RED);
        StdDraw.text(0.07, 0.97, "Day " + hour/24);
        StdDraw.show();
        StdDraw.pause(PAUSE_TIME);
    }
    
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
     * Plot the number of infected/uninfected/recovered people over time, using
     * the xchart library
     * 
     * @param infectedCount An array of the counts of infected people over time
     * @param uninfectedCount An array of the counts of uninfected people over time
     * @param recoveredCount An array of the counts of recovered people over time
     * @param numPeople The total number of people in the simulation
     * @param numMoving The total number of people in the simulation who are moving
     * @param res The resolution of the simulation
     */
    public static void plotResults(double[] infectedCount, double[] uninfectedCount, 
                                   double[] recoveredCount, int numPeople, int numMoving, int res) {
        int N = infectedCount.length;
        XYChart chart = new XYChart(500, 400);
        String title = "Epidemic Simulation: " + numPeople + " People, ";
        title += "Moving = " + (100.0*numMoving/(double)numPeople) + "%, ";
        title += "Space = " + res + " x " + res;
        chart.setTitle(title);
        chart.setXAxisTitle("Day");
        chart.setYAxisTitle("Counts");
        double[] days = new double[N];
        for (int i = 0; i < N; i++) {
            days[i] = i/24.0;
        }
        XYSeries s1 = chart.addSeries("Infected", days, infectedCount);
        XYSeries s2 = chart.addSeries("Uninfected", days, uninfectedCount);
        XYSeries s3 = chart.addSeries("Recovered", days, recoveredCount);
        //series.setMarker(SeriesMarkers.CIRCLE);
        ArrayList<XYChart> charts = new ArrayList<XYChart>();
        charts.add(chart);
        new SwingWrapper<XYChart>(charts).displayChartMatrix();
    }
    
    /**
     * 
     * @param numPeople The total number of people in the simulation
     * @param numMoving The total number of people in the simulation who are moving
     * @param numHours The total number of hours in the simulation
     * @param res The resolution of the simulation
     * @param recoveryTime The number of hours it takes to recover from being infected
     * @param draw If true, draw every hour of the simulation.  Otherwise, just
     *             run the simulation and plot the results at the end
     */
    public static void simulatePandemic(int numPeople, int numMoving, int numHours, int res, int recoveryTime, boolean draw) {
        double pixel = 1.0/res;
        
        // Arrays for holding the current state of all people;
        double[] xcoords = new double[numPeople];
        double[] ycoords = new double[numPeople];
        boolean[] isMoving = new boolean[numPeople];
        int[] states = new int[numPeople];
        int[] timeSick = new int[numPeople]; // The time a person has been sick
        
        
        // Arrays for holding the results
        double[] infectedCount = new double[numHours];
        double[] uninfectedCount = new double[numHours];
        double[] recoveredCount = new double[numHours];
        
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
        
        // Step 2: Run the Monte Carlo Simulation
        for (int hour = 0; hour < numHours; hour++) {
            if (draw) {
                drawPoints(xcoords, ycoords, states, pixel, hour);
            }
            doRandomWalks(xcoords, ycoords, isMoving, pixel);
            updateInfections(pixel*2.1, xcoords, ycoords, states, timeSick, recoveryTime);
            // Update counts
            infectedCount[hour] = 0;
            uninfectedCount[hour] = 0;
            recoveredCount[hour] = 0;
            
            for (int i = 0; i < numPeople; i++) {
                switch(states[i]) {
                    case INFECTED:
                        infectedCount[hour]++;
                        break;
                    case UNINFECTED:
                        uninfectedCount[hour]++;
                        break;
                    case RECOVERED:
                        recoveredCount[hour]++;
                        break;
                }
            }
        }
        
        plotResults(infectedCount, uninfectedCount, recoveredCount, numPeople, numMoving, res);
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        int numPeople = 1000;
        int numMoving = numPeople;
        int numHours = 24*120;
        int res = 200;
        int recoveryTime = 24*14;
        boolean draw = true;
        simulatePandemic(numPeople, numMoving, numHours, res, recoveryTime, draw);
    }
    
}
