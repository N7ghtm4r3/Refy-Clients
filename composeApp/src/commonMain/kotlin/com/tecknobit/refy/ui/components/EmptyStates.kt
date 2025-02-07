package com.tecknobit.refy.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.tecknobit.refy.ui.theme.AppTypography
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import refy.composeapp.generated.resources.Res
import refy.composeapp.generated.resources.no_data
import refy.composeapp.generated.resources.no_links
import refy.composeapp.generated.resources.no_teams

@Composable
@NonRestartableComposable
// TODO: ADD THE CREDITS IN THE DOCU https://undraw.co/search/No%20data
fun EmptyLinks() {
    EmptyState(
        resource = Res.drawable.no_links,
        contentDescription = null
    )
}

@Composable
@NonRestartableComposable
// TODO: ADD THE CREDITS IN THE DOCU <a href="https://storyset.com/data">Data illustrations by Storyset</a>
fun EmptyCollections(
    size: Dp = 300.dp
) {
    EmptyState(
        resourceSize = size,
        resource = Res.drawable.no_data,
        contentDescription = null
    )
}

@Composable
@NonRestartableComposable
// TODO: ADD THE CREDITS IN THE DOCU <a href="https://storyset.com/people">People illustrations by Storyset</a>
fun EmptyTeams() {
    EmptyState(
        resourceSize = 300.dp,
        resource = Res.drawable.no_teams,
        contentDescription = null
    )
}

@Composable
@NonRestartableComposable
@Deprecated("USE THE EQUINOX BUILT-IN ONE")
// TODO: MORE CUSTOMIZATION IN THE OFFICIAL COMPONENT ONE
private fun EmptyState(
    containerModifier: Modifier = Modifier,
    resourceModifier: Modifier = Modifier,
    resourceSize: Dp = 200.dp,
    resource: DrawableResource,
    contentDescription: String?,
    title: StringResource? = null,
    subTitle: StringResource? = null,
    action: @Composable (() -> Unit)? = null,
) {
    Column(
        modifier = containerModifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            modifier = resourceModifier
                .size(resourceSize),
            painter = painterResource(resource),
            contentDescription = contentDescription
        )
        title?.let {
            Text(
                modifier = Modifier
                    .padding(
                        vertical = 5.dp
                    ),
                text = stringResource(title),
                style = AppTypography.headlineSmall,
                color = MaterialTheme.colorScheme.primary
            )
        }
        subTitle?.let {
            Text(
                text = stringResource(subTitle),
                style = AppTypography.bodyLarge,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}
