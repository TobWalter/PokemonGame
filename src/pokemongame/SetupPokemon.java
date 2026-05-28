package pokemongame;

/**
 * Klasse für Pokémon 
 * Stellt die Teams zusammen, indem sie sich die Attacken aus der SetupAttacken-Bibliothek greift.
 */
public class SetupPokemon {

    /**
     * Erstellt die spielbereiten Starter-Pokémon und bestückt sie 
     * flexibel mit Attacken aus der Attacken-Bibliothek.
     */
    public static Pokemon[] erstelleStartOptionen() {
        
        // Bisasam zusammenbauen
        Pokemon bisasam  = new Pokemon("Bisasam", PokemonTyp.PFLANZE, 22, 5, 7, 10, new Attacke[]{
            SetupAttacken.rankenhieb(),   // DMG
            SetupAttacken.heuler(),       // Status (Stat)
            SetupAttacken.stachelspore(), // Status (Paralyse)
            SetupAttacken.giftpuder()     // Status (Gift)
        });
                
        // Glumanda zusammenbauen
        Pokemon glumanda = new Pokemon("Glumanda", PokemonTyp.FEUER, 19, 7, 5, 12, new Attacke[]{
            SetupAttacken.glut(),         // DMG
            SetupAttacken.kratzer(),      // DMG
            SetupAttacken.rutenschlag(),  // Status (Stat)
            SetupAttacken.giftgas()       // Status (Gift)
        });
                
        // Schiggy zusammenbauen
        Pokemon schiggy  = new Pokemon("Schiggy", PokemonTyp.WASSER, 25, 4, 8, 11, new Attacke[]{
            SetupAttacken.blubber(),      // DMG
            SetupAttacken.biss(),         // Hybrid (Schaden + Zurückschrecken)
            SetupAttacken.nebelschleier(),// Status (Stat)
            SetupAttacken.kopfnuss()      // Hybrid (Schaden + Zurückschrecken)
        });

        return new Pokemon[]{ bisasam, glumanda, schiggy };
    }
    public static Pokemon[] erstelleAllePokemon() {
        Pokemon[] starter = erstelleStartOptionen();
        Pokemon raupy = new Pokemon("Raupy", PokemonTyp.KAEFER, 15, 3, 4, 8, new Attacke[]{
            SetupAttacken.tackle(),      // DMG
            SetupAttacken.fadenschuss(), // senkt INIT
            SetupAttacken.stachelspore(),// Status (Paralyse)
            SetupAttacken.giftpuder()    // Status (Gift)
        });
        Pokemon habitak = new Pokemon("Habitak", PokemonTyp.NORMAL, 18, 6, 5, 9, new Attacke[]{
            SetupAttacken.kopfnuss(),     // Hybrid (Schaden + Zurückschrecken)
            SetupAttacken.biss(),         // Hybrid (Schaden + Zurückschrecken)
            SetupAttacken.rutenschlag(),  // Status (Stat)
            SetupAttacken.giftgas()       // Status (Gift)
        });
        Pokemon rattfratz = new Pokemon("Rattfratz", PokemonTyp.NORMAL, 16, 5, 4, 10, new Attacke[]{
            SetupAttacken.kratzer(),      // DMG
            SetupAttacken.biss(),         // Hybrid (Schaden + Zurückschrecken)
            SetupAttacken.rutenschlag(),  // Status (Stat)
            SetupAttacken.giftgas()       // Status (Gift)
        });

        return new Pokemon[]{ starter[0], starter[1], starter[2], raupy, habitak, rattfratz };
    }   
}