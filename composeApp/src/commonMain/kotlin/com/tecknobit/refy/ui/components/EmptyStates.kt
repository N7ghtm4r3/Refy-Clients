@file:OptIn(ExperimentalMultiplatform::class)

package com.tecknobit.refy.ui.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecknobit.equinoxcompose.components.EmptyState
import com.tecknobit.equinoxcompose.utilities.responsiveAssignment
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
        containerModifier = Modifier
            .fillMaxSize(),
        resource = Res.drawable.no_links,
        contentDescription = null
    )
}

/**
 * Custom layout used to display the empty state about the no collections availability
 *
 * Credits to [Data illustrations by Storyset](https://storyset.com/data)
 */
@Composable
fun EmptyCollections() {
    EmptyState(
        containerModifier = Modifier
            .fillMaxSize(),
        resourceSize = responsiveAssignment(
            onExpandedSizeClass = { 400.dp },
            onMediumSizeClass = { 300.dp },
            onCompactSizeClass = { 300.dp }
        ),
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
        containerModifier = Modifier
            .fillMaxSize(),
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
        containerModifier = Modifier
            .fillMaxSize(),
        resourceSize = 275.dp,
        resource = Res.drawable.no_members,
        contentDescription = null
    )
}
