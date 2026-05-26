package pokemongame;
/**
 * Interface für Effekte, die auf ein Pokémon angewendet werden können.
 */
public interface Effekt {
    void anwenden(KampfSystem kampf, Pokemon anwender, Pokemon ziel, PokemonTyp attackenTyp);
}
