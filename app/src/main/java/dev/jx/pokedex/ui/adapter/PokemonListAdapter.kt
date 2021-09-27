package dev.jx.pokedex.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import dev.jx.pokedex.R
import dev.jx.pokedex.databinding.PokemonItemBinding
import dev.jx.pokedex.model.Pokemon
import dev.jx.pokedex.ui.color.Color
import dev.jx.pokedex.util.changeBgColor
import dev.jx.pokedex.util.loadPokemonImageWithHolder

@SuppressLint("NotifyDataSetChanged")
class PokemonListAdapter(private val onClick: (List<Pokemon>, Int, ImageView) -> Unit) :
    RecyclerView
    .Adapter<PokemonListAdapter.PokemonViewHolder>() {

    private var pokemonList: MutableList<Pokemon> = mutableListOf()
    private var pokemonListAll: MutableList<Pokemon> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder {
        val binding = PokemonItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PokemonViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
        holder.bind(pokemonList[position], position)
    }

    override fun getItemCount(): Int {
        return pokemonList.size
    }

    override fun getItemId(position: Int): Long {
        return pokemonList[position].id.toLong()
    }

    inner class PokemonViewHolder(private val binding: PokemonItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(pokemon: Pokemon, pos: Int) = with(binding) {
            id.text =
                binding.root.resources.getString(R.string.formattedId, pokemon.formattedId)
            name.text = pokemon.name

            typeName1.text = pokemon.type.first().name
            if (pokemon.type.size > 1) {
                typeName2.text = pokemon.type[1].name
                typeName2.visibility = View.VISIBLE
            } else {
                typeName2.visibility = View.GONE
            }

            binding.image.loadPokemonImageWithHolder(pokemon.id)

            changeItemPrimaryColor(pokemon)
            binding.image.transitionName = "sharedImage_${pokemon.id}"
            root.setOnClickListener { onClick.invoke(pokemonList.toList(), pos, binding.image) }

        }

        private fun changeItemPrimaryColor(pokemon: Pokemon) = with(binding) {
            val black = root.resources.getColor(Color.BLACK.id, root.context.theme)
            val white = root.resources.getColor(Color.WHITE.id, root.context.theme)
            itemLayout.changeBgColor(pokemon.specie.color)

            // For color contrast
            if (pokemon.specie.color == Color.WHITE) {
                name.setTextColor(black)
                id.setTextColor(black)
                typeName1.setTextColor(black)
                typeName2.setTextColor(black)
                typeName1.setBackgroundResource(R.drawable.bg_type_item_black)
                typeName2.setBackgroundResource(R.drawable.bg_type_item_black)
            } else {
                name.setTextColor(white)
                id.setTextColor(white)
                typeName1.setTextColor(white)
                typeName2.setTextColor(white)
                typeName1.setBackgroundResource(R.drawable.bg_type_item_white)
                typeName2.setBackgroundResource(R.drawable.bg_type_item_white)
            }
        }


    }

    fun setPokemonList(list: List<Pokemon>) {
        pokemonList = list.toMutableList()
        pokemonListAll = list.toMutableList()
        notifyDataSetChanged()
    }

    fun clearPokemonList() {
        pokemonList.clear()
        pokemonListAll.clear()
        notifyDataSetChanged()
    }

    fun setFilter(str: String?) {
        pokemonList =
            if (str.isNullOrEmpty()) pokemonListAll else pokemonListAll.filter { pokemon ->
                pokemon.name.contains(str, true)
            }.toMutableList()
        notifyDataSetChanged()
    }

}
