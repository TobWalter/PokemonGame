package pokemongame;

/**
 * Repraesentiert eine Status-Attacke, die temporäre Statuswerte (ATK, DEF, INIT) modifiziert
 * oder langanhaltende primäre Statuseffekte (z.B. Gift, Paralyse) zufügt.
 */
public class StatusAttacke extends Attacke {

    private String beschreibung;
    private StatusEffekt effekt;
    private double effektChance;
    private double atkMod;
    private double defMod;
    private double initMod;
    private boolean targetIsSelf;

    /**
     * Erstellt eine Status-Attacke, die zusätzlich zu optionalen Statuseffekten 
     * auch Statuswerte des Ziels oder Anwenders modifizieren kann.
     * @param name          Name der Attacke
     * @param typ           Elementartyp
     * @param genauigkeit   Trefferchance der Attacke (0.0 bis 1.0)
     * @param beschreibung  Textausgabe beim Einsatz der Attacke
     * @param effekt        Der langanhaltende Statuseffekt
     * @param effektChance  Wahrscheinlichkeit des Effekts (0.0 bis 1.0)
     * @param atkMod        Multiplikator für den Angriffswert
     * @param defMod        Multiplikator für den Verteidigungswert
     * @param initMod       Multiplikator für den Initiativwert
     * @param targetIsSelf  True, wenn sich die Modifikatoren auf den Anwender beziehen
     */
    public StatusAttacke(String name, PokemonTyp typ, double genauigkeit, String beschreibung, StatusEffekt effekt, double effektChance, double atkMod, double defMod, double initMod, boolean targetIsSelf) {
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
     * Erstellt eine Status-Attacke, die keine direkten Wertemodifikationen vornimmt, 
     * sondern sich auf Statuseffekte fokussiert.
     * @param name          Name der Attacke
     * @param typ           Elementartyp
     * @param beschreibung  Textausgabe beim Einsatz der Attacke
     * @param effekt        Der langanhaltende Statuseffekt
     * @param genauigkeit   Trefferchance der Attacke (0.0 bis 1.0)
     * @param effektChance  Wahrscheinlichkeit des Effekts (0.0 bis 1.0)
     */
    public StatusAttacke(String name, PokemonTyp typ, String beschreibung, StatusEffekt effekt, double genauigkeit, double effektChance) {
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
     * Führt die Logik der Status-Attacke aus. Modifiziert bei Bedarf Werte und 
     * triggert bei erfolgreicher Wahrscheinlichkeitsprüfung einen Statuseffekt.
     * @param anwender Das ausführende Pokémon
     * @param ziel     Das Ziel-Pokémon der Attacke
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

        if (this.effekt != null && this.effekt != StatusEffekt.KEINER && this.effekt != StatusEffekt.WERTE_MODIFIKATION) {
            if (Math.random() <= this.effektChance) {
                anwender.verarbeiteNebeneffekt(ziel, this.effekt);
            }
        }
    }
}