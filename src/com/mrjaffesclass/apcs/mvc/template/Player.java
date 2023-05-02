package com.mrjaffesclass.apcs.mvc.template;

public class Player
{
    private final int color;

    /**
     * Player constructor
     * @param color   One of Constants.WHITE or Constants.BLACK
     */
    public Player(int color) {
        this.color = color;
    }

    /**
     * Gets the player color
     * @return        Player color
     */
    public int getColor() {
        return this.color;
    }

    /**
     * Gets the player name
     * @return        Player name
     */
    public String getName() {
        return this.getClass().getSimpleName();
    }

    /**
     * Are this player and the passed-in player the same?
     * @param p Passed-in player
     * @return true if the players are the same
     */
    boolean isThisPlayer(Player p) {
        return p.getColor() == this.getColor();
    }

    /**
     * Are this color and the passed-in color the same?
     * @param p Passed-in color (represented as an integer)
     * @return true if the colors (integers) are the same
     */
    boolean isThisPlayer(int p) {
        return p == this.getColor();
    }

    @Override
    public String toString() {
        return switch (this.color) {
            case Constants.BLACK -> this.getName() + " (BLACK)";
            case Constants.WHITE -> this.getName() + " (WHITE)";
            default -> this.getName() + " (?????)";
        };
    }

}
