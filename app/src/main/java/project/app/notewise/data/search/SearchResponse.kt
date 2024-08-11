package project.app.notewise.data.search

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class SearchResponse(
    @SerializedName("content")
    val content: String? = null,
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("response")
    val response: String? = null
)