package dev.jx.pokedex.util

import android.graphics.drawable.Drawable
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import dev.jx.pokedex.BuildConfig
import dev.jx.pokedex.ui.color.Color
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import android.animation.ArgbEvaluator

import android.animation.ObjectAnimator
import android.graphics.drawable.ColorDrawable


fun ViewGroup.changeBgColor(color: Color) {
    setBackgroundColor(resources.getColor(color.id, context.theme))
}

fun ViewGroup.fadeBgColor(color: Color) {
    ObjectAnimator.ofObject(
        this, "backgroundColor", ArgbEvaluator(),
        (background as ColorDrawable).color, // From color
        resources.getColor(color.id, context.theme) // To color
    ).run {
        duration = 300
        start()
    }
}

fun ImageView.loadPokemonImage(id: Int) {
    Glide.with(this.context)
        .load("${BuildConfig.POKE_IMAGE_URL}$id.png")
//        .centerInside()
//        .placeholder(R.drawable.whoisthatpokemon)
        .dontAnimate()
        .into(this)
}

//fun loadImage(){
//    val options = RequestOptions()
//        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
//        .centerCrop()
//        .priority(Priority.HIGH)
//        .format(DecodeFormat.PREFER_RGB_565)
//
//    Glide.with(context)
//        .applyDefaultRequestOptions(options)
//        .load(slider1Item.getImageURL())
//        .thumbnail(0.4f)
//        .into<Target<Drawable>>(imageView)
//}
