package pokemongame;

/**
 * Repraesentiert eine schadensverursachende Attacke.
 * TODO: Kann optional langanhaltende Nebeneffekte auf das Ziel ausloesen. 
 */
public class SchadensAttacke extends Attacke {

    private double staerke;
    private String effekt;
    private double effektChance;

    /**
     * Erstellt eine neue Schadensattacke.
     * @param name         Der Name der Attacke
     * @param typ          Der Elementartyp der Attacke
     * @param genauigkeit  Die Trefferchance (0.0 bis 1.0)
     * @param staerke      Der Basis-Schadenswert der Attacke
     * @param effekt       Zusätzlicher Statuseffekt (z. B. "Zurueckschrecken"), sonst leerer String
     * @param effektChance Die Wahrscheinlichkeit (0.0 bis 1.0) für den Zusatzeffekt
     */
    public SchadensAttacke(String name, PokemonTyp typ, double genauigkeit, double staerke, String effekt, double effektChance) {
        super(name, typ, genauigkeit);
        this.staerke = staerke;
        this.effekt = effekt;
        this.effektChance = effektChance;
    }

    /**
     * Berechnet den Schaden unter Berücksichtigung von ATK, DEF und Typen-Multiplikatoren
     * und zieht die KP beim Ziel ab. Triggert ggf. Zusatzeffekte.
     * @param anwender Das angreifende Pokemon
     * @param ziel     Das verteidigende Pokemon
     */
    @Override
    public void anwenden(Pokemon anwender, Pokemon ziel) {
        System.out.printf("%s setzt %s ein!%n", anwender.getName(), this.getName());
        int effektiverSchaden = KampfSystem.berechneSchaden(anwender, this, ziel);
        
        ziel.erleideSchaden(effektiverSchaden);

        // Effektchance prüfen (Auskommentiert, bleibt als Struktur für später erhalten)
        /* if (!effekt.isEmpty() && Math.random() < effektChance) {
            anwender.verarbeiteNebeneffekt(ziel, effekt);
        } */
    }

    // Getter && Setter
    public double getStaerke() { return staerke; }
}