package pokemongame;

/**
 * Repraesentiert ein Pokemon mit seinen Statuswerten, Attacken und Zustandsveraenderungen.
 * Kapselt saemtliche Kampfdaten und die Logik fuer Schadensberechnungen.
 */
public class Pokemon {
    // Stats
    private String name;
    private PokemonTyp typ;
    private double hp;
    private int maxHp;
    private int baseAtk;
    private int baseDef;
    private int baseInit;
    // Leveling
    private int level;
    private int basisErfahrung;   // Wie viel EP dieses Pokemon beim Besiegen gibt
    private int aktuelleEp = 0;
    private boolean hatGekaempft = false; // Für EP-Berechtigung
    // Attacken
    private Attacke[] attacken;
 
    // Stat-Stufen: Werte zwischen -6 und +6, die die effektiven Stats modifizieren
    private int atkStufe   = 0;
    private int defStufe   = 0;
    private int initStufe  = 0;
    private StatusEffekt aktiverStatus = StatusEffekt.KEINER;


    /**
     * Erstellt ein neues Pokemon mit vollen Lebenspunkten.
     * @param name     Der Name des Pokemons
     * @param typ      Der Elementartyp (z. B. "Feuer")
     * @param maxHp    Die maximalen Lebenspunkte
     * @param atk      Der Basis-Angriffswert
     * @param def      Der Basis-Verteidigungswert
     * @param init     Die Basis-Initiative (Geschwindigkeit)
     * @param attacken Das Array der vier erlernbaren Attacken
     */
    public Pokemon(String name, PokemonTyp typ, int maxHp, int atk, int def, int init, int level, int basisErfahrung, Attacke[] attacken) {
        this.name     = name;
        this.typ      = typ;
        this.maxHp    = maxHp;
        this.hp       = maxHp;
        this.baseAtk  = atk;
        this.baseDef  = def;
        this.baseInit = init;
        this.level = level;
        this.basisErfahrung = basisErfahrung;
        this.attacken = attacken;
    }

    // =========================================================================
    // HP-LOGIK 
    // =========================================================================
    
    public void erleideSchaden(double punkte) {
        this.hp = Math.max(0, this.hp - punkte);
        System.out.printf("%s verliert %.0f KP! -> %.0f/%d KP%n",
                this.name, punkte, this.hp, this.maxHp);
    }

    public void heile(double punkte) {
        this.hp = Math.min(this.maxHp, this.hp + punkte);
        System.out.printf("%s wird geheilt! -> %.0f/%d KP%n", 
                this.name, this.hp, this.maxHp);
    }

    // =========================================================================
    // STAT-STUFEN MODIFIKATIONEN
    // =========================================================================

    public void aendereAtkStufe(int delta) {
        if (this.atkStufe == 6 && delta > 0) {
            System.out.printf("Die ATK von %s kann nicht weiter steigen!%n", this.name); return;
        }
        if (this.atkStufe == -6 && delta < 0) {
            System.out.printf("Die ATK von %s kann nicht weiter sinken!%n", this.name); return;
        }
        this.atkStufe = Math.max(-6, Math.min(6, this.atkStufe + delta));
        System.out.printf("%s ATK wurde %s!%n", this.name, delta > 0 ? "erhoeht" : "gesenkt");
    }

    public void aendereDefStufe(int delta) {
        if (this.defStufe == 6 && delta > 0) {
            System.out.printf("Die DEF von %s kann nicht weiter steigen!%n", this.name); return;
        }
        if (this.defStufe == -6 && delta < 0) {
            System.out.printf("Die DEF von %s kann nicht weiter sinken!%n", this.name); return;
        }
        this.defStufe = Math.max(-6, Math.min(6, this.defStufe + delta));
        System.out.printf("%s DEF wurde %s!%n", this.name, delta > 0 ? "erhoeht" : "gesenkt");
    }

    public void aendereInitStufe(int delta) {
        if (this.initStufe == 6 && delta > 0) {
            System.out.printf("Die Initiative von %s kann nicht weiter steigen!%n", this.name); return;
        }
        if (this.initStufe == -6 && delta < 0) {
            System.out.printf("Die Initiative von %s kann nicht weiter sinken!%n", this.name); return;
        }
        this.initStufe = Math.max(-6, Math.min(6, this.initStufe + delta));
        System.out.printf("%s Initiative wurde %s!%n", this.name, delta > 0 ? "erhoeht" : "gesenkt");
    }

    // =========================================================================
    // LEVELING-LOGIK
    // =========================================================================

    public int epFuerNaechstesLevel() {
        // Gen-1 "Medium Fast": level^3
        return (int) Math.pow(level + 1, 3);
    }

    public boolean gebeErfahrung(int ep) {
        this.aktuelleEp += ep;
        System.out.printf("%s erhaelt %d EP! (%d/%d)%n", 
            name, ep, aktuelleEp, epFuerNaechstesLevel());
        if (aktuelleEp >= epFuerNaechstesLevel()) {
            levelUp();
            return true;
        }
        return false;
    }

    private void levelUp() {
        level++;
        // Stats proportional zum Level skalieren (einfache lineare Skalierung)
        int neuesMaxHp = (int) Math.round(maxHp * (1.0 + 0.07));
        int neuesAtk   = (int) Math.round(baseAtk * (1.0 + 0.07));
        int neuesDef   = (int) Math.round(baseDef * (1.0 + 0.07));
        int neuesInit  = (int) Math.round(baseInit * (1.0 + 0.07));

        int hpZuwachs = neuesMaxHp - maxHp;
        this.maxHp   = neuesMaxHp;
        this.hp      = Math.min(hp + hpZuwachs, maxHp); // HP steigen mit, aber nicht über Max
        this.baseAtk  = neuesAtk;
        this.baseDef  = neuesDef;
        this.baseInit = neuesInit;

        System.out.printf("%n★ %s ist auf Level %d aufgestiegen! ★%n", name, level);
        System.out.printf("  MAX KP +%d | ATK +%d | DEF +%d | INIT +%d%n",
                hpZuwachs, neuesAtk - baseAtk + (neuesAtk - baseAtk),
                neuesDef - baseDef, neuesInit - baseInit);
}

    // =========================================================================
    // EFFEKTIVE STAT-BERECHNUNG
    // =========================================================================

    private double getStufenMultiplikator(int stufe) {
        return stufe >= 0 ? (2.0 + stufe) / 2.0 : 2.0 / (2.0 - stufe);
    }

    public int getEffectiveAtk() {
        return (int) Math.max(1, Math.round(this.baseAtk * getStufenMultiplikator(this.atkStufe)));
    }

    public int getEffectiveDef() {
        return (int) Math.max(1, Math.round(this.baseDef * getStufenMultiplikator(this.defStufe)));
    }

    public int getEffectiveInit() {
        int base = (int) Math.max(1, Math.round(this.baseInit * getStufenMultiplikator(this.initStufe)));
        return (this.aktiverStatus == StatusEffekt.PARALYSE) ? base / 2 : base;
    }

    // Getter und Setter
    public String getName()          { return name; }
    public PokemonTyp getTyp()       { return typ; }
    public double getHp()            { return hp; }
    public int getMaxHp()            { return maxHp; }
    public int getBaseAtk()          { return baseAtk; }
    public int getBaseDef()          { return baseDef; }
    public int getBaseInit()         { return baseInit; }
    public Attacke[] getAttacken()   { return attacken; }
    public StatusEffekt getAktiverStatus() { return aktiverStatus; }

    public boolean hatGekaempft()       { return hatGekaempft; }
    public int getLevel()              { return level; }
    public int getBasisErfahrung()     { return basisErfahrung; }
    public void markiereAlsBeteiligt() { this.hatGekaempft = true; }
    public void resetKampfBeteiligung() { this.hatGekaempft = false; }

    public void setHp(double hp)     { this.hp = hp; }
    public void setAktiverStatus(StatusEffekt s) { this.aktiverStatus = s; }

}