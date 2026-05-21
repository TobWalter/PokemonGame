package pokemongame;

/**
 * Repräsentiert eine Attacke, die von einem Pokemon erlernt werden kann.
 */
public class Attacke {

    public String name;
    public String typ;          
    public double staerke;      
    public String beschreibung;  
    public String effekt;       
    public double effektChance; 
    public double genauigkeit;  

    /**
     * Konstruktor 1: Erstellt eine normale Schadens- oder Hybrid-Attacke.
     * 
     * @param name         Der Name der Attacke (z.B. "Glut")
     * @param typ          Der Elementartyp (z.B. "Feuer")
     * @param staerke      Der Basis-Schadenswert der Attacke
     * @param effekt       Zusätzlicher Statuseffekt (z.B. "Zurueckschrecken", "Gift") oder leer ""
     * @param effektChance Die Wahrscheinlichkeit für den Nebeneffekt als Dezimalzahl (0.0 bis 1.0)
     * @param genauigkeit  Die Trefferwahrscheinlichkeit der Attacke (0.0 bis 1.0)
     */
    public Attacke(String name, String typ, double staerke, String effekt, double effektChance, double genauigkeit) {
        this.name = name;
        this.typ = typ;
        this.staerke = staerke;
        this.effekt = effekt;
        this.effektChance = effektChance;
        this.genauigkeit = genauigkeit;
        this.beschreibung = "";
    }

    /**
     * Konstruktor 2: Erstellt eine reine Status-Attacke ohne Direktschaden.
     * 
     * @param name         Der Name der Attacke (z.B. "Heuler")
     * @param typ          Der Elementartyp (z.B. "Normal")
     * @param beschreibung Text, der beim Einsatz auf dem Bildschirm erscheint
     * @param effekt       Die Art der Statusveränderung (z.B. "Stat", "Paralyse")
     * @param effektChance Wie sicher der Effekt eintritt (1.0 für 100%)
     * @param atkMod       Multiplikator für den Angriff des Ziels (z.B. 0.75 für Senkung, 1.2 für Steigerung)
     * @param defMod       Multiplikator für die Verteidigung des Ziels (z.B. 0.75 für Senkung, 1.2 für Steigerung)
     * @param genauigkeit  Die Trefferwahrscheinlichkeit der Status-Attacke (0.0 bis 1.0)
     */
    public Attacke(String name, String typ, String beschreibung, String effekt, double effektChance, double atkMod, double defMod, double genauigkeit) {
        this.name = name;
        this.typ = typ;
        this.beschreibung = beschreibung;
        this.effekt = effekt;
        this.effektChance = effektChance;
        this.genauigkeit = genauigkeit;
        this.staerke = 0.0; 
    }
}