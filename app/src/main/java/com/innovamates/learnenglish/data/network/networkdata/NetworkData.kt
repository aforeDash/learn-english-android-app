package com.innovamates.learnenglish.data.network.networkdata

import com.google.gson.annotations.SerializedName
import com.innovamates.learnenglish.data.models.Category

data class NetworkData(
    @SerializedName("data") var data: List<Category> = listOf(),
)