package pokemongame;

import java.util.Random;

/**
 * Der Schiedsrichter des Spiels. Regelt Ablaeufe und Statuseffekte.
 */
public class KampfSystem {

    private Pokemon spieler;
    private Pokemon gegner;
    private Random random;

    /**
     * Erstellt eine neue Instanz des Kampfsystems für ein konkretes Match.
     * @param spieler Das aktive Pokemon des Spielers
     * @param gegner  Das aktive Pokemon des computergesteuerten Rivalen
     * @param random  Der zentrale Zufallsgenerator für Attacken-Auswahlen und Treffer
     */
    public KampfSystem(Pokemon spieler, Pokemon gegner, Random random) {
        this.spieler = spieler;
        this.gegner = gegner;
        this.random = random;
    }

    /**
     * Führt eine komplette Kampfrunde aus inklusive Initiative-Prüfung und Giftschaden.
     * @param atkIndex Index der vom Spieler ausgewaehlten Attacke (0 bis 3)
     * @param runde    Die aktuelle Rundenzahl des laufenden Kampfes
     */
    public void fuehreRundeAus(int atkIndex, int runde) {
        boolean spielerZuerst = spieler.getEffectiveInit() > gegner.getEffectiveInit() ||
                        (spieler.getEffectiveInit() == gegner.getEffectiveInit() && runde % 2 != 0);

        if (spielerZuerst) {
            if (spieler.kannAgieren()) {
                spieler.fuehreAktionAus(gegner, atkIndex);
            }
            if (gegner.getHp() > 0) {
                Rivale.fuehreZufallsAktionAus(gegner, spieler, random);
            }
        } else {
            Rivale.fuehreZufallsAktionAus(gegner, spieler, random);
            if (spieler.getHp() > 0) {
                if (spieler.kannAgieren()) {
                    spieler.fuehreAktionAus(gegner, atkIndex);
                }
            }
        }

        verarbeiteGiftschaden(spieler);
        verarbeiteGiftschaden(gegner);
    }

    /**
     * Berechnet den Schadensmultiplikator basierend auf Elementartypen.
     * Berücksichtigt auch den STAB (Same-Type-Attack-Bonus).
        * @param attackTyp Der Typ der eingesetzten Attacke
     * @param defTyp    Der Typ des verteidigenden Pokemons
     * @param angrTyp   Der Typ des angreifenden Pokemons (fuer STAB-Pruefung)
     * @return Der berechnete Schadensmultiplikator (0.8, 1.0, 1.3 oder 1.5)
     */
    public static double berechneTypMultiplikator(String attackTyp, String defTyp, String angrTyp) {
        double mult;
        if (   (attackTyp.equals("Feuer")   && defTyp.equals("Pflanze"))
            || (attackTyp.equals("Pflanze") && defTyp.equals("Wasser"))
            || (attackTyp.equals("Wasser")  && defTyp.equals("Feuer"))) {
            mult = 1.3;
        } else if (
            (attackTyp.equals("Feuer")   && defTyp.equals("Wasser"))
            || (attackTyp.equals("Pflanze") && defTyp.equals("Feuer"))
            || (attackTyp.equals("Wasser")  && defTyp.equals("Pflanze"))) {
            mult = 0.8;
        } else {
            mult = 1.0;
        }
        
        if (mult == 1.3 && attackTyp.equals(angrTyp)) {
            mult = 1.5;
        }
        return mult;
    }

    /**
     * Prueft, ob ein Pokemon vergiftet ist und zieht am Rundenende KP ab.
     * @param p Das zu pruefende Pokemon
     */
    private void verarbeiteGiftschaden(Pokemon p) {
        if (p.istVergiftet() && p.getHp() > 0) {
            double schaden = Math.max(1, Math.round(p.getMaxHp() * 0.1)); // 10% der max HP als Giftschaden
            System.out.printf("%n[STATUS] %s leidet unter dem Gift!%n", p.getName());
            p.erleideSchaden(schaden);
        }
    }
}