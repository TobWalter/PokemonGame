package pokemongame;
/**
 * Repräsentiert die Entwicklung eines Pokemons, definiert durch eine Level-Schwelle und das Ziel-Pokemon.
 * Wird verwendet, um die Entwicklungspfade der Starter-Pokémon zu modellieren.
 */
public class Entwicklung {
    private int levelSchwelle;
    private Pokemon ziel; // Das neue Pokemon nach der Entwicklung

    public Entwicklung(int levelSchwelle, Pokemon ziel) {
        this.levelSchwelle = levelSchwelle;
        this.ziel = ziel;
    }

    public boolean istBereit(int level) {
        return level >= levelSchwelle;
    }

    public Pokemon getZiel() { return ziel; }
}