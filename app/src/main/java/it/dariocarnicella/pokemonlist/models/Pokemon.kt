package it.dariocarnicella.pokemonlist.ui.models

data class Pokemon(
    val id: Int,
    val name: String?,
    val weight: Int,
    val height: Int,
    val sprites: Sprites,
    val stats: List<Stat>,
    val types: List<Type>,
    val count: Int,
    val next: String,
    val previous: String,
    val results: List<PokemonResult>
)

data class PokemonResult(
    val name: String,
    val url: String
) {
    fun getPokemonImage(): String {
        val index = url.split("/".toRegex()).dropLast(1).last()
        return "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$index.png"
    }
}
