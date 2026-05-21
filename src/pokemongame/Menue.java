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

        while (meinPokemon.hp > 0 && gegnerPokemon.hp > 0 && !geflohen) {
            runde++;
            
            // Phase 1: Status auf dem Bildschirm aktualisieren
            zeigeRundenStatus(runde, meinPokemon, gegnerPokemon);

            // Phase 2: Spieler-Interaktion starten
            System.out.println("1 - Angreifen | 2 - Wechseln | 3 - Items | 4 - Flucht");
            int hauptmenue = InputHelper.leseZahl(1, 4, scanner);

            // Phase 3: Eingabe verarbeiten & Spielzustand verändern
            if (hauptmenue == 2) { 
                System.out.println("Kein weiteres Pokemon!"); 
                runde--; 
                continue; // <--- WICHTIG: Sofort zurück zum Schleifenkopf!
            } 
            else if (hauptmenue == 4) { 
                System.out.println("Du bist geflohen!"); 
                geflohen = true; 
                continue; // <--- WICHTIG: Beendet die Schleife im nächsten Durchlauf sauber
            } 
            else if (hauptmenue == 3) {
                // Verarbeitet das Item-Menü komplett intern
                boolean zugVerbraucht = verarbeiteItemMenue(meinBeutel, meinPokemon, gegnerPokemon, random, scanner);
                if (!zugVerbraucht) {
                    runde--; 
                    continue; // <--- WICHTIG: Verhindert Runden-Spam bei Abbruch
                }
            } 
            else if (hauptmenue == 1) {
                // Verarbeitet das Angriffs-Untermenü komplett intern
                boolean zugVerbraucht = verarbeiteAngriffMenue(meinPokemon, gegnerPokemon, runde, random, scanner);
                if (!zugVerbraucht) {
                    runde--; 
                    continue; // <--- WICHTIG: Verhindert Runden-Spam bei Abbruch
                }
            }
        }

        // ===== 4. SPIELENDE =====
        zeigeSpielEnde(trainerName, meinPokemon, runde, geflohen);
        scanner.close();
    }

    // =========================================================================
    // HILFSMETHODEN FÜR DIE UI & LOGIK-KAPSELUNG
    // =========================================================================

    private static void zeigePokemonAuswahl(Pokemon[] allePokemon) {
        System.out.println("Diese Pokemon stehen zur Auswahl!");
        for (int i = 0; i < allePokemon.length; i++) {
            System.out.printf("%d - %-10s (%s) %n", i + 1, allePokemon[i].name, allePokemon[i].typ);
        }
        System.out.println("Welches Pokemon wirst du waehlen?");
    }

    private static void zeigeKampfStart(String trainerName, Pokemon meinPokemon, Pokemon gegner) {
        System.out.printf("%s waehlt %s! %nHP: %.0f/%d %n", trainerName, meinPokemon.name, meinPokemon.hp, meinPokemon.maxHp);
        System.out.println("=".repeat(25));
        System.out.printf("Der Rivale schickt %s in den Kampf! %nHP: %.0f/%d %n", gegner.name, gegner.hp, gegner.maxHp);
    }

    private static void zeigeRundenStatus(int runde, Pokemon meinPokemon, Pokemon gegner) {
        System.out.println("\n" + "=".repeat(25));
        System.out.printf("RUNDE %d %n", runde);
        System.out.printf("%s:  %.0f/%d HP %s %s%n", meinPokemon.name,  meinPokemon.hp,  meinPokemon.maxHp,  meinPokemon.istVergiftet ? "[GFT]" : "", meinPokemon.istParalysiert ? "[PAR]" : "");
        System.out.printf("%s:  %.0f/%d HP %s %s%n", gegner.name, gegner.hp, gegner.maxHp, gegner.istVergiftet ? "[GFT]" : "", gegner.istParalysiert ? "[PAR]" : "");
        System.out.println("=".repeat(25));
    }

    private static boolean verarbeiteItemMenue(Beutel beutel, Pokemon meinPokemon, Pokemon gegner, Random random, Scanner sc) {
        boolean itemBenutzt = BeutelInteraktion.oeffneBeutelMenue(beutel, meinPokemon, sc);    
        if (itemBenutzt && gegner.hp > 0) {
            Rivale.fuehreZufallsAktionAus(gegner, meinPokemon, random);
            return true; // Zug erfolgreich beendet
        }
        return itemBenutzt; // false, wenn abgebrochen wurde
    }

    private static boolean verarbeiteAngriffMenue(Pokemon meinPokemon, Pokemon gegner, int runde, Random random, Scanner sc) {
        System.out.println("Waehle eine Attacke:");
        for (int i = 0; i < meinPokemon.attacken.length; i++) {
            System.out.printf("%d - %-15s (%s)%n", i + 1, meinPokemon.attacken[i].name, meinPokemon.attacken[i].typ);
        }
        System.out.println("5 - Zurueck");

        int attacke = InputHelper.leseZahl(1, 5, sc);
        if (attacke == 5) { 
            return false; // Abgebrochen, kein Zug verbraucht
        }

        // Kampf ausführen
        KampfSystem.fuehreRundeAus(meinPokemon, gegner, attacke - 1, runde, random);
        return true; // Zug erfolgreich beendet
    }

    private static void zeigeSpielEnde(String trainerName, Pokemon meinPokemon, int runde, boolean geflohen) {
        System.out.println("\n" + "=".repeat(25));
        if (geflohen) {
            System.out.println("Du bist geflohen!");
        } else if (meinPokemon.hp > 0) {
            System.out.printf("Sieg! %s hat mit %s nach %d Runden gewonnen! %n", trainerName, meinPokemon.name, runde);
        } else {
            System.out.printf("Du hast verloren! Der Rivale verpruegelt dich nach %d Runden. %n", runde);
        }
    }
}