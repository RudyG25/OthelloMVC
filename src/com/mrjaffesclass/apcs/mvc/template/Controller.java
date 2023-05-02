package com.mrjaffesclass.apcs.mvc.template;
import com.mrjaffesclass.apcs.messenger.*;
import java.awt.Color;
import java.awt.event.*;
import javax.swing.*;


/**
 * 
 * The Controller is the master of the App you're writing. It instantiates the
 * view and the model, receives messages from the View in response to user
 * interface (UI) actions like clicking a button, changing an input field, 
 * etc.  It also sends and receives messages to the Model to commuincate
 * changes required and changes made to the Model variables.  
 *
 * @author Roger Jaffe
 * @version 1.0
 */
public class Controller implements MessageHandler, MouseListener {

  private final Messenger mvcMessaging;
  JFrame frame = new JFrame();
  private int width = 655;
  private int height = 655;
  /**
   * Controller constructor The Controller is responsible for creating the View
   * and the Model that it will be controlling. The mvcMessaging object is
   * passed to the view and the model and is used as a local messenger
   * between the Controller, Model and View without have direct access to the
   * View and Model.  Remember, you want the three components separated so
   * that one class works independently of the others.
   *
   * Messages that can be received in the Controller:
   *  view:toggleButtonClick (sent by the View when the toggle button is clicked)
   *  view:buttonClick (sent by the View when the regular button is clicked)
   *  view:changeButton (sent by the View when the Up or Down buttons are clicked)
   * Message that are sent from the Controller:
   *  controller:changeButton (sent by the Controller to notify the Model to change 
   *    the value of a Model variable
   */
  public Controller() {
    // Create the local messaging class
    int width = width;
    int height = height;
    mvcMessaging = new Messenger();
    View view = new View(mvcMessaging, this.width, this.height);
    view.init();
    frame.setSize(this.width + 15, this.height + 39);
    frame.setTitle("Othello MVC");
    frame.add(view);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setVisible(true);
    frame.addMouseListener(this);
    // Create the model
    Model model = new Model(mvcMessaging);  // This creates our model
    model.init();
  }


  /**
   * Initialize the model here and subscribe
   * to any required messages
   * 
   * "this" refers to this controller object.
   */
  public void init() {
    // This is where you would subscribe to any messages the controller
    // would need to process
    // A sample subscriber call would be like...
    //mvcMessaging.subscribe("view:toggleButtonClick", this);
    mvcMessaging.subscribe("repaint", this);
  }

  @Override
  public void messageHandler(String messageName, Object messagePayload) {
    if (messagePayload != null) {
      System.out.println("MSG: received by controller: "+messageName+" | "+messagePayload.toString());
    } else {
      System.out.println("MSG: received by controller: "+messageName+" | No data sent");
    }
    // This is where the controller would handle any messages
    if (messageName.equals("repaint")) {
      frame.repaint();
      System.out.println("repaint");
    }
  }

  /**
   * Program entry -- main is called when the program starts
   *
   * @param args the command line arguments
   */
  public static void main(String[] args) {
    Controller app = new Controller();  // Create our controller...
    app.init();                         // ...and init it too
  }
  public void mouseExited(MouseEvent clicked){

  }
  public void mouseEntered(MouseEvent clicked){

  }
  public void mouseReleased(MouseEvent clicked){}
  public void mousePressed(MouseEvent clicked){
    System.out.println(clicked.getX());
    System.out.println(clicked.getY());
  }
  public void mouseClicked(MouseEvent clicked){
    int x = clicked.getX();
    int y = clicked.getY();
    int row = (y  - 15) / ((width/8));
    int col = (x - 15) / ((width/8));
    System.out.println("row: " + row + " col: " + col);
    mvcMessaging.notify("MouseClicked", row + "," + col);
  }
}
