package com.example.travelmate.data.repository

import android.net.Uri
import com.example.travelmate.domain.model.Response.Failure
import com.example.travelmate.domain.model.Response.Success
import com.example.travelmate.domain.repository.PhotoRepository
import com.example.travelmate.domain.repository.UploadPhotoResponse
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PhotoRepositoryImpl @Inject constructor(
    private val storage: FirebaseStorage
) : PhotoRepository {
    override suspend fun uploadPhoto(uri: Uri, path: String): Flow<UploadPhotoResponse> = flow {
        try {
            val filePath = if (path.isBlank()) {
                UUID.randomUUID().toString()
            } else {
                path
            }

            val uploadTask = storage.reference.child(filePath).putFile(uri)

            // Pause the task for now
            uploadTask.pause()

            // Resume the task and await its completion
            uploadTask.resume()
            uploadTask.await()

            // Get the download URL of the uploaded file
            val downloadUrl = uploadTask.snapshot.metadata?.reference?.downloadUrl?.await()
            downloadUrl?.let {
                emit(Success(it.toString()))
            } ?: run {
                emit(Failure(Exception("Failed to get download URL of the uploaded photo")))
            }
        } catch (e: Exception) {
            emit(Failure(e))
        }
    }
}
