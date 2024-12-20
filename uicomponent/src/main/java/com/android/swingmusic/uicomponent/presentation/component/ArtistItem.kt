package com.android.swingmusic.uicomponent.presentation.component

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Wallpapers
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.android.swingmusic.core.domain.model.Artist
import com.android.swingmusic.uicomponent.R
import com.android.swingmusic.uicomponent.presentation.theme.SwingMusicTheme_Preview

@Composable
fun ArtistItem(
    modifier: Modifier,
    artist: Artist,
    baseUrl: String,
    onClick: (artistHash: String) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(
            top = 16.dp,
            bottom = 8.dp,
            start = 8.dp,
            end = 8.dp
        )
    ) {
        AsyncImage(
            modifier = modifier
                .clip(CircleShape)
                .clickable { onClick(artist.artistHash) },
            model = ImageRequest.Builder(LocalContext.current)
                .data("${baseUrl}img/artist/medium/${artist.image}")
                .crossfade(true)
                .build(),
            placeholder = painterResource(R.drawable.artist_fallback),
            fallback = painterResource(R.drawable.artist_fallback),
            error = painterResource(R.drawable.artist_fallback),
            contentDescription = "Artist Image",
            contentScale = ContentScale.FillWidth,
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = artist.name,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            fontWeight = FontWeight.SemiBold,
            style = MaterialTheme.typography.bodyMedium,
        )
        Spacer(modifier = Modifier.height(2.dp))

        val helperText = artist.helpText.replace("minutes", "mins")

        if (artist.helpText.isNotEmpty())
            Text(
                text = helperText,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = .75F)
            )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, wallpaper = Wallpapers.BLUE_DOMINATED_EXAMPLE)
@Composable
fun ArtistItemPreview() {
    fun generateDummyArtist(): Artist {
        val artisthash = "dummy_artist"
        val colors = listOf("#FFFFFF", "#000000", "#FF0000")
        val createdDate = System.currentTimeMillis().toDouble()
        val helpText = "2hrs, 10 minutes"
        val image = "https://example.com/image.jpg"
        val name = "Dummy Artist"

        return Artist(artisthash, colors, createdDate, helpText, image, name)
    }

    SwingMusicTheme_Preview {
        Surface {
            ArtistItem(
                modifier = Modifier.size(150.dp),
                generateDummyArtist(),
                baseUrl = "",
                onClick = {

                }
            )
        }
    }
}
