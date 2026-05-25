package pokemongame;
/**
 * Implementierung eines Schadenseffekts, der bei einer Attacke angewendet wird.
 * Berechnet den Schaden basierend auf der Stärke der Attacke, den Typen des Angreifers und des Verteidigers sowie anderen Faktoren wie STAB und Trefferchance.
 */
public class SchadensEffekt implements Effekt {
    private double staerke;

    public SchadensEffekt(double staerke) {
        this.staerke = staerke;
    }

    @Override
    public void anwenden(Pokemon anwender, Pokemon ziel, PokemonTyp attackenTyp) {
        int schaden = KampfSystem.berechneSchaden(anwender, attackenTyp, this.staerke, ziel);
        ziel.erleideSchaden(schaden);
    }
}