package dev.jx.pokedex.util

import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import dev.jx.pokedex.BuildConfig
import dev.jx.pokedex.ui.color.Color
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.graphics.drawable.ColorDrawable
import android.widget.ProgressBar
import com.bumptech.glide.Priority
import dev.jx.pokedex.R


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

fun ProgressBar.setAnimatedProgress(value: Int) {
    ObjectAnimator.ofInt(this, "progress", value)
        .setDuration(500)
        .start()
}

fun ImageView.loadPokemonImage(id: Int){
    val options = RequestOptions()
        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
        .priority(Priority.HIGH)
        .format(DecodeFormat.PREFER_RGB_565)

    Glide.with(this.context)
        .applyDefaultRequestOptions(options)
        .load("${BuildConfig.POKE_IMAGE_URL}$id.png")
        .placeholder(R.drawable.whoisthatpokemon)
        .thumbnail(0.4f)
        .into(this)
}
