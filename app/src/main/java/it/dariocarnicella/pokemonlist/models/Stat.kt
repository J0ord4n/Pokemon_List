package it.dariocarnicella.pokemonlist.ui.models


data class Stat(
    val base_stat: Int,
    val effort: Int,
    val stat: StatX
)

data class StatX(
    val name: String,
    val url: String
)
