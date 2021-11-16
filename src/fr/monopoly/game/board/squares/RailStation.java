package fr.monopoly.game.board.squares;

import fr.monopoly.game.Player;

import java.util.ArrayList;

public class RailStation extends Property {

    public RailStation(int position, String name, int price, int rent) {
        super(position, name, price, rent);
    }

    @Override
    public int updateRent(Player owner, Player currentPlayer) {
        ArrayList<RailStation> railStationsOwned = new ArrayList<RailStation>();
        for (Property p : owner.getPropertiesOwned()) {
            if (p instanceof RailStation)
                railStationsOwned.add((RailStation) p);
        }
        switch (railStationsOwned.size()) {
            case 1:
                return this.getRent();
            case 2:
                return 2 * this.getRent();
            case 3:
                return 3 * this.getRent();
            case 4:
                return 4 * this.getRent();
        }
        return 0;
    }
}
