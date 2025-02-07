package com.tecknobit.refy.ui.screens.collection.components

import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.tecknobit.refy.ui.components.ItemTitle
import com.tecknobit.refy.ui.components.TeamLogo
import com.tecknobit.refy.ui.screens.teams.data.Team
import org.jetbrains.compose.resources.pluralStringResource
import refy.composeapp.generated.resources.Res
import refy.composeapp.generated.resources.members

@Composable
@NonRestartableComposable
fun CollectionTeamCard(
    team: Team
) {
    Card(
        modifier = Modifier
            .width(width = 175.dp),
        shape = RoundedCornerShape(
            size = 10.dp
        ),
        onClick = {
            // TODO: NAV TO TEAM
        }
    ) {
        ListItem(
            colors = ListItemDefaults.colors(
                containerColor = Color.Transparent
            ),
            leadingContent = {
                TeamLogo(
                    team = team,
                    size = 50.dp
                )
            },
            headlineContent = {
                ItemTitle(
                    item = team
                )
            },
            supportingContent = {
                val members = team.members.size
                Text(
                    text = pluralStringResource(
                        resource = Res.plurals.members,
                        quantity = members,
                        members
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        )
    }
}