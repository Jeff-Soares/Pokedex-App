package dev.jx.pokedex.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import dev.jx.pokedex.ui.AboutFragment
import dev.jx.pokedex.ui.EvolutionFragment
import dev.jx.pokedex.ui.StatsFragment

class PokemonInfoAdapter(
    private val fragmentManager: FragmentManager, private val lifeCycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifeCycle) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        when (position){
            1 -> return StatsFragment()
            2 -> return EvolutionFragment()
        }
        return AboutFragment()
    }

}