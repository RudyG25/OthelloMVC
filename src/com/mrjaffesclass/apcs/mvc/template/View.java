package com.mrjaffesclass.apcs.mvc.template;
import com.mrjaffesclass.apcs.messenger.*;

import javax.swing.*;

public class View extends JFrame implements MessageHandler {

    private final Messenger mvcMessaging;

    private String turn = "O";
    private JPanel tictactoePanel;
    private JButton topMid;
    private JButton midMid;
    private JButton bottomMid;
    private JButton rightMid;
    private JButton leftMid;
    private JButton rightTop;
    private JButton rightBottom;
    private JButton leftTop;
    private JButton leftBottom;
    private JButton newGameButton;
    private JLabel ticTacToeLabel;

    public View(Messenger messages) {
        mvcMessaging = messages;   // Save the calling controller instance
        initComponents();         // Initialize the GUI components
    }

    private void initComponents() {
        setContentPane(tictactoePanel);
        setTitle("Tic Tac Toe");
        setSize(300, 300);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
        leftTop.setName("00");
        leftMid.setName("10");
        leftBottom.setName("20");
        topMid.setName("01");
        midMid.setName("11");
        bottomMid.setName("21");
        rightTop.setName("02");
        rightMid.setName("12");
        rightBottom.setName("22");

        topMid.addActionListener(e -> {
            setTile(topMid);
        });
        midMid.addActionListener(e -> {
            setTile(midMid);
        });
        bottomMid.addActionListener(e -> {
            setTile(bottomMid);
        });
        rightMid.addActionListener(e -> {
            setTile(rightMid);
        });
        leftMid.addActionListener(e -> {
            setTile(leftMid);
        });
        rightTop.addActionListener(e -> {
            setTile(rightTop);
        });
        rightBottom.addActionListener(e -> {
            setTile(rightBottom);
        });
        leftTop.addActionListener(e -> {
            setTile(leftTop);
        });
        leftBottom.addActionListener(e -> {
            setTile(leftBottom);
        });
        newGameButton.addActionListener(e -> {
            mvcMessaging.notify("newGame", null);
        });

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

    @Override
    public void messageHandler(String messageName, Object messagePayload) {
        if (messagePayload != null) {
            System.out.println("MSG: received by view: "+messageName+" | "+messagePayload.toString());
        } else {
            System.out.println("MSG: received by view: "+messageName+" | No data sent");
        }
        if (messageName.equals("boardChange")) {
            // Get the message payload and cast it as a 2D string array since we
            // know that the model is sending out the board data with the message
            String[][] board = (String[][]) messagePayload;
            // Now set the button text with the contents of the board
            leftTop.setText(board[0][0]);
            topMid.setText(board[0][1]);
            rightTop.setText(board[0][2]);
            leftMid.setText(board[1][0]);
            midMid.setText(board[1][1]);
            rightMid.setText(board[1][2]);
            leftBottom.setText(board[2][0]);
            bottomMid.setText(board[2][1]);
            rightBottom.setText(board[2][2]);
        }
        if (messageName.equals("gameOver")) {
            if (messagePayload.equals("X")) {
                JOptionPane.showMessageDialog(null, "X wins!");
            } else if (messagePayload.equals("O")) {
                JOptionPane.showMessageDialog(null, "O wins!");
            } else {
                JOptionPane.showMessageDialog(null, "It's a tie!");
            }
        }
        if (messageName.equals("whoseMove")) {
            if (messagePayload.equals(true)) {
                setTitle("X's Turn");
            } else {
                setTitle("O's Turn");
            }
        }
    }
    public boolean isFull() {
        for (JButton button : new JButton[]{topMid, midMid, bottomMid, rightMid, leftMid, rightTop, rightBottom, leftTop, leftBottom}) {
            if (button.getText().isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public void setTile(JButton button) {
        System.out.println("Sending playerMove message with payload: " + button.getName());
        this.mvcMessaging.notify("playerMove", button.getName());
    }
}
