package com.mrjaffesclass.apcs.mvc.template;

import com.mrjaffesclass.apcs.messenger.*;

/**
 * The model represents the data that the app uses.
 * @author Roger Jaffe
 * @version 1.0
 */
public class Model implements MessageHandler {

  // Messaging system for the MVC
  private final Messenger mvcMessaging;

  // Model's data variables
  private boolean whoseMove;
  private boolean gameOver;
  private String[][] board;

  /**
   * Model constructor: Create the data representation of the program
   * @param messages Messaging class instantiated by the Controller for 
   *   local messages between Model, View, and controller
   */
  public Model(Messenger messages) {
    mvcMessaging = messages;
    this.board = new String[3][3];
  }

  /**
   * Reset the state for a new game
   */
  private void newGame() {
    for(int row=0; row<this.board.length; row++) {
      for (int col=0; col<this.board[0].length; col++) {
        this.board[row][col] = "";
      }
    }
    this.whoseMove = false;
    this.gameOver = false;
  }

  /**
   * Initialize the model here and subscribe to any required messages
   */
  public void init() {
    this.newGame();
    this.mvcMessaging.subscribe("playerMove", this);
    this.mvcMessaging.subscribe("newGame", this);
    this.mvcMessaging.subscribe("whoseMove", this);
  }

  @Override
  public void messageHandler(String messageName, Object messagePayload) {
    // Display the message to the console for debugging
    if (messagePayload != null) {
      System.out.println("MSG: received by model: "+messageName+" | "+messagePayload.toString());
    } else {
      System.out.println("MSG: received by model: "+messageName+" | No data sent");
    }

    // playerMove message handler
    if (messageName.equals("playerMove")) {
      // Get the position string and convert to row and col
      String position = (String) messagePayload;
      int row = Integer.parseInt(position.substring(0, 1));
      int col = Integer.parseInt(position.substring(1, 2));
      // If square is blank...
      if (this.board[row][col].equals("")) {
        // ... then set X or O depending on whose move it is
        if (this.whoseMove) {
          this.board[row][col] = "X";
        } else {
          this.board[row][col] = "O";
        }
        this.whoseMove = !this.whoseMove;
        this.mvcMessaging.notify("whoseMove", this.whoseMove);
        // Send the boardChange message along with the new board
        if (!this.gameOver) {
          this.mvcMessaging.notify("boardChange", this.board);
        }
        this.gameOver = (isFull() || !isWinner().equals(""));
        if (this.gameOver) {
          this.mvcMessaging.notify("gameOver", isWinner());
        }
      }

      // newGame message handler
    } else if (messageName.equals("newGame")) {
      // Reset the app state
      this.newGame();
      // Send the boardChange message along with the new board
      this.mvcMessaging.notify("boardChange", this.board);
    }
  }

  public boolean isFull() {
    for (String[] strings : this.board) {
      for (String string : strings) {
        if (string.equals("")) {
          return false;
        }
      }
    }
    return true;
  }

  public String isWinner() {
    // Check for a winner using two for loops
    for (String[] strings : this.board) {
      for (int col = 0; col < this.board[0].length; col++) {
        // Check for a winner in the row
        if (strings[0].equals(strings[1]) && strings[1].equals(strings[2]) && !strings[0].equals("")) {
          return strings[0];
        }
        // Check for a winner in the column
        if (this.board[0][col].equals(this.board[1][col]) && this.board[1][col].equals(this.board[2][col]) && !this.board[0][col].equals("")) {
          return board[0][col];
        }
      }
    }
    // Check for winner in a diagonals
    if (board[0][0].equals(this.board[1][1]) && this.board[1][1].equals(this.board[2][2]) && !this.board[0][0].equals("")) {
      return board[0][0];
    }
    if(this.board[0][2].equals(this.board[1][1]) && this.board[1][1].equals(this.board[2][0]) && !this.board[0][2].equals("")) {
      return board[0][2];
    }
    return "";
  }
}
