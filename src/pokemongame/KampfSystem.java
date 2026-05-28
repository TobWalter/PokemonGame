package pokemongame;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Der Schiedsrichter des Spiels.
 * Besitzt den gesamten Kampfzustand (Runde, Flucht, aktive Pokemon)
 * und stellt dem Menue einfache Aktions-Methoden bereit.
 * Das Menue liest nur noch Eingaben und zeigt Ausgaben — es entscheidet nichts.
 */
public class KampfSystem {

    // Kampfzustand: hier verwaltet, nicht im Menue
    private Spieler spieler;
    private Rivale  rivale;
    private Random  random;
    private int     runde    = 0;
    private boolean geflohen = false;

    /**
     * Erstellt das KampfSystem für ein konkretes Match.
     * @param spieler Der menschliche Spieler mit seinem aktiven Pokemon
     * @param rivale  Der computergesteuerte Gegner
     * @param random  Zentraler Zufallsgenerator
     */
    public KampfSystem(Spieler spieler, Rivale rivale, Random random) {
        this.spieler = spieler;
        this.rivale  = rivale;
        this.random  = random;
    }

    // =========================================================================
    // ÖFFENTLICHE AKTIONS-METHODEN (Interface für das Menue)
    // =========================================================================

    /**
     * Spieler greift an: eine volle Runde wird ausgefuehrt.
     * @param spielerAtkIndex Die vom Spieler gewaehlte Attacke (0-3)
     */
    public void verarbeiteAngriff(int spielerAtkIndex) {
        runde++;
        int gegnerAtkIndex = rivale.waehleAttacke(random);
        fuehreRundeAus(spielerAtkIndex, gegnerAtkIndex);
    }

    /**
     * Spieler wechselt sein aktives Pokemon: Gegner erhaelt einen freien Zug.
     * Das KampfSystem aktualisiert seinen internen Verweis selbst.
     * @param neuesPokemon Das neu eingewechselte Pokemon des Spielers
     */
    public void verarbeitePokemonWechsel(Pokemon neuesPokemon) {
        runde++;
        System.out.printf("Zurueck, %s! Komm raus, %s!%n",
                spieler.getAktivesPokemon().getName(), neuesPokemon.getName());
        // Interner Verweis im Spieler-Objekt wird gesetzt (kapselt public-Feld-Zugriff)
        spieler.setAktivesPokemon(neuesPokemon);

        // Gegner bekommt seinen freien Zug nach dem Wechsel
        int gegnerAtkIndex = rivale.waehleAttacke(random);
        if (kannAgieren(rivale.getAktivesPokemon())) {
            fuehreAktionAus(rivale.getAktivesPokemon(), spieler.getAktivesPokemon(), gegnerAtkIndex);
        }
        verarbeiteGiftschaden(spieler.getAktivesPokemon());
        verarbeiteGiftschaden(rivale.getAktivesPokemon());
    }

    /**
     * Spieler nutzt ein Item: Item wird angewendet, Gegner erhaelt einen freien Zug.
     * @param item  Das genutzte Item
     * @param ziel  Das Ziel-Pokemon (aktives Pokemon des Spielers)
     * @return true wenn das Item erfolgreich angewendet wurde, false wenn es fehlschlug
     */
    public boolean verarbeiteItem(Item item, Pokemon ziel) {
        boolean erfolgreich = item.benutzen(ziel);
        if (!erfolgreich) return false;

        runde++;
        // Gegner bekommt seinen freien Zug nach der Item-Nutzung
        int gegnerAtkIndex = rivale.waehleAttacke(random);
        if (kannAgieren(rivale.getAktivesPokemon())) {
            fuehreAktionAus(rivale.getAktivesPokemon(), spieler.getAktivesPokemon(), gegnerAtkIndex);
        }
        verarbeiteGiftschaden(spieler.getAktivesPokemon());
        verarbeiteGiftschaden(rivale.getAktivesPokemon());
        return true;
    }

    /**
     * Verteilt Erfahrungspunkte an alle beteiligten Spieler-Pokémon, wenn der Rivale besiegt wurde.
     * Berechnet die EP anhand der Gen-1 Formel: (basisEP * gegnerLevel / 5) * Trainer-Bonus
     * Verteilt die EP gleichmäßig auf alle beteiligten Pokémon (mindestens 1 EP pro Pokemon).
     * Setzt die Beteiligungsmarkierung zurück, damit sie nur für einen Kampf gilt.
     */
    public void verteileErfahrung() {
        if (rivale.getAktivesPokemon().getHp() > 0) return;

        Pokemon besiegter = rivale.getAktivesPokemon();
        int basisEp = (int) (besiegter.getBasisErfahrung() * besiegter.getLevel() / 5.0 * 1.5);

        List<Pokemon> teilnehmer = spieler.getTeam().stream()
                .filter(Pokemon::hatGekaempft)
                .collect(Collectors.toList());

        if (teilnehmer.isEmpty()) return;

        int epProPokemon = Math.max(1, basisEp / teilnehmer.size());
        for (Pokemon p : teilnehmer) {
            Pokemon entwickeltZu = p.gebeErfahrung(epProPokemon); // nur einmal!
            if (entwickeltZu != null) {
                entwickeltZu.uebertrageZustand(p); // Zustand rüberkopieren
                spieler.ersetzeImTeam(p, entwickeltZu);
                if (spieler.getAktivesPokemon() == p) {
                    spieler.setAktivesPokemon(entwickeltZu);
                }
            }
            p.resetKampfBeteiligung();
        }
    }

    /**
     * Spieler versucht zu fliehen. Der Fluchtversuch zaehlt keine Runde.
     */
    public void versucheFlucht() {
        geflohen = true;
        System.out.println("Du bist aus dem Kampf geflohen!");
    }

    // =========================================================================
    // ZUSTANDSABFRAGEN (fuer die Schleifenbedingung im Menue)
    // =========================================================================

    /** @return true solange der Kampf noch laeuft */
    public boolean laeuft() {
        return !geflohen
            && spieler.getAktivesPokemon().getHp() > 0
            && rivale.getAktivesPokemon().getHp() > 0;
    }

    public boolean istGeflohen()    { return geflohen; }
    public int     getRunde()       { return runde; }
    public Spieler getSpieler()     { return spieler; }
    public Rivale  getRivale()      { return rivale; }

    // =========================================================================
    // INTERNE RUNDEN-LOGIK (privat — nur fuer dieses System)
    // =========================================================================

    private void fuehreRundeAus(int spielerAtkIndex, int gegnerAtkIndex) {
        boolean spielerZuerst =
            spieler.getAktivesPokemon().getEffectiveInit() > rivale.getAktivesPokemon().getEffectiveInit() ||
            (spieler.getAktivesPokemon().getEffectiveInit() == rivale.getAktivesPokemon().getEffectiveInit()
                && runde % 2 != 0);

        if (spielerZuerst) {
            if (kannAgieren(spieler.getAktivesPokemon()))
                fuehreAktionAus(spieler.getAktivesPokemon(), rivale.getAktivesPokemon(), spielerAtkIndex);
            if (rivale.getAktivesPokemon().getHp() > 0 && kannAgieren(rivale.getAktivesPokemon()))
                fuehreAktionAus(rivale.getAktivesPokemon(), spieler.getAktivesPokemon(), gegnerAtkIndex);
        } else {
            if (kannAgieren(rivale.getAktivesPokemon()))
                fuehreAktionAus(rivale.getAktivesPokemon(), spieler.getAktivesPokemon(), gegnerAtkIndex);
            if (spieler.getAktivesPokemon().getHp() > 0 && kannAgieren(spieler.getAktivesPokemon()))
                fuehreAktionAus(spieler.getAktivesPokemon(), rivale.getAktivesPokemon(), spielerAtkIndex);
        }
        // Beteiligte markieren (für EP-Berechtigung)
        spieler.getAktivesPokemon().markiereAlsBeteiligt();

        verarbeiteGiftschaden(spieler.getAktivesPokemon());
        verarbeiteGiftschaden(rivale.getAktivesPokemon());
    }

    private void fuehreAktionAus(Pokemon angreifer, Pokemon ziel, int atkIndex) {
        Attacke ausgewaehlteAttacke = angreifer.getAttacken()[atkIndex];
        if (Math.random() > ausgewaehlteAttacke.getGenauigkeit()) {
            System.out.println("Die Attacke von " + angreifer.getName() + " ging daneben!");
            return;
        } else {
        ausgewaehlteAttacke.anwenden(this, angreifer, ziel);
        }
    }

    // =========================================================================
    // STATUS-EFFEKT-VERARBEITUNG
    // =========================================================================

    public void fuegeStatusEffektHinzu(Pokemon ziel, StatusEffekt neuerStatus) {
        if (ziel.getAktiverStatus() == StatusEffekt.KEINER) {
            ziel.setAktiverStatus(neuerStatus);
            System.out.printf("%s leidet jetzt unter %s!%n", ziel.getName(), neuerStatus);
        }
    }

    public boolean kannAgieren(Pokemon p) {
        if (p.getAktiverStatus() == StatusEffekt.PARALYSE && Math.random() < 0.25) {
            System.out.printf("%s ist paralysiert und kann sich nicht bewegen!%n", p.getName());
            return false;
        }
        return true;
    }

    private void verarbeiteGiftschaden(Pokemon p) {
        if (p.getAktiverStatus() == StatusEffekt.VERGIFTUNG && p.getHp() > 0) {
            double schaden = Math.max(1, Math.round(p.getMaxHp() * 0.1));
            System.out.printf("%n[PSN] %s leidet unter dem Gift!%n", p.getName());
            p.erleideSchaden(schaden);
        }
    }

    // =========================================================================
    // SCHADENS-BERECHNUNG
    // =========================================================================

    public int berechneSchaden(Pokemon angreifer, PokemonTyp attackenTyp, double basisStaerke, Pokemon verteidiger) {
        double effektivitaet = typMultiplikator(attackenTyp, verteidiger.getTyp());
        double stab          = (angreifer.getTyp() == attackenTyp) ? 1.5 : 1.0;
        double schadenBasis  = (angreifer.getEffectiveAtk() * basisStaerke) / verteidiger.getEffectiveDef();
        return (int) Math.max(1, Math.round(schadenBasis * effektivitaet * stab));
    }

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
}