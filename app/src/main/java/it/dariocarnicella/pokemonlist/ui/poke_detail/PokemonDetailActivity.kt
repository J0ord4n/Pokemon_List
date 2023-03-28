package it.dariocarnicella.pokemonlist.ui.poke_detail

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import it.dariocarnicella.pokemonlist.R
import it.dariocarnicella.pokemonlist.adapters.PokemonDetailAdapter
import it.dariocarnicella.pokemonlist.databinding.ActivityPokemonDetailBinding
import it.dariocarnicella.pokemonlist.repository.PokemonRepository
import kotlinx.android.synthetic.main.activity_pokemon_detail.*
import java.io.*
import java.util.*

class PokemonDetailActivity : AppCompatActivity() {

    private lateinit var viewModel: PokemonDetailViewModel
    private lateinit var binding: ActivityPokemonDetailBinding
    private var REQUEST_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPokemonDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initPokeVM()
        initPokeRV()
        saveToGallery()
    }

    private fun initPokeVM() {
        val pokemonRepository = PokemonRepository()
        val viewModelDetailProviderFactory = PokemonDetailViewModelFactory(pokemonRepository)
        viewModel =
            ViewModelProvider(
                this@PokemonDetailActivity,
                viewModelDetailProviderFactory
            )[PokemonDetailViewModel::class.java]
    }

    @SuppressLint("SetTextI18n")
    private fun initPokeRV() {
        val id = intent.extras?.get("id") as String
        viewModel.getPokemonInfo(id)
        viewModel.pokemonInfo.observe(this, { pokemon ->
            namePokemon.text = pokemon.name!!.toUpperCase(Locale.ROOT)
            rvStat.layoutManager = GridLayoutManager(this, 2)
            rvStat.adapter = PokemonDetailAdapter(pokemon.stats)
            pokemon.types.forEach {
                typeText.text =
                    typeText.text.toString() + " " + it.type.name.toUpperCase(Locale.ROOT)
            }
            Glide.with(this).load(pokemon.sprites.frontDefault).into(pokemonImg)
        })
    }

    private fun saveToGallery() {
        pokemonImg.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Salva il tuo pokémon")
            builder.setMessage("Vuoi salvare l'immagine del pokémon?")
            builder.setIcon(R.drawable.ic_info)
            builder.setPositiveButton("Sì") { _, _ ->
                if (ContextCompat.checkSelfPermission(
                        this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                    == PackageManager.PERMISSION_GRANTED
                ) {
                    saveImage()
                } else {
                    ActivityCompat.requestPermissions(
                        this, arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                        REQUEST_CODE
                    )
                }
            }
            builder.setNegativeButton("No") { dialogInterface, _ ->
                dialogInterface.cancel()
            }
            val alertDialog = builder.create()
            alertDialog.show()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                saveImage()
            } else {
                Toast.makeText(
                    this, "Per favore, provvedi a " +
                            "concedere i permessi richiesti", Toast.LENGTH_SHORT
                ).show()
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun saveImage() {
        val contentResolver = contentResolver
        val images: Uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        } else {
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        }

        val contentValues = ContentValues()
        contentValues.put(
            MediaStore.Images.Media.DISPLAY_NAME,
            "${System.currentTimeMillis()}" + "jpg"
        )
        contentValues.put(MediaStore.Images.Media.MIME_TYPE, "images/*")
        val uri: Uri? = contentResolver.insert(images, contentValues)

        try {
            val bitmapDrawable: BitmapDrawable = pokemonImg.drawable as BitmapDrawable
            val bitmap: Bitmap = bitmapDrawable.bitmap

            val outPutStream: OutputStream? =
                contentResolver.openOutputStream(Objects.requireNonNull(uri)!!)

            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outPutStream)
            Objects.requireNonNull(outPutStream)

            Toast.makeText(this, "Immagine salvata correttamente!", Toast.LENGTH_SHORT).show()

        } catch (e: Exception) {
            Toast.makeText(this, "Immagine non salvata", Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }
    }
}