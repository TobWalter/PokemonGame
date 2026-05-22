package pokemongame;

import java.util.Scanner;

/**
 * Uebernimmt ausschließlich die visuelle Darstellung und die Steuerung
 * des Beutel-Menues auf der Konsole.
 */
public class BeutelInteraktion {

    /**
     * Oeffnet das Beutel-Menue auf der Konsole und verwaltet die Artikelauswahl.
     * * @param beutel        Der Beutel, dessen Inhalt angezeigt werden soll
     * @param meinPokemon   Das aktive Pokemon, das das Item empfangen soll
     * @param scanner       Der Scanner für die Benutzereingabe
     * @return true, wenn ein Item erfolgreich verbraucht wurde, sonst false bei Abbruch
     */
    public static boolean oeffneBeutelMenue(Beutel beutel, Pokemon meinPokemon, Scanner scanner) {
        System.out.println("\n--- DEIN BEUTEL ---");
        
        Item[] inventar = beutel.getInventar();
        
        // Items auflisten
        for (int i = 0; i < inventar.length; i++) {
            Item kit = inventar[i];
            System.out.printf("%d - %-15s (Anzahl: %dx) | %s%n", 
                              i + 1, kit.getName(), kit.getAnzahl(), kit.getBeschreibung());
        }
        
        // Dynamische Zurück-Option berechnen
        int zurueckOption = beutel.getAnzahlItemTypen() + 1;
        System.out.printf("%d - Zurueck zum Hauptmenue%n", zurueckOption);
        System.out.print("Waehle ein Item: ");

        // Sichere Zahleneingabe über den Helfer steuern
        int wahl = InputHelper.leseZahl(1, zurueckOption, scanner);

        // Wenn "Zurück" gewaehlt wurde, abbrechen
        if (wahl == zurueckOption) {
            return false; 
        }

        // Item heraussuchen und benutzen
        Item gewaehltesItem = beutel.getItem(wahl - 1);
        return gewaehltesItem.benutzen(meinPokemon);
    }
}