package com.tecknobit.refy.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.tecknobit.refy.ui.screens.teams.data.Team
import org.jetbrains.compose.resources.painterResource
import refy.composeapp.generated.resources.Res
import refy.composeapp.generated.resources.logo

@Composable
@NonRestartableComposable
fun TeamLogo(
    modifier: Modifier = Modifier,
    size: Dp,
    onClick: (() -> Unit)? = null,
    team: Team
) {
    AsyncImage(
        modifier = modifier
            .clip(CircleShape)
            .size(size)
            .clickable(
                enabled = onClick != null,
                onClick = {
                    onClick?.invoke()
                }
            ),
        model = ImageRequest.Builder(LocalPlatformContext.current)
            .data(team.logoPic)
            .crossfade(true)
            .crossfade(500)
            .build(),
        //imageLoader = imageLoader, TODO: TO SET
        contentScale = ContentScale.Crop,
        error = painterResource(Res.drawable.logo),
        contentDescription = team.title
    )
}