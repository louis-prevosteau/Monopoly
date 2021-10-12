package fr.monopoly.game.board.squares;

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

    public void setName(String name) {
        this.name = name;
    }

    public Space getNext() {
        return next;
    }

    public void setNext(Space next) {
        this.next = next;
    }
}
