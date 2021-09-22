package dev.jx.pokedex.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import dev.jx.pokedex.data.PokedexRepository
import dev.jx.pokedex.databinding.FragmentPokemonListBinding
import dev.jx.pokedex.model.Err
import dev.jx.pokedex.model.Ok
import dev.jx.pokedex.model.Pokemon
import dev.jx.pokedex.ui.adapter.PokemonListAdapter
import dev.jx.pokedex.ui.animation.BounceEdgeEffectFactory
import dev.jx.pokedex.ui.state.ViewState
import dev.jx.pokedex.ui.viewmodel.PokemonListViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class PokemonListFragment : Fragment() {

    private lateinit var binding: FragmentPokemonListBinding
    private val viewModel by viewModels<PokemonListViewModel>()
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

        viewModel.pokemonList.observe(viewLifecycleOwner) { result ->
            when (result) {
                is ViewState.Success -> onSuccess(result)
                is ViewState.Error -> onFailure(result)
                is ViewState.Loading -> onLoading(true)
            }
        }

        viewModel.getPokemonList()
    }

    private fun onSuccess(result: ViewState.Success<List<Pokemon>>) {
        onLoading(false)
        pokemonListAdapter.setPokemonList(result.value!!)
        binding.pokemonList.scheduleLayoutAnimation()
    }

    private fun onFailure(result: ViewState.Error<List<Pokemon>>) {
        onLoading(false)
        pokemonListAdapter.clearPokemonList()
    }

    private fun onLoading(b: Boolean) {
        binding.loading.visibility = if (b) View.VISIBLE else View.GONE
    }

}
