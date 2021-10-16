package fr.monopoly.game.board.squares;

import fr.monopoly.Monopoly;
import fr.monopoly.game.Player;
import fr.monopoly.game.board.cardstack.CardStack;
import fr.monopoly.game.board.cardstack.Type;

import java.util.ArrayList;

public class Community extends Space {

    public Community(int position) {
        super(position, "Caisse de Communauté");
    }

    @Override
    public void doAction(Player player, ArrayList<Player> players, Monopoly game, CardStack stack) {
        int action = stack.getCard(Type.COMMUNITY).execute(player, players);
        if (action == 3)
            game.isTurnPhase2();
        game.refreshAll();
    }
}
