package pokemongame;

import java.util.Random;

/**
 * Ein Effekt, der einen Statuszustand mit einer bestimmten Wahrscheinlichkeit auf das Ziel-Pokemon anwenden kann (z.B. Verbrennen, Vergiften, etc.).
 */
public class StatusZustandsEffekt implements Effekt {
private StatusEffekt status;
    private double chance;
    private Random random = new Random();

    public StatusZustandsEffekt(StatusEffekt status, double chance) {
        this.status = status;
        this.chance = chance;
    }

@Override
    public void anwenden(KampfSystem kampf, Pokemon anwender, Pokemon ziel, PokemonTyp attackenTyp) {
        if (random.nextDouble() <= chance) {
            kampf.fuegeStatusEffektHinzu(ziel, this.status);
        }
    }

}