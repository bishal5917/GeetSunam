package com.example.geetsunam.features.domain.usecases

import com.example.geetsunam.database.entities.FeaturedArtist
import com.example.geetsunam.features.data.datasource.UserLocalDatasource
import com.example.geetsunam.utils.models.Artist

class SaveFeaturedArtistsUsecase(private val localDatasource: UserLocalDatasource) {
    suspend fun call(artists: List<Artist?>?) {
        artists?.takeIf { it.isNotEmpty() }?.let { list ->
            val artists = list.map { a ->
                FeaturedArtist(
                    id = a?.id!!,
                    email = a.email,
                    fullname = a.fullname,
                    isFeatured = a.isFeatured,
                    profileImage = a.profileImage,
                )
            }
            localDatasource.saveFeaturedArtists(artists)
        }
    }
}