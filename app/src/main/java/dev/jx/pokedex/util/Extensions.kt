package dev.jx.pokedex.util

import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.content.Context
import android.content.res.Resources
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import dev.jx.pokedex.BuildConfig
import dev.jx.pokedex.R
import dev.jx.pokedex.ui.color.Color


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

fun ImageView.loadPokemonImageWithHolder(id: Int) {
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

fun ImageView.loadPokemonImage(id: Int) {
    Glide.with(this.context)
        .load("${BuildConfig.POKE_IMAGE_URL}$id.png")
        .centerInside()
        .dontAnimate()
        .into(this)
}

fun Any.toast(context: Context, duration: Int = Toast.LENGTH_SHORT): Toast {
    return Toast.makeText(context, this.toString(), duration).apply { show() }
}

fun DialogFragment.setWidthPercent(percentage: Int) {
    val percent = percentage.toFloat() / 100
    val dm = Resources.getSystem().displayMetrics
    val rect = dm.run { Rect(0, 0, widthPixels, heightPixels) }
    val percentWidth = rect.width() * percent
    dialog?.window?.setLayout(percentWidth.toInt(), ViewGroup.LayoutParams.WRAP_CONTENT)
}
