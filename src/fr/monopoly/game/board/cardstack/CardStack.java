package fr.monopoly.game.board.cardstack;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Scanner;

public class CardStack {

    private LinkedList<Card> chance = new LinkedList<>();
    private LinkedList<Card> community = new LinkedList<>();

    public CardStack() {
        try {
            Scanner cardScanner = new Scanner(new File("CardsStack.txt"));
            int action, value, position;
            String message;
            Card card;
            int CARDS = 24;
            for (int i = 0; i < CARDS; i++) {
                switch (cardScanner.nextLine().charAt(0)) {
                    case 'h' -> {
                        message = cardScanner.nextLine();
                        action = Integer.parseInt(cardScanner.nextLine());
                        value = Integer.parseInt(cardScanner.nextLine());
                        position = Integer.parseInt(cardScanner.nextLine());
                        card = new Card(action, value, position, message, Type.CHANCE);
                        chance.add(card);
                    }
                    case 'c' -> {
                        message = cardScanner.nextLine();
                        action = Integer.parseInt(cardScanner.nextLine());
                        value = Integer.parseInt(cardScanner.nextLine());
                        position = Integer.parseInt(cardScanner.nextLine());
                        card = new Card(action, value, position, message, Type.COMMUNITY);
                        community.add(card);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Collections.shuffle(chance);
        Collections.shuffle(community);
    }

    public Card getCard(Type type) {
        Card card = null;
        if (type.equals(Type.CHANCE)) {
            card = chance.removeFirst();
            chance.addLast(card);
        } else if (type.equals(Type.COMMUNITY)) {
            card = community.removeFirst();
            community.addLast(card);
        }
        return card;
    }
}
