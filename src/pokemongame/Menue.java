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

        // ===== 1. INITIALISIERUNG =====
        Pokemon[] allePokemon = SetupPokemon.erstelleStartOptionen();
        Beutel meinBeutel     = SetupItems.erstelleStartBeutel();

        // ===== 2. BEGRÜSSUNG & WAHL =====
        System.out.println("Wie heißt du?");
        String trainerName = scanner.nextLine();

        zeigePokemonAuswahl(allePokemon);
        int auswahl = InputHelper.leseZahl(1, 3, scanner);

        // Pokémon zuweisen
        Pokemon meinPokemon   = allePokemon[auswahl - 1];
        Pokemon gegnerPokemon = allePokemon[auswahl % 3];

        zeigeKampfStart(trainerName, meinPokemon, gegnerPokemon);

        // ===== 3. HAUPTSCHLEIFE (MAIN LOOP) =====
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
                    zugBeendet = verarbeiteAngriffMenue(meinPokemon, gegnerPokemon, runde, random, scanner);
                    if (!zugBeendet) {
                        aktion = 0; // Zurück zum Hauptmenü erzwingen
                    }
                } else if (aktion == 2) {
                    zugBeendet = verarbeiteBeutelMenue(meinBeutel, meinPokemon, gegnerPokemon, random, scanner);
                    if (!zugBeendet) {
                        aktion = 0; // Zurück zum Hauptmenü erzwingen
                    }
                } else if (aktion == 3) {
                    geflohen = true;
                    zugBeendet = true;
                } else {
                    // Falls von Untermenü zurückgekehrt, Hauptoptionen neu abfragen
                    System.out.println("\nWas willst du tun?");
                    System.out.println("1 - Kaempfen");
                    System.out.println("2 - Beutel");
                    System.out.println("3 - Fliehen");
                    aktion = InputHelper.leseZahl(1, 3, scanner);
                }
            }
        }

        // ===== 4. SPIELENDE =====
        zeigeSpielEnde(trainerName, meinPokemon, gegnerPokemon, runde, geflohen);
        scanner.close();
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
            // Gegner darf angreifen, da Item-Nutzung eine Runde kostet
            Rivale.fuehreZufallsAktionAus(gegner, meinPokemon, random);
            return true; 
        }
        return itemBenutzt; // false, wenn abgebrochen wurde
    }

    private static boolean verarbeiteAngriffMenue(Pokemon meinPokemon, Pokemon gegner, int runde, Random random, Scanner sc) {
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

        // Kampf ausführen
        KampfSystem.fuehreRundeAus(meinPokemon, gegner, attacke - 1, runde, random);
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