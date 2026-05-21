package pokemongame;

import java.util.Random;

/**
 * Steuert die einfache KI des gegnerischen Rivalen.
 */
public class Rivale {

    /**
     * Wählt zufällig eine der 4 Attacken des Rivalen-Pokémons und führt sie aus.
     */
    public static void fuehreZufallsAktionAus(Pokemon angreifer, Pokemon ziel, Random random) {
        if (angreifer.kannAgieren()) {
            int zufallsIndex = random.nextInt(angreifer.attacken.length);
            angreifer.fuehreAktionAus(ziel, zufallsIndex);
        }
    }
}