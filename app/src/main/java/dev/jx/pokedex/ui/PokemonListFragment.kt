package dev.jx.pokedex.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.SimpleItemAnimator
import androidx.transition.TransitionInflater
import dagger.hilt.android.AndroidEntryPoint
import dev.jx.pokedex.R
import dev.jx.pokedex.databinding.FragmentPokemonListBinding
import dev.jx.pokedex.model.Pokemon
import dev.jx.pokedex.ui.adapter.PokemonListAdapter
import dev.jx.pokedex.ui.animation.BounceEdgeEffectFactory
import dev.jx.pokedex.ui.state.ViewState
import dev.jx.pokedex.ui.viewmodel.PokemonListViewModel

@AndroidEntryPoint
class PokemonListFragment : Fragment() {

    private lateinit var binding: FragmentPokemonListBinding
    private val viewModel by viewModels<PokemonListViewModel>()
    private val pokemonListAdapter by lazy {
        PokemonListAdapter(::goToDetails).apply {
            setHasStableIds(true)
        }
    }

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

        (binding.pokemonList.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false

        viewModel.pokemonList.observe(viewLifecycleOwner) { result ->
            when (result) {
                is ViewState.Success -> onSuccess(result)
                is ViewState.Error -> onFailure(result)
                is ViewState.Loading -> onLoading(true)
            }
        }

        if (viewModel.recyclerState == RecyclerState.EMPTY) viewModel.getPokemonList()
    }

    private fun onSuccess(result: ViewState.Success<List<Pokemon>>) {
        onLoading(false)
//        if (viewModel.recyclerState == RecyclerState.EMPTY) {
            viewModel.recyclerState = RecyclerState.POPULATED
            pokemonListAdapter.setPokemonList(result.value!!)
            binding.pokemonList.scheduleLayoutAnimation()
//        }

    }

    private fun onFailure(result: ViewState.Error<List<Pokemon>>) {
        onLoading(false)
        pokemonListAdapter.clearPokemonList()
    }

    private fun onLoading(b: Boolean) {
        binding.loading.visibility = if (b) View.VISIBLE else View.GONE
    }

    private fun goToDetails(pokemons: List<Pokemon>, pos: Int, sharedImage: ImageView) {
        val extras = FragmentNavigatorExtras(sharedImage to sharedImage.transitionName)
        findNavController().navigate(
            PokemonListFragmentDirections
                .actionPokemonListFragmentToFragmentPokemonDetail(
                    pokemons.toTypedArray(),
                    pos,
                    sharedImage.transitionName
                ), extras
        )
    }

    enum class RecyclerState {
        POPULATED, EMPTY
    }

}
