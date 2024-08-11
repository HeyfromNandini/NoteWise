package project.app.notewise.domain.network

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import project.app.notewise.data.login.SignInResponse
import project.app.notewise.data.login.SignUpRequest
import project.app.notewise.data.login.VerifyEmailRequest
import project.app.notewise.data.login.VerifyEmailResponse
import project.app.notewise.data.search.SearchRequest
import project.app.notewise.data.search.SearchResponse
import project.app.notewise.domain.Constants

class ApiServiceImpl(
    private val client: HttpClient,
) : ApiService {
    override suspend fun signIn(email: String, password: String): Result<SignInResponse> {
        return safeApiCall(
            apiCall = {
                client.get {
                    url("${Constants.API.BASE_URL}/signIn?email=$email&password=$password")
                    header(HttpHeaders.ContentType, ContentType.Application.Json)
                }
            },
            parseResponse = { response ->
                response.body<SignInResponse>()
            }
        )
    }

    override suspend fun signUp(signUpRequest: SignUpRequest): Result<SignInResponse> {
        return safeApiCall(
            apiCall = {
                client.post {
                    url("${Constants.API.BASE_URL}/signUp")
                    header(HttpHeaders.ContentType, ContentType.Application.Json)
                    setBody(signUpRequest)
                }
            },
            parseResponse = {
                it.body<SignInResponse>()
            }
        )
    }

    override suspend fun verifyEmail(
        verifyEmailRequest: VerifyEmailRequest,
        email: String
    ): Result<VerifyEmailResponse> {
        return safeApiCall(
            apiCall = {
                client.post {
                    url("${Constants.API.BASE_URL}/verifyEmail?email=$email")
                    header(HttpHeaders.ContentType, ContentType.Application.Json)
                    setBody(verifyEmailRequest)
                }
            },
            parseResponse = {
                it.body<VerifyEmailResponse>()
            }
        )
    }

    override suspend fun aiChat(searchRequest: SearchRequest, idToken: String): Result<SearchResponse> {
        return authenticatedApiCall(
            apiCall = {
                println("idToken is $idToken")
                client.post {
                    url("${Constants.API.BASE_URL}/searchNote")
                    header(HttpHeaders.ContentType, ContentType.Application.Json)
                    setBody(searchRequest)
                    headers {
                        append("Authorization", "Bearer $idToken")
                    }
                }
            },
            parseResponse = {
                it.body<SearchResponse>()
            },
            idToken = idToken,
            onUnauthorized = {

            }
        )
    }

}