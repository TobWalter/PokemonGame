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
     * Berechnet den Schadensmultiplikator basierend auf dem Typ der Attacke 
     * und dem Typ des verteidigenden Pokémon (Klassisches Gen-1-Regelwerk).
     * @param attacke    Der Typ der eingesetzten Attacke
     * @param verteidiger Der Typ des gegnerischen Pokémon
     * @return Der Multiplikator (2.0 = sehr effektiv, 0.5 = nicht sehr effektiv, 0.0 = keine Wirkung, 1.0 = normal)
     */
    public static double typMultiplikator(PokemonTyp attacke, PokemonTyp verteidiger) {
        return switch (attacke) {
            case NORMAL -> switch (verteidiger) {
                case GESTEIN -> 0.5;
                case GEIST -> 0.0;
                default -> 1.0;
            };
            
            case FEUER -> switch (verteidiger) {
                case PFLANZE, EIS, KAEFER -> 2.0;
                case FEUER, WASSER, GESTEIN, DRACHE -> 0.5;
                default -> 1.0;
            };
            
            case WASSER -> switch (verteidiger) {
                case FEUER, BODEN, GESTEIN -> 2.0;
                case WASSER, PFLANZE, DRACHE -> 0.5;
                default -> 1.0;
            };
            
            case PFLANZE -> switch (verteidiger) {
                case WASSER, BODEN, GESTEIN -> 2.0;
                case FEUER, PFLANZE, GIFT, FLUG, KAEFER, DRACHE -> 0.5;
                default -> 1.0;
            };
            
            case ELEKTRO -> switch (verteidiger) {
                case WASSER, FLUG -> 2.0;
                case ELEKTRO, PFLANZE, DRACHE -> 0.5;
                case BODEN -> 0.0;
                default -> 1.0;
            };
            
            case EIS -> switch (verteidiger) {
                case PFLANZE, BODEN, FLUG, DRACHE -> 2.0;
                case FEUER, WASSER, EIS -> 0.5;
                default -> 1.0;
            };
            
            case KAMPF -> switch (verteidiger) {
                case NORMAL, EIS, GESTEIN -> 2.0;
                case GIFT, FLUG, PSYCHO, KAEFER -> 0.5;
                case GEIST -> 0.0;
                default -> 1.0;
            };
            
            case GIFT -> switch (verteidiger) {
                case PFLANZE, KAEFER -> 2.0;
                case GIFT, BODEN, GESTEIN, GEIST -> 0.5;
                default -> 1.0;
            };
            
            case BODEN -> switch (verteidiger) {
                case FEUER, ELEKTRO, GIFT, GESTEIN -> 2.0;
                case PFLANZE, KAEFER -> 0.5;
                case FLUG -> 0.0;
                default -> 1.0;
            };
            
            case FLUG -> switch (verteidiger) {
                case PFLANZE, KAMPF, KAEFER -> 2.0;
                case ELEKTRO, GESTEIN -> 0.5;
                default -> 1.0;
            };
            
            case PSYCHO -> switch (verteidiger) {
                case KAMPF, GIFT -> 2.0;
                case PSYCHO -> 0.5;
                default -> 1.0;
            };
            
            case KAEFER -> switch (verteidiger) {
                case PFLANZE, PSYCHO, GIFT -> 2.0;
                case FEUER, KAMPF, FLUG -> 0.5;
                default -> 1.0;
            };
            
            case GESTEIN -> switch (verteidiger) {
                case FEUER, EIS, FLUG, KAEFER -> 2.0;
                case KAMPF, BODEN -> 0.5;
                default -> 1.0;
            };
            
            case GEIST -> switch (verteidiger) {
                case GEIST -> 2.0;
                case NORMAL, PSYCHO -> 0.0;
                default -> 1.0;
            };
            
            case DRACHE -> switch (verteidiger) {
                case DRACHE -> 2.0;
                case FEUER, WASSER, ELEKTRO, PFLANZE -> 0.5;
                default -> 1.0;
            };
        };
    }

    /**
     * Berechnet den endgültigen Schaden einer Attacke.
     * @param angreifer  Das angreifende Pokémon
     * @param attacke    Die eingesetzte Attacke
     * @param verteidiger Das verteidigende Pokémon
     * @return Der berechnete Gesamtschaden als Ganzzahl
     */
    public static int berechneSchaden(Pokemon angreifer, PokemonTyp attackenTyp, double basisStaerke, Pokemon verteidiger) {
    // 1. Typ-Multiplikator berechnen
    double effektivitaet = typMultiplikator(attackenTyp, verteidiger.getTyp());

    // 2. STAB prüfen
    double stab = (angreifer.getTyp() == attackenTyp) ? 1.5 : 1.0;

    // 3. Schaden berechnen
    double schadenBasis = (angreifer.getAtk() * basisStaerke) / verteidiger.getDef();
    double endgueltigerSchaden = schadenBasis * effektivitaet * stab;

    return (int) Math.max(1, Math.round(endgueltigerSchaden));
}

    /**
     * Prueft, ob ein Pokemon vergiftet ist und zieht am Rundenende KP ab.
     * @param p Das zu pruefende Pokemon
     */
    private void verarbeiteGiftschaden(Pokemon p) {
        if (p.getAktiverStatus() == StatusEffekt.VERGIFTUNG && p.getHp() > 0) {
            double schaden = Math.max(1, Math.round(p.getMaxHp() * 0.1)); 
            System.out.printf("%n[PSN] %s leidet unter dem Gift!%n", p.getName());
            p.erleideSchaden(schaden);
        }
    }
}