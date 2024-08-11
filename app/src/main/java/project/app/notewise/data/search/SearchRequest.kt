package project.app.notewise.data.search

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class SearchRequest(
    @SerializedName("content")
    val content: String? = null,
    @SerializedName("namespace")
    val namespace: String? = null
)