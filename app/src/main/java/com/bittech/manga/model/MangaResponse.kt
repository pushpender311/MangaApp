package com.bittech.manga.model

import com.google.gson.annotations.SerializedName

data class MangaResponse(
    @SerializedName("data") val data: List<MangaDto>
)