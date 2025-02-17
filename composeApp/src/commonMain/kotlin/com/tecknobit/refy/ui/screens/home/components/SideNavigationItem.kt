package com.tecknobit.refy.ui.screens.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecknobit.equinoxcompose.components.ChameleonText
import com.tecknobit.equinoxcompose.components.getContrastColor
import com.tecknobit.refy.ui.screens.home.data.NavigationTab
import org.jetbrains.compose.resources.stringResource

/**
 * Custom component used to represent a tab of a side navigation bar
 *
 * @param tab The tab to represent
 * @param selected Whether the tab is currently selected
 * @param onClick The action to execute when a tab is clicked
 */
// TODO: ANNOTATE WITH SPECIFIC SizeClass annotations
@Composable
@NonRestartableComposable
fun SideNavigationItem(
    tab: NavigationTab,
    selected: Boolean,
    onClick: () -> Unit
) {
    val itemBackground = if(selected)
        MaterialTheme.colorScheme.onSurface
    else
        MaterialTheme.colorScheme.surfaceContainer
    Column (
        modifier = Modifier
            .fillMaxSize()
            .clickable {
                onClick()
            }
            .background(
                color = itemBackground
            )
    ) {
        Row (
            modifier = Modifier
                .padding(
                    vertical = 5.dp,
                    horizontal = 10.dp
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Column (
                modifier = Modifier
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = tab.icon,
                    contentDescription = null,
                    tint = getContrastColor(
                        backgroundColor = itemBackground
                    )
                )
            }
            Column (
                modifier = Modifier
                    .weight(2f)
            ) {
                ChameleonText(
                    text = stringResource(tab.title),
                    backgroundColor = itemBackground
                )
            }
        }
    }
}