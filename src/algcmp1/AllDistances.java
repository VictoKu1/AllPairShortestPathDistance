package algcmp1;

import java.util.Map;

public class AllDistances implements AllDistancesInterface {
    private final int[] vertices_weights;
    private int[][] edges_weights;
    public int[][] directionMatrix;
    private boolean shortestPathDone = false;
    private static final int inf = 1000000;

    //    private int numOfEdges;
//    private boolean defAlgoParam = false; //*True for Johnson, false for Floyd-Warshall .
//    private Graph graph;
    public AllDistances(int[] vertices_weights, int[][] edges_weights) throws IllegalArgumentException {
//        numOfEdges = 0;
//        graph = new Graph();
//        int index = 0;
//        for (int ver : vertices_weights) {
//            graph.addNode(index, ver);
//            index++;
//        }
//        for (int i = 0; i < edges_weights.length; i++) {
//            for (int j = i; j < edges_weights.length; j++) {
//                if (edges_weights[i][j] != edges_weights[j][i]) {
//                    throw new IllegalArgumentException("This is a undirected graph, you cannot enter an adjacency matrix with distance from point a to point b that differs from a distance from point b to point a.");
//                }
//                if (edges_weights[i][j] != inf || edges_weights[i][j] == 0 && i != j) {
//                    numOfEdges++;
//                }
//                graph.connect(i, j, edges_weights[i][j]);
//            }
//        }
//        defineAlgo();
        this.vertices_weights = vertices_weights;
        this.edges_weights = edges_weights;


        directionMatrix = new int[edges_weights.length][edges_weights.length];
    }

    @Override
    public int[][] distance_matrix() {
        if (shortestPathDone) {
            return edges_weights;
        }
//        if (true) {
//            johnson();
//        } else {
        floydWarshall();
//        }
        this.shortestPathDone = true;
        return edges_weights;
    }

    @Override
    public int distance(int u, int v) {
        if (shortestPathDone) {
            return edges_weights[u - 1][v - 1];
        } else {
            return distance_matrix()[u - 1][v - 1];
        }
    }

    @Override
    public String path(int u, int v) {
        if (u == v) {
            return "" + u;
        }
        String str = "";
        if (u < v) {
            return pathHlp(u - 1, v - 1, str);
        }
        return pathHlp(v - 1, u - 1, str);
    }

    private String pathHlp(int u, int v, String str) {
        if (u == v) {
            return str + "-" + (u + 1);
        }
        return pathHlp(directionMatrix[u][v], v, str.concat("" + (u + 1)));
    }

    private void floydWarshall() {
        for (int k = -1; k <= edges_weights.length; k++) {
            for (int i = 0; i < edges_weights.length; i++) {
                for (int j = i; j < edges_weights.length; j++) {
                    if (k == edges_weights.length) {
                        edges_weights[i][j] = (edges_weights[i][j] + vertices_weights[i] + vertices_weights[j]) / 2;
                        //*edges_weights[j][i] = edges_weights[i][j];
                    } else {
                        if (k == -1) {
                            if (edges_weights[i][j] != inf) {
                                directionMatrix[i][j] = j;
                                //*directionMatrix[j][i] = i;
                                if (i != j) {
                                    edges_weights[i][j] = 2 * edges_weights[i][j] + vertices_weights[i] + vertices_weights[j];
                                    //* edges_weights[j][i] = edges_weights[i][j] ;
                                }
                            }
                        } else {
                            if (edges_weights[i][k] + edges_weights[k][j] < edges_weights[i][j]) {
                                edges_weights[i][j] = edges_weights[i][k] + edges_weights[k][j];
                                //*edges_weights[j][i] = edges_weights[i][j];
                                directionMatrix[i][j] = directionMatrix[j][k];
                                //*directionMatrix[j][i] = directionMatrix[j][k];
                            }
                        }
                    }
                }
            }
        }
    }

//    private void defineAlgo() {
//        int v = vertices_weights.length;
//        int e = numOfEdges;
//        defAlgoParam = (Math.pow(v, 3) - (e * v + Math.pow(v, 2) * (Math.log(v) / Math.log(2)))) > 0;
//    }


//    private void johnson() {
//        graph.johnson();
//    }
//
//    private int[][] toIntMat(Graph graph) {
//        //*TODO.
//        return new int[0][];
//    }

    @Override
    public void print_path(int i, int j) {
        if (i != j) {
            print_path(i, directionMatrix[i][j]);
        }
        System.out.println(j);
    }
}