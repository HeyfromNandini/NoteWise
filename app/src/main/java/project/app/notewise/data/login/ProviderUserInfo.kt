package project.app.notewise.data.login


import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class ProviderUserInfo(
    @SerializedName("email")
    val email: String? = null,
    @SerializedName("federatedId")
    val federatedId: String? = null,
    @SerializedName("providerId")
    val providerId: String? = null,
    @SerializedName("rawId")
    val rawId: String? = null
)