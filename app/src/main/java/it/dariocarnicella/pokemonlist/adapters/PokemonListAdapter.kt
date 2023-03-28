package it.dariocarnicella.pokemonlist.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import it.dariocarnicella.pokemonlist.databinding.ListItemBinding
import it.dariocarnicella.pokemonlist.ui.models.PokemonResult
import java.util.*
import kotlin.collections.ArrayList

class PokemonListAdapter(private val clickListener: (PokemonResult) -> Unit) :
    RecyclerView.Adapter<PokemonListAdapter.PokemonListViewHolder>(), Filterable {

    private var pokeList: ArrayList<PokemonResult> = ArrayList()
    private var pokeListFilter: ArrayList<PokemonResult> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonListViewHolder {
        val binding = ListItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return PokemonListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PokemonListViewHolder, position: Int) {
        val pokemonList = pokeList[position]
        holder.bind(pokemonList, clickListener)
    }

    override fun getItemCount(): Int = pokeList.size

    fun addData(list: List<PokemonResult>) {
        pokeList = list as ArrayList<PokemonResult>
        pokeListFilter = pokeList
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence?): FilterResults {
                val filterResults = FilterResults()
                if (charSequence == null || charSequence.length < 0) {
                    filterResults.count = pokeListFilter.size
                    filterResults.values = pokeListFilter
                } else {
                    val searchChar = charSequence.toString().toLowerCase(Locale.ROOT)
                    val listPoke = ArrayList<PokemonResult>()
                    for (item in pokeListFilter) {
                        if (item.name.contains(searchChar)) {
                            listPoke.add(item)
                        }
                    }
                    filterResults.count = listPoke.size
                    filterResults.values = listPoke
                }
                return filterResults
            }

            override fun publishResults(
                charSequence: CharSequence?,
                filterResults: FilterResults?
            ) {
                pokeList = filterResults!!.values as ArrayList<PokemonResult>
                notifyDataSetChanged()
            }
        }
    }

    class PokemonListViewHolder(private val binding: ListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("CheckResult")
        fun bind(pokemonResult: PokemonResult, clickListener: (PokemonResult) -> Unit) {
            binding.tvName.text = pokemonResult.name.toUpperCase(Locale.ROOT)
            Glide.with(binding.pokeImg)
                .load(pokemonResult.getPokemonImage())
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(binding.pokeImg)
            binding.root.setOnClickListener {
                clickListener(pokemonResult)
            }
        }
    }
}

