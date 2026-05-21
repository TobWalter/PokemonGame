package pokemongame;

/**
 * Verwaltet exakt das Inventar des Spielers (Reines Daten-Objekt).
 * Weiß nichts von Konsolen-Ausgaben oder Scannern.
 */
public class Beutel {

    private Item[] inventar;

    public Beutel(Item[] startInventar) {
        this.inventar = startInventar;
    }

    // Gibt das gesamte Inventar-Array zurück, damit die UI es anzeigen kann
    public Item[] getInventar() {
        return this.inventar;
    }

    // Liefert die Anzahl der verschiedenen Item-Typen im Beutel
    public int getAnzahlItemTypen() {
        return inventar.length;
    }

    // Holt ein spezifisches Item über seinen Index
    public Item getItem(int index) {
        if (index >= 0 && index < inventar.length) {
            return inventar[index];
        }
        return null;
    }
}