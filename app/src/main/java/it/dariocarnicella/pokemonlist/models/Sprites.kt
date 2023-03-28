package it.dariocarnicella.pokemonlist.ui.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Sprites(
    @Expose @SerializedName("front_default") val frontDefault: String,
    @Expose @SerializedName ("front_shiny") val frontShiny: String
)
