package pokemongame;

import java.util.Random;

/**
 * Der Schiedsrichter des Spiels. Regelt Ablaeufe und Statuseffekte.
 */
public class KampfSystem {

    /**
     * Führt eine komplette Kampfrunde aus inklusive Initiative-Prüfung und Giftschaden.\r
     */
    public static void fuehreRundeAus(Pokemon spieler, Pokemon gegner, int atkIndex, int runde, Random random) {
        
        // 1. Initiative prüfen
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

        // 2. Rundenende abwickeln (Giftschaden)
        verarbeiteGiftschaden(spieler);
        verarbeiteGiftschaden(gegner);
    }

    /**
     * Rechnet den Elementar-Typenvorteil aus.
     * Beispiel: Glut (Feuer) gegen Bisasam (Pflanze)
     * -> Feuer schlägt Pflanze -> mult = 1.3
     * -> Angreifer ist Glumanda (Feuer) -> Typ == Attackentyp -> STAB greift -> 1.3 * 1.15? Nein, hier vereinfacht auf festen Wert 1.5 gesetzt.
     * * Beispiel 2: Kratzer (Normal) gegen Schiggy (Wasser)
     * -> kein Typvorteil -> mult = 1.0
     * -> Glumanda setzt Kratzer ein -> Angreifer-Typ != Attackentyp -> kein STAB -> mult bleibt 1.0
     * * Beispiel: Rankenhieb (Pflanze) gegen Glumanda (Feuer)
     * -> Pflanze ist schwach gegen Feuer -> mult = 0.8
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
        // STAB: Attackentyp == Typ des Angreifers UND bereits Typvorteil -> 1.5
        if (mult == 1.3 && attackTyp.equals(angrTyp)) {
            mult = 1.5;
        }
        return mult;
    }

    private static void verarbeiteGiftschaden(Pokemon p) {
        if (p.istVergiftet() && p.getHp() > 0) {
            double schaden = Math.max(1, Math.round(p.getMaxHp() * 0.0625)); // 1/16 der max HP
            System.out.printf("%n[STATUS] %s leidet unter dem Gift!%n", p.getName());
            p.schade(schaden);
        }
    }
}