
public class TestingAlgorithms {

    public static void main(String[] args) {
        long startTime;
        // Greedy Coloring
        System.out.println("--GREEDY ALGORITHM--");
        Graph g1 = getGraph();

        startTime = System.nanoTime();
        Greedy.greedyColoring(g1);
        timeTracker(startTime);

        System.out.println("-----------------------------");

        // Backtracking Coloring
        System.out.println("--BACKTRACKING ALGORITHM--");
        int maxColors = 15;
        Graph g2 = getGraph();

        startTime = System.nanoTime();
        Backtracking.backTrackingColoring(g2, maxColors);
        timeTracker(startTime);
    }

    private static void timeTracker(long startTime) {
        long totalTime;
        long endTime;
        endTime = System.nanoTime();
        totalTime = endTime - startTime;
        System.out.println(totalTime);
    }

    private static Graph getGraph() {
        Graph g1 = new Graph(5);

        System.out.println("Graph:");
        // add Edges
        g1.addEdge(0, 1);
        g1.addEdge(0, 2);
        g1.addEdge(1, 2);
        g1.addEdge(1, 3);
        g1.addEdge(2, 3);
        g1.addEdge(3, 4);
        // print Graph
        g1.printGraph();
        return g1;
    }
}