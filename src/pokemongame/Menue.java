package pokemongame;

import java.util.Random;
import java.util.Scanner;
import java.util.List;

/**
 * Die Hauptklasse des Spiels.
 * Zustaendig fuer: Menueauswahl einlesen, Zustand anzeigen, Ergebnisse ausgeben.
 * Keine Kampflogik — das KampfSystem entscheidet alles.
 */
public class Menue {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Random  random  = new Random();
        fuehreSpielAus(scanner, random);
        scanner.close();
    }

    private static void fuehreSpielAus(Scanner scanner, Random random) {
        Pokemon[] startPokemon = SetupPokemon.erstelleStartOptionen();
        Pokemon[] allePokemon  = SetupPokemon.erstelleAllePokemon();

        System.out.println("Wie heißt du?");
        String trainerName = scanner.nextLine();
        Spieler spieler = new Spieler(trainerName, SetupItems.erstelleStartBeutel());

        zeigePokemonAuswahl(startPokemon);
        int auswahl = InputHelper.leseZahl(1, 3, scanner);
        spieler.fuegePokemonHinzu(startPokemon[auswahl - 1]);
        spieler.fuegePokemonHinzu(allePokemon[random.nextInt(allePokemon.length)]); // Zufälliges zweites Pokemon für den Spieler

        Rivale rivale = new Rivale("Rivale");
        rivale.fuegePokemonHinzu(startPokemon[auswahl % 3]);

        zeigeKampfStart(spieler, rivale);
        KampfSystem rivalKampf = new KampfSystem(spieler, rivale, random);
        starteKampfSchleife(rivalKampf, scanner, true);

        // Außenwelt nur bei Sieg
        if (spieler.getAktivesPokemon().getHp() > 0) {
            starteAussenwelt(spieler, allePokemon, scanner, random);
        }
    }

    // =========================================================================
    // KAMPF-SCHLEIFE: nur Eingaben lesen und Ausgaben zeigen
    // =========================================================================

    private static void starteKampfSchleife(KampfSystem kampf, Scanner scanner, boolean trainerKampf) {
        while (kampf.laeuft()) {
            // Wenn aktives Pokemon K.O. ist, direkt zum Wechsel zwingen
            if (kampf.getSpieler().getAktivesPokemon().getHp() <= 0) {
            System.out.println("\nDein Pokemon ist kampfunfaehig! Waehle ein neues.");
            verarbeiteWechselZwang(kampf, scanner);
            continue;
        }
            Pokemon meinPokemon   = kampf.getSpieler().getAktivesPokemon();
            Pokemon gegnerPokemon = kampf.getRivale().getAktivesPokemon();

            zeigeRundenStart(kampf.getRunde() + 1, meinPokemon, gegnerPokemon);

            int aktion = leseHauptmenue(scanner);

            boolean zugBeendet = false;
            while (!zugBeendet) {
                switch (aktion) {
                    case 1 -> {
                        zugBeendet = verarbeiteAngriffMenue(kampf, scanner);
                        if (!zugBeendet) aktion = leseHauptmenue(scanner);
                    }
                    case 2 -> {
                        zugBeendet = verarbeiteWechselMenue(kampf, scanner);
                        if (!zugBeendet) aktion = leseHauptmenue(scanner);
                    }
                    case 3 -> {
                        zugBeendet = verarbeiteBeutelMenue(kampf, scanner);
                        if (!zugBeendet) aktion = leseHauptmenue(scanner);
                    }
                    case 4 -> {
                        kampf.versucheFlucht();
                        zugBeendet = true;
                    }
                    default -> aktion = leseHauptmenue(scanner);
                }
            }
        }

        zeigeSpielEnde(kampf, trainerKampf);
    }

    /**
     * Erzwingt einen Pokemon-Wechsel ohne Gegner-Zug — das Pokemon ist bereits K.O.
     * Schleift solange bis ein gueltiges Pokemon gewaehlt wurde.
     */
    private static void verarbeiteWechselZwang(KampfSystem kampf, Scanner scanner) {
        Spieler spieler = kampf.getSpieler();
        List<Pokemon> team = spieler.getTeam();

        while (true) {
            System.out.println("\nDein Team:");
            for (int i = 0; i < team.size(); i++) {
                Pokemon p = team.get(i);
                if (p.getHp() <= 0) {
                    System.out.printf("%d - %-12s (K.O.)%n", i + 1, p.getName());
                } else {
                    System.out.printf("%d - %-12s (%.0f/%d KP)%n",
                            i + 1, p.getName(), p.getHp(), p.getMaxHp());
                }
            }
            System.out.print("> ");
            int wahl = InputHelper.leseZahl(1, team.size(), scanner);
            Pokemon gewaehltes = team.get(wahl - 1);

            if (gewaehltes.getHp() <= 0) {
                System.out.println(gewaehltes.getName() + " ist kampfunfaehig!");
                continue;
            }

            // Kein Gegner-Zug — das Pokemon ist bereits gefallen
            spieler.setAktivesPokemon(gewaehltes);
            System.out.printf("Komm raus, %s!%n", gewaehltes.getName());
            kampf.aktualisiereSpielerpokemon(gewaehltes);
            return;
        }
    }

    // =========================================================================
    // MENUE-HANDLER: lesen + delegieren, nie selbst entscheiden
    // =========================================================================

    private static int leseHauptmenue(Scanner scanner) {
        System.out.println("\nWas wirst du tun?");
        System.out.printf("%-15s %-15s%n", "1 - Kampf", "2 - Pokemon");
        System.out.printf("%-15s %-15s%n", "3 - Items",  "4 - Flucht");
        System.out.print("> ");
        return InputHelper.leseZahl(1, 4, scanner);
    }

    /**
     * Zeigt die Attacken-Auswahl und delegiert den Angriff an das KampfSystem.
     * @return true wenn ein Angriff ausgefuehrt wurde, false bei "Zurueck"
     */
    private static boolean verarbeiteAngriffMenue(KampfSystem kampf, Scanner scanner) {
        Attacke[] attacken = kampf.getSpieler().getAktivesPokemon().getAttacken();
        System.out.println("Waehle eine Attacke:");
        for (int i = 0; i < attacken.length; i++) {
            System.out.printf("%d - %-15s (%s)%n", i + 1, attacken[i].getName(), attacken[i].getTyp());
        }
        System.out.println("5 - Zurueck");

        int wahl = InputHelper.leseZahl(1, 5, scanner);
        if (wahl == 5) return false;

        kampf.verarbeiteAngriff(wahl - 1); // KampfSystem fuehrt die komplette Runde aus
        return true;
    }

    /**
     * Zeigt das Team und delegiert einen Wechsel an das KampfSystem.
     * @return true wenn gewechselt wurde, false bei "Zurueck"
     */
    private static boolean verarbeiteWechselMenue(KampfSystem kampf, Scanner scanner) {
    Spieler spieler = kampf.getSpieler();
    List<Pokemon> team = spieler.getTeam();
    Pokemon aktiv = spieler.getAktivesPokemon();

    System.out.println("\nDein Team:");
    for (int i = 0; i < team.size(); i++) {
        Pokemon p = team.get(i);
        if (p == aktiv) {
            System.out.printf("%d - %-12s (Aktiv | %.0f/%d KP)%n",
                    i + 1, p.getName(), p.getHp(), p.getMaxHp());
        } else if (p.getHp() <= 0) {
            System.out.printf("%d - %-12s (K.O.)%n", i + 1, p.getName());
        } else {
            System.out.printf("%d - %-12s (%.0f/%d KP)%n",
                    i + 1, p.getName(), p.getHp(), p.getMaxHp());
        }
    }
    int zurueckOption = team.size() + 1;
    System.out.printf("%d - Zurueck%n", zurueckOption);

    int wahl = InputHelper.leseZahl(1, zurueckOption, scanner);
    if (wahl == zurueckOption) return false;

    Pokemon gewaehltes = team.get(wahl - 1);

    if (gewaehltes == aktiv) {
        System.out.println(gewaehltes.getName() + " kaempft bereits!");
        return false;
    }
    if (gewaehltes.getHp() <= 0) {
        System.out.println(gewaehltes.getName() + " ist kampfunfaehig!");
        return false;
    }

    kampf.verarbeitePokemonWechsel(gewaehltes);
    return true;
}

    /**
     * Oeffnet den Beutel und delegiert die Item-Nutzung an das KampfSystem.
     * @return true wenn ein Item erfolgreich genutzt wurde, false bei Abbruch
     */
    private static boolean verarbeiteBeutelMenue(KampfSystem kampf, Scanner scanner) {
    return BeutelInteraktion.oeffneBeutelMenue(kampf, scanner);
}

    // =========================================================================
    // AUSSENWELT
    // =========================================================================
    private static void starteAussenwelt(Spieler spieler, Pokemon[] allePokemon, 
                                      Scanner scanner, Random random) {
        System.out.println("\nDu hast den Rivalen besiegt! Die Welt steht dir offen.");

        boolean laeuft = true;
        while (laeuft) {
            zeigeAussenweltMenue();
            int wahl = InputHelper.leseZahl(1, 4, scanner);

            switch (wahl) {
                case 1 -> starteWildenKampf(spieler, allePokemon, scanner, random);
                case 2 -> besuchePokecenter(spieler);
                case 3 -> besuchePokemarkt();
                case 4 -> {
                    System.out.println("\nAuf Wiedersehen!");
                    laeuft = false;
                }
            }
        }
    }

    private static void zeigeAussenweltMenue() {
        System.out.println("\n" + "=".repeat(25));
        System.out.printf("%-22s %-22s%n", "1 - Wilder Kampf", "2 - Pokecenter");
        System.out.printf("%-22s %-22s%n", "3 - Pokemarkt",    "4 - Beenden");
        System.out.println("=".repeat(25));
        System.out.print("> ");
    }

    private static void starteWildenKampf(Spieler spieler, Pokemon[] allePokemon,
                                        Scanner scanner, Random random) {
        // Prüfen ob überhaupt ein Pokemon kampffähig ist
        boolean teamKampffaehig = spieler.getTeam().stream().anyMatch(p -> p.getHp() > 0);
        if (!teamKampffaehig) {
            System.out.println("\nAlle deine Pokemon sind kampfunfaehig! Besuche zuerst das Pokecenter.");
            return;
        }

        // Zufälliges wildes Pokemon aus allen verfügbaren wählen
        Pokemon vorlage = allePokemon[random.nextInt(allePokemon.length)];

        // Zufälliges Level +-2 um das Level der Vorlage
        int wildLevel = Math.max(1, vorlage.getLevel() + random.nextInt(5) - 2);

        // Wildes Pokemon als frischen Rivalen verpacken
        Rivale wildnis = new Rivale("Wildes " + vorlage.getName());
        wildnis.fuegePokemonHinzu(erstelleWildesPokemon(vorlage, wildLevel));

        System.out.printf("%nEin wildes %s (Lv. %d) tauchte auf!%n",
                vorlage.getName(), wildLevel);

        KampfSystem wildKampf = new KampfSystem(spieler, wildnis, random);
        starteKampfSchleife(wildKampf, scanner, false);
    }

    /**
     * Erstellt eine Kopie eines Pokemon-Templates mit angepasstem Level.
     * Stats werden proportional zum Level skaliert.
     */
    private static Pokemon erstelleWildesPokemon(Pokemon vorlage, int wildLevel) {
        double levelFaktor = Math.pow(1.07, wildLevel - vorlage.getLevel());
        int hp   = Math.max(1, (int) Math.round(vorlage.getMaxHp()   * levelFaktor));
        int atk  = Math.max(1, (int) Math.round(vorlage.getBaseAtk() * levelFaktor));
        int def  = Math.max(1, (int) Math.round(vorlage.getBaseDef() * levelFaktor));
        int init = Math.max(1, (int) Math.round(vorlage.getBaseInit()* levelFaktor));

        return new Pokemon(vorlage.getName(), vorlage.getTyp(),
                hp, atk, def, init,
                wildLevel, vorlage.getBasisErfahrung(),
                vorlage.getAttacken());
    }

    private static void besuchePokecenter(Spieler spieler) {
        System.out.println("\n" + "=".repeat(25));
        System.out.println("Willkommen im Pokecenter!");
        System.out.println("Wir heilen deine Pokemon wieder auf volle Kraft...");
        spieler.heileTeam();
        System.out.println("Deine Pokemon wurden vollstaendig geheilt!");
        System.out.println("=".repeat(25));
    }

    private static void besuchePokemarkt() {
        System.out.println("\n" + "=".repeat(25));
        System.out.println("Willkommen im Pokemarkt!");
        System.out.println("Wir haben leider noch keine Waren auf Lager. Schau spaeter nochmal vorbei!");
        System.out.println("=".repeat(25));
    }
    // =========================================================================
    // AUSGABE-HILFSMETHODEN
    // =========================================================================

    private static void zeigePokemonAuswahl(Pokemon[] optionen) {
        System.out.println("\nWaehle dein Start-Pokemon:");
        for (int i = 0; i < optionen.length; i++) {
            System.out.printf("%d - %s (%s)%n", i + 1, optionen[i].getName(), optionen[i].getTyp());
        }
        System.out.print("Deine Wahl: ");
    }

    private static void zeigeKampfStart(Spieler spieler, Rivale rivale) {
        System.out.println("\n" + "=".repeat(25));
        System.out.printf("Trainer %s fordert %s heraus!%n", spieler.getName(), rivale.getName());
        System.out.printf("Du schickst %s in den Kampf!%n", spieler.getAktivesPokemon().getName());
        System.out.printf("%s waehlt %s!%n", rivale.getName(), rivale.getAktivesPokemon().getName());
        System.out.println("=".repeat(25));
    }

    private static void zeigeRundenStart(int runde, Pokemon spieler, Pokemon gegner) {
        System.out.printf("%n=== RUNDE %d ===%n", runde);
        System.out.printf("DEIN POKEMON: %-15s | %.0f/%d KP%n", spieler.getName(), spieler.getHp(), spieler.getMaxHp());
        System.out.printf("GEGNER:       %-15s | %.0f/%d KP%n", gegner.getName(), gegner.getHp(), gegner.getMaxHp());
        System.out.println("-".repeat(20));
    }

    private static void zeigeSpielEnde(KampfSystem kampf, boolean trainerKampf) {
        kampf.verteileErfahrung(trainerKampf); // Erfahrungspunkte verteilen, wenn der Rivale besiegt wurde
        Pokemon meinPokemon   = kampf.getSpieler().getAktivesPokemon();
        Pokemon gegnerPokemon = kampf.getRivale().getAktivesPokemon();

        System.out.println("\n" + "=".repeat(25));
        if (kampf.istGeflohen()) {
            System.out.println("Du bist geflohen!");
        } else if (meinPokemon.getHp() <= 0 && gegnerPokemon.getHp() <= 0) {
            System.out.printf("Unentschieden! Sowohl %s als auch %s sind kampfunfaehig!%n",
                    meinPokemon.getName(), gegnerPokemon.getName());
        } else if (meinPokemon.getHp() > 0) {
            System.out.printf("Sieg! %s hat mit %s gewonnen!%n",
                    kampf.getSpieler().getName(), meinPokemon.getName());
        } else {
            System.out.println("Du hast verloren! Dein Pokemon wurde besiegt.");
        }
        System.out.printf("Der Kampf dauerte %d Runden.%n", kampf.getRunde());
        System.out.println("=".repeat(25));
    }
}