package algcmp1;

public class AllDistances implements AllDistancesInterface {
    private final int[] vertices_weights;
    private int[][] edges_weights;
    private boolean shortestPathDone = false;
    private static final int inf = 1000000;

    public AllDistances(int[] vertices_weights, int[][] edges_weights) throws IllegalArgumentException {
        for (int i = 0; i < edges_weights.length; i++) {
            for (int j = i; j < edges_weights.length; j++) {
                if (edges_weights[i][j] != edges_weights[j][i]) {
                    throw new IllegalArgumentException("This is a undirected graph, you cannot enter an adjacency matrix with distance from point a to point b that differs from a distance from point b to point a.");
                }
            }
        }
        this.vertices_weights = vertices_weights;
        this.edges_weights = edges_weights;
    }

    @Override
    public int[][] distance_matrix() {
        if (shortestPathDone) {
            return edges_weights;
        }
        boolean defAlgoParam = defineAlgo();
        putEdges();
        if (defAlgoParam) {
            jhonson();
        } else {
            floydWarshall();
        }
        this.shortestPathDone = true;
        return edges_weights;
    }

    @Override
    public int distance(int u, int v) {
        if (shortestPathDone) {
            return edges_weights[u-1][v-1];
        } else {
            return distance_matrix()[u-1][v-1];
        }
    }

    @Override
    public String path(int u, int v) {
        return null;
    }

    void floydWarshall() {
        for (int k = 0; k < edges_weights.length; k++) {
            for (int j = 0; j < edges_weights.length; j++) {
                for (int i = 0; i < edges_weights.length; i++) {
                    if (edges_weights[i][k] + edges_weights[k][j] < edges_weights[i][j]) {
                        edges_weights[i][j] = edges_weights[i][k] + edges_weights[k][j];
                    }
                }
            }
        }
    }

    void putEdges() {
        for (int i = 0; i < edges_weights.length; i++) {
            edges_weights[i][i] = vertices_weights[i];
        }
    }

    boolean defineAlgo() {
        int v = vertices_weights.length;
        int e = countEdges();
        return (Math.pow(v, 3) - (e * v + Math.pow(v, 2) * (Math.log(v) / Math.log(2)))) > 0;
    }

    int countEdges() {
        int counter = 0;
        for (int i = 0; i < edges_weights.length; i++) {
            for (int j = 0; j < edges_weights.length; j++) {
                if (edges_weights[i][j] < inf) {
                    counter++;
                }
            }
        }
        return counter;
    }
}