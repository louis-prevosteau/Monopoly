package fr.monopoly.game.board.squares;

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

    public void setHousePrice(int housePrice) {
        this.housePrice = housePrice;
    }

    @Override
    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
