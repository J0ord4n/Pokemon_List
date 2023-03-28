package it.dariocarnicella.pokemonlist.adapters

import android.animation.ObjectAnimator
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import it.dariocarnicella.pokemonlist.databinding.ListItemStatBinding
import it.dariocarnicella.pokemonlist.ui.models.Stat
import kotlinx.android.synthetic.main.list_item_stat.view.*
import java.util.*

class PokemonDetailAdapter(private val statList: List<Stat>) :
    RecyclerView.Adapter<PokemonDetailViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonDetailViewHolder {
        val binding = ListItemStatBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return PokemonDetailViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PokemonDetailViewHolder, position: Int) {
        val pokeStat = statList[position]
        val max = statList.maxByOrNull { it.base_stat }
        val progressStat = holder.itemView.pbStat
        val currentProgress = pokeStat.base_stat
        progressStat.max = max?.base_stat ?: 200
        holder.itemView.statName.text = pokeStat.stat.name.toUpperCase(Locale.ROOT)
        holder.itemView.itemNumber.text = pokeStat.base_stat.toString()
        ObjectAnimator.ofInt(progressStat, "progress", currentProgress)
            .setDuration(1000)
            .start()
    }

    override fun getItemCount(): Int = statList.size
}

class PokemonDetailViewHolder(binding: ListItemStatBinding) : RecyclerView.ViewHolder(binding.root)