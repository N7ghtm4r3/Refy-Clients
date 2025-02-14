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
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.tecknobit.refy.localUser
import com.tecknobit.refy.ui.screens.teams.data.Team
import org.jetbrains.compose.resources.painterResource
import refy.composeapp.generated.resources.Res
import refy.composeapp.generated.resources.logo

@Composable
@NonRestartableComposable
fun TeamLogo(
    modifier: Modifier = Modifier,
    size: Dp = 40.dp,
    onClick: (() -> Unit)? = null,
    team: Team
) {
    TeamLogo(
        modifier = modifier,
        size = size,
        onClick = onClick,
        teamLogo = team.logoPic
    )
}

@Composable
@NonRestartableComposable
fun TeamLogo(
    modifier: Modifier = Modifier,
    size: Dp = 40.dp,
    onClick: (() -> Unit)? = null,
    teamLogo: String?
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
            .data(localUser.hostAddress + "/" + teamLogo)
            .crossfade(true)
            .crossfade(500)
            .build(),
        imageLoader = imageLoader,
        contentScale = ContentScale.Crop,
        error = painterResource(Res.drawable.logo),
        contentDescription = "Team logo"
    )
}