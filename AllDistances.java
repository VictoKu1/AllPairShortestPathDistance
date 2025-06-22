/**
 * AllDistances computes all-pairs shortest path distances and paths in a
 * weighted graph, supporting both edge and vertex weights,
 * using an optimized Floyd-Warshall algorithm
 */
public class AllDistances {
    // Vertex weights for each node
    private final int[] vertexWeights;
    // Distance matrix (edge weights, updated to shortest distances)
    private int[][] distanceMatrix;
    // Matrix to reconstruct shortest paths
    public int[][] pathMatrix;
    // Representation of infinity (large value to avoid overflow)
    private static final int INF = Integer.MAX_VALUE / 2;
    // Number of vertices in the graph
    private final int numVertices;

    /*
     * 
     * Constructs the All istances object and computes all-pairs shortest paths.
     * 
     * @param vertexWeights Array of vertex weights
     * 
     * @param edgeWeights Adjacency matrix of edge weights (use INF for no edge)
     */
    public AllDistances(int[] vertexWeights, int[][] edgeWeights) {
        this.vertexWeights = vertexWeights;
        this.distanceMatrix = edgeWeights;
        this.numVertices = edgeWeights.length;
        this.pathMatrix = new int[numVertices][numVertices];
        computeAllPairsShortestPaths();
    }

    /**
     * Returns the matrix of shortest distances between all pairs of vertices.
     */
    public int[][] getDistanceMatrix() {
        return distanceMatrix;
    }

    /**
     * Returns the shortest distance between vertex u and vertex v (1-based
     * indexing).
     */
    public int getDistance(int u, int v) {
        return distanceMatrix[u - 1][v - 1];

    }

    /**
     * Returns the shortest path from vertex u to vertex v as a string (e.g.,
     * "1-2-3").
     * Returns an empty string if no path exists.
     */
    public String getPath(int u, int v) {
        int dist = distanceMatrix[u - 1][v - 1];
        if (dist == INF) {
            return "";
        }
        return reconstructPath(u - 1, v - 1);
    }

    /**
     * Reconstructs the shortest path from u to v using the pathMatrix.
     */
    private String reconstructPath(int u, int v) {
        if (u == v) {
            return String.valueOf(u + 1);
        }
        // Pre-allocate buffer for path string
        int estimatedCapacity = Math.min(32, numVertices * 2);
        StringBuilder path = new StringBuilder(estimatedCapacity);
        path.append(u + 1);
        int current = u;
        int steps = 0;
        final int maxSteps = numVertices; // Prevent infinite loops
        while (current != v && steps < maxSteps) {
            int next = pathMatrix[current][v];
            if (next == -1 || next == current) {
                path.append('-').append(v + 1);
                break;
            }
            path.append('-').append(next + 1);
            current = next;
            steps++;
        }

        return path.toString();
    }

    /**
     * Main method to compute all-pairs shortest paths using an optimized
     * Floyd-Warshall algorithm.
     */
    private void computeAllPairsShortestPaths() {
        initializeAndApplyVertexWeights();
        floydWarshallOptimized();
        finalizeVertexWeightAdjustment();
    }

    /**
     * Initializes the path matrix and applies vertex weights to edge weights.
     * Uses loop unrolling for performance.
     */
    private void initializeAndApplyVertexWeights() {
        int i = 0;
        for (; i <= numVertices - 4; i += 4) {
            initializeRow(i);
            initializeRow(i + 1);
            initializeRow(i + 2);
            initializeRow(i + 3);
        }
        for (; i < numVertices; i++) {
            initializeRow(i);
        }
    }

    /**
     * Initializes a single row of the path and distance matrices.
     */
    private void initializeRow(int i) {
        int[] pathRow = pathMatrix[i];
        int[] distRow = distanceMatrix[i];
        int vertexWeightI = vertexWeights[i];
        int j = 0;
        for (; j <= numVertices - 4; j += 4) {
            pathRow[j] = (i == j) ? i : (distRow[j] != INF && distRow[j] != 0) ? j : -1;
            if (i != j && distRow[j] != INF && distRow[j] != 0) {
                distRow[j] = (distRow[j] << 1) + vertexWeightI + vertexWeights[j];
            }
            pathRow[j + 1] = (i == j + 1) ? i : (distRow[j + 1] != INF && distRow[j + 1] != 0) ? j + 1 : -1;
            if (i != j + 1 && distRow[j + 1] != INF && distRow[j + 1] != 0) {
                distRow[j + 1] = (distRow[j + 1] << 1) + vertexWeightI + vertexWeights[j + 1];
            }
            pathRow[j + 2] = (i == j + 2) ? i : (distRow[j + 2] != INF && distRow[j + 2] != 0) ? j + 2 : -1;
            if (i != j + 2 && distRow[j + 2] != INF && distRow[j + 2] != 0) {
                distRow[j + 2] = (distRow[j + 2] << 1) + vertexWeightI + vertexWeights[j + 2];
            }
            pathRow[j + 3] = (i == j + 3) ? i : (distRow[j + 3] != INF && distRow[j + 3] != 0) ? j + 3 : -1;
            if (i != j + 3 && distRow[j + 3] != INF && distRow[j + 3] != 0) {
                distRow[j + 3] = (distRow[j + 3] << 1) + vertexWeightI + vertexWeights[j + 3];
            }
        }
        for (; j < numVertices; j++) {
            pathRow[j] = (i == j) ? i : (distRow[j] != INF && distRow[j] != 0) ? j : -1;
            if (i != j && distRow[j] != INF && distRow[j] != 0) {
                distRow[j] = (distRow[j] << 1) + vertexWeightI + vertexWeights[j];
            }
        }
    }

    /**
     * Optimized Floyd-Warshall algorithm with early termination and cache friendly
     * access.
     */
    private void floydWarshallOptimized() {
        boolean[] hasConnections = new boolean[numVertices];
        for (int i = 0; i < numVertices; i++) {
            for (int j = 0; j < numVertices; j++) {
                if (distanceMatrix[i][j] != INF) {
                    hasConnections[i] = true;
                    break;
                }
            }
        }
        int consecutiveNoImprovements = 0;
        final int maxNoImprovements = Math.min(3, numVertices / 10);
        for (int k = 0; k < numVertices; k++) {
            if (!hasConnections[k])
                continue;

            boolean improved = false;
            int[] distRowK = distanceMatrix[k];
            boolean hasIncoming = false, hasOutgoing = false;
            for (int i = 0; i < numVertices && (!hasIncoming || !hasOutgoing); i++) {
                if (distanceMatrix[i][k] != INF)
                    hasIncoming = true;
                if (distRowK[i] != INF)
                    hasOutgoing = true;
            }
            if (!hasIncoming || !hasOutgoing)
                continue;
            for (int i = 0; i < numVertices; i++) {
                int distIK = distanceMatrix[i][k];
                if (distIK == INF)
                    continue;
                int[] distRowI = distanceMatrix[i];
                int[] pathRowI = pathMatrix[i];
                int j = 0;
                for (; j <= numVertices - 4; j += 4) {
                    improved |= tryUpdateDistance(distRowI, pathRowI, distRowK, distIK, j, k);
                    improved |= tryUpdateDistance(distRowI, pathRowI, distRowK, distIK, j + 1, k);
                    improved |= tryUpdateDistance(distRowI, pathRowI, distRowK, distIK, j + 2, k);
                    improved |= tryUpdateDistance(distRowI, pathRowI, distRowK, distIK, j + 3, k);
                }
                for (; j < numVertices; j++) {
                    improved |= tryUpdateDistance(distRowI, pathRowI, distRowK, distIK, j, k);
                }
            }
            if (!improved) {
                consecutiveNoImprovements++;
                if (consecutiveNoImprovements >= maxNoImprovements) {
                    break;
                }
            } else {
                consecutiveNoImprovements = 0;
            }
        }
    }

    /**
     * Attempts to update the shortest distance and path for a given pair (i, j) via
     * k.
     * Returns true if an improvement was made.
     */
    private boolean tryUpdateDistance(int[] distRowI, int[] pathRowI, int[] distRowK, int distIK, int j, int k) {
        int distKJ = distRowK[j];
        if (distKJ == INF)
            return false;
        long newDistance = (long) distIK + distKJ;
        if (newDistance < distRowI[j]) {
            distRowI[j] = (int) newDistance;
            pathRowI[j] = k;
            return true;
        }
        return false;
    }

    /**
     * Final adjustment to incorporate vertex weights into the final distances.
     */
    private void finalizeVertexWeightAdjustment() {
        for (int i = 0; i < numVertices; i++) {
            int[] distRow = distanceMatrix[i];
            int vertexWeightI = vertexWeights[i];
            int j = 0;
            for (; j <= numVertices - 4; j += 4) {
                if (i != j && distRow[j] != INF) {
                    distRow[j] = (distRow[j] + vertexWeightI + vertexWeights[j]) >> 1;
                }
                if (i != j + 1 && distRow[j + 1] != INF) {
                    distRow[j + 1] = (distRow[j + 1] + vertexWeightI + vertexWeights[j + 1]) >> 1;
                }
                if (i != j + 2 && distRow[j + 2] != INF) {
                    distRow[j + 2] = (distRow[j + 2] + vertexWeightI + vertexWeights[j + 2]) >> 1;
                }
                if (i != j + 3 && distRow[j + 3] != INF) {
                    distRow[j + 3] = (distRow[j + 3] + vertexWeightI + vertexWeights[j + 3]) >> 1;
                }
            }
            for (; j < numVertices; j++) {
                if (i != j && distRow[j] != INF) {
                    distRow[j] = (distRow[j] + vertexWeightI + vertexWeights[j]) >> 1;
                }
            }
        }
    }
}