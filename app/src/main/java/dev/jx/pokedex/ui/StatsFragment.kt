package dev.jx.pokedex.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import dev.jx.pokedex.databinding.FragmentStatsBinding
import dev.jx.pokedex.model.Pokemon
import dev.jx.pokedex.ui.viewmodel.PokemonDetailViewModel

class StatsFragment : Fragment() {

    private lateinit var binding: FragmentStatsBinding
    private val viewModel: PokemonDetailViewModel by lazy {
        ViewModelProvider(requireActivity()).get(PokemonDetailViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStatsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.pokemonSelected.observe(viewLifecycleOwner) {
            onPokemonSelected(it.value!!)
        }

    }

    private fun onPokemonSelected(pokemon: Pokemon) = with(binding) {
        hp.text = pokemon.stats.hp.toString()
        attack.text = pokemon.stats.attack.toString()
        defense.text = pokemon.stats.defense.toString()
        spatk.text = pokemon.stats.espAttack.toString()
        spdef.text = pokemon.stats.espDefense.toString()
        speed.text = pokemon.stats.speed.toString()
        total.text = pokemon.stats.totalPoints.toString()

        hpBar.progress = pokemon.stats.hp
        attackBar.progress = pokemon.stats.attack
        defenseBar.progress = pokemon.stats.defense
        spatkBar.progress = pokemon.stats.espAttack
        spdefBar.progress = pokemon.stats.espDefense
        speedBar.progress = pokemon.stats.speed
        totalBar.progress = (pokemon.stats.totalPoints * 100) / 600
    }

}
