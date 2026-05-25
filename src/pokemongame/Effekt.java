package pokemongame;
/**
 * Interface für Effekte, die auf ein Pokémon angewendet werden können.
 */
public interface Effekt {
    void anwenden(Pokemon anwender, Pokemon ziel, PokemonTyp attackenTyp);
}
