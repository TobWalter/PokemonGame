package pokemongame;

public class StatMod implements Effekt {
    private int statVeraenderung; // z.B. -1 oder +1
    private String statName;      // "ATK", "DEF" oder "INIT"
    private boolean aufSichSelbst; // true = Anwender, false = Gegner

    public StatMod(String statName, int statVeraenderung, boolean aufSichSelbst) {
        this.statName = statName;
        this.statVeraenderung = statVeraenderung;
        this.aufSichSelbst = aufSichSelbst;
    }

    @Override
    public void anwenden(KampfSystem kampf, Pokemon anwender, Pokemon ziel, PokemonTyp attackenTyp) {
        Pokemon betroffenesPokemon = aufSichSelbst ? anwender : ziel;
        switch (statName.toUpperCase()) {
            case "ATK"  -> betroffenesPokemon.aendereAtkStufe(statVeraenderung);
            case "DEF"  -> betroffenesPokemon.aendereDefStufe(statVeraenderung);
            case "INIT" -> betroffenesPokemon.aendereInitStufe(statVeraenderung);
        }
    }
}