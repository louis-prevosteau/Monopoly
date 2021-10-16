package fr.monopoly.game.board.squares;

import fr.monopoly.Monopoly;
import fr.monopoly.game.Player;
import fr.monopoly.game.board.cardstack.CardStack;

import javax.swing.*;
import java.util.ArrayList;

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

    @Override
    public void doAction(Player player, ArrayList<Player> players, Monopoly game, CardStack stack) {
        if (this.getOwner().equals(null)) {
            String[] yesNoOptions = {"Oui", "Non"};
            String details = "Nom: " + this.getName() +
                    "\nCouleur: " + this.getColor() +
                    "\nPrix: " + this.getPrice() + " €" +
                    "\nLoyer: " + this.rent + " €";
            int yesNo = JOptionPane.showOptionDialog(null, details, "Acheter " + this.getName(), 0, JOptionPane.QUESTION_MESSAGE, null, yesNoOptions, null);
            if (yesNo == 0) {
                player.buy(this);
                JOptionPane.showMessageDialog(null, "Félicitations " + player.getName() + ", vous avez acheté " + this.getName(), "Acquisition", JOptionPane.INFORMATION_MESSAGE);
            }
        } else if (this.getOwner().equals(player))
            JOptionPane.showMessageDialog(null, player.getName() + ", " + this.getName() + " vous appartient.", "RAS", JOptionPane.INFORMATION_MESSAGE);
        else {
            Player owner = this.owner;
            int rentPayed = player.payRent(owner, this);
            JOptionPane.showMessageDialog(null, "Loyer de " + this.getName() +
                    "\nLocataire: " + player.getName() +
                    "\nPropriétaire; " + owner.getName() +
                    "\nLoyer: " + rentPayed + " €", "Prélèvement de loyer", JOptionPane.INFORMATION_MESSAGE);
        }
        game.refreshAll();
    }
}
