package pokemongame;

/**
 * Verwaltet exakt das Inventar des Spielers (Reines Daten-Objekt).
 * Diese Klasse ist entkoppelt von Konsolen-Ausgaben oder Eingabe-Scannern.
 */
public class Beutel {

    private Item[] inventar;

    /**
     * Erstellt ein neues Beutel-Objekt mit einem Startinventar.
     * * @param startInventar Das Array der initialen Gegenstaende
     */
    public Beutel(Item[] startInventar) {
        this.inventar = startInventar;
    }

    /**
     * Gibt das gesamte Inventar-Array zurueck, damit die UI es anzeigen kann.
     * * @return Das Array der im Beutel enthaltenen Items
     */
    public Item[] getInventar() {
        return this.inventar;
    }

    /**
     * Liefert die Anzahl der verschiedenen Item-Typen im Beutel.
     * * @return Die Laenge des Inventar-Arrays
     */
    public int getAnzahlItemTypen() {
        return inventar.length;
    }

    /**
     * Holt ein spezifisches Item ueber seinen Array-Index.
     * * @param index Der Verzeichnis-Index des gesuchten Gegenstands
     * @return Das Item-Objekt an der Stelle des Index, oder null bei ungueltigen Indizes
     */
    public Item getItem(int index) {
        if (index >= 0 && index < inventar.length) {
            return inventar[index];
        }
        return null;
    }
}