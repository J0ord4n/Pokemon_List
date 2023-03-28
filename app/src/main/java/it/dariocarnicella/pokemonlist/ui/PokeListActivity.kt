package it.dariocarnicella.pokemonlist.ui

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.*
import android.net.ConnectivityManager
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.View.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import it.dariocarnicella.pokemonlist.R
import it.dariocarnicella.pokemonlist.adapters.PokemonListAdapter
import it.dariocarnicella.pokemonlist.databinding.ActivityPokeListBinding
import it.dariocarnicella.pokemonlist.receiver.PokeNotificationReceiver
import it.dariocarnicella.pokemonlist.repository.PokemonRepository
import it.dariocarnicella.pokemonlist.ui.poke_detail.PokemonDetailActivity
import kotlinx.android.synthetic.main.activity_poke_list.*

@Suppress("DEPRECATION")
class PokeListActivity : AppCompatActivity() {

    private lateinit var viewModel: PokemonViewModel
    private lateinit var pokemonAdapter: PokemonListAdapter
    private lateinit var binding: ActivityPokeListBinding
    private lateinit var broadcastReceiver: BroadcastReceiver
    private lateinit var wifiManager: WifiManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPokeListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        wifiManager = this.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        broadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent) {
                val checkInternet = intent.getBooleanExtra(
                    ConnectivityManager.EXTRA_NO_CONNECTIVITY,
                    false
                )
                if (checkInternet) {
                    hidePokeRV()
                    internetDialog()
                } else {
                    initPokeVM()
                    initPokeRV()
                }
            }
        }
        createNotificationChannel()
    }

    override fun onStart() {
        super.onStart()
        registerReceiver(broadcastReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(broadcastReceiver)

    }

    override fun onDestroy() {
        super.onDestroy()
        startNotification()
    }

    private fun initPokeVM() {
        val pokemonRepository = PokemonRepository()
        val viewModelProviderFactory = PokemonViewModelProviderFactory(pokemonRepository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory)[PokemonViewModel::class.java]
    }

    private fun initPokeRV() {
        binding.prIndicator.visibility = VISIBLE
        pokemonAdapter = PokemonListAdapter {
            val intent = Intent(this@PokeListActivity, PokemonDetailActivity::class.java)
            intent.putExtra("id", it.name)
            startActivity(intent)
        }
        rvPoke.apply {
            adapter = pokemonAdapter
            layoutManager = GridLayoutManager(this@PokeListActivity, 2)
        }
        viewModel.pokemonList.observe(this, { response ->
            response.results.let {
                pokemonAdapter.addData(response.results)
                binding.prIndicator.visibility = GONE
            }
        })
    }

    private fun hidePokeRV() {
        binding.prIndicator.visibility = INVISIBLE
        binding.rvPoke.visibility = INVISIBLE
    }

    private fun internetDialog() {
        val builder = AlertDialog.Builder(this@PokeListActivity)
        builder.setIcon(R.drawable.ic_warning)
        builder.setTitle("No Internet")
        builder.setMessage(
            "Per favore, controlla che il dispositivo sia " +
                    "connesso ad internet"
        )
        builder.setPositiveButton("Abilita WI-Fi E RIPROVA") { _, _ ->
            wifiManager.isWifiEnabled = true
            finish()
            Handler().postDelayed({
                startActivity(intent)
            }, 2000)
        }
        builder.setNegativeButton("Annulla") { dialogInterface, _ ->
            dialogInterface.cancel()
        }
        val alertDialog = builder.create()
        alertDialog.show()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "PokemonListChannel"
            val description = "PokemonChannelNotification"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel =
                NotificationChannel("it.dariocarnicella.pokemonlist.channel1", name, importance)
            channel.description = description

            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun startNotification() {
        val intent = Intent(this, PokeNotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0)
        val alarmManager: AlarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val timer: Long = System.currentTimeMillis()
        val second = 1000 * 3
        alarmManager.set(AlarmManager.RTC_WAKEUP, timer + second, pendingIntent)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        val menuItem = menu!!.findItem(R.id.searchMenu)
        val searchView = menuItem.actionView as SearchView
        searchView.queryHint = "Cerca il tuo pokémon preferito..."
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                pokemonAdapter.filter.filter(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                pokemonAdapter.filter.filter(newText)
                return true
            }
        })
        return true
    }

    override fun onBackPressed() {
        val builder = AlertDialog.Builder(this@PokeListActivity)
        builder.setIcon(R.drawable.ic_exit)
        builder.setTitle("Esci")
        builder.setMessage("Vuoi davvero abbandonare Pokemon List?")
        builder.setPositiveButton("Si") { _, _ ->
            finish()
        }
        builder.setNegativeButton("No") { dialogInterface, _ ->
            dialogInterface.cancel()
        }
        val alertDialog = builder.create()
        alertDialog.show()
    }

}





