package pokemongame;

import java.util.Random;

/**
 * Steuert die einfache KI des gegnerischen Rivalen.
 * Waehlt automatisiert Aktionen aus, ohne dass eine Benutzereingabe erforderlich ist.
 */
public class Rivale {

    /**
     * Waehlt zufaellig eine der verfuegbaren Attacken des Angreifer-Pokemons 
     * aus und fuehrt sie gegen das Ziel aus, sofern das Pokemon handlungsfaehig ist.
     * @param angreifer Das vom Computer gesteuerte Pokemon des Rivalen
     * @param ziel      Das zu fokussierende Ziel-Pokemon des Spielers
     * @param random    Der Zufallsgenerator fuer die Bestimmung des Attacken-Indexes
     */
    public static void fuehreZufallsAktionAus(Pokemon angreifer, Pokemon ziel, Random random) {
        if (angreifer.kannAgieren()) {
            int zufallsIndex = random.nextInt(angreifer.getAttacken().length);
            angreifer.fuehreAktionAus(ziel, zufallsIndex);
        }
    }
}