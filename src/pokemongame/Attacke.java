package pokemongame;

/**
 * Basisklasse für alle Arten von Attacken (Schaden, Status, Hybrid).
 * Sie definiert die gemeinsamen Eigenschaften und Methoden, die von allen Attacken geteilt werden.
 */
public abstract class Attacke {

    private String name;
    private String typ;          
    private double genauigkeit;

    /**
     * Konstruktor für die Basisklasse Attacke.
     * Initialisiert die grundlegenden Eigenschaften einer Attacke, die für alle Typen gelten.
     * @param name        Der Name der Attacke (z. B. "Glut")
     * @param typ         Der Elementartyp der Attacke (z. B. "Feuer")
     * @param genauigkeit Die Genauigkeit der Attacke (Wert zwischen 0.0 und 1.0)
     */
    public Attacke(String name, String typ, double genauigkeit) {
        this.name = name;
        this.typ = typ;
        this.genauigkeit = genauigkeit;
    }

    public String getName() { return name; }
    public String getTyp() { return typ; }
    public double getGenauigkeit() { return genauigkeit; }

    /**
     * Fuehrt die spezifische Logik der Attacke aus.
     * Muss von Unterklassen individuell implementiert werden.
     * @param anwender Das Pokemon, welches die Attacke einsetzt
     * @param ziel     Das Ziel-Pokemon der Attacke
     */
    public abstract void anwenden(Pokemon anwender, Pokemon ziel);
}