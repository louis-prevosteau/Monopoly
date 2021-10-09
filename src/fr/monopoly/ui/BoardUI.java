package fr.monopoly.ui;

import fr.monopoly.game.Player;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class BoardUI extends JPanel {

    private ArrayList<Player> players;

    public BoardUI(ArrayList<Player> players){
        this.players = players;
    }
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Image board = new ImageIcon("Board.jpg").getImage();
        g.drawImage(board,0,0,this.getWidth(), this. getHeight(),0,0,965,868, this);
        for(int i = 0; i<players.size(); i++){
            if(i == 0){
                g.setColor(Color.magenta);
            }else if(i == 1){
                g.setColor(Color.green);
            }else if(i == 2){
                g.setColor(Color.blue);
            }else if(i== 3){
                g.setColor(Color.red);
            }
            if(players.get(i).getCurrentSpace().getPosition() >= 1 && players.get(i).getCurrentSpace().getPosition()<= 10){
                g.fillOval((this.getWidth()/12)*(12-(players.get(i).getCurrentSpace().getPosition())) - 5, (this.getHeight()/11)*(10) + 3 + 6*i , this.getWidth()/40, this.getHeight()/40);
            } else if(players.get(i).getCurrentSpace().getPosition() >= 11 && players.get(i).getCurrentSpace().getPosition() <= 20){
                g.fillOval(((this.getWidth()/11)/4) - 5*i + 7, ((this.getHeight()/12)*(11-(players.get(i).getCurrentSpace().getPosition()%11)) - 3), this.getWidth()/40, this.getHeight()/40);
            } else if(players.get(i).getCurrentSpace().getPosition() >= 21 && players.get(i).getCurrentSpace().getPosition() <= 30){
                g.fillOval((this.getWidth()/12)*(players.get(i).getCurrentSpace().getPosition()%20), ((this.getHeight()/11) - 5) - 6*i+1, this.getWidth()/40, this.getHeight()/40);
            } else if(players.get(i).getCurrentSpace().getPosition() >= 31 && players.get(i).getCurrentSpace().getPosition() <= 40){
                g.fillOval(((((this.getWidth()/11))*(10)) - 3) + 6*i, (this.getHeight()/12)*(players.get(i).getCurrentSpace().getPosition()%30)-3,this.getWidth()/40, this.getHeight()/40);
            }
        }
    }
}
