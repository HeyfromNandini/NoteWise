package project.app.notewise.data.login


import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class User(
    @SerializedName("createdAt")
    val createdAt: String? = null,
    @SerializedName("email")
    val email: String? = null,
    @SerializedName("emailVerified")
    val emailVerified: Boolean? = null,
    @SerializedName("lastLoginAt")
    val lastLoginAt: String? = null,
    @SerializedName("lastRefreshAt")
    val lastRefreshAt: String? = null,
    @SerializedName("localId")
    val localId: String? = null,
    @SerializedName("passwordHash")
    val passwordHash: String? = null,
    @SerializedName("passwordUpdatedAt")
    val passwordUpdatedAt: Long? = null,
    @SerializedName("providerUserInfo")
    val providerUserInfo: List<ProviderUserInfo?>? = null,
    @SerializedName("validSince")
    val validSince: String? = null
)