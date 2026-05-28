package pokemongame;

import java.util.ArrayList;
import java.util.List;

public class Spieler {
    private String name;
    private List<Pokemon> team;
    private Beutel beutel;
    private int aktivesPokemonIndex = 0;

    public Spieler(String name, Beutel beutel) {
        this.name = name;
        this.beutel = beutel;
        this.team = new ArrayList<>();
    }

    public void fuegePokemonHinzu(Pokemon pokemon) {
        if (team.size() < 6) {
            team.add(pokemon);
        }
    }

    /**
     * Wechselt das aktive Pokemon auf das angegebene.
     * Das Pokemon muss im Team vorhanden sein.
     * @param pokemon Das neue aktive Pokemon
     */
    public void setAktivesPokemon(Pokemon pokemon) {
        int index = team.indexOf(pokemon);
        if (index >= 0) {
            aktivesPokemonIndex = index;
        }
    }
    /**
     * Ersetzt ein Pokemon im Team durch ein anderes. Wird verwendet, um die Entwicklung eines Pokemons zu vollziehen. 
     * @param alt Das Pokemon, das ersetzt werden soll (z. B. Glumanda)
     * @param neu Das Pokemon, das das alte ersetzt (z. B. Glutexo)
     */
    public void ersetzeImTeam(Pokemon alt, Pokemon neu) {
        int index = team.indexOf(alt);
        if (index >= 0) {
            team.set(index, neu);
        }
    }
    
    // Getter & Setter 
    public String getName() { return name; }
    public List<Pokemon> getTeam() { return team; }
    public Beutel getBeutel() { return beutel; }
    public Pokemon getAktivesPokemon() { return team.get(aktivesPokemonIndex); }
}