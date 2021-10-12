package fr.monopoly.ui;

import fr.monopoly.game.Player;
import fr.monopoly.game.board.squares.Property;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import java.awt.*;
import java.util.ArrayList;

public class TopUI extends JPanel {

    private ArrayList<String> holder;
    private JList list;
    private Player player;

    public TopUI(){
        holder = new ArrayList<String>();
    }

    public void refreshList(Player player){
        this.removeAll();
        if(player.getPropertiesOwned().size() < 1 && holder.isEmpty()){
            String detail = "Pas de propriétés";
            holder.add(0, detail);
        }else{
            holder = new ArrayList<String>();
            for(int i = 0; i<player.getPropertiesOwned().size(); i++){
                String prop = player.getPropertiesOwned().get(i).getName() + " - " + player.getPropertiesOwned().get(i).getColor();
                holder.add(i, prop);
            }
        }
        if(player.getPropertiesOwned().size() < 1 && holder.isEmpty()){
            String detail = "Pas de propriétés";
            holder.add(0, detail);
        }
        String name = player.getName();
        JLabel nameLabel = new JLabel(name);
        nameLabel.setFont(new Font("Serif", Font.BOLD, 24));
        JLabel moneyLabel = new JLabel(	player.getMoney() + " €");
        moneyLabel.setFont(new Font("Serif", Font.BOLD, 18));
        this.add(BorderLayout.NORTH, nameLabel);
        this.add(BorderLayout.NORTH, moneyLabel);
        list = new JList(holder.toArray());
        list.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        list.setName("Propriétés");
        list.setSelectedIndex (1);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.setLayout(new FlowLayout());
        this.setPreferredSize(new Dimension(800, 140));
        this.add(BorderLayout.CENTER, list);
        this.validate();
        this.repaint();
    }

    public void valueChanged(ListSelectionEvent e) {
        for(Property p : player.getPropertiesOwned()){
            if(p.getName() == list.getSelectedValue()){
                String details = "Nom: " + p.getName() +
                        "\nCouleur: " + p.getColor() +
                        "\nLoyer: " + p.getRent() + " €" +
                        "\nNombre de maisons: " + p.getNbHouse() +
                        "\nHypothéquée: " + p.isMortgage();
                JOptionPane.showMessageDialog(null, details, p.getName(), JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }
}
