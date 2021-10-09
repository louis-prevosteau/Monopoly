package fr.monopoly.game.board.cardstack;

import fr.monopoly.game.Player;

import javax.swing.*;
import java.util.ArrayList;

public class Card {

    private int action, value, position;
    private String message;
    private Type type;

    public Card(int action, int value, int position, String message, Type type) {
        this.action = action;
        this.value = value;
        this.position = position;
        this.message = message;
        this.type = type;
    }

    public int execute(Player player, ArrayList<Player> players) {
        String title;
        if (this.type.equals(Type.CHANCE))
            title = "Chance";
        else
            title = "Caisse de Communauté";
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
        switch (action) {
            case 1:
                player.setMoney(player.getMoney() + value);
                break;
            case 2:
                player.setMoney(player.getMoney() + players.size() * value);
                for (Player p : players)
                    p.setMoney(p.getMoney() - value);
                break;
            case 3:
                player.moveTo(position);
                break;
            case 4:
                player.setJailExit(player.getJailExit() + 1);
                break;
        }
        return this.action;
    }
}
