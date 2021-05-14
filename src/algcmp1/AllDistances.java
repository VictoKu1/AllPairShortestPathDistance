package algcmp1;

public class AllDistances implements AllDistancesInterface {
    private int[] vertices_weights;
    private int[][] edges_weights;

    public AllDistances(int[] vertices_weights, int[][] edges_weights) {
        //*TODO.
        this.vertices_weights = vertices_weights;
        this.edges_weights = edges_weights;
    }

    @Override
    public int[][] distance_matrix() {
        return new int[0][];
    }

    @Override
    public int distance(int u, int v) {
        return 0;
    }

    @Override
    public String path(int u, int v) {
        return null;
    }
}