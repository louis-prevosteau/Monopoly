package fr.monopoly;

import fr.monopoly.game.Player;
import fr.monopoly.game.board.Board;
import fr.monopoly.game.board.cardstack.CardStack;
import fr.monopoly.game.board.squares.Ground;
import fr.monopoly.game.board.squares.Property;
import fr.monopoly.game.board.squares.Space;
import fr.monopoly.ui.BoardUI;
import fr.monopoly.ui.SideUI;
import fr.monopoly.ui.TopUI;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Monopoly {

    private JFrame frame = new JFrame("Monopoly");
    private TopUI north, south;
    private SideUI east, west;
    private BoardUI center;
    private ArrayList<Player> players;
    private Player currentPlayer;
    private Board board;
    private CardStack cardStack;
    private int numberOfPlayers;
    private int houses = 32;
    private int hotels = 12;
    private boolean win = false;


    public Monopoly() {
    }

    public int getHouses() {
        return houses;
    }

    public void setHouses(int houses) {
        this.houses = houses;
    }

    public int getHotels() {
        return hotels;
    }

    public void setHotels(int hotels) {
        this.hotels = hotels;
    }

    public void start() {
        String snPlayers = JOptionPane.showInputDialog("Nombre de joueurs (2, 3, 4):");
        if(snPlayers.charAt(0) == '2' || snPlayers.charAt(0) == '3' || snPlayers.charAt(0) == '4')
            numberOfPlayers = Integer.parseInt(snPlayers);
        else
            start();
        players = new ArrayList<>();
        board = new Board();
        cardStack = new CardStack();
        for(int i = 0 ; i<numberOfPlayers ; i++)
            players.add(i, new Player("Joueur "+(i+1), board.getFirst()));
        int currentPlayerIndex = 0;
        new Player();
        currentPlayer = players.get(currentPlayerIndex);
        center = new BoardUI(players);
        createUI(players);
        JOptionPane.showMessageDialog(null, "Bienvenue!", "Monopoly", JOptionPane.INFORMATION_MESSAGE);
        while(!win){
            isTurnPhase1();
            try {Thread.sleep(300);} catch (InterruptedException e) {e.printStackTrace();}
            isTurnPhase2();
            for(int k = 0; k<players.size(); k++){
                if(players.get(k).getMoney() < 1){
                    String loserName = players.get(k).getName();
                    JOptionPane.showMessageDialog(null, currentPlayer.getName() + ", vous ??tes en faillite.", loserName + " - Banqueroute", JOptionPane.INFORMATION_MESSAGE);
                    isDying();
                    if(players.get(k).getMoney() < 1){
                        players.remove(k);
                        JOptionPane.showMessageDialog(null, currentPlayer.getName() + ", vous avez perdu.", loserName + " - Banqueroute!", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
                if(players.size() == 1){
                    String winner = players.get(0).getName();
                    JOptionPane.showMessageDialog(null, winner + " a gagn?? la partie!", "FIN", JOptionPane.INFORMATION_MESSAGE);
                    win = true;
                }
            }
            currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
            currentPlayer = players.get(currentPlayerIndex);
        }
    }

    public void createUI(ArrayList<Player> players) {
        frame.setResizable(true);
        frame.setSize(830, 720);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.getContentPane().add(BorderLayout.CENTER, center);
        frame.setLocationRelativeTo(null);
        frame.setBackground(new Color(80, 160, 250));
        if(players.size() >= 2){
            frame.setSize(920, 560);
            west = new SideUI();
            west.setBackground(Color.magenta);
            frame.getContentPane().add(BorderLayout.WEST, west);
            west.refreshList(players.get(0));
            west.repaint();
            east = new SideUI();
            east.setBackground(Color.green);
            frame.getContentPane().add(BorderLayout.EAST, east);
            east.refreshList(players.get(1));
            east.repaint();
        }
        if(players.size() >= 3){
            frame.setSize(870, 640);
            north = new TopUI();
            north.setBackground(Color.blue);
            frame.getContentPane().add(BorderLayout.NORTH, north);
            north.refreshList(players.get(2));
            north.repaint();
        }
        if(players.size() == 4){
            frame.setSize(830, 720);
            south = new TopUI();
            south.setBackground(Color.red);
            frame.getContentPane().add(BorderLayout.SOUTH, south);
            south.refreshList(players.get(3));
            south.repaint();
        }
        frame.validate();
    }
    
    public void refreshAll() {
        west.refreshList(players.get(0));
        east.refreshList(players.get(1));
        west.repaint();
        east.repaint();
        if(players.size() > 2){
            north.refreshList(players.get(2));
            north.repaint();
        }
        if(players.size() > 3){
            south.refreshList(players.get(3));
            south.repaint();
        }
        center.repaint();
    }

    public void isDying() {
        String[] survive = {
                "Confirmer",
                "Vendre",
                "Vendre une maison",
                "Hypoth??quer",
                "Echanger"
        };
        ArrayList<String> props = new ArrayList<>(currentPlayer.getPropertiesOwned().size());
        for (Property p : currentPlayer.getPropertiesOwned())
            props.add(p.getName());
        ArrayList<Ground> groundsOwned = new ArrayList<>();
        for (Property p : currentPlayer.getPropertiesOwned()) {
            if (p instanceof Ground)
                groundsOwned.add((Ground) p);
        }
        String[] grounds = new String[groundsOwned.size()];
        for (int i = 0 ; i < grounds.length ; i++)
            grounds[i] = groundsOwned.get(i).getName();
        String[] names = new String[players.size()];
        for (int i = 0 ; i < names.length ; i++)
            names[i] = players.get(i).getName();
        int optionSelected = JOptionPane.showOptionDialog(null, currentPlayer.getName() + ", que voulez-vous faire ?", "Alerte Banqueroute", 0, JOptionPane.QUESTION_MESSAGE, null, survive, null);
        if (optionSelected == 1) {
            if (currentPlayer.getPropertiesOwned().isEmpty()) {
                JOptionPane.showMessageDialog(null, currentPlayer.getName() + ", vous n'avez pas de propri??t??s.", "Pas de propri??t??s", JOptionPane.ERROR_MESSAGE);
            } else {
                Property choice = currentPlayer.getPropertiesOwned().get(JOptionPane.showOptionDialog(null, currentPlayer.getName() + ", quelle propri??t?? voulez-vous vendre ?", "Vendre", 0, JOptionPane.QUESTION_MESSAGE, null, props.toArray(), null));
                if (choice instanceof Ground && currentPlayer.hasMonopoly(choice.getColor()) && !currentPlayer.isNoHousesOnGrounds(choice.getColor())) {
                    JOptionPane.showMessageDialog(null, "Vous ne pouvez pas vendre " + choice.getName() + " car des maisons sont construites sur cette propri??t?? ou sur les propri??t??s voisines.", "Vente impossible", JOptionPane.ERROR_MESSAGE);
                    isDying();
                } else {
                    currentPlayer.sale(choice);
                    JOptionPane.showMessageDialog(null, "Vous avez vendu " + choice.getName(), "Vendu", JOptionPane.INFORMATION_MESSAGE);
                }
                refreshAll();
            }
            isDying();
        } else if (optionSelected == 2) {
            Ground choice = groundsOwned.get(JOptionPane.showOptionDialog(null, currentPlayer.getName() + ", sur quelle propri??t?? voulez-vous vendre une maison ?", "Vendre une maison", 0, JOptionPane.QUESTION_MESSAGE, null, grounds, null));
            currentPlayer.saleHouse(choice, this);
            refreshAll();
            isDying();
        } else if (optionSelected == 3) {
            Property choice = currentPlayer.getPropertiesOwned().get(JOptionPane.showOptionDialog(null, currentPlayer.getName() + ", quelle propri??t?? voulez-vous hypoth??quer ?", "Hypoth??quer", 0, JOptionPane.QUESTION_MESSAGE, null, props.toArray(), null));
            if (choice.isMortgage()) {
                JOptionPane.showMessageDialog(null, choice.getName() + " est d??j?? hypoth??quer.", "Ooops", JOptionPane.ERROR_MESSAGE);
                isDying();
            } else if (choice instanceof Ground && currentPlayer.isNoHousesOnGrounds(choice.getColor())) {
                JOptionPane.showMessageDialog(null, "Il y a encore des maisons sur " + choice.getName() + " ou sur les propri??t??s voisines.", "Ooops", JOptionPane.ERROR_MESSAGE);
                isDying();
            } else {
                currentPlayer.mortgage(choice);
                JOptionPane.showMessageDialog(null, "Vous avez hypoth??quer " + choice.getName(), "Hypoth??qu??e", JOptionPane.INFORMATION_MESSAGE);
            }
            refreshAll();
            isDying();
        } else if (optionSelected == 4) {
            if (currentPlayer.getPropertiesOwned().isEmpty()) {
                JOptionPane.showMessageDialog(null, currentPlayer.getName() + ", vous n'avez pas de propri??t??s.", "Pas de propri??t??s", JOptionPane.ERROR_MESSAGE);
                isDying();
            } else {
                Player trader = players.get(JOptionPane.showOptionDialog(null, currentPlayer.getName() + ", avec quel joueur souhaitez-vous ??changer ?", "Echanger", 0, JOptionPane.QUESTION_MESSAGE, null, names, null));
                if (trader.equals(currentPlayer))
                    isDying();
                if (trader.getPropertiesOwned().isEmpty()) {
                    JOptionPane.showMessageDialog(null, trader.getName() + " n'a pas de propri??t??s.", "Pas de propri??t??s", JOptionPane.ERROR_MESSAGE);
                    isDying();
                } else {
                    ArrayList<String> propsOfTrader = new ArrayList<>(trader.getPropertiesOwned().size());
                    for (Property p : trader.getPropertiesOwned())
                        propsOfTrader.add(p.getName());
                    ArrayList<Property> giveProps = new ArrayList<>();
                    ArrayList<Property> takeProps = new ArrayList<>();
                    int giveNumber = Integer.parseInt(JOptionPane.showInputDialog(currentPlayer.getName() + ", Combien de propri??t??s voulez-vous ??changer ?"));
                    for (int i = 0 ; i < giveNumber ; i++) {
                        Property give = currentPlayer.getPropertiesOwned().get(JOptionPane.showOptionDialog(null, currentPlayer.getName() + ", quelle propri??t?? voulez-vous ??changer ?", "Echanger", 0, JOptionPane.QUESTION_MESSAGE, null, props.toArray(), null));
                        if (give instanceof Ground && !currentPlayer.isNoHousesOnGrounds(give.getColor())) {
                            JOptionPane.showMessageDialog(null, """
                                    Un ??change n'est valable si et seulement si :
                                    - Les propri??t??s ??chang??s ou leurs voisines ne poss??dent pas de maisons (terrains)
                                    - Les joueurs participant ?? l'??change poss??dent la somme d'argent suffisante pour ??changer""", "Echange non-conforme", JOptionPane.ERROR_MESSAGE);
                            isDying();
                        } else {
                            giveProps.add(give);
                            props.remove(give.getName());
                        }
                    }
                    int takeNumber = Integer.parseInt(JOptionPane.showInputDialog(trader.getName() + ", Combien de propri??t??s voulez-vous ??changer ?"));
                    for (int i = 0 ; i < takeNumber ; i++) {
                        Property take = trader.getPropertiesOwned().get(JOptionPane.showOptionDialog(null, trader.getName() + ", quelle propri??t?? voulez-vous ??changer ?", "Echanger", 0, JOptionPane.QUESTION_MESSAGE, null, propsOfTrader.toArray(), null));
                        if (take instanceof Ground && !currentPlayer.isNoHousesOnGrounds(take.getColor())) {
                            JOptionPane.showMessageDialog(null, """
                                    Un ??change n'est valable si et seulement si :
                                    - Les propri??t??s ??chang??s ou leurs voisines ne poss??dent pas de maisons (terrains)
                                    - Les joueurs participant ?? l'??change poss??dent la somme d'argent suffisante pour ??changer""", "Echange non-conforme", JOptionPane.ERROR_MESSAGE);
                            isDying();
                        } else {
                            takeProps.add(take);
                            propsOfTrader.remove(take.getName());
                        }
                    }
                    int cashGive = Integer.parseInt(JOptionPane.showInputDialog(null, currentPlayer.getName() + ", combien voulez-vous donner ?", "Echanger", JOptionPane.QUESTION_MESSAGE));
                    int cashTake = Integer.parseInt(JOptionPane.showInputDialog(null, trader.getName() + ", combien voulez-vous donner ?", "Echanger", JOptionPane.QUESTION_MESSAGE));
                    String[] yesNoOptions = {"Oui", "Non"};
                    int yesNo = JOptionPane.showOptionDialog(null, "Je soussign?? : " + currentPlayer.getName() +
                            ", accepte de donner les propri??t??s :\n " + Arrays.toString(giveProps.toArray()) + "\net " + cashGive + " ??? ?? " + trader.getName() +
                            " contre les propri??t??s :\n" + Arrays.toString(takeProps.toArray()) + "\net " + cashTake + " ??? et inversement.", "Contrat", 0, JOptionPane.QUESTION_MESSAGE, null, yesNoOptions, null);
                    if (yesNo == 1)
                        JOptionPane.showMessageDialog(null, "Contrat refus??", "Contrat refus??", JOptionPane.ERROR_MESSAGE);
                    else {
                        JOptionPane.showMessageDialog(null, "Contrat accept??", "Contrat accept??", JOptionPane.INFORMATION_MESSAGE);
                        currentPlayer.trade(trader, giveProps, takeProps, cashGive, cashTake);
                        refreshAll();
                    }
                }
            }
            isDying();
        }
    }

    public void isInJail() {
        if (currentPlayer.getJailTime() > 0) {
            String[] jailOptions = {"Faire un double", "Utiliser une carte de sortie", "Rester en prison"};
            int exitChoice = JOptionPane.showOptionDialog(null, currentPlayer.getName() + ", vous ??tes en prison que voulez-vous faire ?", "D??tention", 0, JOptionPane.QUESTION_MESSAGE, null, jailOptions, null);
            if (exitChoice == 0) {
                currentPlayer.roll();
                if (currentPlayer.getDice1() == currentPlayer.getDice2()) {
                    JOptionPane.showMessageDialog(null, currentPlayer.getName() + ", vous avez fait un double. Sortez de prison.", "Double", JOptionPane.INFORMATION_MESSAGE);
                    JOptionPane.showMessageDialog(null, currentPlayer.getName() + ", vous avez fait un " + currentPlayer.getRoll() + ". Avancez.", "Avancez", JOptionPane.INFORMATION_MESSAGE);
                    currentPlayer.setJailTime(0);
                    currentPlayer.move();
                    center.repaint();
                } else {
                    JOptionPane.showMessageDialog(null, "Vous n'avez pas fait un double. Restez en prison.", "D??tention", JOptionPane.INFORMATION_MESSAGE);
                    currentPlayer.setJailTime(currentPlayer.getJailTime() - 1);
                    return;
                }
            } else if (exitChoice == 1) {
                if (currentPlayer.getJailExit() > 0) {
                    String[] yesNoOptions = {"Oui", "Non"};
                    int yesNo = JOptionPane.showOptionDialog(null, "Voulez-vous utilisez une permission de sortie ?", "Permission de sortie", 0, JOptionPane.QUESTION_MESSAGE, null, yesNoOptions, null);
                    if (yesNo == 0) {
                        JOptionPane.showMessageDialog(null, "Sortez de prison.", "Evasion", JOptionPane.INFORMATION_MESSAGE);
                        currentPlayer.setJailExit(currentPlayer.getJailExit() - 1);
                        currentPlayer.setJailTime(0);
                    } else {
                        JOptionPane.showMessageDialog(null, "Restez en prison.", "D??tention", JOptionPane.INFORMATION_MESSAGE);
                        currentPlayer.setJailTime(currentPlayer.getJailTime() - 1);
                        return;
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Vous n'avez pas de carte de permission de sortie. Restez en prison.", "D??tention", JOptionPane.INFORMATION_MESSAGE);
                    currentPlayer.setJailTime(currentPlayer.getJailTime() - 1);
                    return;
                }
            } else if (exitChoice == 2) {
                JOptionPane.showMessageDialog(null, "Restez en prison.", "D??tention", JOptionPane.INFORMATION_MESSAGE);
                currentPlayer.setJailTime(currentPlayer.getJailTime() - 1);
                return;
            }
        }
    }

    public void isTurnPhase1() {
        try {
            Thread.sleep(400);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        isInJail();
        if (currentPlayer.getJailTime() != 0)
            return;
        String[] phase1Options = {
                "Lancer les d??s",
                "Voir les propri??t??s d'un joueur",
                "Acheter une maison",
                "Echanger",
                "Vendre une propri??t??",
                "Vendre une maison",
                "Hypoth??quer",
                "D??shypoth??quer",
                "Quitter"
                };
        ArrayList<String> props = new ArrayList<>(currentPlayer.getPropertiesOwned().size());
        for (Property p : currentPlayer.getPropertiesOwned())
            props.add(p.getName());
        ArrayList<Ground> groundsOwned = new ArrayList<>();
        for (Property p : currentPlayer.getPropertiesOwned()) {
            if (p instanceof Ground)
                groundsOwned.add((Ground) p);
        }
        String[] grounds = new String[groundsOwned.size()];
        for (int i = 0 ; i < grounds.length ; i++)
            grounds[i] = groundsOwned.get(i).getName();
        String[] names = new String[players.size()];
        for (int i = 0 ; i < names.length ; i++)
            names[i] = players.get(i).getName();
        int phase1 = JOptionPane.showOptionDialog (null, "Que voulez-vous faire ?", currentPlayer.getName(), 0, JOptionPane.QUESTION_MESSAGE, null, phase1Options, null);
        if (phase1 == 0) {
            currentPlayer.roll();
            JOptionPane.showMessageDialog(null, currentPlayer.getName() + ", vous avez fait un " + currentPlayer.getRoll(), "Lancer", JOptionPane.INFORMATION_MESSAGE);
            currentPlayer.move();
            center.repaint();
        } else if (phase1 == 1) {
            Player player = players.get(JOptionPane.showOptionDialog(null, "Selectionnez un joueur :", "Voir les propri??t??s", 0, JOptionPane.QUESTION_MESSAGE, null, names, null));
            if (player.getPropertiesOwned().isEmpty()) {
                JOptionPane.showMessageDialog(null, player.getName() + " n'a pas de propri??t??s.", "Pas de propri??t??s", JOptionPane.INFORMATION_MESSAGE);
            } else {
                String[] playerProps = new String[player.getPropertiesOwned().size()];
                for (int i = 0 ; i < playerProps.length ; i++)
                    playerProps[i] = player.getPropertiesOwned().get(i).getName();
                Property viewed = player.getPropertiesOwned().get(JOptionPane.showOptionDialog(null, "Quelle propri??t?? voulez-vous voir ?", "Voir une propri??t?? de " + player.getName(), 0, JOptionPane.QUESTION_MESSAGE, null, playerProps, null));
                int currentRent = viewed.updateRent(viewed.getOwner(), currentPlayer);
                String[] okOption = {"Ok"};
                String details = "Nom: " + viewed.getName() +
                        "\nLoyer: " + currentRent + " ???" +
                        "\nNombre de maisons: " + viewed.getNbHouse() +
                        "\nHypoth??qu??e: " + viewed.isMortgage();
                JOptionPane.showOptionDialog(null, details, viewed.getName(), 0, JOptionPane.QUESTION_MESSAGE, null, okOption, null);
            }
            isTurnPhase1();
        } else if (phase1 == 2) {
            if (currentPlayer.getPropertiesOwned().isEmpty()) {
                JOptionPane.showMessageDialog(null, currentPlayer.getName() + ", vous n'avez pas de propri??t??s.", "Pas de propri??t??s", JOptionPane.ERROR_MESSAGE);
            } else {
                Ground choice = groundsOwned.get(JOptionPane.showOptionDialog(null, "Sur quelle terrains voulez-vous construire une maison ?", "Construire une maison", 0, JOptionPane.QUESTION_MESSAGE, null, grounds, null));
                if (currentPlayer.hasMonopoly(choice.getColor()))
                    currentPlayer.buyHouse(choice, this);
                else
                    JOptionPane.showMessageDialog(null, "Vous n'avez pas le monopole.", "Ooops", JOptionPane.INFORMATION_MESSAGE);
                refreshAll();
            }
            isTurnPhase1();
        } else if (phase1 == 3) {
            if (currentPlayer.getPropertiesOwned().isEmpty()) {
                JOptionPane.showMessageDialog(null, currentPlayer.getName() + ", vous n'avez pas de propri??t??s.", "Pas de propri??t??s", JOptionPane.ERROR_MESSAGE);
                isTurnPhase1();
            } else {
                Player trader = players.get(JOptionPane.showOptionDialog(null, currentPlayer.getName() + ", avec quel joueur souhaitez-vous ??changer ?", "Echanger", 0, JOptionPane.QUESTION_MESSAGE, null, names, null));
                if (trader.equals(currentPlayer))
                    isDying();
                if (trader.getPropertiesOwned().isEmpty()) {
                    JOptionPane.showMessageDialog(null, trader.getName() + " n'a pas de propri??t??s.", "Pas de propri??t??s", JOptionPane.ERROR_MESSAGE);
                    isDying();
                } else {
                    ArrayList<String> propsOfTrader = new ArrayList<>(trader.getPropertiesOwned().size());
                    for (Property p : trader.getPropertiesOwned())
                        propsOfTrader.add(p.getName());
                    ArrayList<Property> giveProps = new ArrayList<>();
                    ArrayList<Property> takeProps = new ArrayList<>();
                    int giveNumber = Integer.parseInt(JOptionPane.showInputDialog(currentPlayer.getName() + ", Combien de propri??t??s voulez-vous ??changer ?"));
                    for (int i = 0 ; i < giveNumber ; i++) {
                        Property give = currentPlayer.getPropertiesOwned().get(JOptionPane.showOptionDialog(null, currentPlayer.getName() + ", quelle propri??t?? voulez-vous ??changer ?", "Echanger", 0, JOptionPane.QUESTION_MESSAGE, null, props.toArray(), null));
                        if (give instanceof Ground && !currentPlayer.isNoHousesOnGrounds(give.getColor())) {
                            JOptionPane.showMessageDialog(null, """
                                    Un ??change n'est valable si et seulement si :
                                    - Les propri??t??s ??chang??s ou leurs voisines ne poss??dent pas de maisons (terrains)
                                    - Les joueurs participant ?? l'??change poss??dent la somme d'argent suffisante pour ??changer""", "Echange non-conforme", JOptionPane.ERROR_MESSAGE);
                            isTurnPhase1();
                        } else {
                            giveProps.add(give);
                            props.remove(give.getName());
                        }
                    }
                    int takeNumber = Integer.parseInt(JOptionPane.showInputDialog(trader.getName() + ", Combien de propri??t??s voulez-vous ??changer ?"));
                    for (int i = 0 ; i < takeNumber ; i++) {
                        Property take = trader.getPropertiesOwned().get(JOptionPane.showOptionDialog(null, trader.getName() + ", quelle propri??t?? voulez-vous ??changer ?", "Echanger", 0, JOptionPane.QUESTION_MESSAGE, null, propsOfTrader.toArray(), null));
                        if (take instanceof Ground && !currentPlayer.isNoHousesOnGrounds(take.getColor())) {
                            JOptionPane.showMessageDialog(null, """
                                    Un ??change n'est valable si et seulement si :
                                    - Les propri??t??s ??chang??s ou leurs voisines ne poss??dent pas de maisons (terrains)
                                    - Les joueurs participant ?? l'??change poss??dent la somme d'argent suffisante pour ??changer""", "Echange non-conforme", JOptionPane.ERROR_MESSAGE);
                            isTurnPhase1();
                        } else {
                            takeProps.add(take);
                            propsOfTrader.remove(take.getName());
                        }
                    }
                    int cashGive = Integer.parseInt(JOptionPane.showInputDialog(null, currentPlayer.getName() + ", combien voulez-vous donner ?", "Echanger", JOptionPane.QUESTION_MESSAGE));
                    int cashTake = Integer.parseInt(JOptionPane.showInputDialog(null, trader.getName() + ", combien voulez-vous donner ?", "Echanger", JOptionPane.QUESTION_MESSAGE));
                    String[] yesNoOptions = {"Oui", "Non"};
                    int yesNo = JOptionPane.showOptionDialog(null, "Je soussign?? : " + currentPlayer.getName() +
                            ", accepte de donner la propri??t??s :\n" + Arrays.toString(giveProps.toArray()) + "\net " + cashGive + " ??? ?? " + trader.getName() +
                            "contre la propri??t??s :\n" + Arrays.toString(takeProps.toArray()) + "\net " + cashTake + " ??? et inversement.", "Contrat", 0, JOptionPane.QUESTION_MESSAGE, null, yesNoOptions, null);
                    if (yesNo == 1)
                        JOptionPane.showMessageDialog(null, "Contrat refus??", "Contrat refus??", JOptionPane.ERROR_MESSAGE);
                    else {
                        JOptionPane.showMessageDialog(null, "Contrat accept??", "Contrat accept??", JOptionPane.INFORMATION_MESSAGE);
                        currentPlayer.trade(trader, giveProps, takeProps, cashGive, cashTake);
                        refreshAll();
                    }
                    isTurnPhase1();
                }
            }
        } else if (phase1 == 4) {
            if (currentPlayer.getPropertiesOwned().isEmpty()) {
                JOptionPane.showMessageDialog(null, currentPlayer.getName() + ", vous n'avez pas de propri??t??s.", "Pas de propri??t??s", JOptionPane.ERROR_MESSAGE);
                isTurnPhase1();
            } else {
                Property choice = currentPlayer.getPropertiesOwned().get(JOptionPane.showOptionDialog(null, currentPlayer.getName() + ", quelle propri??t?? voulez-vous vendre ?", "Vendre", 0, JOptionPane.QUESTION_MESSAGE, null, props.toArray(), null));
                if (choice instanceof Ground && currentPlayer.hasMonopoly(choice.getColor()) && !currentPlayer.isNoHousesOnGrounds(choice.getColor())) {
                    JOptionPane.showMessageDialog(null, "Vous ne pouvez pas vendre " + choice.getName() + " car des maisons sont construites sur cette propri??t?? ou sur les propri??t??s voisines.", "Vente impossible", JOptionPane.ERROR_MESSAGE);
                    isTurnPhase1();
                } else {
                    currentPlayer.sale(choice);
                    JOptionPane.showMessageDialog(null, "Vous avez vendu " + choice.getName(), "Vendu", JOptionPane.INFORMATION_MESSAGE);
                }
            }
            refreshAll();
            isTurnPhase1();
        } else if (phase1 == 5) {
            if (currentPlayer.getPropertiesOwned().isEmpty()) {
                JOptionPane.showMessageDialog(null, currentPlayer.getName() + ", vous n'avez pas de propri??t??s.", "Pas de propri??t??s", JOptionPane.ERROR_MESSAGE);
            } else {
                Ground choice = groundsOwned.get(JOptionPane.showOptionDialog(null, "Sur quelle terrains voulez-vous d??truire une maison ?", "D??truire une maison", 0, JOptionPane.QUESTION_MESSAGE, null, grounds, null));
                currentPlayer.saleHouse(choice, this);
                refreshAll();
            }
            isTurnPhase1();
        } else if (phase1 == 6) {
            if (currentPlayer.getPropertiesOwned().isEmpty()) {
                JOptionPane.showMessageDialog(null, currentPlayer.getName() + ", vous n'avez pas de propri??t??s.", "Pas de propri??t??s", JOptionPane.ERROR_MESSAGE);
                isTurnPhase1();
            } else {
                Property choice = currentPlayer.getPropertiesOwned().get(JOptionPane.showOptionDialog(null, currentPlayer.getName() + ", quelle propri??t?? voulez-vous hypoth??quer ?", "Hypoth??quer", 0, JOptionPane.QUESTION_MESSAGE, null, props.toArray(), null));
                if (choice instanceof Ground && currentPlayer.hasMonopoly(choice.getColor()) && !currentPlayer.isNoHousesOnGrounds(choice.getColor())) {
                    JOptionPane.showMessageDialog(null, "Vous ne pouvez pas hypoth??quer " + choice.getName() + " car des maisons sont construites sur cette propri??t?? ou sur les propri??t??s voisines.", "hypoth??que impossible", JOptionPane.ERROR_MESSAGE);
                    isTurnPhase1();
                } else if (choice.isMortgage()) {
                    JOptionPane.showMessageDialog(null, choice.getName() + " est d??j?? hypoth??qu??e", "Ooops", JOptionPane.ERROR_MESSAGE);
                    isTurnPhase1();
                } else {
                    currentPlayer.mortgage(choice);
                    JOptionPane.showMessageDialog(null, "Vous avez hypoth??qu?? " + choice.getName(), "Hypoth??qu??", JOptionPane.INFORMATION_MESSAGE);
                }
            }
            refreshAll();
            isTurnPhase1();
        } else if (phase1 == 7) {
            if (currentPlayer.getPropertiesOwned().isEmpty()) {
                JOptionPane.showMessageDialog(null, currentPlayer.getName() + ", vous n'avez pas de propri??t??s.", "Pas de propri??t??s", JOptionPane.ERROR_MESSAGE);
                isTurnPhase1();
            } else {
                Property choice = currentPlayer.getPropertiesOwned().get(JOptionPane.showOptionDialog(null, currentPlayer.getName() + ", quelle propri??t?? voulez-vous hypoth??quer ?", "Hypoth??quer", 0, JOptionPane.QUESTION_MESSAGE, null, props.toArray(), null));
                if (!currentPlayer.canPay(choice.getPrice() / 2)) {
                    JOptionPane.showMessageDialog(null, "Vous n' avez pas assez d'argent pour d??shypoth??quer.", "d??shypoth??que impossible", JOptionPane.ERROR_MESSAGE);
                    isTurnPhase1();
                } else if (!choice.isMortgage()) {
                    JOptionPane.showMessageDialog(null, choice.getName() + " est d??j?? d??shypoth??qu??e", "Ooops", JOptionPane.ERROR_MESSAGE);
                    isTurnPhase1();
                } else {
                    currentPlayer.unmortgage(choice);
                    JOptionPane.showMessageDialog(null, "Vous avez d??shypoth??qu?? " + choice.getName(), "D??shypoth??qu??", JOptionPane.INFORMATION_MESSAGE);
                }
            }
            refreshAll();
            isTurnPhase1();
        } else if (phase1 == 8)
            System.exit(0);
        if (currentPlayer.getDice1() == currentPlayer.getDice2()) {
            currentPlayer.setDoubleCount(currentPlayer.getDoubleCount() + 1);
            if (currentPlayer.getDoubleCount() == 3) {
                JOptionPane.showMessageDialog(null, currentPlayer.getName() + ", vous avez fait 3 doubles d'affil??,  allez en prison. Ne passez pas par la case D??part.", "Allez en prison", JOptionPane.INFORMATION_MESSAGE);
                currentPlayer.moveTo(11);
                currentPlayer.setDoubleCount(0);
                currentPlayer.setJailTime(3);
                return;
            }
            isTurnPhase2();
            JOptionPane.showMessageDialog(null, currentPlayer.getName() + ", vous avez fait un double");
            isTurnPhase1();
        } else
            currentPlayer.setDoubleCount(0);
    }

    public void isTurnPhase2() {
        if (currentPlayer.getJailTime() > 0)
            return;
        int reference = currentPlayer.getCurrentSpace().getPosition();
        Space landed = board.getFirst();
        while (landed.getPosition() != reference)
            landed = landed.getNext();
        landed.doAction(currentPlayer, players, this, cardStack);
    }

    public static void main(String[] args) {
         Monopoly game = new Monopoly();
         game.start();
    }
}
