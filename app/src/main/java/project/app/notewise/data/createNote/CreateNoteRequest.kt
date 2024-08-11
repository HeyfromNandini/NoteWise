package project.app.notewise.data.createNote


import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class CreateNoteRequest(
    @SerializedName("author")
    val author: String? = null,
    @SerializedName("category")
    val category: String? = null,
    @SerializedName("content")
    val content: String? = null,
    @SerializedName("isEncrypted")
    val isEncrypted: Boolean? = null,
    @SerializedName("namespace")
    val namespace: String? = null,
    @SerializedName("priority")
    val priority: String? = null,
    @SerializedName("tags")
    val tags: List<String?>? = null,
    @SerializedName("timestamp")
    val timestamp: Long? = null,
    @SerializedName("title")
    val title: String? = null,
    @SerializedName("uId")
    val uId: String? = null
)