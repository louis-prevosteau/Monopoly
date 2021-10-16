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

public class Monopoly {

    private JFrame frame = new JFrame("Monopoly");
    private TopUI north, south;
    private SideUI east, west;
    private BoardUI center;
    private ArrayList<Player> players;
    private Player currentPlayer;
    private Board board;
    private CardStack cardStack;
    private int currentPlayerIndex, numberOfPlayers;
    private boolean win = false;


    public Monopoly() {
    }

    public void start() {
        String numberOfPlayersStr = JOptionPane.showInputDialog(null, "Nombre de joueurs (2, 3, 4): ");
        if (numberOfPlayersStr.charAt(0) == '2' || numberOfPlayersStr.charAt(0) == '3' || numberOfPlayersStr.charAt(0) == '4')
            numberOfPlayers = Integer.parseInt(numberOfPlayersStr);
        else
            start();
        players = new ArrayList<Player>();
        board = new Board();
        cardStack = new CardStack();
        for (int i = 0 ; i < numberOfPlayers ; i++)
            players.add(new Player("Joueur " + (i+1), board.getFirst()));
        currentPlayerIndex = 0;
        currentPlayer = new Player();
        currentPlayer = players.get(currentPlayerIndex);
        center = new BoardUI(players);
        createUI(players);
        JOptionPane.showMessageDialog(null, "Bienvenue", "Monopoly", JOptionPane.INFORMATION_MESSAGE);
        while (win == false) {
            isTurnPhase1();
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            isTurnPhase2();
            for (Player p : players) {
                if (p.getMoney() < 1) {
                    String loser = p.getName();
                    JOptionPane.showMessageDialog(null, "Faillite pour " + loser);
                    isDying();
                    if (p.getMoney() < 1) {
                        for (Property prop : p.getPropertiesOwned()) {
                            prop.setOwner(null);
                            p.getPropertiesOwned().remove(prop);
                        }
                        players.remove(p);
                        JOptionPane.showMessageDialog(null, p.getName() + ", vous avez perdu.", "Game Over", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
                if (players.size() == 1) {
                    String winner = players.get(0).getName();
                    JOptionPane.showMessageDialog(null, winner + " a gagné la partie", "Vainqueur", JOptionPane.INFORMATION_MESSAGE);
                    win = true;
                }
                currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
                currentPlayer = players.get(currentPlayerIndex);
            }
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
                "Hypothéquer",
                "Echanger"
        };
        String[] props = new String[currentPlayer.getPropertiesOwned().size()];
        for (int i = 0 ; i < props.length ; i++)
            props[i] = currentPlayer.getPropertiesOwned().get(i).getName();
        ArrayList<Ground> groundsOwned = new ArrayList<Ground>();
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
                JOptionPane.showMessageDialog(null, currentPlayer.getName() + ", vous n'avez pas de propriétés.", "Pas de propriétés", JOptionPane.ERROR_MESSAGE);
                isDying();
            } else {
                Property choice = currentPlayer.getPropertiesOwned().get(JOptionPane.showOptionDialog(null, currentPlayer.getName() + ", quelle propriété voulez-vous vendre ?", "Vendre", 0, JOptionPane.QUESTION_MESSAGE, null, props, null));
                if (choice instanceof Ground && currentPlayer.hasMonopoly(choice.getColor()) && !currentPlayer.isNoHousesOnGrounds(choice.getColor())) {
                    JOptionPane.showMessageDialog(null, "Vous ne pouvez pas vendre " + choice.getName() + " car des maisons sont construites sur cette propriété ou sur les propriétés voisines.", "Vente impossible", JOptionPane.ERROR_MESSAGE);
                    isDying();
                } else {
                    currentPlayer.sale(choice);
                    JOptionPane.showMessageDialog(null, "Vous avez vendu " + choice.getName(), "Vendu", JOptionPane.INFORMATION_MESSAGE);
                }
                refreshAll();
                isDying();
            }
        } else if (optionSelected == 2) {
            Ground choice = groundsOwned.get(JOptionPane.showOptionDialog(null, currentPlayer.getName() + ", sur quelle propriété voulez-vous vendre une maison ?", "Vendre une maison", 0, JOptionPane.QUESTION_MESSAGE, null, grounds, null));
            currentPlayer.saleHouse(choice);
            JOptionPane.showMessageDialog(null, "Maison vendu sur " + choice.getName(), "Maison vendu", JOptionPane.INFORMATION_MESSAGE);
            refreshAll();
            isDying();
        } else if (optionSelected == 3) {
            Property choice = currentPlayer.getPropertiesOwned().get(JOptionPane.showOptionDialog(null, currentPlayer.getName() + ", quelle propriété voulez-vous hypothéquer ?", "Hypothéquer", 0, JOptionPane.QUESTION_MESSAGE, null, props, null));
            if (choice.isMortgage() == false) {
                JOptionPane.showMessageDialog(null, choice.getName() + " est déjà hypothéquer.", "Ooops", JOptionPane.ERROR_MESSAGE);
                isDying();
            } else if (choice instanceof Ground && currentPlayer.isNoHousesOnGrounds(choice.getColor())) {
                JOptionPane.showMessageDialog(null, "Il y a encore des maisons sur " + choice.getName() + " ou sur les propriétés voisines.", "Ooops", JOptionPane.ERROR_MESSAGE);
                isDying();
            } else {
                currentPlayer.mortgage(choice);
                JOptionPane.showMessageDialog(null, "Vous avez hypothéquer " + choice.getName(), "Hypothéquée", JOptionPane.INFORMATION_MESSAGE);
            }
            refreshAll();
            isDying();
        } else if (optionSelected == 4) {
            if (currentPlayer.getPropertiesOwned().isEmpty()) {
                JOptionPane.showMessageDialog(null, currentPlayer.getName() + ", vous n'avez pas de propriétés.", "Pas de propriétés", JOptionPane.ERROR_MESSAGE);
                isDying();
            } else {
                Player trader = players.get(JOptionPane.showOptionDialog(null, currentPlayer.getName() + ", avec quel joueur souhaitez-vous échanger ?", "Echanger", 0, JOptionPane.QUESTION_MESSAGE, null, names, null));
                if (trader.equals(currentPlayer))
                    isDying();
                if (trader.getPropertiesOwned().isEmpty()) {
                    JOptionPane.showMessageDialog(null, trader.getName() + " n'a pas de propriétés.", "Pas de propriétés", JOptionPane.ERROR_MESSAGE);
                    isDying();
                } else {
                    String[] propsOfTrader = new String[trader.getPropertiesOwned().size()];
                    for (int i = 0 ; i < propsOfTrader.length ; i++)
                        propsOfTrader[i] = trader.getPropertiesOwned().get(i).getName();
                    Property give = currentPlayer.getPropertiesOwned().get(JOptionPane.showOptionDialog(null, currentPlayer.getName() + ", quelle propriété voulez-vous échanger ?", "Echanger", 0, JOptionPane.QUESTION_MESSAGE, null, props, null));
                    Property take = trader.getPropertiesOwned().get(JOptionPane.showOptionDialog(null, trader.getName() + ", quelle propriété voulez-vous échanger ?", "Echanger", 0, JOptionPane.QUESTION_MESSAGE, null, propsOfTrader, null));
                    int cashGive = Integer.parseInt(JOptionPane.showInputDialog(null, currentPlayer.getName() + ", combien voulez-vous donner ?", "Echanger", JOptionPane.QUESTION_MESSAGE));
                    int cashTake = Integer.parseInt(JOptionPane.showInputDialog(null, trader.getName() + ", combien voulez-vous donner ?", "Echanger", JOptionPane.QUESTION_MESSAGE));
                    if ((give instanceof Ground && !currentPlayer.isNoHousesOnGrounds(give.getColor()) || (take instanceof Ground && !trader.isNoHousesOnGrounds(take.getColor())) || !currentPlayer.canPay(cashGive) || !trader.canPay(cashTake))) {
                        JOptionPane.showMessageDialog(null, "Un échange n'est valable si et seulement si :\n" +
                                "- Les propriétés échangés ou leurs voisines ne possèdent pas de maisons (terrains)\n" +
                                "- Les joueurs participant à l'échange possèdent la somme d'argent suffisante pour échanger", "Echange non-conforme", JOptionPane.ERROR_MESSAGE);
                        isDying();
                    } else {
                        String[] yesNoOptions = {"Oui", "Non"};
                        int yesNo = JOptionPane.showOptionDialog(null, "Je soussigné : " + currentPlayer.getName() +
                                ", accepte de donner la propriété " + give.getName() + " et " + cashGive + " € à " + trader.getName() +
                                "contre la propriété " + take.getName() + " et " + cashTake + " € et inversement.", "Contrat", 0, JOptionPane.QUESTION_MESSAGE, null, yesNoOptions, null);
                        if (yesNo == 1)
                            JOptionPane.showMessageDialog(null, "Contrat refusé", "Contrat refusé", JOptionPane.ERROR_MESSAGE);
                        else {
                            JOptionPane.showMessageDialog(null, "Contrat accepté", "Contrat accepté", JOptionPane.INFORMATION_MESSAGE);
                            currentPlayer.trade(trader, give, take, cashGive, cashTake);
                            refreshAll();
                        }
                    }
                }
            }
            isDying();
        }
    }

    public void isInJail() {
        if (currentPlayer.getJailTime() > 0) {
            String[] jailOptions = {"Faire un double", "Utiliser une carte de sortie", "Rester en prison"};
            int exitChoice = JOptionPane.showOptionDialog(null, currentPlayer.getName() + ", vous êtes en prison que voulez-vous faire ?", "Détention", 0, JOptionPane.QUESTION_MESSAGE, null, jailOptions, null);
            if (exitChoice == 0) {
                currentPlayer.roll();
                if (currentPlayer.getDice1() == currentPlayer.getDice2()) {
                    JOptionPane.showMessageDialog(null, currentPlayer.getName() + ", vous avez fait un double. Sortez de prison.", "Double", JOptionPane.INFORMATION_MESSAGE);
                    JOptionPane.showMessageDialog(null, currentPlayer.getName() + ", vous avez fait un " + currentPlayer.getRoll() + ". Avancez.", "Avancez", JOptionPane.INFORMATION_MESSAGE);
                    currentPlayer.setJailTime(0);
                    currentPlayer.move();
                    center.repaint();
                } else {
                    JOptionPane.showMessageDialog(null, "Vous n'avez pas fait un double. Restez en prison.", "Détention", JOptionPane.INFORMATION_MESSAGE);
                    currentPlayer.setJailTime(currentPlayer.getJailTime() - 1);
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
                        JOptionPane.showMessageDialog(null, "Restez en prison.", "Détention", JOptionPane.INFORMATION_MESSAGE);
                        currentPlayer.setJailTime(currentPlayer.getJailTime() - 1);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Vous n'avez pas de carte de permission de sortie. Restez en prison.", "Détention", JOptionPane.INFORMATION_MESSAGE);
                    currentPlayer.setJailTime(currentPlayer.getJailTime() - 1);
                }
            } else if (exitChoice == 2) {
                JOptionPane.showMessageDialog(null, "Restez en prison.", "Détention", JOptionPane.INFORMATION_MESSAGE);
                currentPlayer.setJailTime(currentPlayer.getJailTime() - 1);
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
        String[] phase1Options = {
                "Lancer les dés",
                "Voir les propriétés d'un joueur",
                "Acheter une maison",
                "Echanger",
                "Vendre une propriété",
                "Vendre une maison",
                "Hypothéquer",
                "Déshypothéquer",
                "Quitter"
                };
        String[] props = new String[currentPlayer.getPropertiesOwned().size()];
        for (int i = 0 ; i < props.length ; i++)
            props[i] = currentPlayer.getPropertiesOwned().get(i).getName();
        ArrayList<Ground> groundsOwned = new ArrayList<Ground>();
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
            Player player = players.get(JOptionPane.showOptionDialog(null, "Selectionnez un joueur :", "Voir les propriétés", 0, JOptionPane.QUESTION_MESSAGE, null, names, null));
            if (player.getPropertiesOwned().isEmpty()) {
                JOptionPane.showMessageDialog(null, player.getName() + " n'a pas de propriétés.", "Pas de propriétés", JOptionPane.INFORMATION_MESSAGE);
                isTurnPhase1();
            } else {
                String[] playerProps = new String[player.getPropertiesOwned().size()];
                for (int i = 0 ; i < playerProps.length ; i++)
                    playerProps[i] = player.getPropertiesOwned().get(i).getName();
                Property viewed = player.getPropertiesOwned().get(JOptionPane.showOptionDialog(null, "Quelle propriété voulez-vous voir ?", "Voir une propriété de " + player.getName(), 0, JOptionPane.QUESTION_MESSAGE, null, playerProps, null));
                String[] okOption = {"Ok"};
                String details = "Nom: " + viewed.getName() +
                        "\nLoyer: " + viewed.getRent() + " €" +
                        "\nNombre de maisons: " + viewed.getNbHouse() +
                        "\nHypothéquée: " + viewed.isMortgage();
                JOptionPane.showOptionDialog(null, details, viewed.getName(), 0, JOptionPane.QUESTION_MESSAGE, null, okOption, null);
                isTurnPhase1();
            }
        } else if (phase1 == 2) {
            if (currentPlayer.getPropertiesOwned().isEmpty()) {
                JOptionPane.showMessageDialog(null, currentPlayer.getName() + ", vous n'avez pas de propriétés.", "Pas de propriétés", JOptionPane.ERROR_MESSAGE);
                isTurnPhase1();
            } else {
                Ground choice = groundsOwned.get(JOptionPane.showOptionDialog(null, "Sur quelle terrains voulez-vous construire une maison ?", "Construire une maison", 0, JOptionPane.QUESTION_MESSAGE, null, grounds, null));
                if (currentPlayer.hasMonopoly(choice.getColor())) {
                    currentPlayer.buyHouse(choice);
                    JOptionPane.showMessageDialog(null, currentPlayer.getName() + " a construit une maison sur " + choice.getName(), "Construction", JOptionPane.INFORMATION_MESSAGE);
                } else
                    JOptionPane.showMessageDialog(null, "Vous n'avez pas le monopole.", "Ooops", JOptionPane.INFORMATION_MESSAGE);
                refreshAll();
                isTurnPhase1();
            }
        } else if (phase1 == 3) {
            if (currentPlayer.getPropertiesOwned().isEmpty()) {
                JOptionPane.showMessageDialog(null, currentPlayer.getName() + ", vous n'avez pas de propriétés.", "Pas de propriétés", JOptionPane.ERROR_MESSAGE);
                isTurnPhase1();
            } else {
                Player trader = players.get(JOptionPane.showOptionDialog(null, currentPlayer.getName() + ", avec quel joueur souhaitez-vous échanger ?", "Echanger", 0, JOptionPane.QUESTION_MESSAGE, null, names, null));
                if (trader.equals(currentPlayer))
                    isDying();
                if (trader.getPropertiesOwned().isEmpty()) {
                    JOptionPane.showMessageDialog(null, trader.getName() + " n'a pas de propriétés.", "Pas de propriétés", JOptionPane.ERROR_MESSAGE);
                    isDying();
                } else {
                    String[] propsOfTrader = new String[trader.getPropertiesOwned().size()];
                    for (int i = 0 ; i < propsOfTrader.length ; i++)
                        propsOfTrader[i] = trader.getPropertiesOwned().get(i).getName();
                    Property give = currentPlayer.getPropertiesOwned().get(JOptionPane.showOptionDialog(null, currentPlayer.getName() + ", quelle propriété voulez-vous échanger ?", "Echanger", 0, JOptionPane.QUESTION_MESSAGE, null, props, null));
                    Property take = trader.getPropertiesOwned().get(JOptionPane.showOptionDialog(null, trader.getName() + ", quelle propriété voulez-vous échanger ?", "Echanger", 0, JOptionPane.QUESTION_MESSAGE, null, propsOfTrader, null));
                    int cashGive = Integer.parseInt(JOptionPane.showInputDialog(null, currentPlayer.getName() + ", combien voulez-vous donner ?", "Echanger", JOptionPane.QUESTION_MESSAGE));
                    int cashTake = Integer.parseInt(JOptionPane.showInputDialog(null, trader.getName() + ", combien voulez-vous donner ?", "Echanger", JOptionPane.QUESTION_MESSAGE));
                    if ((give instanceof Ground && !currentPlayer.isNoHousesOnGrounds(give.getColor()) || (take instanceof Ground && !trader.isNoHousesOnGrounds(take.getColor())) || !currentPlayer.canPay(cashGive) || !trader.canPay(cashTake))) {
                        JOptionPane.showMessageDialog(null, "Un échange n'est valable si et seulement si :\n" +
                                "- Les propriétés échangés ou leurs voisines ne possèdent pas de maisons (terrains)\n" +
                                "- Les joueurs participant à l'échange possèdent la somme d'argent suffisante pour échanger", "Echange non-conforme", JOptionPane.ERROR_MESSAGE);
                        isTurnPhase1();
                    } else {
                        String[] yesNoOptions = {"Oui", "Non"};
                        int yesNo = JOptionPane.showOptionDialog(null, "Je soussigné : " + currentPlayer.getName() +
                                ", accepte de donner la propriété " + give.getName() + " et " + cashGive + " € à " + trader.getName() +
                                "contre la propriété " + take.getName() + " et " + cashTake + " € et inversement.", "Contrat", 0, JOptionPane.QUESTION_MESSAGE, null, yesNoOptions, null);
                        if (yesNo == 1)
                            JOptionPane.showMessageDialog(null, "Contrat refusé", "Contrat refusé", JOptionPane.ERROR_MESSAGE);
                        else {
                            JOptionPane.showMessageDialog(null, "Contrat accepté", "Contrat accepté", JOptionPane.INFORMATION_MESSAGE);
                            currentPlayer.trade(trader, give, take, cashGive, cashTake);
                            refreshAll();
                        }
                        isTurnPhase1();
                    }
                }
            }
        } else if (phase1 == 4) {
            if (currentPlayer.getPropertiesOwned().isEmpty()) {
                JOptionPane.showMessageDialog(null, currentPlayer.getName() + ", vous n'avez pas de propriétés.", "Pas de propriétés", JOptionPane.ERROR_MESSAGE);
                isTurnPhase1();
            } else {
                Property choice = currentPlayer.getPropertiesOwned().get(JOptionPane.showOptionDialog(null, currentPlayer.getName() + ", quelle propriété voulez-vous vendre ?", "Vendre", 0, JOptionPane.QUESTION_MESSAGE, null, props, null));
                if (choice instanceof Ground && currentPlayer.hasMonopoly(choice.getColor()) && !currentPlayer.isNoHousesOnGrounds(choice.getColor())) {
                    JOptionPane.showMessageDialog(null, "Vous ne pouvez pas vendre " + choice.getName() + " car des maisons sont construites sur cette propriété ou sur les propriétés voisines.", "Vente impossible", JOptionPane.ERROR_MESSAGE);
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
                JOptionPane.showMessageDialog(null, currentPlayer.getName() + ", vous n'avez pas de propriétés.", "Pas de propriétés", JOptionPane.ERROR_MESSAGE);
                isTurnPhase1();
            } else {
                Ground choice = groundsOwned.get(JOptionPane.showOptionDialog(null, "Sur quelle terrains voulez-vous détruire une maison ?", "Détruire une maison", 0, JOptionPane.QUESTION_MESSAGE, null, grounds, null));
                currentPlayer.saleHouse(choice);
                JOptionPane.showMessageDialog(null, currentPlayer.getName() + " a détruit une maison sur " + choice.getName(), "Déstruction", JOptionPane.INFORMATION_MESSAGE);
                refreshAll();
                isTurnPhase1();
            }
        } else if (phase1 == 6) {
            if (currentPlayer.getPropertiesOwned().isEmpty()) {
                JOptionPane.showMessageDialog(null, currentPlayer.getName() + ", vous n'avez pas de propriétés.", "Pas de propriétés", JOptionPane.ERROR_MESSAGE);
                isTurnPhase1();
            } else {
                Property choice = currentPlayer.getPropertiesOwned().get(JOptionPane.showOptionDialog(null, currentPlayer.getName() + ", quelle propriété voulez-vous hypothéquer ?", "Hypothéquer", 0, JOptionPane.QUESTION_MESSAGE, null, props, null));
                if (choice instanceof Ground && currentPlayer.hasMonopoly(choice.getColor()) && !currentPlayer.isNoHousesOnGrounds(choice.getColor())) {
                    JOptionPane.showMessageDialog(null, "Vous ne pouvez pas hypothéquer " + choice.getName() + " car des maisons sont construites sur cette propriété ou sur les propriétés voisines.", "hypothéque impossible", JOptionPane.ERROR_MESSAGE);
                    isTurnPhase1();
                } else if (choice.isMortgage()) {
                    JOptionPane.showMessageDialog(null, choice.getName() + " est déjà hypothéquée", "Ooops", JOptionPane.ERROR_MESSAGE);
                    isTurnPhase1();
                } else {
                    currentPlayer.mortgage(choice);
                    JOptionPane.showMessageDialog(null, "Vous avez hypothéqué " + choice.getName(), "Hypothéqué", JOptionPane.INFORMATION_MESSAGE);
                }
            }
            refreshAll();
            isTurnPhase1();
        } else if (phase1 == 7) {
            if (currentPlayer.getPropertiesOwned().isEmpty()) {
                JOptionPane.showMessageDialog(null, currentPlayer.getName() + ", vous n'avez pas de propriétés.", "Pas de propriétés", JOptionPane.ERROR_MESSAGE);
                isTurnPhase1();
            } else {
                Property choice = currentPlayer.getPropertiesOwned().get(JOptionPane.showOptionDialog(null, currentPlayer.getName() + ", quelle propriété voulez-vous hypothéquer ?", "Hypothéquer", 0, JOptionPane.QUESTION_MESSAGE, null, props, null));
                if (!currentPlayer.canPay(choice.getPrice() / 2)) {
                    JOptionPane.showMessageDialog(null, "Vous n\' avez pas assez d'argent pour déshypothéquer.", "déshypothéque impossible", JOptionPane.ERROR_MESSAGE);
                    isTurnPhase1();
                } else if (!choice.isMortgage()) {
                    JOptionPane.showMessageDialog(null, choice.getName() + " est déjà déshypothéquée", "Ooops", JOptionPane.ERROR_MESSAGE);
                    isTurnPhase1();
                } else {
                    currentPlayer.unmortgage(choice);
                    JOptionPane.showMessageDialog(null, "Vous avez déshypothéqué " + choice.getName(), "Déshypothéqué", JOptionPane.INFORMATION_MESSAGE);
                }
            }
            refreshAll();
            isTurnPhase1();
        } else if (phase1 == 8)
            System.exit(0);
        if (currentPlayer.getDice1() == currentPlayer.getDice2()) {
            currentPlayer.setDoubleCount(currentPlayer.getDoubleCount() + 1);
            if (currentPlayer.getDoubleCount() == 3) {
                JOptionPane.showMessageDialog(null, currentPlayer.getName() + ", vous avez fait 3 doubles d'affilé,  allez en prison. Ne passez pas par la case Départ.", "Allez en prison", JOptionPane.INFORMATION_MESSAGE);
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
