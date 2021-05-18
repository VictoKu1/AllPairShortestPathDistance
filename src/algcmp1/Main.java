package algcmp1;

public class Main {
    public static void main(String[] args) {
        int inf = 1000000;
        int[][] matrix = {
                {0, 18, 5, inf}, {18, 0, 2, 3}, {5, 2, 0, inf}, {inf, 3, inf, 0}
        };
        int[] ver = {2, 4, 3, 6};
        AllDistances a = new AllDistances(ver, matrix);
        int[][] n1Matrix = a.distance_matrix();
        int[][] nMatrix = a.directionMatrix;
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                System.out.print(nMatrix[i][j] + ",");

            }
            System.out.println();
        }
        System.out.println();
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                System.out.print(n1Matrix[i][j] + ",");

            }
            System.out.println();
        }
        a.print_path(0,3);
    }
}
