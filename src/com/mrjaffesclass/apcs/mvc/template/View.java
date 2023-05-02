package com.mrjaffesclass.apcs.mvc.template;
import com.mrjaffesclass.apcs.messenger.*;
import java.awt.*;
import java.awt.geom.*;
import javax.swing.*;

public class View extends JComponent implements MessageHandler {

    private final Messenger mvcMessaging;
    private Square[][] squares = new Square[Constants.SIZE][Constants.SIZE];

    private int height;
    private int width;
    Board board;
    private Graphics2D g2d;


    public View(Messenger messages, int w, int h) {
        mvcMessaging = messages;   // Save the calling controller instance
        width = w;
        height = h;
        initBoard(squares);
        board = new Board(squares);
    }
    private void updateBoard(){
        System.out.println("Called Update Board");
        for (int row = 0; row < Constants.SIZE; row++){
            for (int col = 0; col < Constants.SIZE; col++){
                System.out.print(squares[row][col]);
                if (squares[row][col].getStatus() == Constants.BLACK){
                    putPiece(row, col, false);
                }else if (squares[row][col].getStatus() == Constants.WHITE){
                    putPiece(row, col, true);
                }
            }
            System.out.println();
        }
        System.out.println();
    }
    private void putPiece(int row, int col, boolean isWhite){
        if (isWhite){
            g2d.setColor(Color.WHITE);
        }else{
            g2d.setColor(Color.BLACK);
        }
        g2d.fillOval(col*width/8 + (width/8-width/10)/2, row*width/8 + (width/8-width/10)/2, width/10, height/10);
    }
    private void drawBoard(){
        g2d.setColor(new Color(0, 100, 0));
        g2d.fillRect(0, 0, width, height);
        g2d.setColor(Color.BLACK);
        for (int i = 0; i < Constants.SIZE; i++){
            g2d.drawLine(0, i*width/8, width, i*height/8);
            g2d.drawLine(i*height/8, 0, i*height/8, height);
        }
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    }





    /**
     * Initialize the model here and subscribe
     * to any required messages
     */
    public void init() {
        this.mvcMessaging.subscribe("boardChange", this);
        this.mvcMessaging.subscribe("gameOver", this);
        this.mvcMessaging.subscribe("whoseMove", this);
    }
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
    public void paintComponent(Graphics g) {
        g2d = (Graphics2D) g;
        drawBoard();
        updateBoard();
    }

    @Override
    public void messageHandler(String messageName, Object messagePayload) {
        if (messagePayload != null) {
            System.out.println("MSG: received by view: " + messageName + " | " + messagePayload.toString());
        } else {
            System.out.println("MSG: received by view: " + messageName + " | No data sent");
        }
        if (messageName.equals("boardChange")) {
            squares = (Square[][]) messagePayload;
            updateBoard();
            repaint();
        }
        if (messageName.equals("gameOver")) {
            String[] message = (String[]) messagePayload;
            if (message[0].equals("tie")) {
                JOptionPane.showMessageDialog(null, "Tie Game!");
            }
            else {
                JOptionPane.showMessageDialog(null, message[0] + " wins with a score of "
                        + message[1] + " to " + message[2]);
            }
        }
    }

}
