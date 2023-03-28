package it.dariocarnicella.pokemonlist.ui.splash_screen

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import it.dariocarnicella.pokemonlist.databinding.ActivityPokeSplashScreenBinding
import it.dariocarnicella.pokemonlist.ui.PokeListActivity
import kotlinx.coroutines.*

class PokeSplashScreen : AppCompatActivity() {

    private lateinit var binding: ActivityPokeSplashScreenBinding
    private val activityScope = CoroutineScope(Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPokeSplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        activityScope.launch {
            delay(3500)

            val intent = Intent(this@PokeSplashScreen, PokeListActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onPause() {
        activityScope.cancel()
        super.onPause()
    }
}