package fr.monopoly.game.board.squares;

import fr.monopoly.Monopoly;
import fr.monopoly.game.Player;
import fr.monopoly.game.board.cardstack.CardStack;

import java.util.ArrayList;

public class Space {

    private int position;
    private String name;
    private Space next;

    public Space() {
    }

    public Space(int position, String name) {
        this.position = position;
        this.name = name;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getName() {
        return name;
    }

    public Space getNext() {
        return next;
    }

    public void setNext(Space next) {
        this.next = next;
    }

    public void doAction(Player player, ArrayList<Player> players, Monopoly game, CardStack stack) {}
}
