package pokemongame;

/**
 * Repräsentiert alle offiziellen primären Statuseffekte der 1. Pokémon-Generation.
 * Ein Pokémon kann im offiziellen Regelwerk immer nur von maximal einem 
 * dieser Effekte gleichzeitig betroffen sein.
 */
public enum StatusEffekt {
    /** Das Pokémon hat keinen Statuseffekt */
    KEINER,
    
    /** 
     * PSN - Das Pokémon ist vergiftet. 
     * Es verliert am Ende jeder Kampfrunde kontinuierlich KP. 
     */
    VERGIFTUNG,

    /**
     * BRN - Das Pokémon brennt. 
     * Es verliert jede Runde KP und sein physischer Angriff (ATK) wird halbiert. 
     */
    BRENNEN,
    
    /**
     * FRZ - Das Pokémon ist eingefroren. 
     * Es kann sich überhaupt nicht mehr bewegen, bis es durch feurige Attacken auftaut. 
     */
    EINFRIEREN,
    
    /** 
     * PAR - Das Pokémon ist paralysiert. 
     * Seine Initiative (INIT) wird stark gesenkt und es besteht jede Runde die Chance, unfähig zu handeln. 
     */
    PARALYSE,
    
    /** 
     * SLP - Das Pokémon schläft. 
     * Es ist für eine zufällige Anzahl von Runden kampfunfähig, bis es aufwacht. 
     */
    SCHLAF,
    
    /**
     * KEINE TAG - Das Pokémon ist von keinem Status betroffen.
     * Wenn zuerst im Kampfablauf angewendet, lässt es das Verteidigerpokemon zurückschrecken und verhindert so die Ausführung der Aktion des Verteidigers in dieser Runde.
     */
    ZURUECKSCHRECKEN,

    /** 
     * KEIN TAG - Reine Stat-Änderung wie ATK- oder DEF-Senkung/Erhöhung, ohne zusätzlichen Status.
     * Kann sowohl auf den Anwender als auch auf den Gegner wirken, abhängig von der Attacke.
     */
    WERTE_MODIFIKATION
}