package pokemongame;

/**
 * Repraesentiert eine Status-Attacke, die temporäre Statuswerte (ATK, DEF, INIT) modifiziert
 * oder langanhaltende primäre Statuseffekte (z.B. Gift, Paralyse) zufügt.
 */
public class StatusAttacke extends Attacke {

    private String beschreibung;
    private String effekt;
    private double effektChance;
    private double atkMod;
    private double defMod;
    private double initMod;
    private boolean targetIsSelf;

    /**
     * Grosser Konstruktor für komplexe Status-Attacken, die primär Werte modifizieren.
     * @param name         Name der Attacke
     * @param typ          Elementartyp der Attacke
     * @param genauigkeit  Genauigkeit der Attacke (0.0 bis 1.0)
     * @param beschreibung Beschreibung des Effekts für die Konsolenausgabe
     * @param effekt       Der anzuwendende Statuseffekt (z.B. "Stat")
     * @param effektChance Die Chance (0.0 bis 1.0), dass der Effekt eintritt
     * @param atkMod       Multiplikator für den Angriffswert (1.0 = keine Änderung, kleiner als 1.0 = Senkung, größer als 1.0 = Erhöhung)
     * @param defMod       Multiplikator für den Verteidigungswert (1.0 = keine Änderung, kleiner als 1.0 = Senkung, größer als 1.0 = Erhöhung)
     * @param initMod      Multiplikator für die Initiative (1.0 = keine Änderung, kleiner als 1.0 = Senkung, größer als 1.0 = Erhöhung)
     * @param targetIsSelf true, wenn der Anwender gestärkt wird; false, wenn das Ziel geschwächt wird
     */
    public StatusAttacke(String name, String typ, double genauigkeit, String beschreibung, String effekt, double effektChance, double atkMod, double defMod, double initMod, boolean targetIsSelf) {
        super(name, typ, genauigkeit);
        this.beschreibung = beschreibung;
        this.effekt = effekt;
        this.effektChance = effektChance;
        this.atkMod = atkMod;
        this.defMod = defMod;
        this.initMod = initMod;
        this.targetIsSelf = targetIsSelf;
    }

    /**
     * Kleiner Konstruktor für einfache Status-Attacken, die langanhaltende Zustände (z. B. Gift) zufügen.
     * Modifikatoren werden standardmäßig auf 1.0 gesetzt und targetIsSelf ist false.
     * @param name         Name der Attacke
     * @param typ          Elementartyp der Attacke
     * @param beschreibung Beschreibung des Effekts für die Konsolenausgabe
     * @param effekt       Der langanhaltende Zustand (z. B. "Gift", "Paralyse")
     * @param genauigkeit  Genauigkeit der Attacke (0.0 bis 1.0)
     * @param effektChance Die Chance (0.0 bis 1.0), dass der Zustand übertragen wird
     */
    public StatusAttacke(String name, String typ, String beschreibung, String effekt, double genauigkeit, double effektChance) {
        super(name, typ, genauigkeit);
        this.beschreibung = beschreibung;
        this.effekt = effekt;
        this.effektChance = effektChance;
        this.atkMod = 1.0;  
        this.defMod = 1.0;  
        this.initMod = 1.0;  
        this.targetIsSelf = false;
    }

    /**
     * Wendet die Wertemodifikationen auf das korrekte Ziel an und wälzt 
     * bei Erfolg langanhaltende Statuseffekte auf das Ziel ab.
     * @param anwender Das Pokemon, das die Attacke einsetzt
     * @param ziel     Das gegnerische Ziel-Pokemon
     */
    @Override
    public void anwenden(Pokemon anwender, Pokemon ziel) {
        System.out.println(this.beschreibung); 

        Pokemon statZiel = this.targetIsSelf ? anwender : ziel;

        if (this.atkMod < 1.0) statZiel.aendereAtkStufe(-1);
        if (this.atkMod > 1.0) statZiel.aendereAtkStufe(1);
        if (this.defMod < 1.0) statZiel.aendereDefStufe(-1);
        if (this.defMod > 1.0) statZiel.aendereDefStufe(1);
        if (this.initMod < 1.0) statZiel.aendereInitStufe(-1);
        if (this.initMod > 1.0) statZiel.aendereInitStufe(1);
    }
}