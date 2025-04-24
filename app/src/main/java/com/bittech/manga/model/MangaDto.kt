package com.bittech.manga.model

import com.bittech.manga.local.entity.Manga
import com.google.gson.annotations.SerializedName

data class MangaDto(
    @SerializedName("id") val id: String,
    @SerializedName("title") val title: String,
    @SerializedName("thumb") val thumb: String,
    @SerializedName("sub_title") val subTitle: String,
    @SerializedName("summary") val summary: String,
    @SerializedName("nsfw") val nsfw: Boolean,
    @SerializedName("type") val type: String,
    @SerializedName("total_chapter") val totalChapter: Int,
    @SerializedName("created_at") val createdAt: Double,
    @SerializedName("updated_at") val updateAt: Double
) {
    fun toManga(): Manga {
        return Manga(
            id = id,
            title = title,
            thumb = thumb,
            subTitle = subTitle,
            summary = summary,
            nsfw = nsfw,
            type = type,
            totalChapter = totalChapter,
            createdAt = createdAt,
            updateAt = updateAt
        )
    }
}