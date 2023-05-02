package com.mrjaffesclass.apcs.mvc.template;

import com.mrjaffesclass.apcs.messenger.*;

/**
 * The model represents the data that the app uses.
 * @author Roger Jaffe
 * @version 1.0
 */
public class Model implements MessageHandler {
  private Square[][] squares = new Square[Constants.SIZE][Constants.SIZE];

  // Messaging system for the MVC
  private final Messenger mvcMessaging;

  // Model's data variables
  private boolean whoseMove;
  private boolean gameOver;


  /**
   * Model constructor: Create the data representation of the program
   * @param messages Messaging class instantiated by the Controller for 
   *   local messages between Model, View, and controller
   */
  public Model(Messenger messages) {
    mvcMessaging = messages;
    squares = this.initBoard(squares);
  }


  /**
   * Initialize the model here and subscribe to any required messages
   */
  public void init() {
    mvcMessaging.subscribe("MouseClicked", this);
    whoseMove = true;

  }
  public Player checkPlayer(boolean x) {
    Player player;
    if (x){
      player = new Player(Constants.WHITE);
    }else{
      player = new Player(Constants.BLACK);
    }
    return player;
  }
  @Override
  public void messageHandler(String messageName, Object messagePayload) {
    // Display the message to the console for debugging
    if (messagePayload != null) {
      System.out.println("MSG: received by model: "+messageName+" | "+messagePayload.toString());
    } else {
      System.out.println("MSG: received by model: "+messageName+" | No data sent");
    }
    if (messageName.equals("MouseClicked") && !gameOver) {
      System.out.println("recieved");
        String message = messagePayload.toString();
        int x = Integer.parseInt(message.substring(0, 1));
        int y = Integer.parseInt(message.substring(2));
        Position pos = new Position(x, y);
        Player player;
        player = this.checkPlayer(this.whoseMove);

        if (isLegalMove(player, pos)) {
          makeMove(player, pos);
          mvcMessaging.notify("boardChange", squares);
          whoseMove = !this.whoseMove;
          System.out.println("legal");
        }
        // write an algorithm that will fill make move every position with black, then white, then black, then white, etc.  until the board is full or there are no more moves available for either player'
//        for (int i = 0; i < squares.length; i++) {
//          for (int j = 0; j < squares.length; j++) {
//            if (squares[i][j].getStatus() == Constants.EMPTY) {
//              Position pos2 = new Position(i, j);
//              Player player2;
//              player2 = this.checkPlayer(this.whoseMove);
//              makeMove(player2, pos2);
//              mvcMessaging.notify("boardChange", squares);
//              whoseMove = !this.whoseMove;
//
//            }
//          }
//        }


      if (noMovesAvailable(player)) {
        whoseMove = !this.whoseMove;
        player = this.checkPlayer(this.whoseMove);
        if (noMovesAvailable(player)) {
          String[] finalMessage = new String[3];

          gameOver = true;
          System.out.println(countSquares(Constants.BLACK));
          System.out.println(countSquares(Constants.WHITE));
          if (countSquares(Constants.BLACK) > countSquares(Constants.WHITE)) {
            finalMessage[0] = "Black";
            finalMessage[1] = Integer.toString(countSquares(Constants.BLACK));
            finalMessage[2] = Integer.toString(countSquares(Constants.WHITE));
            mvcMessaging.notify("gameOver", finalMessage);
          }
          else if (countSquares(Constants.BLACK) < countSquares(Constants.WHITE)) {

            finalMessage[0] = "White";
            finalMessage[1] = Integer.toString(countSquares(Constants.WHITE));
            finalMessage[2] = Integer.toString(countSquares(Constants.BLACK));
            mvcMessaging.notify("gameOver", finalMessage);
          }
          else {
            finalMessage[0] = "Tie";
            mvcMessaging.notify("gameOver", finalMessage);
          }
        }
        System.out.println("no moves");
      }

    }

  }
  /**
   * Initialize a new player board with empty spaces except
   * the four middle spaces.  These are initialized
   * W B
   * B W
   *
   * @return     New board
   */
  private Square[][] initBoard(Square[][] squares)
  {
    for (int row = 0; row < Constants.SIZE; row++) {
      for (int col = 0; col < Constants.SIZE; col++) {
        squares[row][col] = new Square(Constants.EMPTY);
      }
    }
    squares[3][3].setStatus(Constants.WHITE);
    squares[4][4].setStatus(Constants.WHITE);
    squares[3][4].setStatus(Constants.BLACK);
    squares[4][3].setStatus(Constants.BLACK);
    return squares;
  }

  public boolean isLegalMove(Player player, Position positionToCheck) {
    // If the space isn't empty, it's not a legal move
    if (getSquare(positionToCheck).getStatus() != Constants.EMPTY)
      return false;
    // Check all directions to see if the move is legal
    for (String direction : Directions.getDirections()) {
      Position directionVector = Directions.getVector(direction);
      if (step(player, positionToCheck, directionVector, 0)) {
        return true;
      }
    }
    return false;
  }

  public Square getSquare(Position position) {
    return this.squares[position.getRow()][position.getCol()];
  }

  protected boolean step(Player player, Position position, Position direction, int count) {
    Position newPosition = position.translate(direction);

    while (!newPosition.isOffBoard()) {

      if (this.getSquare(newPosition).getStatus() == Constants.EMPTY) {
        return false;
      } else if (!player.isThisPlayer(this.getSquare(newPosition).getStatus()) && this.getSquare(newPosition).getStatus() != Constants.EMPTY) {
        // If space has opposing player then move to next space in same direction
        return step(player, newPosition, direction, count + 1);
      } else if (player.isThisPlayer(this.getSquare(newPosition).getStatus())) {
        return count > 0;
      }

      newPosition = position.translate(direction);
    }
    return false;
  }

  public void makeMove(Player playerToMove, Position positionToMove) {
    for (String direction : Directions.getDirections()) {
      Position directionVector = Directions.getVector(direction);
      if (makeMoveStep(playerToMove, positionToMove, directionVector, 0)) {
        this.setSquare(playerToMove, positionToMove);
      }
    }
  }
  public int countSquares(int toMatch) {
    int count = 0;
    for (Square[] row : this.squares) {
      for (Square square : row) {
        if (square.getStatus() == toMatch) {
          count++;
        }
      }
    }
    return count;
  }
  protected void setSquare(Player player, Position position) {
    this.squares[position.getRow()][position.getCol()].setStatus(player.getColor());
  }

  private boolean makeMoveStep(Player player, Position position, Position direction, int count) {
    Position newPosition = position.translate(direction);
    int color = player.getColor();
    if (newPosition.isOffBoard()) {
      return false;
    } else if (this.getSquare(newPosition).getStatus() == -color) {
      boolean valid = makeMoveStep(player, newPosition, direction, count+1);
      if (valid) {
        this.setSquare(player, newPosition);
      }
      return valid;
    } else if (this.getSquare(newPosition).getStatus() == color) {
      return count > 0;
    } else {
      return false;
    }
  }

  public boolean noMovesAvailable(Player player) {
    for (int row = 0; row < Constants.SIZE; row++) {
      for (int col = 0; col < Constants.SIZE; col++) {
        Position pos = new Position(row, col);
        if (isLegalMove(player, pos)) {
          return false;
        }
      }
    }
    return true;
  }





  }
