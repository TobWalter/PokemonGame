package pokemongame;

import java.util.Scanner;

/**
 * Uebernimmt ausschließlich die visuelle Darstellung und die Steuerung
 * des Beutel-Menues auf der Konsole.
 */
public class BeutelInteraktion {

    /**
     * Oeffnet das Beutel-Menue auf der Konsole und verwaltet die Artikelauswahl.
     * @param kampf   Das aktive KampfSystem — steuert Item-Anwendung und Gegner-Zug
     * @param scanner Der Scanner für die Benutzereingabe
     * @return true, wenn ein Item erfolgreich verbraucht wurde, sonst false bei Abbruch
     */
    public static boolean oeffneBeutelMenue(KampfSystem kampf, Scanner scanner) {
        Spieler spieler = kampf.getSpieler();
        Beutel beutel   = spieler.getBeutel();
        Pokemon ziel    = spieler.getAktivesPokemon();

        System.out.println("\n--- DEIN BEUTEL ---");
        Item[] inventar = beutel.getInventar();
        
        // Items dynamisch auflisten
        for (int i = 0; i < inventar.length; i++) {
            Item kit = inventar[i];
            System.out.printf("%d - %-15s (Anzahl: %dx) | %s%n", 
                              i + 1, kit.getName(), kit.getAnzahl(), kit.getBeschreibung());
        }
        
        // Dynamische Zurück-Option am Ende der Liste
        int zurueckOption = beutel.getAnzahlItemTypen() + 1;
        System.out.printf("%d - Zurueck zum Hauptmenue%n", zurueckOption);
        System.out.print("Waehle ein Item: ");

        int wahl = InputHelper.leseZahl(1, zurueckOption, scanner);
        if (wahl == zurueckOption) return false;

        Item gewaehltesItem = beutel.getItem(wahl - 1);
        return kampf.verarbeiteItem(gewaehltesItem, ziel); // Gegner-Zug wird korrekt ausgefuehrt
    }
}