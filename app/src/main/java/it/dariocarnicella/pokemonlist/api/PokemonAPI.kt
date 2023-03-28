package it.dariocarnicella.pokemonlist.api

import it.dariocarnicella.pokemonlist.ui.models.Pokemon
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokemonAPI {
    @GET("pokemon")
    suspend fun getPokemonList(@Query("limit") limit: Int,
                               @Query("offset") offset: Int) : Response<Pokemon>

    @GET("pokemon/{id}")
    suspend fun getPokemonInfo(@Path("id") id : String) : Response<Pokemon>
}