package pokemongame;

/**
 * Repräsentiert alle offiziellen primären Statuseffekte der 1. Pokémon-Generation.
 * Ein Pokémon kann im offiziellen Regelwerk immer nur von maximal einem 
 * dieser Effekte gleichzeitig betroffen sein.
 */
public enum StatusEffekt {
    /** Das Pokémon ist vollkommen gesund */
    KEINER,
    
    /** * BRN - Das Pokémon brennt. 
     * Es verliert jede Runde KP und sein physischer Angriff (ATK) wird halbiert. 
     */
    BRENNEN,
    
    /** * FRZ - Das Pokémon ist eingefroren. 
     * Es kann sich überhaupt nicht mehr bewegen, bis es durch feurige Attacken auftaut. 
     */
    EINFRIEREN,
    
    /** * PAR - Das Pokémon ist paralysiert. 
     * Seine Initiative (INIT) wird stark gesenkt und es besteht jede Runde die Chance, unfähig zu handeln. 
     */
    PARALYSE,
    
    /** * PSN - Das Pokémon ist vergiftet. 
     * Es verliert am Ende jeder Kampfrunde kontinuierlich KP. 
     */
    VERGIFTUNG,
    
    /** * SLP - Das Pokémon schläft. 
     * Es ist für eine zufällige Anzahl von Runden kampfunfähig, bis es aufwacht. 
     */
    SCHLAF,
    
    /** * Interner Hilfswert für reine Statusattacken (z. B. Heuler, Rutenschlag).
     * Signalisiert dem System, dass keine langanhaltende Krankheit übertragen wird, 
     * sondern nur die Statusstufen modifiziert werden.
     */
    WERTE_MODIFIKATION
}