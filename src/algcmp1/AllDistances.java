package algcmp1;

public class AllDistances {
    private final int[] vertices_weights;
    private int[][] edges_weights;
    public int[][] directionMatrix;
    private static final int inf = 1000000;

    public AllDistances(int[] vertices_weights, int[][] edges_weights) {
        this.vertices_weights = vertices_weights;
        this.edges_weights = edges_weights;
        directionMatrix = new int[edges_weights.length][edges_weights.length];
        floydWarshall();
    }

    public int[][] distance_matrix() {
        return edges_weights;
    }

    public int distance(int u, int v) {
        return edges_weights[u - 1][v - 1];
    }

    public String path(int u, int v) {
        if (distance(u, v) == inf) {
            return "";
        }
        if (u == v) {
            return "" + u;
        }
        String str = "";
        if (u < v) {
            return pathHlp(u - 1, v - 1, str);
        }
        return transpose(pathHlp(v - 1, u - 1, str));
    }

    private String transpose(String pathHlp) {
        return String.valueOf((new StringBuffer(pathHlp)).reverse());
    }

    private String pathHlp(int u, int v, String str) {
        if (u == v) {
            return str + "" + (u + 1);
        }
        return pathHlp(directionMatrix[u][v], v, str.concat((u + 1) + "-"));
    }

    private void floydWarshall() {
        for (int k = -1; k <= edges_weights.length; k++) {
            for (int i = 0; i < edges_weights.length; i++) {
                for (int j = (k == -1) ? 0 : i; j < edges_weights.length; j++) {
                    if (k == edges_weights.length) {
                        if (edges_weights[i][j] >= inf) {
                            edges_weights[i][j] = inf;
                        } else {
                            edges_weights[i][j] = (edges_weights[i][j] + vertices_weights[i] + vertices_weights[j]) / 2;
                        }
                        edges_weights[j][i] = edges_weights[i][j];
                    } else {
                        if (k == -1) {
                            if (i == j) {
                                directionMatrix[i][j] = i;
                            } else {
                                if (edges_weights[i][j] != inf && edges_weights[i][j] != 0) {
                                    directionMatrix[i][j] = j;
                                } else {
                                    directionMatrix[i][j] = k;
                                }
                            }
                            if (edges_weights[i][j] != inf && i != j) {
                                edges_weights[i][j] = 2 * edges_weights[i][j] + vertices_weights[i] + vertices_weights[j];
                                edges_weights[j][i] = edges_weights[i][j];
                            }
                        } else {
                            if (edges_weights[i][k] + edges_weights[k][j] < edges_weights[i][j]) {
                                edges_weights[i][j] = edges_weights[i][k] + edges_weights[k][j];
                                edges_weights[j][i] = edges_weights[i][j];
                                directionMatrix[i][j] = k;
                                directionMatrix[j][i] = k;
                            }
                        }
                    }
                }
            }
        }
    }
}