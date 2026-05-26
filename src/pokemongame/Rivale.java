package pokemongame;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Steuert die einfache KI des gegnerischen Rivalen.
 */
public class Rivale {

    private String name;
    private List<Pokemon> team;
    private int aktivesPokemonIndex = 0;

    public Rivale(String name) {
        this.name = name;
        this.team = new ArrayList<>();
    }

    public void fuegePokemonHinzu(Pokemon pokemon) {
        if (team.size() < 6) team.add(pokemon);
    }

    /**
     * Waehlt zufaellig eine Attacke und fuehrt sie ueber das KampfSystem aus.
     */
    public int waehleAttacke(Random random) {
        Pokemon aktives = getAktivesPokemon();
        return random.nextInt(aktives.getAttacken().length);
    }

    public String getName()           { return name; }
    public List<Pokemon> getTeam()    { return team; }
    public Pokemon getAktivesPokemon() { return team.get(aktivesPokemonIndex); }
}