package fr.monopoly.game.board.squares;

import fr.monopoly.Monopoly;
import fr.monopoly.game.Player;
import fr.monopoly.game.board.cardstack.CardStack;

import javax.swing.*;
import java.util.ArrayList;

public class GoToJail extends Space {

    public GoToJail(int position) {
        super(position, "Allez en prison");
    }

    @Override
    public void doAction(Player player, ArrayList<Player> players, Monopoly game, CardStack stack) {
        JOptionPane.showMessageDialog(null, player.getName() + ", Allez en prison. Ne passez pas par la case Départ.", this.getName(), JOptionPane.INFORMATION_MESSAGE);
        player.setJailTime(3);
        player.moveTo(11);
        game.refreshAll();
    }
}
