package MsnStructures;

import java.util.Arrays;
import MsnLib.Msn;

/**
 * @author Mason Marker
 * @version 1.0 - 10/27/21
 */
public class MsnMatrix {

  public static double[][] add(double[][] matrix1, double[][] matrix2) {
    if (!canAdd(matrix1, matrix2))
      thr(matrix1, matrix2);
    double[][] added = new double[matrix1.length][matrix1[0].length];
    for (int i = 0; i < added.length; i++)
      for (int j = 0; j < added[i].length; j++)
        added[i][j] = matrix1[i][j] + matrix2[i][j];
    return added;
  }

  public static double[][] multiply(double[][] matrix1, double[][] matrix2) {
    if (!canMultiply(matrix1, matrix2))
      thr(matrix1, matrix2);

    double[][] multiply = new double[Msn.getDims(matrix1)[0]][Msn.getDims(matrix2)[1]];

    

    return multiply;
  }

  public static double[][] transposed(double[][] matrix) {
    double[][] transpose = new double[matrix[0].length][matrix.length];
    for (int i = 0; i < transpose.length; i++)
      for (int j = 0; j < transpose[i].length; j++)
        transpose[i][j] = matrix[j][i];
    return transpose;
  }

  public static boolean canAdd(double[][] matrix1, double[][] matrix2) {
    return Arrays.equals(Msn.getDims(matrix1), Msn.getDims(matrix2));
  }

  public static int[][] intMatrix(double[][] matrix) {
    return Msn.toInt(matrix);
  }

  public static double[][] doubleMatrix(int[][] matrix) {
    return Msn.toDouble(matrix);
  }

  public static boolean canMultiply(double[][] matrix1, double[][] matrix2) {
    return Msn.getDims(matrix1)[1] == Msn.getDims(matrix2)[0];
  }

  private static void thr(double[][] matrix1, double[][] matrix2) {
    throw new IllegalArgumentException(
        "Incompatible matrix dimensions: " + Arrays.deepToString(Msn.box(Msn.getDims(matrix1)))
            + " , " + Arrays.deepToString(Msn.box(Msn.getDims(matrix2))));
  }


}
