package pokemongame;

import java.util.Random;
import java.util.Scanner;

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
        Pokemon[] allePokemon = SetupPokemon.erstelleStartOptionen();

        System.out.println("Wie heißt du?");
        String trainerName = scanner.nextLine();
        Spieler spieler = new Spieler(trainerName, SetupItems.erstelleStartBeutel());

        zeigePokemonAuswahl(allePokemon);
        int auswahl = InputHelper.leseZahl(1, 3, scanner);
        spieler.fuegePokemonHinzu(allePokemon[auswahl - 1]);

        Rivale rivale = new Rivale("Rivale");
        rivale.fuegePokemonHinzu(allePokemon[auswahl % 3]);

        zeigeKampfStart(spieler, rivale);

        // KampfSystem bekommt Spieler + Rivale — verwaltet alles intern
        KampfSystem kampf = new KampfSystem(spieler, rivale, random);
        starteKampfSchleife(kampf, scanner);
    }

    // =========================================================================
    // KAMPF-SCHLEIFE: nur Eingaben lesen und Ausgaben zeigen
    // =========================================================================

    private static void starteKampfSchleife(KampfSystem kampf, Scanner scanner) {
        while (kampf.laeuft()) {
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

        zeigeSpielEnde(kampf);
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
        System.out.println("\nDein Team:");
        Pokemon aktiv = spieler.getAktivesPokemon();
        System.out.printf("1 - %s (Aktiv | %.0f/%d KP)%n", aktiv.getName(), aktiv.getHp(), aktiv.getMaxHp());
        System.out.println("2 - Zurueck");

        int wahl = InputHelper.leseZahl(1, 2, scanner);
        if (wahl == 2) return false;

        System.out.println("Du hast aktuell keine weiteren Pokemon im Team!");
        return false;
        // Sobald das Team mehrere Pokemon hat:
        // kampf.verarbeitePokemonWechsel(gewaehltesPokemon);
        // return true;
    }

    /**
     * Oeffnet den Beutel und delegiert die Item-Nutzung an das KampfSystem.
     * @return true wenn ein Item erfolgreich genutzt wurde, false bei Abbruch
     */
    private static boolean verarbeiteBeutelMenue(KampfSystem kampf, Scanner scanner) {
        Spieler spieler = kampf.getSpieler();
        Beutel beutel   = spieler.getBeutel();
        Pokemon ziel    = spieler.getAktivesPokemon();

        System.out.println("\n--- DEIN BEUTEL ---");
        Item[] inventar = beutel.getInventar();
        for (int i = 0; i < inventar.length; i++) {
            Item kit = inventar[i];
            System.out.printf("%d - %-15s (Anzahl: %dx) | %s%n",
                    i + 1, kit.getName(), kit.getAnzahl(), kit.getBeschreibung());
        }
        int zurueckOption = beutel.getAnzahlItemTypen() + 1;
        System.out.printf("%d - Zurueck zum Hauptmenue%n", zurueckOption);
        System.out.print("Waehle ein Item: ");

        int wahl = InputHelper.leseZahl(1, zurueckOption, scanner);
        if (wahl == zurueckOption) return false;

        Item gewaehltesItem = beutel.getItem(wahl - 1);
        return kampf.verarbeiteItem(gewaehltesItem, ziel); // KampfSystem fuehrt Gegner-Zug danach aus
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

    private static void zeigeSpielEnde(KampfSystem kampf) {
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