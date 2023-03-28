package it.dariocarnicella.pokemonlist.receiver

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.TaskStackBuilder
import it.dariocarnicella.pokemonlist.R
import it.dariocarnicella.pokemonlist.ui.PokeListActivity
import it.dariocarnicella.pokemonlist.ui.splash_screen.PokeSplashScreen

@Suppress("NAME_SHADOWING")
class PokeNotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        val intent = Intent(context, PokeSplashScreen::class.java)
        val pendingIntent = TaskStackBuilder.create(context).run {
            addNextIntentWithParentStack(intent)
            getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
        }

        val builder: NotificationCompat.Builder =
            NotificationCompat.Builder(context, "it.dariocarnicella.pokemonlist.channel1")
                .setContentTitle("Pokemon List")
                .setContentText(
                    "Ops! Torna su Pokemon List per cercare il tuo " +
                            "pokémon preferito"
                )
                .setStyle(
                    NotificationCompat.BigTextStyle().bigText(
                        "Ops! Torna su Pokemon List per cercare il tuo " +
                                "pokémon preferito"
                    )
                )
                .setSmallIcon(R.drawable.ic_notifications)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .addAction(R.drawable.ic_launcher_foreground, "Riapri Pokemon List", pendingIntent)

        val notificationManager: NotificationManagerCompat = NotificationManagerCompat.from(context)
        notificationManager.notify(200, builder.build())
    }
}