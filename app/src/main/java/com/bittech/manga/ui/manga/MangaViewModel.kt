package com.bittech.manga.ui.manga

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.bittech.manga.local.entity.Manga
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MangaViewModel @Inject constructor(
    private val repository: MangaRepository
) : ViewModel() {

    fun fetchAndCachePage(page: Int) {
        viewModelScope.launch {
            try {
                repository.fetchAndCacheManga(page)
            } catch (e: Exception) {
                Log.e("MangaViewModel", "Failed to fetch page $page: ${e.message}")
            }
        }
    }

    suspend fun getMangaById(id: String): Manga? {
        return repository.getMangaById(id)
    }


    val mangaPagingFlow = repository.getManga().flow.cachedIn(viewModelScope)
}