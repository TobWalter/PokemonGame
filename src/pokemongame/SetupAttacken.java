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
    public static Attacke rankenhieb()   { return new Attacke("Rankenhieb", "Pflanze", 3.0, "", 0.0, 1.0); }
    public static Attacke glut()         { return new Attacke("Glut", "Feuer", 3.0, "", 0.0, 1.0); }
    public static Attacke kratzer()      { return new Attacke("Kratzer", "Normal", 2.5, "", 0.0, 1.0); }
    public static Attacke blubber()      { return new Attacke("Blubber", "Wasser", 3.0, "", 0.0, 1.0); }


    // =========================================================================
    // 2. HYBRID-ATTACKEN (Schaden + Zusatzeffekt/Statuseffekt)
    // =========================================================================
    public static Attacke biss()         { return new Attacke("Biss", "Normal", 2.0, "Zurueckschrecken", 0.15, 0.85); }
    public static Attacke kopfnuss()     { return new Attacke("Kopfnuss", "Normal", 2.0, "Zurueckschrecken", 0.15, 0.90); }


    // =========================================================================
    // 3. REINE STATUS-ATTACKEN (Veränderungen ohne Direktschaden)
    // =========================================================================
    
    // --- Stat-Veränderungen (ATK / DEF) ---
    public static Attacke heuler()       { return new Attacke("Heuler", "Normal", "senkt ATK des Gegners!", "Stat", 0.75, 1.0, 1.0,false, 1.0); }
    public static Attacke rutenschlag()  { return new Attacke("Rutenschlag", "Normal", "senkt DEF des Gegners!", "Stat", 1.0, 0.75, 1.0, false, 1.0); }
    public static Attacke nebelschleier(){ return new Attacke("Nebelschleier", "Normal", "erhoeht eigene DEF!", "Stat", 1.0, 1.0, 1.1, true,1.0); }

    // --- Statusveränderungen (Paralyse / Gift) ---
    public static Attacke stachelspore() { return new Attacke("Stachelspore", "Käfer", "paralysiert den Gegner!", "Paralyse", 1.0, 1.0, 1.0, false, 1.0); }
    public static Attacke giftpuder()    { return new Attacke("Giftpuder", "Gift", "vergiftet den Gegner!", "Gift", 1.0, 1.0, 1.0, false, 0.7); }
    public static Attacke giftgas()      { return new Attacke("Giftgas", "Gift", "vergiftet den Gegner!", "Gift", 1.0, 1.0, 1.0, false, 0.75); }
}