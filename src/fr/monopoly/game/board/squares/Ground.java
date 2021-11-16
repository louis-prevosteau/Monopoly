package fr.monopoly.game.board.squares;

import fr.monopoly.game.Player;

public class Ground extends Property {

    private int nbHouse = 0, housePrice;
    private Color color;

    public Ground(int position, String name, int price, int rent, int housePrice, Color color) {
        super(position, name, price, rent);
        this.housePrice = housePrice;
        this.color = color;
    }

    @Override
    public int getNbHouse() {
        return nbHouse;
    }

    public void setNbHouse(int nbHouse) {
        this.nbHouse = nbHouse;
    }

    public int getHousePrice() {
        return housePrice;
    }

    @Override
    public Color getColor() {
        return color;
    }

    @Override
    public int updateRent(Player owner, Player currentPlayer) {
        switch (this.getNbHouse()) {
            case 0:
                if (owner.hasMonopoly(this.getColor()))
                    return 2 * this.getRent();
                else
                    return this.getRent();
            case 1:
                return 5 * this.getRent();
            case 2:
                return 15 * this.getRent();
            case 3:
                return 45 * this.getRent();
            case 4:
                return 90 * this.getRent();
            case 5:
                return 180 * this.getRent();
        }
        return 0;
    }
}
