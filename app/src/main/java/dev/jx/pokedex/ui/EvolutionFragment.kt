package dev.jx.pokedex.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import dev.jx.pokedex.databinding.FragmentEvolutionBinding
import dev.jx.pokedex.model.Pokemon
import dev.jx.pokedex.ui.viewmodel.PokemonDetailViewModel
import dev.jx.pokedex.util.loadPokemonImageWithHolder

class EvolutionFragment : Fragment() {

    lateinit var binding: FragmentEvolutionBinding
    private val viewModel: PokemonDetailViewModel by lazy {
        ViewModelProvider(requireActivity()).get(PokemonDetailViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEvolutionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.pokemonSelected.observe(viewLifecycleOwner) {
            onPokemonSelected(it.value!!)
        }
    }

    private fun onPokemonSelected(pokemon: Pokemon) = with(binding) {
        val evolutionChain = pokemon.specie.evolutions?.sortedBy { it.order }
        listOf(evoOneName, evoTwoName, evoThreeName, evoFourName).forEach { it.text = "" }
        listOf(evoOne, evoTwo, evoThree, evoFour).forEach { it.setImageResource(0) }

        if (evolutionChain != null && evolutionChain.size >= 3) {
            evoOneName.text = evolutionChain[0].name
            evoTwoName.text = evolutionChain[1].name
            evoThreeName.text = evolutionChain[1].name
            evoFourName.text = evolutionChain[2].name
            evoOne.loadPokemonImageWithHolder(evolutionChain[0].pokemonId)
            evoTwo.loadPokemonImageWithHolder(evolutionChain[1].pokemonId)
            evoThree.loadPokemonImageWithHolder(evolutionChain[1].pokemonId)
            evoFour.loadPokemonImageWithHolder(evolutionChain[2].pokemonId)
        } else if (evolutionChain?.size == 2) {
            evoOneName.text = evolutionChain[0].name
            evoTwoName.text = evolutionChain[1].name
            evoOne.loadPokemonImageWithHolder(evolutionChain[0].pokemonId)
            evoTwo.loadPokemonImageWithHolder(evolutionChain[1].pokemonId)
        }
    }

}
