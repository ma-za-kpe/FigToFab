package com.maku.figmaparser

import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.maku.figmaparser.model.finali.FigmaFile
import com.maku.figmaparser.service.FigmaService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FigmaRemoteDataSource @Inject constructor(
    private val figmaService: FigmaService
){
    suspend fun getFigmaResponse(file: String): Flow<Result<JsonObject>> = flow {
        emit(Result.Loading)
        try {
            val response = figmaService.getFile(file)
            if (response .isSuccessful) {
                val figmaFile = response.body()
                if (figmaFile!= null) {
                    println("figmaFile ${figmaFile}")
                    val gson = Gson()
                    val jsonObject = gson.fromJson(figmaFile, JsonObject::class.java)

                    val documentId = jsonObject.get("document").asJsonObject.get("id").asString
                    println("Document ID: $documentId")
                    emit(Result.Success(figmaFile))
                } else {
                    emit(Result.Error(Throwable("Response body is null")))
                }
            } else {
                emit(Result.Error(Throwable("Error: ${response.code()}")))
            }
        } catch (e: Exception) {
            Log.d("TAG-error", "getFigmaResponse: $e")
            emit(Result.Error(e))
        }
    }
}


class FigmaRepository @Inject constructor(
    private val figmaRemoteDataSource: FigmaRemoteDataSource,
) {
    suspend fun getFigmaResponse(file: String): Flow<Result<JsonObject>> {
        return figmaRemoteDataSource.getFigmaResponse(file)
    }
}

sealed class Result<out T> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val exception: Throwable) : Result<Nothing>()
    object Loading : Result<Nothing>()
}
