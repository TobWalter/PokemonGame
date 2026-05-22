package pokemongame;

import java.util.Random;
import java.util.Scanner;

/**
 * Die Hauptklasse des Spiels. 
 * Steuert den globalen Ablauf von der Charakterwahl bis zum Game Over.
 */
public class Menue {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Random  random  = new Random();

        // Startet den zentralen Ablauf der Anwendung
        fuehreSpielAus(scanner, random);

        scanner.close();
    }

    /**
     * Koordiniert den gesamten Spielablauf von der Initialisierung bis zum Ende.
     * * @param scanner Der Scanner für die Konsoleneingaben
     * @param random  Der zentrale Zufallsgenerator für die KI-Entscheidungen
     */
    private static void fuehreSpielAus(Scanner scanner, Random random) {
        // Initialisierung der Spieldaten
        Pokemon[] allePokemon = SetupPokemon.erstelleStartOptionen();
        Beutel meinBeutel     = SetupItems.erstelleStartBeutel();

        // Spieler-Begruessung und Charakterwahl
        System.out.println("Wie heißt du?");
        String trainerName = scanner.nextLine();

        zeigePokemonAuswahl(allePokemon);
        int auswahl = InputHelper.leseZahl(1, 3, scanner);

        // Zuweisung der beiden Kampf-Kontrahenten
        Pokemon meinPokemon   = allePokemon[auswahl - 1];
        Pokemon gegnerPokemon = allePokemon[auswahl % 3];

        zeigeKampfStart(trainerName, meinPokemon, gegnerPokemon);

        // Instanziierung des Kampfsystems und Uebergang zur Rundenverwaltung
        KampfSystem kampf = new KampfSystem(meinPokemon, gegnerPokemon, random);
        starteKampfSchleife(meinPokemon, gegnerPokemon, kampf, meinBeutel, trainerName, random, scanner);
    }

    /**
     * Verwaltet die rundenbasierte Hauptschleife des Kampfes bis zur Entscheidung.
     * * @param meinPokemon   Das gewaehlte Pokemon des Spielers
     * @param gegnerPokemon Das zugewiesene Pokemon des Gegners
     * @param kampf         Das aktive Kampfsystem-Objekt
     * @param meinBeutel    Das Inventar des Spielers
     * @param trainerName   Der eingegebene Name des Spielers
     * @param random        Der Zufallsgenerator für die Gegner-Zuege
     * @param scanner       Der Scanner fuer die Menue-Eingaben
     */
    private static void starteKampfSchleife(Pokemon meinPokemon, Pokemon gegnerPokemon, KampfSystem kampf, Beutel meinBeutel, String trainerName, Random random, Scanner scanner) {
        int     runde     = 0;
        boolean geflohen  = false;

        while (meinPokemon.getHp() > 0 && gegnerPokemon.getHp() > 0 && !geflohen) {
            runde++;
            zeigeRundenStart(runde, meinPokemon, gegnerPokemon);

            System.out.println("Was willst du tun?");
            System.out.println("1 - Kaempfen");
            System.out.println("2 - Beutel");
            System.out.println("3 - Fliehen");

            int aktion = InputHelper.leseZahl(1, 3, scanner);
            
            boolean zugBeendet = false;
            while (!zugBeendet) {
                if (aktion == 1) {
                    zugBeendet = verarbeiteAngriffMenue(meinPokemon, kampf, runde, scanner);
                    if (!zugBeendet) {
                        aktion = 0; // Abbruch erzwingt die Rueckkehr zur Hauptauswahl
                    }
                } else if (aktion == 2) {
                    zugBeendet = verarbeiteBeutelMenue(meinBeutel, meinPokemon, gegnerPokemon, random, scanner);
                    if (!zugBeendet) {
                        aktion = 0; // Abbruch erzwingt die Rueckkehr zur Hauptauswahl
                    }
                } else if (aktion == 3) {
                    geflohen = true;
                    zugBeendet = true;
                } else {
                    System.out.println("\nWas willst du tun?");
                    System.out.println("1 - Kaempfen");
                    System.out.println("2 - Beutel");
                    System.out.println("3 - Fliehen");
                    aktion = InputHelper.leseZahl(1, 3, scanner);
                }
            }
        }

        zeigeSpielEnde(trainerName, meinPokemon, gegnerPokemon, runde, geflohen);
    }

    // =========================================================================
    // HILFSMETHODEN FÜR KONSOLENAUSGABEN (VIEW-ANTEXTE)
    // =========================================================================

    private static void zeigePokemonAuswahl(Pokemon[] optionen) {
        System.out.println("\nWaehle dein Start-Pokemon:");
        for (int i = 0; i < optionen.length; i++) {
            System.out.printf("%d - %s (%s)%n", i + 1, optionen[i].getName(), optionen[i].getTyp());
        }
        System.out.print("Deine Wahl: ");
    }

    private static void zeigeKampfStart(String name, Pokemon spieler, Pokemon gegner) {
        System.out.println("\n" + "=".repeat(25));
        System.out.printf("Trainer %s fordert den Rivalen heraus!%n", name);
        System.out.printf("Du schickst %s in den Kampf!%n", spieler.getName());
        System.out.printf("Der Rivale waehlt %s!%n", gegner.getName());
        System.out.println("=".repeat(25));
    }

    private static void zeigeRundenStart(int runde, Pokemon spieler, Pokemon gegner) {
        System.out.printf("%n=== RUNDE %d ===%n", runde);
        System.out.printf("DEIN POKEMON: %s | %.0f/%d KP%n", spieler.getName(), spieler.getHp(), spieler.getMaxHp());
        System.out.printf("GEGNER:       %s | %.0f/%d KP%n", gegner.getName(), gegner.getHp(), gegner.getMaxHp());
        System.out.println("-".repeat(20));
    }

    private static boolean verarbeiteBeutelMenue(Beutel beutel, Pokemon meinPokemon, Pokemon gegner, Random random, Scanner sc) {
        boolean itemBenutzt = BeutelInteraktion.oeffneBeutelMenue(beutel, meinPokemon, sc);
        if (itemBenutzt) {
            // Die Benutzung eines Items beendet den eigenen Zug, die Gegner-KI agiert folglich
            Rivale.fuehreZufallsAktionAus(gegner, meinPokemon, random);
            return true; 
        }
        return itemBenutzt; 
    }

    private static boolean verarbeiteAngriffMenue(Pokemon meinPokemon, KampfSystem kampf, int runde, Scanner sc) {
        System.out.println("Waehle eine Attacke:");
        Attacke[] attacken = meinPokemon.getAttacken();
        for (int i = 0; i < attacken.length; i++) {
            System.out.printf("%d - %-15s (%s)%n", i + 1, attacken[i].getName(), attacken[i].getTyp());
        }
        System.out.println("5 - Zurueck");

        int attacke = InputHelper.leseZahl(1, 5, sc);
        if (attacke == 5) { 
            return false; 
        }

        // Leitet die gewaehlte Aktion an das Kampfsystem-Objekt weiter
        kampf.fuehreRundeAus(attacke - 1, runde);
        return true; 
    }

    private static void zeigeSpielEnde(String trainerName, Pokemon meinPokemon, Pokemon gegnerPokemon, int runde, boolean geflohen) {
        System.out.println("\n" + "=".repeat(25));
        if (geflohen) {
            System.out.println("Du bist geflohen!");
        } else if (meinPokemon.getHp() <= 0 && gegnerPokemon.getHp() <= 0) {
            System.out.printf("Unentschieden! Sowohl %s als auch %s sind kampfunfaehig!%n", meinPokemon.getName(), gegnerPokemon.getName());
        } else if (meinPokemon.getHp() > 0) {
            System.out.printf("Sieg! %s hat mit %s gewonnen!%n", trainerName, meinPokemon.getName());
        } else {
            System.out.println("Du hast verloren! Dein Pokemon wurde besiegt.");
        }
        System.out.printf("Der Kampf dauerte %d Runden.%n", runde);
        System.out.println("=".repeat(25));
    }
}