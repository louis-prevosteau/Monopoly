package fr.monopoly.game.board.squares;

import fr.monopoly.game.Player;

import java.util.ArrayList;

public class RailStation extends Property {

    public RailStation(int position, String name, int price, int rent) {
        super(position, name, price, rent);
    }

    @Override
    public int updateRent(Player owner, Player currentPlayer) {
        ArrayList<RailStation> railStationsOwned = new ArrayList<>();
        for (Property p : owner.getPropertiesOwned()) {
            if (p instanceof RailStation && !p.isMortgage())
                railStationsOwned.add((RailStation) p);
        }
        return switch (railStationsOwned.size()) {
            case 1 -> this.getRent();
            case 2 -> 2 * this.getRent();
            case 3 -> 3 * this.getRent();
            case 4 -> 4 * this.getRent();
            default -> 0;
        };
    }
}
