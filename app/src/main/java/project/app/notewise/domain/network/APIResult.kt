package project.app.notewise.domain.network

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.RedirectResponseException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.request.headers
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.client.statement.readText
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headers
import io.ktor.utils.io.errors.IOException
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import project.app.notewise.data.ErrorResponse
import project.app.notewise.data.login.SignInResponse
import project.app.notewise.data.login.SignUpRequest

suspend fun <T> safeApiCall(
    apiCall: suspend () -> HttpResponse,
    parseResponse: suspend (HttpResponse) -> T
): Result<T> {
    return try {
        val response = apiCall()
        when {
            response.status == HttpStatusCode.OK -> {
                val body = parseResponse(response)
                Result.success(body)
            }
            else -> {
                val errorResponse = parseError(response)
                println("Responnse is $response")
                Result.failure(Throwable(errorResponse.message))
            }
        }
    } catch (e: Exception) {
        Result.failure(mapToThrowable(e))
    }
}

suspend fun <T> authenticatedApiCall(
    idToken: String,
    apiCall: suspend () -> HttpResponse,
    onUnauthorized: suspend () -> Unit,
    parseResponse: suspend (HttpResponse) -> T
): Result<T> {
    return try {
        val response = apiCall().apply {
            headers {
                append("Authorization", "Bearer $idToken")
            }
        }
        when (response.status) {
            HttpStatusCode.OK -> {
                val body = parseResponse(response)
                Result.success(body)
            }
            HttpStatusCode.Unauthorized -> {
                // Handle token expiration or unauthorized access
                onUnauthorized()
                val errorResponse = parseError(response)
                Result.failure(Throwable("Unauthorized: ${errorResponse.message}"))
            }
            else -> {
                val errorResponse = parseError(response)
                Result.failure(Throwable(errorResponse.message))
            }
        }
    } catch (e: Exception) {
        Result.failure(mapToThrowable(e))
    }
}

private suspend fun parseError(response: HttpResponse): ErrorResponse {
    return try {
        val errorBody = response.bodyAsText()
        println("Response is $errorBody")
        Json.decodeFromString<ErrorResponse>(errorBody)
    } catch (e: Exception) {
        ErrorResponse(
            error = null,
            message = "Error occurred: ${response.status.value}",
            success = false
        )
    }
}

private fun mapToThrowable(exception: Exception): Throwable {
    return when (exception) {
        is IOException -> Throwable(message = "Network error occurred")
        is ClientRequestException -> Throwable(message = "Client request error occurred")
        is ServerResponseException -> Throwable(message = "Server error occurred")
        is RedirectResponseException -> Throwable(message = "Unexpected redirect occurred")
        else -> Throwable(message = exception.localizedMessage ?: "Unknown error occurred")
    }
}


