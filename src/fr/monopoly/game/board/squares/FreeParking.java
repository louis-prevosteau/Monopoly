package fr.monopoly.game.board.squares;

import fr.monopoly.Monopoly;
import fr.monopoly.game.Player;
import fr.monopoly.game.board.cardstack.CardStack;

import javax.swing.*;
import java.util.ArrayList;

public class FreeParking extends Space {

    private int jackpot;

    public FreeParking(int position) {
        super(position, "Parc Gratuit");
        jackpot = 0;
    }

    public int getJackpot() {
        return jackpot;
    }

    public void setJackpot(int jackpot) {
        this.jackpot = jackpot;
    }

    @Override
    public void doAction(Player player, ArrayList<Player> players, Monopoly game, CardStack stack) {
        JOptionPane.showMessageDialog(null, player.getName() + ", vous êtes sur la case Parc Gratuit", this.getName(), JOptionPane.INFORMATION_MESSAGE);
        if (jackpot > 0) {
            JOptionPane.showMessageDialog(null, player.getName() + ", vous empochez le jackpot de " + jackpot + " €", "Jackpot", JOptionPane.INFORMATION_MESSAGE);
            player.setMoney(player.getMoney() + jackpot);
            jackpot = 0;
        }
        game.refreshAll();
    }
}
