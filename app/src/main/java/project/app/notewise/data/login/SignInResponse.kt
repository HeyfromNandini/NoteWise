package project.app.notewise.data.login

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable


@Serializable
data class SignInResponse(
    @SerializedName("email")
    val email: String? = null,
    @SerializedName("idToken")
    val idToken: String? = null,
    @SerializedName("kind")
    val kind: String? = null,
    @SerializedName("userId")
    val userId: String? = null
)