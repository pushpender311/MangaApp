package com.bittech.manga.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "manga")
data class Manga(
    @PrimaryKey val id:String,
    val title:String,
    val thumb:String,
    val subTitle:String,
    val summary:String,
    val nsfw: Boolean,
    val type:String,
    val totalChapter:Int,
    val createdAt:Double,
    val updateAt:Double
    )
