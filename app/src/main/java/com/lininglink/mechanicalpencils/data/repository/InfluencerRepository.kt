package com.lininglink.mechanicalpencils.data.repository

import com.lininglink.mechanicalpencils.data.api.ApiService
import com.lininglink.mechanicalpencils.data.model.InfluencerDetail
import com.lininglink.mechanicalpencils.data.model.InfluencersResponse

class InfluencerRepository(private val apiService: ApiService) {

    suspend fun getInfluencers(): Result<InfluencersResponse> {
        return try {
            val response = apiService.getInfluencers()
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getInfluencer(id: Int): Result<InfluencerDetail> {
        return try {
            val response = apiService.getInfluencer(id)
            Result.success(response.influencer)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
