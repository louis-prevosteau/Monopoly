package fr.monopoly.game.board.squares;

import fr.monopoly.Monopoly;
import fr.monopoly.game.Player;
import fr.monopoly.game.board.cardstack.Card;
import fr.monopoly.game.board.cardstack.CardStack;
import fr.monopoly.game.board.cardstack.Type;

import java.util.ArrayList;

public class Community extends Space {

    public Community(int position) {
        super(position, "Caisse de Communauté");
    }

    @Override
    public void doAction(Player player, ArrayList<Player> players, Monopoly game, CardStack stack) {
        Card card = stack.getCard(Type.COMMUNITY);
        int action = card.execute(player, players);
        if (action == 3) {
            if (card.getMessage().equals("Allez directement en prison. Ne passez pas par la case Départ, ne collectez pas 200 €")) {
                game.refreshAll();
                return;
            }
            game.isTurnPhase2();
        }
        game.refreshAll();
    }
}
