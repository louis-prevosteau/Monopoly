package fr.monopoly.game.board;

import fr.monopoly.game.board.squares.*;

public class Board {

    private Space first, current;

    public Board() {
        first = null;
        generate();
    }

    public Space getFirst() {
        return first;
    }

    public void addSpace(Space space) {
        if (space instanceof Go) {
            first = space;
            current = first;
        } else {
            current.setNext(space);
            current = space;
            current.setNext(first);
        }
    }

    public void generate() {
        addSpace(new Go(1));
        addSpace(new Ground(2, "Boulevard de Belleville", 60, 2, 50, Color.PINK));
        addSpace(new Community(3));
        addSpace(new Ground(4, "Rue Lecourbe", 60, 4, 50, Color.PINK));
        addSpace(new Tax(5, "Impôt sur le revenu", 200));
        addSpace(new RailStation(6, "Gare Montparnasse", 200, 25));
        addSpace(new Ground(7, "Rue de Vaugirard", 100, 6, 50, Color.SKYBLUE));
        addSpace(new Chance(8));
        addSpace(new Ground(9, "Rue de Courcelles", 100, 6, 50, Color.SKYBLUE));
        addSpace(new Ground(10, "Avenue de la République", 120, 8, 50, Color.SKYBLUE));
        addSpace(new Jail(11));
        addSpace(new Ground(12, "Boulevard de la  Villette", 140, 10, 100, Color.PURPLE));
        addSpace(new Company(13, "Compagnie de Distribution d'Electricité", 150, 4));
        addSpace(new Ground(14, "Avenue de Neuilly", 140, 10, 100, Color.PURPLE));
        addSpace(new Ground(15, "Rue de Pradis", 160, 12, 100, Color.PURPLE));
        addSpace(new RailStation(16, "Gare de Lyon", 200, 25));
        addSpace(new Ground(17, "Avenue Mozart", 180, 14, 100, Color.ORANGE));
        addSpace(new Community(18));
        addSpace(new Ground(19, "Boulevard Saint-Michel", 180, 14, 100, Color.ORANGE));
        addSpace(new Ground(20, "Place Pigalle", 200, 16, 100, Color.ORANGE));
        addSpace(new FreeParking(21));
        addSpace(new Ground(22, "Avenue Matignon", 220, 18, 150, Color.RED));
        addSpace(new Chance(23));
        addSpace(new Ground(24, "Boulevard Malsherbes", 220, 18, 150, Color.RED));
        addSpace(new Ground(25, "Avenue Henri-Martin", 240, 20, 150, Color.RED));
        addSpace(new RailStation(26, "Gare du Nord", 200, 25));
        addSpace(new Ground(27, "Faubourg Saint-Honoré", 260, 22, 150, Color.YELLOW));
        addSpace(new Ground(28, "Place de la Bourse", 260, 22, 150, Color.YELLOW));
        addSpace(new Company(29, "Compagnie de Distribution des Eaux", 150, 4));
        addSpace(new Ground(30, "Rue Lafayette", 280, 24, 150, Color.YELLOW));
        addSpace(new GoToJail(31));
        addSpace(new Ground(32, "Avenue de Breteuil", 300, 26, 200, Color.GREEN));
        addSpace(new Ground(33, "Avenue Foch", 300, 26, 200, Color.GREEN));
        addSpace(new Community(34));
        addSpace(new Ground(35, "Boulevard des Capucines", 320, 28, 200, Color.GREEN));
        addSpace(new RailStation(36, "Gare Saint-Lazare", 200, 25));
        addSpace(new Chance(37));
        addSpace(new Ground(38, "Avenue des Champs-Elysées", 350, 35, 200, Color.BLUE));
        addSpace(new Tax(39, "Taxe de Luxe", 100));
        addSpace(new Ground(40, "Rue de la Paix", 400, 50, 200, Color.BLUE));
    }
}
