package com.bittech.manga.network

import com.bittech.manga.model.MangaResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("manga/fetch")
    suspend fun  fetchManga(
        @Query("page")page:Int
    ): MangaResponse
}