import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.time.Duration;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.*;

public class Backtracking {
    public static void main(String[] args) throws FileNotFoundException {
        // Backtracking Coloring
        System.out.println("--BACKTRACKING ALGORITHM--");
        int maxColors = 20;
        long totalTime;
        long endTime;
        long startTime;
        //start reading from tests
        File input;
        PrintWriter output;

        //for each file, open it , read the associated graph & create output with result
        input = new File("test.in");
        output = new PrintWriter("test.out");

        //build graph from file
        Graph g2 = getGraph(input);

        //start memory-tracking
        Runtime runtime = Runtime.getRuntime();

        //start time-tracking
        startTime = System.nanoTime();

        //start algorithm
        int chromaticNumber = backTrackingColoring(g2, maxColors);

        //end time-tracker
        endTime = System.nanoTime();
        //calculate memory
        long memory = runtime.totalMemory() - runtime.freeMemory();
        //calculate time
        totalTime = endTime - startTime;

        output.println("Test" + "----------------");
        output.println("Chromatic number: " + chromaticNumber);
        output.println("Time taken: " + (double) (totalTime) / 1000000000 + " seconds");
        output.println("Space taken: " + memory / 1048576 + "MB");
        output.println("-----------------------------");
        output.close();

    }

    private static Graph getGraph(File input) throws FileNotFoundException {
            Scanner read = new Scanner(input);
            //read number of nodes
            int n = read.nextInt();
            Graph g = new Graph(n);

            //add edges
            while (read.hasNextLine()) {
                //read neighbours
                int firstNode = read.nextInt();
                int secondNode = read.nextInt();

                //connect them
                g.addEdge(firstNode, secondNode);
            }

            System.out.println("Graph:");
            // add Edges

            // print Graph
            g.printGraph();
            return g;
    }
    public static boolean isSafe(int v, Graph g, int colors[], int cr) {
        for (int i = 0; i < g.getvCount(); i++) {
            if (g.hasEdge(v, i) && cr == colors[i]) {
                return false;
            }
        }
        return true;

    }

    public static boolean graphColoringUtil(Graph g, int m, int colors[], int v) {
        // all vertices have a color then just true
        if (v == g.getvCount())
            return true;

        // try different colors for v
        for (int cr = 1; cr <= m; cr++) {
            // Check if assignment of color cr to v is fine
            if (isSafe(v, g, colors, cr)) {
                colors[v] = cr;
                // recur to assign colors to rest of the vertices
                if (graphColoringUtil(g, m, colors, v + 1))
                    return true;

                // If assigning color cr doesn't lead
                // to a solution then remove
                colors[v] = 0;
            }
        }

        // if no color can be assigned then return false
        return false;
    }
public static int backTrackingColoring(Graph g, int m) {
    int V = g.getvCount();

    // color array
    int colors[] = new int[V];

    // initialize all color values to 0
    Arrays.fill(colors, 0);

   // int min = 0;
    // call graphColoringUtil for vertex 0
    for(int min = 0; min < m ; min++){
        if (!graphColoringUtil(g, min, colors, 0)) {
//            System.out.println("Solution does not exist");
            min++;
        }else{
            break;
        }
    }

    int chromaticNumber = 0;
    for (int i = 0; i < V; i++) {
        if (colors[i] >= chromaticNumber) {
            chromaticNumber = colors[i];
        }
    }
    System.out.println("Chromatic number" + chromaticNumber);
    printColors(colors);
    return  chromaticNumber;
}

// Print Colors Function
public static void printColors(int[] colors) {
        for (int i = 0; i < colors.length; i++)
        System.out.println("Vertex " + i + " --->  Color " + colors[i]);
        }
}