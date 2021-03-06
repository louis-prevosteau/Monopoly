package fr.monopoly.ui;

import fr.monopoly.game.Player;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class TopUI extends JPanel {

    private ArrayList<String> holder;

    public TopUI(){
        holder = new ArrayList<>();
    }

    public void refreshList(Player player){
        this.removeAll();
        if(player.getPropertiesOwned().size() < 1 && holder.isEmpty()){
            String detail = "Pas de propriétés";
            holder.add(0, detail);
        }else{
            holder = new ArrayList<>();
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
        JList list = new JList(holder.toArray());
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
}
