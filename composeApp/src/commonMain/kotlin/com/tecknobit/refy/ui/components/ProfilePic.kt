package com.tecknobit.refy.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.ImageLoader
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.tecknobit.refy.localUser
import org.jetbrains.compose.resources.painterResource
import refy.composeapp.generated.resources.Res
import refy.composeapp.generated.resources.logo

/**
 * `imageLoader` the image loader used by coil library to load the image and by-passing the https self-signed certificates
 */
lateinit var imageLoader: ImageLoader

@Composable
@NonRestartableComposable
fun ProfilePic(
    modifier: Modifier = Modifier,
    size: Dp
) {
    AsyncImage(
        modifier = modifier
            .clip(CircleShape)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.inversePrimary,
                shape = CircleShape
            )
            .size(size)
            .clickable {
                // navigator.navigate(PROFILE_SCREEN) TODO: TO SET
            },
        model = ImageRequest.Builder(LocalPlatformContext.current)
            .data(localUser.profilePic)
            .crossfade(true)
            .crossfade(500)
            .build(),
        //imageLoader = imageLoader, TODO: TO SET
        contentScale = ContentScale.Crop,
        error = painterResource(Res.drawable.logo),
        contentDescription = "User profile picture"
    )
}