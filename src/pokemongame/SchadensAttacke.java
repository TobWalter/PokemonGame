package pokemongame;
public class SchadensAttacke extends Attacke{

    private double staerke;
    private String effekt;
    private double effektChance;

    public SchadensAttacke(String name, String typ, double genauigkeit, double staerke, String effekt, double effektChance) {
        super(name, typ, genauigkeit);
        this.staerke = staerke;
        this.effekt = effekt;
        this.effektChance = effektChance;
    }
    @Override
    public void anwenden(Pokemon anwender, Pokemon ziel) {

        System.out.printf("%s setzt %s ein!%n", anwender.getName(), this.getName());
        double typMult = KampfSystem.berechneTypMultiplikator(this.getTyp(), ziel.getTyp(), anwender.getTyp());
        double effektiverSchaden = Math.max(1, (anwender.getAtk() * this.staerke) / ziel.getDef() * typMult);
        ziel.erleideSchaden(effektiverSchaden);

        /* // Effektchance prüfen
        if (!effekt.isEmpty() && Math.random() < effektChance) {
            anwender.verarbeiteNebeneffekt(ziel, effekt);
        } */
    }
        
}
