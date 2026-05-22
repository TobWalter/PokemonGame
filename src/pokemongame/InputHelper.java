package pokemongame;

import java.util.Scanner;

/**
 * Hilfsklasse für alle Konsolen-Eingaben des Spielers.
 * Sie schützt das Spiel vor Abstürzen durch Falscheingaben.
 */
public class InputHelper {

    /**
     * Liest eine Zahl von der Konsole ein und stellt sicher, dass sie innerhalb 
     * eines bestimmten Bereichs (von min bis max) liegt.
     * * @param min     Die untere Grenze des erlaubten Bereichs (inklusive)
     * @param max     Die obere Grenze des erlaubten Bereichs (inklusive)
     * @param scanner Der Scanner für die Erfassung der Konsoleneingabe
     * @return Die gültige, vom Benutzer eingegebene Ganzzahl
     */
    public static int leseZahl(int min, int max, Scanner scanner) {
        int auswahl = 0;
        boolean gueltig = false;

        while (!gueltig) {
            if (scanner.hasNextInt()) {
                auswahl = scanner.nextInt();
                scanner.nextLine(); // Puffer leeren

                if (auswahl >= min && auswahl <= max) {
                    gueltig = true;
                } else {
                    System.out.printf("Ungueltige Wahl! Bitte eine Zahl von %d bis %d eingeben: ", min, max);
                }
            } else {
                System.out.printf("Das war keine Zahl! Bitte eine Zahl von %d bis %d eingeben: ", min, max);
                scanner.nextLine(); // Ungültigen Text loeschen
            }
        }
        return auswahl;
    }
}