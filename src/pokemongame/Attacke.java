package pokemongame;

/**
 * Repraesentiert eine Attacke, die von einem Pokemon erlernt werden kann.
 */
public class Attacke {

    public String name;
    public String typ;          
    public double staerke;      
    public String beschreibung;  
    public String effekt;       
    public double effektChance; 
    public double genauigkeit;
    public double atkMod;  
    public double defMod;
    public boolean targetIsSelf; // true wenn die Attacke auf das eigene Pokemon wirkt (z.B. Nebelschleier)

    /**
     * Konstruktor 1: Erstellt eine normale Schadens- oder Hybrid-Attacke.
     * 
     * @param name         Der Name der Attacke (z.B. "Glut")
     * @param typ          Der Elementartyp (z.B. "Feuer")
     * @param staerke      Der Basis-Schadenswert der Attacke
     * @param effekt       Zusaetzlicher Statuseffekt (z.B. "Zurueckschrecken", "Gift") oder leer ""
     * @param effektChance Die Wahrscheinlichkeit fuer den Nebeneffekt als Dezimalzahl (0.0 bis 1.0)
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
        this.atkMod = 1.0; // kein Effekt -> fuer spaetere Attacken, die evtl. ATK erhoehen/senken
        this.defMod = 1.0; // kein Effekt -> fuer spaetere Attacken, die evtl. DEF erhoehen/senken
        this.targetIsSelf = false;
    }

    /**
     * Konstruktor 2: Erstellt eine reine Status-Attacke ohne Direktschaden.
     * 
     * @param name         Der Name der Attacke (z.B. "Heuler")
     * @param typ          Der Elementartyp (z.B. "Normal")
     * @param beschreibung Text, der beim Einsatz auf dem Bildschirm erscheint
     * @param effekt       Die Art der Statusveraenderung (z.B. "Stat", "Paralyse")
     * @param effektChance Wie sicher der Effekt eintritt (1.0 fuer 100%)
     * @param atkMod       Multiplikator fuer den Angriff des Ziels (z.B. 0.75 fuer Senkung, 1.2 fuer Steigerung)
     * @param defMod       Multiplikator fuer die Verteidigung des Ziels (z.B. 0.75 fuer Senkung, 1.2 fuer Steigerung)
     * @param targetIsSelf Gibt an, ob die Attacke auf das eigene Pokemon wirkt (z.B. Nebelschleier) oder auf den Gegner (z.B. Heuler)
     * @param genauigkeit  Die Trefferwahrscheinlichkeit der Status-Attacke (0.0 bis 1.0)
     */
    public Attacke(String name, String typ, String beschreibung, String effekt, double effektChance, double atkMod, double defMod, boolean targetIsSelf, double genauigkeit) {
        this.name = name;
        this.typ = typ;
        this.beschreibung = beschreibung;
        this.effekt = effekt;
        this.effektChance = effektChance;
        this.genauigkeit = genauigkeit;
        this.staerke = 0.0;
        this.atkMod = atkMod; 
        this.defMod = defMod; 
        this.targetIsSelf = targetIsSelf;
    }
}