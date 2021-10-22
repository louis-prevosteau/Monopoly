package fr.monopoly.game.board.squares;

import fr.monopoly.Monopoly;
import fr.monopoly.game.Player;
import fr.monopoly.game.board.cardstack.CardStack;

import javax.swing.*;
import java.util.ArrayList;

public class Tax extends Space {

    private int tax;

    public Tax(int position, String name, int tax) {
        super(position, name);
        this.tax = tax;
    }

    @Override
    public void doAction(Player player, ArrayList<Player> players, Monopoly game, CardStack stack) {
        JOptionPane.showMessageDialog(null, player.getName() + ", vous êtes sur la case " + this.getName() + ". Payez " + tax + " €.", this.getName(), JOptionPane.INFORMATION_MESSAGE);
        player.setMoney(player.getMoney() - tax);
        game.refreshAll();
    }
}
