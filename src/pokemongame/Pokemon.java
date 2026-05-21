package pokemongame;

/**
 * Repraesentiert ein Pokemon mit seinen Werten, Attacken und Statuseffekten.
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

    public boolean istVergiftet    = false;
    public boolean istParalysiert  = false;

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
     * 
     * @return true wenn das Pokemon agieren kann, sonst false.
     */
    public boolean kannAgieren() {
        if (this.istParalysiert) {
            System.out.printf("%s ist paralysiert!%n", this.name);
            // 25% Chance: Pokemon setzt aus
            if (Math.random() < 0.25) {
                System.out.printf("%s kann sich nicht bewegen!%n", this.name);
                return false;
            }
        }
        return this.hp > 0;
    }

    /**
     * Gibt die effektive Initiative zurueck.
     * Paralyse halbiert die Initiative.
     */
    public double getEffectiveInit() {
        if (this.istParalysiert) {
            return this.init * 0.5;
        }
        return this.init;
    }

    /**
     * Fuehrt die ausgewaehlte Attacke auf das gegnerische Pokemon aus.
     * 
     * SCHADENSFORMEL:
     *   schaden = round( (atk * staerke) / def * typMultiplikator )
     *
     * Beispiel 1: Glumanda (Feuer, ATK=7) setzt Glut (Feuer, Staerke=3.0)
     *             gegen Bisasam (Pflanze, DEF=7) ein.
     *   -> typMult: Feuer > Pflanze = 1.3, + STAB (Glumanda ist Feuer) = 1.5
     *   -> schaden = round( (7 * 3.0) / 7 * 1.5 ) = round( 4.5 ) = 5
     *
     * Beispiel 2: Bisasam (Pflanze, ATK=5) setzt Rankenhieb (Pflanze, Staerke=3.0)
     *             gegen Glumanda (Feuer, DEF=5) ein.
     *   -> typMult: Pflanze gegen Feuer = 0.8, kein STAB-Bonus bei Nachteil
     *   -> schaden = round( (5 * 3.0) / 5 * 0.8 ) = round( 2.4 ) = 2
     *
     * Beispiel 3: Schiggy (Wasser, ATK=4) setzt Kratzer (Normal, Staerke=2.5)
     *             gegen Bisasam (Pflanze, DEF=7) ein.
     *   -> typMult: Normal = 1.0, kein STAB
     *   -> schaden = round( (4 * 2.5) / 7 * 1.0 ) = round( 1.43 ) = 1
     */
    public void fuehreAktionAus(Pokemon gegner, int index) {
        Attacke a = this.attacken[index];

        // 1. Trefferchance wuerfeln
        if (Math.random() > a.genauigkeit) {
            System.out.printf("%s setzt %s ein! -- Daneben!%n", this.name, a.name);
            return;
        }

        // 2. Statusattacken haben keinen Direktschaden
        if (a.staerke == 0.0) {
            verarbeiteStatusAttacke(gegner, a);
            return;
        }

        // 3. Typmultiplikator berechnen
        double typMult    = KampfSystem.berechneTypMultiplikator(a.typ, gegner.typ, this.typ);
        String effektText = typMult > 1.0 ? " Sehr effektiv!" : (typMult < 1.0 ? " Nicht sehr effektiv..." : "");

        // 4. Schaden berechnen: (atk * staerke) / def * typMult
        double schaden = Math.round((this.atk * a.staerke) / gegner.def * typMult);
        if (schaden < 1) schaden = 1; // Mindestschaden 1

        // 5. Schaden anwenden
        System.out.printf("%s setzt %s ein!%s%n", this.name, a.name, effektText);
        gegner.hp -= schaden;
        if (gegner.hp < 0) gegner.hp = 0;
        System.out.printf("%s verliert %.0f HP -> %.0f/%d HP%n", gegner.name, schaden, gegner.hp, gegner.maxHp);

        // 6. Nebeneffekt bei Hybrid-Attacken wuerfeln
        if (!a.effekt.isEmpty() && !a.effekt.equals("Stat")) {
            if (Math.random() < a.effektChance) {
                verarbeiteNebeneffekt(gegner, a.effekt);
            }
        }
    }

    /**
     * Verarbeitet reine Statusattacken (staerke == 0.0).
     * Wendet Stat-Veraenderungen oder Statuseffekte auf das Ziel an.
     */
    private void verarbeiteStatusAttacke(Pokemon ziel, Attacke a) {
        System.out.printf("%s setzt %s ein! %s%n", this.name, a.name, a.beschreibung);

        if (Math.random() > a.effektChance) {
            System.out.println("Aber es hatte keinen Effekt!");
            return;
        }

        if (a.effekt.equals("Stat")) {
            // Stat-Veraenderungen anwenden, Minimalwert 1 gegen Division durch 0
            ziel.atk = (int) Math.max(1, Math.round(ziel.atk * a.atkMod));
            ziel.def = (int) Math.max(1, Math.round(ziel.def * a.defMod));

            if (a.atkMod < 1.0) System.out.printf("%s ATK wurde gesenkt!%n",  ziel.name);
            if (a.defMod < 1.0) System.out.printf("%s DEF wurde gesenkt!%n",  ziel.name);
            if (a.defMod > 1.0) System.out.printf("%s DEF wurde erhoeht!%n",  ziel.name);
        } else {
            verarbeiteNebeneffekt(ziel, a.effekt);
        }
    }

    /**
     * Wendet einen konkreten Nebeneffekt auf das Ziel an.
     */
    private void verarbeiteNebeneffekt(Pokemon ziel, String effekt) {
        switch (effekt) {
            case "Gift":
                if (!ziel.istVergiftet) {
                    ziel.istVergiftet = true;
                    System.out.printf("%s wurde vergiftet!%n", ziel.name);
                } else {
                    System.out.printf("%s ist bereits vergiftet!%n", ziel.name);
                }
                break;
            case "Paralyse":
                if (!ziel.istParalysiert) {
                    ziel.istParalysiert = true;
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
}