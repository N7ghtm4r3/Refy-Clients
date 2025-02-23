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
import refy.composeapp.generated.resources.no_members
import refy.composeapp.generated.resources.no_teams

/**
 * Custom layout used to display the empty state about the no links availability
 *
 * Credits to [undraw](https://undraw.co/search/No%20data)
 */
@Composable
@NonRestartableComposable
fun EmptyLinks() {
    EmptyState(
        resource = Res.drawable.no_links,
        contentDescription = null
    )
}

/**
 * Custom layout used to display the empty state about the no collections availability
 *
 * Credits to [Data illustrations by Storyset](https://storyset.com/data)
 *
 * @param size The size of the empty state
 */
@Composable
@NonRestartableComposable
fun EmptyCollections(
    size: Dp = 300.dp
) {
    EmptyState(
        resourceSize = size,
        resource = Res.drawable.no_data,
        contentDescription = null
    )
}

/**
 * Custom layout used to display the empty state about the no teams availability
 *
 * Credits to [People illustrations by Storyset](https://storyset.com/people)
 */
@Composable
@NonRestartableComposable
fun EmptyTeams() {
    EmptyState(
        resourceSize = 300.dp,
        resource = Res.drawable.no_teams,
        contentDescription = null
    )
}

/**
 * Custom layout used to display the empty state about the no members are joined in the team scenario
 *
 * Credits to [Work illustrations by Storyset](https://storyset.com/work")
 */
@Composable
@NonRestartableComposable
fun EmptyMembers() {
    EmptyState(
        resourceSize = 275.dp,
        resource = Res.drawable.no_members,
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
