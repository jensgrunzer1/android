package com.android.swingmusic.uicomponent.presentation.component

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Wallpapers
import androidx.compose.ui.unit.dp
import com.android.swingmusic.core.domain.model.Artist
import com.android.swingmusic.core.domain.model.Track
import com.android.swingmusic.core.util.PlayerState
import com.android.swingmusic.uicomponent.R
import com.android.swingmusic.uicomponent.presentation.theme.SwingMusicTheme
import com.android.swingmusic.uicomponent.util.formatDuration


@Composable
fun TrackItem(
    isCurrentTrack: Boolean = false,
    playerState: PlayerState = PlayerState.UNSPECIFIED,
    track: Track,
    onClickTrackItem: (Track) -> Unit,
    onClickMoreVert: () -> Unit
) {
    SwingMusicTheme {
        Surface(
            modifier = Modifier
                .padding(vertical = 4.dp, horizontal = 12.dp)
                .clip(RoundedCornerShape(24))
        ) {
            // Image, Title, Artists, Duration
            Row(
                modifier = Modifier
                    .background(
                        color = if (isCurrentTrack)
                            MaterialTheme.colorScheme.onSurface.copy(alpha = .15F) else
                            Color.Unspecified
                    )
                    .clickable {
                        onClickTrackItem(track)
                    }
                    .padding(all = 8.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .clip(RoundedCornerShape(24))
                            .size(48.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.sample_image),
                            contentDescription = "Sample image"
                        )
                        if (isCurrentTrack) {
                            PlayingTrackIndicator(playerState = playerState)
                        }
                    }

                    Column(modifier = Modifier.padding(start = 4.dp)) {
                        Text(text = track.title)

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            for (artist in track.artists) {
                                Text(
                                    text = artist.name,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = .75F),
                                    style = MaterialTheme.typography.bodySmall
                                )
                                if (track.artists.lastIndex != track.artists.indexOf(artist)) {
                                    Text(
                                        text = ", ",
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = .75F),
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                            }

                            // Dot Separator
                            Box(
                                modifier = Modifier
                                    .padding(horizontal = 8.dp)
                                    .clip(CircleShape)
                                    .size(3.dp)
                                    .background(
                                        MaterialTheme.colorScheme.onSurface.copy(alpha = .50F)
                                    )
                            )

                            Text(
                                text = track.duration.formatDuration(),
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = .75F),
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }

                // More Icon
                IconButton(onClick = { onClickMoreVert() }) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "MoreVert"
                    )
                }
            }
        }
    }
}

@Preview(
    showBackground = true,
    device = Devices.PIXEL_2,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    wallpaper = Wallpapers.RED_DOMINATED_EXAMPLE
)
@Composable
fun TrackItemPreview() {

    val lilPeep = Artist(
        artistHash = "lilpeep123",
        image = "lilpeep.jpg",
        name = "Lil Peep"
    )
    val juice = Artist(
        artistHash = "juicepeep123",
        image = "lilpeep.jpg",
        name = "Juice WRLD"
    )

    val albumArtists = listOf(lilPeep, juice)
    val artists = listOf(lilPeep, juice)
    val genre = listOf("Rap", "Emo")

    val track = Track(
        album = "Sample Album",
        albumArtists = albumArtists,
        albumHash = "albumHash123",
        artistHashes = "artistHashes123",
        artists = artists,
        ati = "ati123",
        bitrate = 320,
        copyright = "Copyright © 2024",
        createdDate = 1648731600.0, // Sample timestamp
        date = 2024,
        disc = 1,
        duration = 204, // Sample duration in seconds
        filepath = "/path/to/track.mp3",
        folder = "/path/to/album",
        genre = genre,
        image = "/path/to/album/artwork.jpg",
        isFavorite = true,
        lastMod = 1648731600, // Sample timestamp
        ogAlbum = "Original Album",
        ogTitle = "Original Title",
        pos = 1,
        title = "Sample Track",
        track = 1,
        trackHash = "trackHash123"
    )

    SwingMusicTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            Column {
                TrackItem(
                    isCurrentTrack = true,
                    playerState = PlayerState.PLAYING,
                    track = track,
                    onClickTrackItem = {

                    },
                    onClickMoreVert = {

                    }
                )
                TrackItem(
                    isCurrentTrack = false,
                    track = track,
                    onClickTrackItem = {

                    },
                    onClickMoreVert = {

                    }
                )
                TrackItem(
                    isCurrentTrack = false,
                    track = track,
                    onClickTrackItem = {

                    },
                    onClickMoreVert = {

                    }
                )
                TrackItem(
                    isCurrentTrack = false,
                    track = track,
                    onClickTrackItem = {

                    },
                    onClickMoreVert = {

                    }
                )
                TrackItem(
                    isCurrentTrack = false,
                    track = track,
                    onClickTrackItem = {

                    },
                    onClickMoreVert = {

                    }
                )
                TrackItem(
                    isCurrentTrack = false,
                    track = track,
                    onClickTrackItem = {

                    },
                    onClickMoreVert = {

                    }
                )
                TrackItem(
                    isCurrentTrack = true,
                    playerState = PlayerState.PAUSED,
                    track = track,
                    onClickTrackItem = {

                    },
                    onClickMoreVert = {

                    }
                )
            }
        }
    }
}