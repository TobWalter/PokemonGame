package pokemongame;

/**
 * Repräsentiert ein Pokemon mit seinen Werten, Attacken und Statuseffekten.
 */
public class Pokemon {

    public String name;
    public String typ;
    public double hp;
    public int maxHp;
    public int atk;
    public int def;
    public int init;
    public Attacke[] attacken;

    public boolean istVergiftet = false;
    public boolean istParalysiert = false;

    /**
     * Konstruktor zum Erstellen eines neuen Pokemons mit vollen Lebenspunkten.
     */
    public Pokemon(String name, String typ, int maxHp, int atk, int def, int init, Attacke[] attacken) {
        this.name = name;
        this.typ = typ;
        this.maxHp = maxHp;
        this.hp = maxHp; // Pokemon startet mit voller HP
        this.atk = atk;
        this.def = def;
        this.init = init;
        this.attacken = attacken;
    }

    /**
     * Prüft vor der Aktion, ob das Pokemon handlungsfähig ist.
     * 
     * @return true, wenn das Pokemon kampfbereit ist, sonst false.
     */
    public boolean kannAgieren() {
        if (this.istParalysiert) {
            System.out.printf("%s ist paralysiert und kann sich vielleicht nicht bewegen!%n", this.name);
        }
        return this.hp > 0;
    }

    /**
     * Führt die ausgewählte Attacke auf das gegnerische Pokemon aus.
     */
    public void fuehreAktionAus(Pokemon gegner, int index) {
        Attacke gewaehlteAttacke = this.attacken[index];
        System.out.printf("%s setzt %s ein!%n", this.name, gewaehlteAttacke.name);
        
        // Hier folgt später deine detaillierte Schadensberechnung
        double schaden = Math.max(1, this.atk - gegner.def) * (gewaehlteAttacke.staerke / 3.0);
        gegner.hp -= Math.round(schaden);
        if (gegner.hp < 0) gegner.hp = 0;
        
        System.out.printf("Das hat %.0f Schaden angerichtet!%n", schaden);
    }
}