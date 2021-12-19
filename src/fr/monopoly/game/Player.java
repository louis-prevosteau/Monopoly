package fr.monopoly.game;

import fr.monopoly.Monopoly;
import fr.monopoly.game.board.squares.*;

import javax.swing.*;
import java.util.ArrayList;

public class Player {

    private String name;
    private int money, dice1, dice2, roll, doubleCount, jailTime, jailExit;
    private boolean onDice2 = false;
    private ArrayList<Property> propertiesOwned;
    private Space currentSpace = new Space();

    public Player() {
    }

    public Player(String name, Space currentSpace) {
        this.name = name;
        this.money = 1500;
        this.doubleCount = 0;
        this.jailExit = 0;
        this.currentSpace = currentSpace;
        propertiesOwned = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public int getDice1() {
        return dice1;
    }

    public void setDice1(int dice1) {
        this.dice1 = dice1;
    }

    public int getDice2() {
        return dice2;
    }

    public void setDice2(int dice2) {
        this.dice2 = dice2;
    }

    public int getRoll() {
        return roll;
    }

    public int getDoubleCount() {
        return doubleCount;
    }

    public void setDoubleCount(int doubleCount) {
        this.doubleCount = doubleCount;
    }

    public int getJailTime() {
        return jailTime;
    }

    public void setJailTime(int jailTime) {
        this.jailTime = jailTime;
    }

    public int getJailExit() {
        return jailExit;
    }

    public void setJailExit(int jailExit) {
        this.jailExit = jailExit;
    }

    public ArrayList<Property> getPropertiesOwned() {
        return propertiesOwned;
    }

    public Space getCurrentSpace() {
        return currentSpace;
    }

    public void setCurrentSpace(Space currentSpace) {
        this.currentSpace = currentSpace;
    }

    public boolean canPay(int price) {
        return this.money >= price;
    }

    public boolean isNoHousesOnGrounds(Color groundColor) {
        ArrayList<Ground> groundsOfColor = new ArrayList<>();
        for (Property p : propertiesOwned) {
            if (p instanceof Ground && p.getColor().equals(groundColor))
                groundsOfColor.add((Ground) p);
        }
        for (Ground g : groundsOfColor) {
            if (g.getNbHouse() != 0)
                return false;
        }
        return true;
    }

    public void move() {
        for (int i = 0 ; i < this.getRoll() ; i++) {
            this.setCurrentSpace(this.getCurrentSpace().getNext());
            if (this.getCurrentSpace().getPosition() == 1)
                this.setMoney(this.getMoney() + 200);
        }
    }

    public void moveTo(int id) {
        if (id == 0) {
            while (this.getCurrentSpace().getPosition() != 40)
                this.setCurrentSpace(this.getCurrentSpace().getNext());
        } else if (id == 11) {
            while (this.getCurrentSpace().getPosition() != id)
                this.setCurrentSpace(this.getCurrentSpace().getNext());
        } else {
            while (this.getCurrentSpace().getPosition() != id) {
                this.setCurrentSpace(this.getCurrentSpace().getNext());
                if (this.getCurrentSpace().getPosition() == 1)
                    this.setMoney(this.getMoney() + 200);
            }
        }
    }

    public void roll() {
        if (!onDice2) {
            this.setDice1((int)(6*Math.random() + 1));
            this.onDice2 = true;
            this.roll();
        } else {
            this.setDice2((int)(6*Math.random() + 1));
            this.roll = this.getDice1() + this.getDice2();
            this.onDice2 = false;
        }
    }

    public void trade(Player trader, ArrayList<Property> give, ArrayList<Property> take, int cashGive, int cashTake) {
        for (Property g : give) {
            this.getPropertiesOwned().remove(g);
            trader.getPropertiesOwned().add(g);
            g.setOwner(trader);
        }
        for (Property t : take) {
            trader.getPropertiesOwned().remove(t);
            this.getPropertiesOwned().add(t);
            t.setOwner(this);
        }
        this.setMoney(this.getMoney() - cashGive);
        trader.setMoney(trader.getMoney() + cashGive);
        trader.setMoney(trader.getMoney() - cashTake);
        this.setMoney(this.getMoney() + cashTake);
    }

    public boolean hasMonopoly(Color propColor) {
        ArrayList<Ground> groundsOfColor = new ArrayList<>();
        for (Property p : propertiesOwned) {
            if (p instanceof Ground && p.getColor().equals(propColor) && !p.isMortgage())
                groundsOfColor.add((Ground) p);
        }
        return (groundsOfColor.size() == 2 && (propColor.equals(Color.PINK)  || propColor.equals(Color.BLUE))) || groundsOfColor.size() == 3;
    }

    public void buy(Property prop) {
        this.setMoney(this.getMoney() - prop.getPrice());
        prop.setOwner(this);
        this.getPropertiesOwned().add(prop);
    }

    public void sale(Property prop) {
        this.setMoney(this.getMoney() + prop.getPrice());
        prop.setOwner(null);
        prop.setMortgage(false);
        this.getPropertiesOwned().remove(prop);
    }

    public void buyHouse(Ground prop, Monopoly game) {
        if (prop.getNbHouse() < 4 && game.getHouses() == 0) {
            JOptionPane.showMessageDialog(null, "Il n'y a plus de maisons dans le jeu", "Plus de maisons disponibles", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (prop.getNbHouse() == 4 && game.getHotels() == 0) {
            JOptionPane.showMessageDialog(null, "Il n'y a plus d'hôtels dans le jeu", "Plus d'hôtels disponibles", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (prop.getNbHouse() < 5) {
            if (prop.getNbHouse() == 4 && game.getHotels() > 0)
                game.setHotels(game.getHotels() - 1);
            else if (prop.getNbHouse() < 4 && game.getHouses() > 0)
                game.setHouses(game.getHouses() - 1);
            this.setMoney(this.getMoney() - prop.getHousePrice());
            prop.setNbHouse(prop.getNbHouse() + 1);
            JOptionPane.showMessageDialog(null, "Vous avez construit une maison sur " + prop.getName(), "Construction", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "Vous ne pouvez plus construire de maisons sur " + prop.getName(), "Construction impossible", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void saleHouse(Ground prop, Monopoly game) {
        if (prop.getNbHouse() > 0) {
            if (prop.getNbHouse() == 5)
                game.setHotels(game.getHotels() + 1);
            else
                game.setHouses(game.getHouses() + 1);
            this.setMoney(this.getMoney() + prop.getHousePrice());
            prop.setNbHouse(prop.getNbHouse() - 1);
            JOptionPane.showMessageDialog(null, "Vous avez retirez une maison sur " + prop.getName(), "Déstruction", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "Vous n'avez plus de maisons sur " + prop.getName(), "Propriété vide", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void mortgage(Property prop) {
        this.setMoney(this.getMoney() + prop.getPrice() / 2);
        prop.setMortgage(true);
    }

    public void unmortgage(Property prop) {
        this.setMoney(this.getMoney() - prop.getPrice() / 2);
        prop.setMortgage(false);
    }

    public int payRent(Player owner, Property landed) {
        int newRent = 0;
        if (!landed.isMortgage())
            newRent = landed.updateRent(owner, this);
        else
            JOptionPane.showMessageDialog(null, landed.getName() + " est hypothéquée.", "Propriété hypothéquée", JOptionPane.ERROR_MESSAGE);
        if (!landed.getOwner().equals(this)) {
            this.money -= newRent;
            owner.setMoney(owner.getMoney() + newRent);
        }
        return newRent;
    }
}
