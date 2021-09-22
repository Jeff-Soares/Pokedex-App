package dev.jx.pokedex.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.scopes.FragmentScoped
import dev.jx.pokedex.data.PokedexRepository
import dev.jx.pokedex.data.PokedexRepositoryImpl


//Just for test, will be replaced by ViewModelModule
@Module
@InstallIn(FragmentComponent::class)
abstract class FragmentModule {

    @Binds
    @FragmentScoped
    abstract fun bindRepo(repo: PokedexRepositoryImpl): PokedexRepository

}