package project.app.notewise.data.login


import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class VerifyEmailResponse(
    @SerializedName("error")
    val error: String? = null,
    @SerializedName("kind")
    val kind: String? = null,
    @SerializedName("users")
    val users: List<User?>? = null
)