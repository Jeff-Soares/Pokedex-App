package dev.jx.pokedex.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import dev.jx.pokedex.data.PokedexRepository
import dev.jx.pokedex.databinding.FragmentPokemonListBinding
import dev.jx.pokedex.model.Err
import dev.jx.pokedex.model.Ok
import dev.jx.pokedex.ui.adapter.PokemonListAdapter
import dev.jx.pokedex.ui.animation.BounceEdgeEffectFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class PokemonListFragment : Fragment() {

    @Inject
    lateinit var repo: PokedexRepository

    private lateinit var binding: FragmentPokemonListBinding
    private val pokemonListAdapter by lazy { PokemonListAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPokemonListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.pokemonList.apply {
            adapter = pokemonListAdapter
            edgeEffectFactory = BounceEdgeEffectFactory()
        }

        CoroutineScope(Dispatchers.IO).launch {
            val result = repo.queryPokemonList()

            launch(Dispatchers.Main) {
                when (result) {
                    is Ok -> pokemonListAdapter.setPokemonList(result.value!!)
                    is Err -> Log.d("Error", result.error.toString())
                }
            }
        }

    }

}