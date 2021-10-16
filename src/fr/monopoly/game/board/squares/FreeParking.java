package fr.monopoly.game.board.squares;

import fr.monopoly.Monopoly;
import fr.monopoly.game.Player;
import fr.monopoly.game.board.cardstack.CardStack;

import javax.swing.*;
import java.util.ArrayList;

public class FreeParking extends Space {

    public FreeParking(int position) {
        super(position, "Parc Gratuit");
    }

    @Override
    public void doAction(Player player, ArrayList<Player> players, Monopoly game, CardStack stack) {
        JOptionPane.showMessageDialog(null, player.getName() + ", vous êtes sur la case Parc Gratuit", this.getName(), JOptionPane.INFORMATION_MESSAGE);
        game.refreshAll();
    }
}
