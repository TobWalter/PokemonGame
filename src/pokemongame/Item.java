package pokemongame;

/**
 * Repraesentiert ein einzelnes Item im Spiel.
 */
public class Item {

    private String name;
    private String beschreibung;
    private String typ;          // "HEILUNG", "BALL", etc.
    private int anzahl;
    private double effektWert;   

    public Item(String name, String beschreibung, String typ, int startAnzahl, double effektWert) {
        this.name = name;
        this.beschreibung = beschreibung;
        this.typ = typ;
        this.anzahl = startAnzahl;
        this.effektWert = effektWert;
    }

    /**
     * Wendet das Item auf ein Ziel-Pokemon an.
     */
    public boolean benutzen(Pokemon ziel) {
        if (this.anzahl <= 0) {
            System.out.printf("Du hast keine %s mehr!%n", this.name);
            return false;
        }

        switch (this.typ) {
            case "HEILUNG":
                if (ziel.getHp() >= ziel.getMaxHp()) {
                    System.out.printf("%s hat bereits volle KP!%n", ziel.getName());
                    return false;
                }
                
                double heilung = Math.min(this.effektWert, ziel.getMaxHp() - ziel.getHp());
                ziel.heile(heilung);
                this.anzahl--; 
                System.out.printf("%s benutzt! +%.0f HP für %s -> %.0f/%d HP%n", 
                                  this.name, heilung, ziel.getName(), ziel.getHp(), ziel.getMaxHp());
                return true;

            case "BALL":
                System.out.println("Pokebaelle koennen aktuell noch nicht geworfen werden.");
                return false;

            default:
                System.out.println("Unbekannter Item-Typ!");
                return false;
        }
    }

    public String getName() { return name; }
    public String getBeschreibung() { return beschreibung; }
    public int getAnzahl() { return anzahl; }
}