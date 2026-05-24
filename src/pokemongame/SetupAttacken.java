package pokemongame;

/**
 * Die zentrale Bibliothek für alle Attacken im Spiel.
 * Hier werden Attacken definiert und
 * nach ihren Einsatzzwecken (Schaden, Hybrid, Status) kategorisiert.
 */
public class SetupAttacken {

    // =========================================================================
    // 1. REINE SCHADENS-ATTACKEN (Pure DMG)
    // =========================================================================
    public static Attacke rankenhieb()   { return new SchadensAttacke("Rankenhieb", PokemonTyp.PFLANZE, 1.0, 3.0, "", 0.0); }
    public static Attacke glut()         { return new SchadensAttacke("Glut", PokemonTyp.FEUER, 1.0, 3.0, "", 0.0); }
    public static Attacke kratzer()      { return new SchadensAttacke("Kratzer", PokemonTyp.NORMAL, 1.0, 2.5, "", 0.0); }
    public static Attacke blubber()      { return new SchadensAttacke("Blubber", PokemonTyp.WASSER, 1.0, 3.0, "", 0.0); }


    // =========================================================================
    // 2. HYBRID-ATTACKEN (Schaden + Zusatzeffekt, Nutzen SchadensAttacke)
    // =========================================================================
    
    public static Attacke biss()         { return new SchadensAttacke("Biss", PokemonTyp.NORMAL, 0.85, 2.0, "Zurueckschrecken", 0.15); }
    public static Attacke kopfnuss()     { return new SchadensAttacke("Kopfnuss", PokemonTyp.NORMAL, 0.90, 2.0, "Zurueckschrecken", 0.15); }


    // =========================================================================
    // 3. REINE STATUS-ATTACKEN (Nutzen StatusAttacke)
    // =========================================================================
    // Großer Konstruktor: name, typ, genauigkeit, beschreibung, effekt, effektChance, atkMod, defMod, initMod, targetIsSelf
    // Kleiner Konstruktor: name, typ, beschreibung, effekt, genauigkeit, effektChance
    
    public static Attacke heuler()       { return new StatusAttacke("Heuler", PokemonTyp.NORMAL, 1.0, "senkt ATK des Gegners!", StatusEffekt.WERTE_MODIFIKATION, 1.0, 0.75, 1.0, 1.0, false); }
    public static Attacke rutenschlag()  { return new StatusAttacke("Rutenschlag", PokemonTyp.NORMAL, 1.0, "senkt DEF des Gegners!", StatusEffekt.WERTE_MODIFIKATION, 1.0, 1.0, 0.75, 1.0, false); }
    public static Attacke nebelschleier(){ return new StatusAttacke("Nebelschleier", PokemonTyp.NORMAL, 1.0, "erhoeht eigene DEF!", StatusEffekt.WERTE_MODIFIKATION, 1.0, 1.0, 1.1, 1.0, true); }

    // Die langanhaltenden Zustandseffekte nutzen den schlankeren Konstruktor
    public static Attacke stachelspore() { return new StatusAttacke("Stachelspore", PokemonTyp.PFLANZE, "paralysiert das Ziel!", StatusEffekt.PARALYSE, 0.75, 1.0); }
    public static Attacke giftpuder()    { return new StatusAttacke("Giftpuder", PokemonTyp.PFLANZE, "vergiftet das Ziel!", StatusEffekt.VERGIFTUNG, 0.75, 1.0); }
    public static Attacke giftgas()      { return new StatusAttacke("Giftgas", PokemonTyp.GIFT, "vergiftet das Ziel!", StatusEffekt.VERGIFTUNG, 0.75, 1.0); }
}