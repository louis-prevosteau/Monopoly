package fr.monopoly.game.board.squares;

import fr.monopoly.game.Player;

import java.util.ArrayList;

public class Company extends Property {

    public Company(int position, String name, int price, int rent) {
        super(position, name, price, rent);
    }

    @Override
    public int updateRent(Player owner, Player currentPlayer) {
        ArrayList<Company> companiesOwned = new ArrayList<>();
        for (Property p : owner.getPropertiesOwned()) {
            if (p instanceof Company && !p.isMortgage())
                companiesOwned.add((Company) p);
        }
        return switch (companiesOwned.size()) {
            case 1 -> this.getRent() * currentPlayer.getRoll();
            case 2 -> 10 * currentPlayer.getRoll();
            default -> 0;
        };
    }
}
