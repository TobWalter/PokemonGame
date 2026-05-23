package pokemongame;

public class StatusAttacke extends Attacke {

    private String beschreibung;
    private String effekt;
    private double effektChance;
    private double atkMod;
    private double defMod;
    private double initMod;
    private boolean targetIsSelf;

    /**
     * (großer) Konstruktor für komplexe Status-Attacken, die sowohl eine Beschreibung als auch spezifische Modifikatoren für ATK, DEF und Initiative haben.
     * Erstellt eine neue Status-Attacke mit den angegebenen Eigenschaften.
     * @param name // Name der Attacke
     * @param typ // Elementartyp der Attacke
     * @param genauigkeit // Genauigkeit der Attacke (0.0 bis 1.0)
     * @param beschreibung // Beschreibung des Effekts für die Anzeige
     * @param effekt // Der Effekt, der auf das Ziel angewendet wird (z.B. "Gift", "Verbrennung", "ATK-Down")
     * @param effektChance // Die Chance (0.0 bis 1.0), dass der Effekt tatsächlich eintritt
     * @param atkMod // Multiplikator für die ATK des Ziels (z.B. 0.5 für -50% ATK, 1.5 für +50% ATK)
     * @param defMod // Multiplikator für die DEF des Ziels (z.B. 0.5 für -50% DEF, 1.5 für +50% DEF)
     * @param initMod // Multiplikator für die Initiative des Ziels (z.B. 0.5 für -50% Init, 1.5 für +50% Init)
     * @param targetIsSelf // Gibt an, ob die Modifikatoren auf den Anwender selbst oder auf das Ziel angewendet werden sollen
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
     * (kleiner) Konstruktor für einfache Status-Attacken, die nur eine Beschreibung und einen Effekt haben.
      * Die Modifikatoren werden auf 1.0 (keine Änderung) gesetzt und targetIsSelf auf false.
     * @param name // Name der Attacke
     * @param typ // Elementartyp der Attacke
     * @param beschreibung // Beschreibung des Effekts für die Anzeige
     * @param effekt // Der Effekt, der auf das Ziel angewendet wird (z.B. "Gift", "Verbrennung", "ATK-Down")
     * @param genauigkeit // Genauigkeit der Attacke (0.0 bis 1.0)
     * @param effektChance // Die Chance (0.0 bis 1.0), dass der Effekt tatsächlich eintritt
     */
    public StatusAttacke(String name, String typ, String beschreibung, String effekt, double genauigkeit, double effektChance) {
        super(name, typ, genauigkeit);
        this.beschreibung = beschreibung;
        this.effekt = effekt;
        this.effektChance = effektChance;
        this.atkMod = 1.0;  // 1.0 bedeutet: Keine Änderung der ATK
        this.defMod = 1.0;  // 1.0 bedeutet: Keine Änderung der DEF
        this.initMod = 1.0;  // 1.0 bedeutet: Keine Änderung der Initiative
    }

    @Override
    public void anwenden(Pokemon anwender, Pokemon ziel) {
        System.out.println(this.beschreibung); // Beschreibung ausgeben (z.B. "senkt ATK des Gegners!")

        Pokemon statZiel = this.targetIsSelf ? anwender : ziel;

        
        statZiel.setAtk((int) Math.max(1, Math.round(statZiel.getAtk() * this.atkMod)));
        statZiel.setDef((int) Math.max(1, Math.round(statZiel.getDef() * this.defMod)));
        statZiel.setInit((int) Math.max(1, Math.round(statZiel.getInit() * this.initMod)));

        if (this.atkMod < 1.0) System.out.printf("%s ATK wurde gesenkt!%n", statZiel.getName());
        if (this.atkMod > 1.0) System.out.printf("%s ATK wurde erhoeht!%n", statZiel.getName());
        if (this.defMod < 1.0) System.out.printf("%s DEF wurde gesenkt!%n", statZiel.getName());
        if (this.defMod > 1.0) System.out.printf("%s DEF wurde erhoeht!%n", statZiel.getName());
        if (this.initMod < 1.0) System.out.printf("%s Initiative wurde gesenkt!%n", statZiel.getName());
        if (this.initMod > 1.0) System.out.printf("%s Initiative wurde erhoeht!%n", statZiel.getName());
        // Langanhaltende Effekte triggern (Gift, Paralyse), wenn ausgewürfelt
        if (!this.effekt.equals("") && !this.effekt.equals("Stat") && Math.random() < this.effektChance) {
            anwender.verarbeiteNebeneffekt(ziel, this.effekt);
        }
    }
}
