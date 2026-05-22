package pokemongame;

/**
 * Repraesentiert ein Pokemon mit seinen Werten, Attacken und Statuseffekten.
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

    private boolean istVergiftet    = false;
    private boolean istParalysiert  = false;

    /**
     * Konstruktor zum Erstellen eines neuen Pokemons mit vollen Lebenspunkten.
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
     * Prueft vor der Aktion ob das Pokemon handlungsfaehig ist.
     * Paralyse: 25% Chance dass das Pokemon in dieser Runde aussetzt.
     * * @return true wenn das Pokemon agieren kann, sonst false.
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

    public int getEffectiveInit() {
        if (this.istParalysiert) {
            return this.init / 2;
        }
        return this.init;
    }

    public void fuehreAktionAus(Pokemon ziel, int atkIndex) {
        Attacke a = this.attacken[atkIndex];
        System.out.printf("%n%s setzt %s ein!%n", this.name, a.getName());

        if (Math.random() > a.getGenauigkeit()) {
            System.out.println("Die Attacke ging daneben!");
            return;
        }

        if (a.getStaerke() > 0) {
            double typMult = KampfSystem.berechneTypMultiplikator(a.getTyp(), ziel.getTyp(), this.typ);
            double schaden = Math.max(1, (this.atk * a.getStaerke()) / ziel.getDef() * typMult);
            
            ziel.schade(schaden);

            if (typMult > 1.1) System.out.println("Das war sehr effektiv!");
            if (typMult < 0.9) System.out.println("Das war nicht sehr effektiv...");

            if (!a.getEffekt().equals("") && Math.random() < a.getEffektChance()) {
                verarbeiteNebeneffekt(ziel, a.getEffekt());
            }
        } else if (a.getEffekt().equals("Stat")) {
            verarbeiteStatusAttacke(ziel, a);
        } else {
            if (Math.random() < a.getEffektChance()) {
                verarbeiteNebeneffekt(ziel, a.getEffekt());
            }
        }
    }

    public void schade(double punkte) {
        this.hp = Math.max(0, this.hp - punkte);
        System.out.printf("%s verliert %.0f KP! -> %.0f/%d KP%n", this.name, punkte, this.hp, this.maxHp);
    }

    public void heile(double punkte) {
        this.hp = Math.min(this.maxHp, this.hp + punkte);
    }

    private void verarbeiteStatusAttacke(Pokemon ziel, Attacke a) {
        System.out.println(a.getBeschreibung());
        Pokemon statziel = a.isTargetIsSelf() ? this : ziel;

        statziel.atk = (int) Math.max(1, Math.round(statziel.atk * a.getAtkMod()));
        statziel.def = (int) Math.max(1, Math.round(statziel.def * a.getDefMod()));

        if (a.getAtkMod() < 1.0) System.out.printf("%s ATK wurde gesenkt!%n",  statziel.name);
        if (a.getAtkMod() > 1.0) System.out.printf("%s ATK wurde erhoeht!%n",  statziel.name);
        if (a.getDefMod() < 1.0) System.out.printf("%s DEF wurde gesenkt!%n",  statziel.name);
        if (a.getDefMod() > 1.0) System.out.printf("%s DEF wurde erhoeht!%n",  statziel.name);
    }

    /**
     * Wendet einen konkreten Nebeneffekt auf das Ziel an.
     */
    private void verarbeiteNebeneffekt(Pokemon ziel, String effekt) {
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

    public String getName() { return name; }
    public String getTyp() { return typ; }
    public double getHp() { return hp; }
    public int getMaxHp() { return maxHp; }
    public int getDef() { return def; }
    public Attacke[] getAttacken() { return attacken; }

    public boolean istVergiftet() { return istVergiftet; }
    public void setVergiftet(boolean status) { this.istVergiftet = status; }

    public boolean istParalysiert() { return istParalysiert; }
    public void setParalysiert(boolean status) { this.istParalysiert = status; }
}