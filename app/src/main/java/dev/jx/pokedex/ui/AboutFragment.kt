package dev.jx.pokedex.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import dev.jx.pokedex.databinding.FragmentAboutBinding
import dev.jx.pokedex.model.Pokemon
import dev.jx.pokedex.ui.viewmodel.PokemonDetailViewModel

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

    private fun onPokemonSelected(pokemon: Pokemon) = with(binding) {
        growthrate.text = pokemon.specie.growthrate
        height.text = pokemon.height.toString()
        weight.text = pokemon.weight.toString()
        abilities.text = pokemon.abilities?.joinToString { "${it.name} " }
        shape.text = pokemon.specie.shape
        egggroup.text = pokemon.specie.eggGroup?.joinToString { "${it.name} " }
        habitat.text = pokemon.specie.habitat
    }

}
