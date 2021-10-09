package fr.monopoly.ui;

import fr.monopoly.game.Player;
import fr.monopoly.game.board.squares.Property;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class SideUI extends JPanel {

    private ArrayList<String> holder;
    private JList list;

    public SideUI(){
        holder = new ArrayList<String>();
    }

    public void refreshList(Player player){
        this.removeAll();
        if(player.getPropertiesOwned().size() < 1 && holder.isEmpty()){
            String detail = "No property";
            holder.add(0, detail);
        }else{
            holder = new ArrayList<String>();
            for(Property p : player.getPropertiesOwned()){
                String prop = p.getName() + " - " + p.getColor();
                holder.add(prop);}
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
        list.setLayoutOrientation(JList.VERTICAL);
        list.setName("Propriétés");
        list.setSelectedIndex (1);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.setLayout(new FlowLayout());
        this.setPreferredSize(new Dimension(185, 800));
        this.add(BorderLayout.NORTH, list);
        this.validate();
        this.repaint();
    }
}
