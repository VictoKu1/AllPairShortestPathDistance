package algcmp1;

public interface AllDistancesInterface {
    /*Returns the distances matrix .*/
    public int[][] distance_matrix();

    /*Returns the distance between given vertices u and v.*/
    public int distance(int u, int v);

    /* Returns a string of names of vertices which are located on the shortest path from the vertex u to v, separeted with '-'. */
    public String path(int u, int v);
}
