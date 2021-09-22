package dev.jx.pokedex.util

import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.squareup.picasso.Picasso
import dev.jx.pokedex.BuildConfig
import dev.jx.pokedex.R
import dev.jx.pokedex.ui.color.Color

fun ViewGroup.changeBgColor(color: Color) {
    setBackgroundColor(resources.getColor(color.id, context.theme))
}

//fun ImageView.loadPokemonImage1(id: Int) {
//    Picasso.get()
//        .load("${BuildConfig.POKE_IMAGE_URL}$id.png")
//        .fit()
//        .centerCrop()
//        .placeholder(R.drawable.whoisthatpokemon)
//        .into(this)
//}

fun ImageView.loadPokemonImage(id: Int) {
    Glide.with(this.context)
        .load("${BuildConfig.POKE_IMAGE_URL}$id.png")
        .centerCrop()
        .placeholder(R.drawable.whoisthatpokemon)
        .dontAnimate()
        .into(this)
}


