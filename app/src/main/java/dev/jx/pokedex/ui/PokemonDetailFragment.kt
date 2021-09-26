package dev.jx.pokedex.ui

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.transition.TransitionInflater
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayout
import dev.jx.pokedex.R
import dev.jx.pokedex.databinding.FragmentPokemonDetailBinding
import dev.jx.pokedex.model.Pokemon
import dev.jx.pokedex.ui.adapter.PokemonInfoAdapter
import dev.jx.pokedex.ui.adapter.PokemonSliderAdapter
import dev.jx.pokedex.ui.animation.ZoomOutPageTransformer
import dev.jx.pokedex.ui.color.Color
import dev.jx.pokedex.ui.viewmodel.PokemonDetailViewModel
import dev.jx.pokedex.util.fadeBgColor
import java.security.InvalidParameterException

class PokemonDetailFragment : Fragment() {

    lateinit var binding: FragmentPokemonDetailBinding
    private val args by navArgs<PokemonDetailFragmentArgs>()
    private val viewModel: PokemonDetailViewModel by lazy {
        ViewModelProvider(requireActivity()).get(PokemonDetailViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPokemonDetailBinding.inflate(inflater, container, false)
        sharedElementEnterTransition = TransitionInflater.from(binding.root.context)
            .inflateTransition(R.transition.image_shared_element_transition)
        sharedElementReturnTransition = null
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // For shared element transition
        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }

        viewModel.setPokemonList(args.pokemon.toList())
        viewModel.onPokemonChange(args.pos)
        binding.pokeballLogo.startAnimation(
            AnimationUtils.loadAnimation(
                context,
                R.anim.infinite_rotation
            )
        )

        setupToolbar()
        setupCollapsingToolbar()
        setupViewPager()
        setupImageSlider()
        setPokemonInfo(args.pokemon[args.pos])

    }

    private fun setupImageSlider() = with(binding) {
        pokemonImage.setPageTransformer(ZoomOutPageTransformer())
        pokemonImage.adapter = PokemonSliderAdapter(args.pokemon.toList())
        pokemonImage.setCurrentItem(args.pos, false)
        pokemonImage.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                setPokemonInfo(args.pokemon[position])
                viewModel.onPokemonChange(position)
                super.onPageSelected(position)
            }
        })
    }

    private fun setupViewPager() = with(binding) {
        viewPagerInfo.adapter = PokemonInfoAdapter(requireActivity().supportFragmentManager, lifecycle)
        tabLayoutInfo.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab != null) {
                    viewPagerInfo.currentItem = tab.position
                }
            }
        })
        viewPagerInfo.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                tabLayoutInfo.selectTab(tabLayoutInfo.getTabAt(position))
            }
        })
    }

    private fun setupToolbar() {
        (activity as AppCompatActivity).run {
            setSupportActionBar(binding.toolbarDetail)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setDisplayShowTitleEnabled(true)
            supportActionBar?.title = args.pokemon[args.pos].name
        }
    }

    private fun setupCollapsingToolbar() {
        binding.appbar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener
        { appBarLayout, verticalOffset ->
            val offsetScale = mapOffSet(appBarLayout.totalScrollRange, verticalOffset, 1f)
            val offsetAlpha70 = mapOffSet(appBarLayout.totalScrollRange, verticalOffset, 0.7f)
            val offsetAlpha20 = mapOffSet(appBarLayout.totalScrollRange, verticalOffset, 0.2f)

            ObjectAnimator.ofFloat(binding.pokemonImage, "scaleX", offsetScale)
                .setDuration(0).start()
            ObjectAnimator.ofFloat(binding.pokemonImage, "scaleY", offsetScale)
                .setDuration(0).start()
            ObjectAnimator.ofFloat(
                binding.pokemonImage,
                "translationY",
                verticalOffset.toFloat()
            )
                .setDuration(0).start()
            binding.pokemonImage.alpha = if (offsetAlpha70 < 0f) 0f else offsetAlpha70
            binding.pokemonId.alpha = if (offsetAlpha20 < 0f) 0f else offsetAlpha20
            binding.pokemonType1.alpha = if (offsetAlpha20 < 0f) 0f else offsetAlpha20
            binding.pokemonType2.alpha = if (offsetAlpha20 < 0f) 0f else offsetAlpha20

            // Disable pokemon slider when toolbar collapsed
            binding.pokemonImage.isUserInputEnabled = (verticalOffset == 0)
        })
    }

    private fun setPokemonInfo(pokemon: Pokemon) = with(binding) {
        collapsingToolbar.title = pokemon.name
        pokemonId.text =
            root.resources.getString(R.string.formattedId, pokemon.formattedId)
        pokemonLayout.fadeBgColor(pokemon.specie.color)

        pokemonType1.text = pokemon.type.first().name
        if (pokemon.type.size > 1) {
            pokemonType2.text = pokemon.type[1].name
            pokemonType2.visibility = View.VISIBLE
        } else {
            pokemonType2.visibility = View.GONE
        }

        // For color contrast
        val black = root.resources.getColor(Color.BLACK.id, root.context.theme)
        val white = root.resources.getColor(Color.WHITE.id, root.context.theme)
        if (pokemon.specie.color == Color.WHITE) {
            collapsingToolbar.setExpandedTitleColor(black)
            collapsingToolbar.setCollapsedTitleTextColor(black)
            pokemonId.setTextColor(black)
            pokemonType1.setTextColor(black)
            pokemonType2.setTextColor(black)
            pokemonType1.setBackgroundResource(R.drawable.bg_type_item_black)
            pokemonType2.setBackgroundResource(R.drawable.bg_type_item_black)
        } else {
            collapsingToolbar.setExpandedTitleColor(white)
            collapsingToolbar.setCollapsedTitleTextColor(white)
            pokemonId.setTextColor(white)
            pokemonType1.setTextColor(white)
            pokemonType2.setTextColor(white)
            pokemonType1.setBackgroundResource(R.drawable.bg_type_item_white)
            pokemonType2.setBackgroundResource(R.drawable.bg_type_item_white)
        }
    }

    private fun mapOffSet(totalSize: Int, offSet: Int, percent: Float): Float {
        if (percent !in 0.0f..1.0f) throw InvalidParameterException("Invalid range")
        val newSize = totalSize * percent
        val newOffSet = mapRange(offSet, inStart = newSize, outStart = totalSize.toFloat())
        return (newOffSet / totalSize) + 1
    }

    private fun mapRange(
        value: Int, inEnd: Float = 0f, inStart: Float, outEnd: Float = 0f, outStart: Float
    ) = outStart + ((outEnd - outStart) / (inEnd - inStart)) * (value - inStart)

}