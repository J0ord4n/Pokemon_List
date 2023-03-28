package it.dariocarnicella.pokemonlist.ui.models

data class Type(
    val slot: Int,
    val type: TypeX
)

data class TypeX(
    val name: String,
    val url: String
)
