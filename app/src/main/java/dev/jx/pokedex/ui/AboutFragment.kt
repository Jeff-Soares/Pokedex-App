package dev.jx.pokedex.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import dev.jx.pokedex.databinding.FragmentAboutBinding
import dev.jx.pokedex.model.Pokemon
import dev.jx.pokedex.ui.viewmodel.PokemonDetailViewModel
import kotlin.math.floor
import kotlin.math.round

class AboutFragment : Fragment() {

    lateinit var binding: FragmentAboutBinding
    private val viewModel: PokemonDetailViewModel by lazy {
        ViewModelProvider(requireActivity()).get(PokemonDetailViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAboutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.pokemonSelected.observe(viewLifecycleOwner) {
            onPokemonSelected(it.value!!)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun onPokemonSelected(pokemon: Pokemon) = with(binding) {
        description.text = pokemon.flavorText
        growthrate.text = pokemon.specie.growthrate
        abilities.text = pokemon.abilities?.joinToString { it.name }
        shape.text = pokemon.specie.shape
        egggroup.text = pokemon.specie.eggGroup?.joinToString { it.name }
        habitat.text = pokemon.specie.habitat
        weaknesses.text = pokemon.typeWeaknesses.joinToString(" - ") { it.name }

        val heightCm = pokemon.height * 10f
        val heightInch = round(heightCm / 2.54)
        val heightFeet = floor(heightInch / 12f)
        val heightFtIn = (heightInch - (12 * heightFeet)).toInt()
        val weightKg = pokemon.weight / 10f
        val wightLb = weightKg * 2.2046
        // Display 2'04" (0.70 cm)
        height.text = "%.0f\'%02d\" (%.2f cm)".format(heightFeet, heightFtIn, heightCm/100)
        // Display 15.2lbs (6.9 kg)
        weight.text = "%.1f lbs (%.1f kg)".format(wightLb, weightKg)
    }

}
