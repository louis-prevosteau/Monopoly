package fr.monopoly.game.board.squares;

public class Ground extends Property {

    private int nbHouse = 0, housePrice;
    private Color color;

    public Ground(int position, String name, int price, int rent, int housePrice, Color color) {
        super(position, name, price, rent);
        this.housePrice = housePrice;
        this.color = color;
    }
}
