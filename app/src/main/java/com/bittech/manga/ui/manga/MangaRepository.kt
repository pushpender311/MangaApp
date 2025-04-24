package com.bittech.manga.ui.manga

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.bittech.manga.local.dao.MangaDao
import com.bittech.manga.local.entity.Manga
import com.bittech.manga.network.ApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MangaRepository @Inject constructor(private val api: ApiService, private val dao: MangaDao) {

    fun getManga():Pager<Int,Manga>{
        return Pager(
            config = PagingConfig(20),
            pagingSourceFactory = {dao.getAllManga()}
        )
    }
    suspend fun fetchAndCacheManga(page:Int){
        val response=api.fetchManga(page)
        dao.insertAll(response.data.map { it.toManga() })
    }

    suspend fun getMangaById(id: String): Manga? {
        return dao.getMangaById(id)
    }

    suspend fun clearCache(){
        dao.clearAll()
    }
}