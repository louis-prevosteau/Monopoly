package fr.monopoly.game.board.squares;

import fr.monopoly.Monopoly;
import fr.monopoly.game.Player;
import fr.monopoly.game.board.cardstack.CardStack;

import javax.swing.*;
import java.util.ArrayList;

public class Go extends Space {

    public Go(int position) {
        super(position, "Départ");
    }

    @Override
    public void doAction(Player player, ArrayList<Player> players, Monopoly game, CardStack stack) {
        JOptionPane.showMessageDialog(null, player.getName() + ", vous êtes sur la case Départ. Recevez 200 €.", this.getName(), JOptionPane.INFORMATION_MESSAGE);
        player.setMoney(player.getMoney() + 200);
        game.refreshAll();
    }
}
