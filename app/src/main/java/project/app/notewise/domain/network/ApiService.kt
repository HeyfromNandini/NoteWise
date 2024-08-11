package project.app.notewise.domain.network

import project.app.notewise.data.login.SignInResponse
import project.app.notewise.data.login.SignUpRequest
import project.app.notewise.data.login.VerifyEmailRequest
import project.app.notewise.data.login.VerifyEmailResponse
import project.app.notewise.data.search.SearchRequest
import project.app.notewise.data.search.SearchResponse

interface ApiService {
    suspend fun signIn(email: String, password: String): Result<SignInResponse>
    suspend fun signUp(signUpRequest: SignUpRequest): Result<SignInResponse>
    suspend fun verifyEmail(
        verifyEmailRequest: VerifyEmailRequest,
        email: String
    ): Result<VerifyEmailResponse>

    suspend fun aiChat(searchRequest: SearchRequest, idToken: String): Result<SearchResponse>

}