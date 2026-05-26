package pokemongame;

import java.util.List;

/**
 * Basisklasse für alle Arten von Attacken (Schaden, Status, Hybrid).
 * Sie definiert die gemeinsamen Eigenschaften und Methoden, die von allen Attacken geteilt werden.
 */
public class Attacke {
    private String name;
    private PokemonTyp typ;          
    private double genauigkeit;
    private List<Effekt> effekte;

    /**
     * Konstruktor für die Basisklasse Attacke.
     * Initialisiert die grundlegenden Eigenschaften einer Attacke, die für alle Typen gelten.
     * @param name        Der Name der Attacke (z. B. "Glut")
     * @param typ         Der Elementartyp der Attacke (z. B. "Feuer")
     * @param genauigkeit Die Genauigkeit der Attacke (Wert zwischen 0.0 und 1.0)
     * @param effekte     Die Effekte, die bei Verwendung der Attacke angewendet werden (zb. Gift, ATK-Modifikation, etc.)
     */
    public Attacke(String name, PokemonTyp typ, double genauigkeit, Effekt... effekte) {
        this.name = name;
        this.typ = typ;
        this.genauigkeit = genauigkeit;
        this.effekte = List.of(effekte);
    }

    /**
     * Fuehrt die spezifische Logik der Attacke aus.
     * Muss von Unterklassen individuell implementiert werden.
     * @param anwender Das Pokemon, welches die Attacke einsetzt
     * @param ziel     Das Ziel-Pokemon der Attacke
     */
    public void anwenden(KampfSystem kampf, Pokemon anwender, Pokemon ziel) {
        System.out.printf("%s setzt %s ein!%n", anwender.getName(), this.name);
        
        for (Effekt effekt : effekte) {
            // Das KampfSystem wird an die Effekte durchgereicht
            effekt.anwenden(kampf, anwender, ziel, this.typ);
        }
    }

    // Getter für die Effekte, damit sie in der Kampf-Logik verarbeitet werden können
    public String getName() { return name; }
    public PokemonTyp getTyp() { return typ; }
    public double getGenauigkeit() { return genauigkeit; }
}