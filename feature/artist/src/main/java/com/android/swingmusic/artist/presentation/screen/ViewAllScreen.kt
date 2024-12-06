package com.android.swingmusic.artist.presentation.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.android.swingmusic.artist.presentation.viewmodel.ArtistInfoViewModel
import com.android.swingmusic.common.presentation.navigator.CommonNavigator
import com.android.swingmusic.core.domain.model.Album
import com.android.swingmusic.core.domain.model.BottomSheetItemModel
import com.android.swingmusic.core.domain.model.Track
import com.android.swingmusic.core.domain.util.BottomSheetAction
import com.android.swingmusic.core.domain.util.PlaybackState
import com.android.swingmusic.core.domain.util.QueueSource
import com.android.swingmusic.player.presentation.event.QueueEvent
import com.android.swingmusic.player.presentation.viewmodel.MediaControllerViewModel
import com.android.swingmusic.uicomponent.R
import com.android.swingmusic.uicomponent.presentation.component.AlbumItem
import com.android.swingmusic.uicomponent.presentation.component.CustomTrackBottomSheet
import com.android.swingmusic.uicomponent.presentation.component.TrackItem
import com.android.swingmusic.uicomponent.presentation.theme.SwingMusicTheme
import com.android.swingmusic.uicomponent.presentation.theme.SwingMusicTheme_Preview
import com.ramcosta.composedestinations.annotation.Destination

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ViewAll(
    title: String,
    artistName: String,
    artistHash: String,
    baseUrl: String,
    playingTrackHash: String,
    playbackState: PlaybackState,
    allTracks: List<Track>?,
    allAlbumsToShow: List<Album>?,
    onClickArtistTrack: (queue: List<Track>, index: Int) -> Unit,
    onClickAlbum: (hash: String) -> Unit,
    onGetSheetAction: (track: Track, sheetAction: BottomSheetAction) -> Unit,
    onGotoArtist: (hash: String) -> Unit
) {
    val gridState = rememberLazyGridState()
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showTrackBottomSheet by remember { mutableStateOf(false) }
    var clickedTrack: Track? by remember { mutableStateOf(null) }

    Scaffold { padding ->
        Scaffold(
            modifier = Modifier.padding(padding),
            topBar = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 8.dp, top = 16.dp),
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.headlineMedium
                    )
                    Text(
                        text = artistName,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = .80F)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        ) { innerPadding ->
            if (showTrackBottomSheet) {
                clickedTrack?.let { track ->
                    CustomTrackBottomSheet(
                        scope = scope,
                        sheetState = sheetState,
                        clickedTrack = track,
                        baseUrl = baseUrl,
                        currentArtisthash = artistHash,
                        bottomSheetItems = listOf(
                            BottomSheetItemModel(
                                label = "Go to Artist",
                                enabled = artistHash != track.trackHash && track.trackArtists.size != 1,
                                painterId = R.drawable.ic_artist,
                                track = track,
                                sheetAction = BottomSheetAction.OpenArtistsDialog(track.trackArtists)
                            ),
                            BottomSheetItemModel(
                                label = "Go to Album",
                                painterId = R.drawable.ic_album,
                                track = track,
                                sheetAction = BottomSheetAction.GotoAlbum
                            ),
                            BottomSheetItemModel(
                                label = "Go to Folder",
                                painterId = R.drawable.folder_outlined_open,
                                track = track,
                                sheetAction = BottomSheetAction.GotoFolder(
                                    name = track.folder.getFolderName(),
                                    path = track.folder
                                )
                            )
                        ),
                        onHideBottomSheet = {
                            showTrackBottomSheet = it
                        },
                        onClickSheetItem = { sheetTrack, sheetAction ->
                            onGetSheetAction(sheetTrack, sheetAction)
                        },
                        onChooseArtist = { hash ->
                            onGotoArtist(hash)
                        }
                    )
                }
            }

            allTracks?.let { tracks ->
                LazyColumn(modifier = Modifier.padding(innerPadding)) {
                    itemsIndexed(
                        items = tracks,
                        key = { index, item -> item.filepath + index }
                    ) { index, track ->
                        TrackItem(
                            track = track,
                            playbackState = playbackState,
                            showMenuIcon = true,
                            isCurrentTrack = track.trackHash == playingTrackHash,
                            onClickTrackItem = {
                                onClickArtistTrack(tracks, index)
                            },
                            onClickMoreVert = { trackClicked ->
                                clickedTrack = trackClicked
                                showTrackBottomSheet = true
                            },
                            baseUrl = baseUrl
                        )

                        if (index == tracks.lastIndex) {
                            Spacer(modifier = Modifier.height(50.dp))
                        }
                    }
                }
            }

            allAlbumsToShow?.let { albums ->
                LazyVerticalGrid(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize()
                        .padding(horizontal = 8.dp),
                    columns = GridCells.Fixed(2),
                    state = gridState,
                ) {
                    items(
                        items = albums,
                        key = { item -> item.albumHash }
                    ) { album ->
                        AlbumItem(
                            modifier = Modifier.fillMaxSize(),
                            album = album,
                            baseUrl = baseUrl,
                            onClick = {
                                onClickAlbum(it)
                            }
                        )
                    }
                    item {
                        Spacer(modifier = Modifier.height(50.dp))
                    }
                    item {
                        Spacer(modifier = Modifier.height(50.dp))
                    }
                }
            }
        }
    }
}

@Destination
@Composable
fun ViewAllScreen(
    commonNavigator: CommonNavigator,
    mediaControllerViewModel: MediaControllerViewModel,
    artistInfoViewModel: ArtistInfoViewModel,
    viewAllType: String,
    artistName: String,
    baseUrl: String,
) {
    val artistInfoState = artistInfoViewModel.artistInfoState.collectAsState()
    val playerUiState = mediaControllerViewModel.playerUiState.collectAsState()

    val artistData = artistInfoState.value.infoResource.data
    val artistHash = artistInfoState.value.infoResource.data?.artist?.artistHash ?: ""
    val tracks = if (viewAllType != "Tracks") null else artistData?.tracks
    val albumsToShow = when (viewAllType) {
        "Albums" -> artistData?.albumsAndAppearances?.albums
        "Ep & Singles" -> artistData?.albumsAndAppearances?.singlesAndEps
        "Compilations" -> artistData?.albumsAndAppearances?.compilations
        "Appearances" -> artistData?.albumsAndAppearances?.appearances
        else -> null
    }

    SwingMusicTheme {
        ViewAll(
            title = viewAllType,
            artistName = artistData?.artist?.name ?: artistName,
            baseUrl = baseUrl,
            artistHash = artistHash,
            playingTrackHash = playerUiState.value.nowPlayingTrack?.trackHash ?: "",
            playbackState = playerUiState.value.playbackState,
            allTracks = tracks,
            allAlbumsToShow = albumsToShow,
            onClickArtistTrack = { queue, index ->
                mediaControllerViewModel.onQueueEvent(
                    QueueEvent.RecreateQueue(
                        source = QueueSource.ARTIST(artistHash = artistHash, name = artistName),
                        clickedTrackIndex = index,
                        queue = queue
                    )
                )
            },
            onClickAlbum = {
                commonNavigator.gotoAlbumWithInfo(it)
            },
            onGetSheetAction = { track, sheetAction ->
                when (sheetAction) {
                    is BottomSheetAction.GotoAlbum -> {
                        commonNavigator.gotoAlbumWithInfo(track.albumHash)
                    }

                    is BottomSheetAction.GotoFolder -> {
                        commonNavigator.gotoSourceFolder(
                            sheetAction.name,
                            sheetAction.path
                        )
                    }

                    else -> {}
                }
            },
            onGotoArtist = { hash ->
                commonNavigator.gotoArtistInfo(hash)
            }
        )
    }
}