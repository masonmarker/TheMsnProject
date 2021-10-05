import java.util.Scanner;
import MsnLib.Msn;

/**
 * (WIP) Plots two Networks against each other.
 * 
 * @author Mason Marker
 * @version 1.0 - 09/28/2021
 */
public class TicTacToe {

  public static void main(String[] args) {

    Scanner sc = new Scanner(System.in);
    System.out.println("choose player against Network");
    Object o = Msn.prompt("player 1: (user/network)", sc);
    switch ((String) o) {

      case "user": {

        

      }
        break;
      case "network": {



      }

      default:

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


  }

}
