package project.app.notewise.data.login


import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class VerifyEmailRequest(
    @SerializedName("idToken")
    val idToken: String? = null
)