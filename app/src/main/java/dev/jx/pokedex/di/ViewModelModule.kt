package dev.jx.pokedex.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import dev.jx.pokedex.data.PokedexRepository
import dev.jx.pokedex.data.PokedexRepositoryImpl

@Module
@InstallIn(ViewModelComponent::class)
abstract class ViewModelModule {

    @Binds
    @ViewModelScoped
    abstract fun bindRepository(repo: PokedexRepositoryImpl): PokedexRepository

}