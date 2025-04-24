package com.bittech.manga.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bittech.manga.local.entity.Manga

@Dao
interface MangaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(mangaList: List<Manga>)

    @Query("Select * from manga")
    fun getAllManga(): PagingSource<Int, Manga>

    @Query("Delete from manga")
    suspend fun clearAll()

    @Query("SELECT * FROM manga WHERE id = :id")
    suspend fun getMangaById(id: String): Manga?

}