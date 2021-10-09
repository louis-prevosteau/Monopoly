package fr.monopoly.game.board.squares;

import fr.monopoly.game.Player;

public abstract class Property extends Space {

    private int price, rent;
    private boolean mortgage = false;
    private Player owner = null;

    public Property(int position, String name, int price, int rent) {
        super(position, name);
        this.price = price;
        this.rent = rent;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getRent() {
        return rent;
    }

    public void setRent(int rent) {
        this.rent = rent;
    }

    public boolean isMortgage() {
        return mortgage;
    }

    public void setMortgage(boolean mortgage) {
        this.mortgage = mortgage;
    }

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public int getNbHouse() { return 0;}

    public Color getColor() {
        return null;
    }
}
