package dev.jx.pokedex.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import dev.jx.pokedex.R
import dev.jx.pokedex.model.Pokemon
import dev.jx.pokedex.util.loadPokemonImage

class PokemonSliderAdapter(private val pokemons: List<Pokemon>) :
    RecyclerView.Adapter<PokemonSliderAdapter.SliderViewHolder>() {

    inner class SliderViewHolder(private val itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        fun bind (pokemon: Pokemon) = with(itemView){
            val image = findViewById<ImageView>(R.id.slider_image)
            image.transitionName = "sharedImage_${pokemon.id}"
            image.loadPokemonImage(pokemon.id)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonSliderAdapter
    .SliderViewHolder {
        return SliderViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.image_slide,
            parent, false))
    }

    override fun onBindViewHolder(holder: PokemonSliderAdapter.SliderViewHolder, position: Int) {
        holder.bind(pokemons[position])
    }

    override fun getItemCount() = pokemons.size

}