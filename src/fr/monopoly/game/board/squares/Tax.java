package fr.monopoly.game.board.squares;

public class Tax extends Space {

    private int tax;

    public Tax(int position, String name, int tax) {
        super(position, name);
        this.tax = tax;
    }

    public int getTax() {
        return tax;
    }

    public void setTax(int tax) {
        this.tax = tax;
    }
}
