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
import com.tecknobit.refy.ui.shared.data.Team
import org.jetbrains.compose.resources.painterResource
import refy.composeapp.generated.resources.Res
import refy.composeapp.generated.resources.logo

/**
 * Custom [AsyncImage] used to display the logo of a [Team]
 *
 * @param modifier The modifier to apply to the component
 * @param size The size of the profile pic
 * @param onClick The action to execute when the component has been clicked
 * @param team The team to display its logo
 */
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

/**
 * Custom [AsyncImage] used to display the logo of a [Team]
 *
 * @param modifier The modifier to apply to the component
 * @param size The size of the profile pic
 * @param onClick The action to execute when the component has been clicked
 * @param teamLogo The team logo to display
 */
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
            .data(teamLogo)
            .crossfade(true)
            .crossfade(500)
            .build(),
        imageLoader = imageLoader,
        contentScale = ContentScale.Crop,
        onError = { println(it) },
        error = painterResource(Res.drawable.logo),
        contentDescription = "Team logo"
    )
}