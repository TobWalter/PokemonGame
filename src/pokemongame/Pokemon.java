package pokemongame;

/**
 * Repraesentiert ein Pokemon mit seinen Statuswerten, Attacken und Zustandsveraenderungen.
 * Kapselt saemtliche Kampfdaten und die Logik fuer Schadensberechnungen.
 */
public class Pokemon {

    private String name;
    private String typ;
    private double hp;
    private int maxHp;
    private int atk;
    private int def;
    private int init;
    private Attacke[] attacken;

    private int atkStufe   = 0;
    private int defStufe   = 0;
    private int initStufe  = 0;

    private boolean istVergiftet    = false;
    private boolean istParalysiert  = false;

    /**
     * Erstellt ein neues Pokemon mit vollen Lebenspunkten.
     * @param name     Der Name des Pokemons
     * @param typ      Der Elementartyp (z. B. "Feuer")
     * @param maxHp    Die maximalen Lebenspunkte
     * @param atk      Der physische Angriffswert
     * @param def      Der physische Verteidigungswert
     * @param init     Die Initiative (Geschwindigkeit)
     * @param attacken Das Array der vier erlernbaren Attacken
     */
    public Pokemon(String name, String typ, int maxHp, int atk, int def, int init, Attacke[] attacken) {
        this.name     = name;
        this.typ      = typ;
        this.maxHp    = maxHp;
        this.hp       = maxHp;
        this.atk      = atk;
        this.def      = def;
        this.init     = init;
        this.attacken = attacken;
    }

    /**
     * 
     * @param delta Die Anzahl der Stufen, um die die ATK veraendert werden soll (positiv oder negativ)
     */
    public void aendereAtkStufe(int delta) {
        if (this.atkStufe == 6 && delta > 0) {
            System.out.printf("Die ATK von %s kann nicht weiter steigen!%n", this.name);
            return;
        }
        if (this.atkStufe == -6 && delta < 0) {
            System.out.printf("Die ATK von %s kann nicht weiter sinken!%n", this.name);
            return;
        }
        this.atkStufe = Math.max(-6, Math.min(6, this.atkStufe + delta));
        System.out.printf("%s ATK wurde %s!%n", this.name, delta > 0 ? "erhoeht" : "gesenkt");
    }
    /**
     * 
     * @param delta Die Anzahl der Stufen, um die die ATK veraendert werden soll (positiv oder negativ)
     */
    public void aendereDefStufe(int delta) {
        if (this.defStufe == 6 && delta > 0) {
            System.out.printf("Die DEF von %s kann nicht weiter steigen!%n", this.name);
            return;
        }
        if (this.defStufe == -6 && delta < 0) {
            System.out.printf("Die DEF von %s kann nicht weiter sinken!%n", this.name);
            return;
        }
        this.defStufe = Math.max(-6, Math.min(6, this.defStufe + delta));
        System.out.printf("%s DEF wurde %s!%n", this.name, delta > 0 ? "erhoeht" : "gesenkt");
    }

    /**
     * 
     * @param delta Die Anzahl der Stufen, um die die ATK veraendert werden soll (positiv oder negativ)
     */
    public void aendereInitStufe(int delta) {
        if (this.initStufe == 6 && delta > 0) {
            System.out.printf("Die Initiative von %s kann nicht weiter steigen!%n", this.name);
            return;
        }
        if (this.initStufe == -6 && delta < 0) {
            System.out.printf("Die Initiative von %s kann nicht weiter sinken!%n", this.name);
            return;
        }
        this.initStufe = Math.max(-6, Math.min(6, this.initStufe + delta));
        System.out.printf("%s Initiative wurde %s!%n", this.name, delta > 0 ? "erhoeht" : "gesenkt");
    }

    /**
     * Setzt die Statusstufen des Pokemons zurueck.
     */
    public void kampfReset() {
        this.atkStufe = 0;
        this.defStufe = 0;
        this.initStufe = 0;
        System.out.printf("[RESET] Die Statusstufen von %s wurden zurueckgesetzt.%n", this.name);
    }

    /**
     * Prueft vor einer Aktion, ob das Pokemon in dieser Runde handlungsfaehig ist.
     * Paralyse reduziert die Chance um 25%.
     * @return true, wenn das Pokemon angreifen kann, sonst false
     */
    public boolean kannAgieren() {
        if (this.istParalysiert) {
            if (Math.random() < 0.25) {
                System.out.printf("%s ist paralysiert und kann sich nicht bewegen!%n", this.name);
                return false;
            }
        }
        return true;
    }

    /**
     * Liefert die effektive Initiative des Pokemons.
     * Bei Paralyse wird die Initiative halbiert.
     * @return Die fuer die Zugreihenfolge relevante Initiative
     */
    public int getEffectiveInit() {
        int effInit = getInit();
        if (this.istParalysiert) {
            effInit = (effInit / 2);
        }
        return effInit;
    }

    /**
     * Fuehrt die gewaehlte Attacke gegen das Ziel-Pokemon aus.
     * Berechnet, ob die Attacke trifft, und wendet dann die spezifische Logik der Attacke an.
     * @param ziel     Das gegnerische Pokemon, das angegriffen wird
     * @param atkIndex Der Index der gewaehlten Attacke im Array (0 bis 3)
     */
    public void fuehreAktionAus(Pokemon ziel, int atkIndex) {
        Attacke a = this.attacken[atkIndex];
        System.out.printf("%n%s setzt %s ein!%n", this.name, a.getName());

        if (Math.random() > a.getGenauigkeit()) {
            System.out.println("Die Attacke ging daneben!");
            return;
        }

        a.anwenden(this, ziel);
    }

    /**
     * Zieht dem Pokemon die berechneten Schadenspunkte ab.
     * Die Lebenspunkte fallen dabei nie unter 0.
     * @param punkte Die Anzahl der abzuziehenden Lebenspunkte
     */
    public void erleideSchaden(double punkte) {
        this.hp = Math.max(0, this.hp - punkte);
        System.out.printf("%s verliert %.0f KP! -> %.0f/%d KP%n", this.name, punkte, this.hp, this.maxHp);
    }

    /**
     * Heilt das Pokemon um eine bestimmte Anzahl von Lebenspunkten.
     * Die Heilung wird bei den maximalen Lebenspunkten gedeckelt.
     * @param punkte Die Anzahl der zu heilenden Lebenspunkte
     */
    public void heile(double punkte) {
        this.hp = Math.min(this.maxHp, this.hp + punkte);
    }

    /**
     * Aktiviert langanhaltende Statuseffekte wie Gift oder Paralyse auf dem Ziel.
     * @param ziel   Das von der Zustandsveraenderung betroffene Pokemon
     * @param effekt Der Name des anzuwendenden Effekts
     */
    public void verarbeiteNebeneffekt(Pokemon ziel, String effekt) {
        switch (effekt) {
            case "Gift":
                if (!ziel.istVergiftet()) {
                    ziel.setVergiftet(true);
                    System.out.printf("%s wurde vergiftet!%n", ziel.name);
                } else {
                    System.out.printf("%s ist bereits vergiftet!%n", ziel.name);
                }
                break;
            case "Paralyse":
                if (!ziel.istParalysiert()) {
                    ziel.setParalysiert(true);
                    System.out.printf("%s wurde paralysiert!%n", ziel.name);
                } else {
                    System.out.printf("%s ist bereits paralysiert!%n", ziel.name);
                }
                break;
            case "Zurueckschrecken":
                System.out.printf("%s schreckt zurueck!%n", ziel.name);
                break;
        }
    }

    // Getter und Setter
    public String getName() { return name; }
    public String getTyp() { return typ; }
    public double getHp() { return hp; }
    public void setHp(double hp) { this.hp = hp; }
    public int getMaxHp() { return maxHp; }
    public int getAtk() { 
        return (int) Math.max(1, Math.round(this.atk * getStufenMultiplikator(this.atkStufe))); 
    }
    public void setAtk(int atk) { this.atk = atk; }
    public int getDef() { 
        return (int) Math.max(1, Math.round(this.def * getStufenMultiplikator(this.defStufe))); 
    }
    public void setDef(int def) { this.def = def; }
    public int getInit() { 
        return (int) Math.max(1, Math.round(this.init * getStufenMultiplikator(this.initStufe))); 
    }
    public void setInit(int init) { this.init = init; }
    private double getStufenMultiplikator(int stufe) {
        if (stufe >= 0) {
            return (2.0 + stufe) / 2.0;
        } else {
            return 2.0 / (2.0 - stufe);
        }
    }
    public Attacke[] getAttacken() { return attacken; }

    public boolean istVergiftet() { return istVergiftet; }
    public void setVergiftet(boolean status) { this.istVergiftet = status; }

    public boolean istParalysiert() { return istParalysiert; }
    public void setParalysiert(boolean status) { this.istParalysiert = status; }
}