package dev.jx.pokedex.ui.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.jx.pokedex.R
import dev.jx.pokedex.databinding.PokemonItemBinding
import dev.jx.pokedex.model.Pokemon
import dev.jx.pokedex.ui.color.Color
import dev.jx.pokedex.util.changeBgColor
import dev.jx.pokedex.util.loadPokemonImage
import kotlinx.coroutines.*

@SuppressLint("NotifyDataSetChanged")
class PokemonListAdapter : RecyclerView.Adapter<PokemonListAdapter.PokemonViewHolder>() {

    private var pokemonList: MutableList<Pokemon> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder {
        val binding = PokemonItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PokemonViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
        holder.bind(pokemonList[position])
    }

    override fun getItemCount(): Int {
        return pokemonList.size
    }

    inner class PokemonViewHolder(private val binding: PokemonItemBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        fun bind(pokemon: Pokemon) = with(binding) {
            id.text =
                binding.root.resources.getString(R.string.formattedId, pokemon.formattedId)
            name.text = pokemon.name

            typeName1.text = pokemon.type.first().name
            typeName1.visibility = View.VISIBLE
            if (pokemon.type.size > 1) {
                typeName2.text = pokemon.type[1].name
                typeName2.visibility = View.VISIBLE
            } else {
                typeName2.visibility = View.GONE
            }

            CoroutineScope(Dispatchers.Main).launch {
                image.layout(0,0,0,0)
            }
            binding.image.loadPokemonImage(pokemon.id)

            changeItemPrimaryColor(pokemon)

            root.setOnClickListener(this@PokemonViewHolder)

        }

        override fun onClick(p0: View?) {
            val pos = adapterPosition
            Log.d("ViewHolderClick", pokemonList[pos].name)
        }

        private fun changeItemPrimaryColor(pokemon: Pokemon) = with(binding) {
            val black = root.resources.getColor(Color.BLACK.id, root.context.theme)
            val white = root.resources.getColor(Color.WHITE.id, root.context.theme)
            itemLayout.changeBgColor(pokemon.specie.color)

            if (pokemon.specie.color == Color.WHITE) {
                name.setTextColor(black)
                typeName1.setTextColor(black)
                typeName2.setTextColor(black)
                typeName1.setBackgroundResource(R.drawable.bg_type_item_black)
                typeName2.setBackgroundResource(R.drawable.bg_type_item_black)
            } else {
                name.setTextColor(white)
                typeName1.setTextColor(white)
                typeName2.setTextColor(white)
                typeName1.setBackgroundResource(R.drawable.bg_type_item_white)
                typeName2.setBackgroundResource(R.drawable.bg_type_item_white)
            }
        }
    }

    fun setPokemonList(list: List<Pokemon>) {
        pokemonList = list.toMutableList()
        notifyDataSetChanged()
    }

    fun clearPokemonList(){
        pokemonList.clear()
        notifyDataSetChanged()
    }
}
