import java.util.Arrays;
import java.util.Scanner;
import MsnLib.Msn;

/**
 * Plots two Networks against each other in Tic Tac Toe.
 * 
 * @author Mason Marker
 * @version 1.0 - 09/28/2021
 */
public class TicTacToe {
  
  public static void main(String[] args) {

    Network bot = new Network(9, 3, 3, 1);
    Network bot2 = new Network(9, 10, 10, 1);
        
    double[] possible = {0, 16.66, 16.66 * 2, 16.66 * 3, 16.66 * 4, 16.66 * 5, 16.66 * 6};
    
    while (true) {
      System.out.println("restarting...");
      Board board = new Board();
      while (!board.hasWinner()) {
        
        
        
        
      }
      
      
    }
    


  }


  static class Board {

    public char[][] board;

    public Board() {
      board = new char[3][3];
    }

    public boolean move(int x, int y, char c) {
      board[x][y] = c;
      if (hasWinner())
        return true;
      return false;
    }

    public boolean hasWinner() {
      if (isWinPattern(Msn.parseVertArray(board, 0)) || isWinPattern(Msn.parseVertArray(board, 1))
          || isWinPattern(Msn.parseVertArray(board, 1)) || isWinPattern(board[0])
          || isWinPattern(board[1]) || isWinPattern(board[2])
          || isWinPattern(new char[] {board[0][0], board[1][1], board[2][2]})
          || isWinPattern(new char[] {board[0][2], board[1][1], board[2][0]})) {
        return true;
      }
      return false;
    }

    public boolean isWinPattern(char[] c) {
      return c[0] == c[1] && c[1] == c[2];
    }
    
    public String toString() {
      return Arrays.deepToString(board).replace("], ", "]\n").replace("[[", "[").replace("]]", "]");
    }

  }

}
