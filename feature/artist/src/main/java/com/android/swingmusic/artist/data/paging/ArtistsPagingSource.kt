package com.android.swingmusic.artist.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.android.swingmusic.core.data.mapper.Map.toArtist
import com.android.swingmusic.core.domain.model.Artist
import com.android.swingmusic.network.data.api.service.NetworkApiService
import retrofit2.HttpException
import java.io.IOException

class ArtistsPagingSource(
    private val api: NetworkApiService,
    private val sortBy: String,
    private val sortOrder: Int,
    private val baseUrl: String,
    private val accessToken: String
) : PagingSource<Int, Artist>() {

    override fun getRefreshKey(state: PagingState<Int, Artist>): Int? = state.anchorPosition

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Artist> {
        return try {
            val nextPageNumber = params.key ?: 0
            val startIndex = nextPageNumber * params.loadSize

            val allArtistsDto = api.getAllArtists(
                url = baseUrl,
                bearerToken = accessToken,
                startIndex = startIndex,
                sortBy = sortBy,
                sortOrder = sortOrder
            )
            LoadResult.Page(
                data = allArtistsDto.artistsDto?.map { it.toArtist() } ?: emptyList(),
                prevKey = if (nextPageNumber == 0) null else nextPageNumber - 1,
                nextKey = if (allArtistsDto.artistsDto?.isEmpty() == true) null else nextPageNumber + 1
            )
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        }
    }
}
