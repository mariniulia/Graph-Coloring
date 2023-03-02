import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Scanner;

public class Greedy {
    public static void main(String[] args) throws FileNotFoundException {
        // Greedy Coloring
        System.out.println("--GREEDY ALGORITHM--");
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
            Graph g1 = getGraph(input);

            //start memory-tracking
            Runtime runtime = Runtime.getRuntime();

            //start time-tracking
            startTime = System.nanoTime();

            //start algorithm
            int chromaticNumber = greedyColoring(g1);
            //end time-tracker
            endTime = System.nanoTime();
            //calculate memory
            long memory = runtime.totalMemory() - runtime.freeMemory();
            //calculate time
            totalTime = endTime - startTime;

            output.println("Test" + "----------------");
            output.println("Chromatic number: " + chromaticNumber);
            output.println("Time taken: " + (double)(totalTime)/1000000000 + " seconds");
            output.println("Space taken: " + memory/1048576 + "MB");
            output.println("-----------------------------");
            output.close();
        }

    public static int greedyColoring(Graph g) {
        int V = g.getvCount();

        // color array
        int colors[] = new int[V];

        // initialize all vertices to '1', which means unassigned
        Arrays.fill(colors, -1);

        // assign first color (0) to first vertex
        colors[0] = 1;

        // boolean array that shows us which colors
        // are still available
        boolean available[] = new boolean[V];

        // starting off, all colors are available
        Arrays.fill(available, true);

        // assign colors to the remaining V-1 vertices
        for (int u = 1; u < V; u++) {
            // process adjacent vertices and flag
            // their colors as unavailable
            Iterator<Integer> it = g.neighbours(u).iterator();
            while (it.hasNext()) {
                int i = it.next();
                if (colors[i] != -1) {
                    available[colors[i]] = false;
                }
            }

            // find the first avaiable color
            int cr;
            for (cr = 1; cr < V; cr++) {
                if (available[cr])
                    break;
            }

            // assign the first avaiable color
            colors[u] = cr;

            // reset values back to true for the next iteration
            Arrays.fill(available, true);
        }

        int chromaticNumber = 0;
        for (int i = 0; i < V; i++) {
            if (colors[i] >= chromaticNumber) {
                chromaticNumber = colors[i];
            }
        }
        System.out.println("numar cromatic " + chromaticNumber);

        printColors(colors);
        return chromaticNumber;
    }

    public static void printColors(int[] colors) {
        for (int i = 0; i < colors.length; i++)
            System.out.println("Vertex " + i + " --->  Color " + colors[i]);
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
}

