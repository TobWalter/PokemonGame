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

    public static Attacke rankenhieb() { 
        return new Attacke("Rankenhieb", PokemonTyp.PFLANZE, 1.0, 
            new SchadensEffekt(3.0)
        ); 
    }
    public static Attacke kratzer() { 
        return new Attacke("Kratzer", PokemonTyp.NORMAL, 1.0, 
            new SchadensEffekt(2.5)
        ); 
    }
    public static Attacke blubber() { 
        return new Attacke("Blubber", PokemonTyp.WASSER, 1.0, 
            new SchadensEffekt(3.0)
        ); 
    }
    public static Attacke tackle() { 
        return new Attacke("Tackle", PokemonTyp.NORMAL, 1.0, 
            new SchadensEffekt(3.0)
        ); 
    }

    // =========================================================================
    // 2. HYBRID-ATTACKEN (Schaden + Zusatzeffekt)
    // =========================================================================
    
    public static Attacke glut() { 
        return new Attacke("Glut", PokemonTyp.FEUER, 1.0, 
            new SchadensEffekt(3.0),
            new StatusZustandsEffekt(StatusEffekt.BRENNEN, 0.10) // 10% Chance
        ); 
    }

    public static Attacke biss() { 
        return new Attacke("Biss", PokemonTyp.NORMAL, 1.0, 
            new SchadensEffekt(3.0),
            new StatusZustandsEffekt(StatusEffekt.ZURUECKSCHRECKEN, 0.10) // 10% Chance
        ); 
    }

    public static Attacke kopfnuss() { 
        return new Attacke("Kopfnuss", PokemonTyp.NORMAL, 1.0, 
            new SchadensEffekt(3.0),
            new StatusZustandsEffekt(StatusEffekt.ZURUECKSCHRECKEN, 0.10) // 10% Chance
        ); 
    }

    // =========================================================================
    // 3. REINE STAT-ATTACKEN 
    // =========================================================================

    public static Attacke heuler() { 
        return new Attacke("Heuler", PokemonTyp.NORMAL, 1.0, 
            new StatMod("ATK", -1, false) // Senkt ATK beim Gegner
        ); 
    }
    public static Attacke rutenschlag() { 
        return new Attacke("Rutenschlag", PokemonTyp.NORMAL, 1.0, 
            new StatMod("DEF", -1, false) // Senkt DEF beim Gegner
        ); 
    }
    public static Attacke nebelschleier() { 
        return new Attacke("Nebelschleier", PokemonTyp.NORMAL, 1.0, 
            new StatMod("DEF", 1, true) // Erhöht DEF beim eigenen Pokemon
        );
    }
    public static Attacke fadenschuss() { 
        return new Attacke("Fadenschuss", PokemonTyp.KAEFER, 1.0, 
            new StatMod("INIT", -1, false) // Senkt INIT beim Gegner
        ); 
    }

    // =========================================================================
    // 4. REINE STATUS-ATTACKEN 
    // =========================================================================

    public static Attacke stachelspore() { 
        return new Attacke("Stachelspore", PokemonTyp.PFLANZE, 0.75, 
            new StatusZustandsEffekt(StatusEffekt.PARALYSE, 1.0) // 100% bei Treffer
        );
    }
    public static Attacke giftpuder() { 
        return new Attacke("Giftpuder", PokemonTyp.PFLANZE, 0.75, 
            new StatusZustandsEffekt(StatusEffekt.VERGIFTUNG, 1.0) // 100% bei Treffer
        );
    }
    public static Attacke giftgas() { 
        return new Attacke("Giftgas", PokemonTyp.GIFT, 0.75, 
            new StatusZustandsEffekt(StatusEffekt.VERGIFTUNG, 1.0) // 100% bei Treffer
        ); 
    }    
}