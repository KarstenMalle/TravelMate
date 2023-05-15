package com.example.travelmate.domain.repository

import android.net.Uri
import com.example.travelmate.domain.model.Response
import kotlinx.coroutines.flow.Flow

typealias UploadPhotoResponse = Response<String>

interface PhotoRepository {
    suspend fun uploadPhoto(uri: Uri, path: String): Flow<UploadPhotoResponse>
}
