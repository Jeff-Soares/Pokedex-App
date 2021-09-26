package dev.jx.pokedex.ui.animation

import android.view.View
import androidx.viewpager2.widget.ViewPager2
import kotlin.math.abs
import kotlin.math.max

private const val MIN_SCALE = 0.7f
private const val MIN_ALPHA = 0.5f

class ZoomOutPageTransformer : ViewPager2.PageTransformer {

    override fun transformPage(view: View, position: Float) {
        val pageWidth = view.width
        val pageHeight = view.height

        view.alpha = when {
            position < -1 -> 0f
            position <= 1 -> {
                val scaleFactor = max(MIN_SCALE, 1 - abs(position))
                val vertMargin = pageHeight * (1 - scaleFactor) / 2
                val horzMargin = pageWidth * (1 - scaleFactor) / 2

                view.translationX =
                    if (position < 0) horzMargin - vertMargin / 2 else -horzMargin + vertMargin / 2

                view.scaleX = scaleFactor
                view.scaleY = scaleFactor

                MIN_ALPHA + (scaleFactor - MIN_SCALE) / (1 - MIN_SCALE) * (1 - MIN_ALPHA)
            }
            else -> 0f
        }
    }

}
