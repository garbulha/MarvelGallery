package com.sample.marvelgallery.presenter

import com.sample.marvelgallery.data.MarvelRepository
import com.sample.marvelgallery.data.network.applySchedulers
import com.sample.marvelgallery.data.network.plusAssign
import com.sample.marvelgallery.data.network.smartSubscribe
import com.sample.marvelgallery.view.main.MainView

class MainPresenter(val view: MainView, val repository: MarvelRepository) : BasePresenter() {

    fun onViewCreated() {
        loadCharacters()
    }

    fun onRefresh() {
        loadCharacters()
    }

    fun onSearchChanged(newText: String) {
        loadCharacters(newText)
    }

    private fun loadCharacters(search: String? = null) {
        val qualifiedSearchQuery = if (search.isNullOrBlank()) null else search
        subscriptions += repository
                .getAllCharacters(qualifiedSearchQuery)
                .applySchedulers()
                .smartSubscribe(
                        onStart = { view.refresh = true },
                        onSuccess = view::show,
                        onError = view::showError,
                        onFinish = { view.refresh = false }
                )
    }
}