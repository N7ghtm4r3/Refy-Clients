package com.tecknobit.refy.ui.screens.teams.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecknobit.refy.ui.components.ItemCardDetails
import com.tecknobit.refy.ui.screens.teams.data.Team
import com.tecknobit.refy.ui.screens.teams.presentation.TeamsScreenViewModel
import refy.composeapp.generated.resources.Res
import refy.composeapp.generated.resources.created_on

@Composable
@NonRestartableComposable
fun TeamCard(
    viewModel: TeamsScreenViewModel,
    team: Team
) {
    val expanded = remember { mutableStateOf(false) }
    val descriptionLines = remember { mutableIntStateOf(0) }
    Card(
        onClick = {
            // TODO: NAV TO TEAM
        }
    ) {
        Column(
            modifier = Modifier
                .padding(
                    horizontal = 10.dp
                )
                .padding(
                    top = 25.dp,
                    bottom = 10.dp
                )
        ) {
            ItemCardDetails(
                modifier = Modifier
                    .padding(
                        bottom = 5.dp
                    ),
                expanded = expanded,
                item = team,
                info = Res.string.created_on,
                descriptionLines = descriptionLines
            )

        }
    }
}