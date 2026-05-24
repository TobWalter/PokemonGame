package pokemongame;

public class SetupItems {
    /**
     * Erstellt den Startbeutel des Spielers mit den ersten vordefinierten Items.
     * 
     * @return Ein fertig befülltes Beutel-Objekt für den Spieler
     */
    public static Beutel erstelleStartBeutel() {
        Item kleinerHeiltrank = new Item("Heiltrank", "Heilt ein Pokemon um 20 HP.", ItemTyp.HEILUNG, 1, 20.0);
                
        Item[] startInventar = { kleinerHeiltrank };
        return new Beutel(startInventar);
    }
}
