@file:OptIn(ExperimentalMultiplatform::class)

package com.tecknobit.refy.ui.screens.home.presenter

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Groups3
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecknobit.equinoxcompose.session.screens.EquinoxNoModelScreen
import com.tecknobit.equinoxcompose.utilities.ResponsiveContent
import com.tecknobit.refy.ui.components.ProfilePic
import com.tecknobit.refy.ui.icons.Collection
import com.tecknobit.refy.ui.icons.Link45deg
import com.tecknobit.refy.ui.icons.TempPreferencesCustom
import com.tecknobit.refy.ui.screens.collections.presenter.CollectionsScreen
import com.tecknobit.refy.ui.screens.customs.presenter.CustomLinksScreen
import com.tecknobit.refy.ui.screens.home.components.AnimatedBottomNavigationBar
import com.tecknobit.refy.ui.screens.home.components.SideNavigationItem
import com.tecknobit.refy.ui.screens.home.data.NavigationTab
import com.tecknobit.refy.ui.screens.links.presenter.LinksScreen
import com.tecknobit.refy.ui.screens.teams.presenter.TeamsScreen
import com.tecknobit.refy.ui.shared.presenters.RefyScreen
import com.tecknobit.refy.ui.theme.RefyTheme
import org.jetbrains.compose.resources.stringResource
import refy.composeapp.generated.resources.Res
import refy.composeapp.generated.resources.app_version
import refy.composeapp.generated.resources.collections
import refy.composeapp.generated.resources.custom
import refy.composeapp.generated.resources.links
import refy.composeapp.generated.resources.teams

class HomeScreen : EquinoxNoModelScreen() {

    /**
     * `currentSelectedHomeTabIndex` the index of the tab currently displayed on the [HomeScreen]
     */
    private lateinit var currentSelectedHomeTabIndex: MutableState<Int>

    private companion object {

        val tabs = arrayOf(
            NavigationTab(
                title = Res.string.links,
                icon = Link45deg
            ),
            NavigationTab(
                title = Res.string.collections,
                icon = Collection
            ),
            NavigationTab(
                title = Res.string.teams,
                icon = Icons.Default.Groups3
            ),
            NavigationTab(
                title = Res.string.custom,
                icon = TempPreferencesCustom
            )
        )

    }

    /**
     * Method to arrange the content of the screen to display
     */
    @Composable
    override fun ArrangeScreenContent() {
        RefyTheme {
            ResponsiveContent(
                onExpandedSizeClass = {
                    SideNavigationBar()
                },
                onMediumSizeClass = {
                    SideNavigationBar()
                },
                onCompactSizeClass = {
                    BottomNavigationBar()
                }
            )
        }
    }

    @Composable
    @NonRestartableComposable
    private fun SideNavigationBar() {
        Row {
            NavigationRail(
                modifier = Modifier
                    .width(200.dp)
                    .fillMaxHeight()
                    .verticalScroll(rememberScrollState()),
                containerColor = MaterialTheme.colorScheme.surfaceContainer,
                header = {
                    ProfilePic(
                        modifier = Modifier
                            .padding(
                                vertical = 5.dp
                            ),
                        size = 95.dp
                    )
                    HorizontalDivider()
                }
            ) {
                tabs.forEachIndexed { index, tab ->
                    SideNavigationItem(
                        tab = tab,
                        selected = index == currentSelectedHomeTabIndex.value,
                        onClick = { currentSelectedHomeTabIndex.value = index }
                    )
                }
                Column (
                    modifier = Modifier
                        .weight(1f)
                        .padding(
                            bottom = 16.dp
                        ),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Bottom
                ) {
                    Text(
                        text = "v" + stringResource(Res.string.app_version),
                        fontSize = 14.sp
                    )
                }
            }
            CurrentTabContent()
        }
    }

    @Composable
    @NonRestartableComposable
    private fun BottomNavigationBar() {
        Box (
          modifier = Modifier
              .fillMaxSize()
        ) {
            CurrentTabContent(
                modifier = Modifier
                    .navigationBarsPadding()
            )
            AnimatedBottomNavigationBar(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .navigationBarsPadding(),
                tabs = tabs,
                currentSelectedTabIndex = currentSelectedHomeTabIndex
            )
        }
    }

    @Composable
    @NonRestartableComposable
    private fun CurrentTabContent(
        modifier: Modifier = Modifier
    ) {
        Column (
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            AnimatedContent(
                targetState = currentSelectedHomeTabIndex.value
            ) { index ->
                val tab = tabs[index]
                var screen by remember { mutableStateOf<RefyScreen<*>?>(null) }
                LaunchedEffect(Unit) {
                    screen = tab.tabRelatedScreen()
                }
                screen?.ShowContent()
            }
        }
    }

    private fun NavigationTab.tabRelatedScreen(): RefyScreen<*> {
        return when (this.title) {
            Res.string.links -> LinksScreen()
            Res.string.collections -> CollectionsScreen()
            Res.string.teams -> TeamsScreen()
            else -> CustomLinksScreen()
        }
    }

    /**
     * Method to collect or instantiate the states of the screen
     */
    @Composable
    override fun CollectStates() {
        currentSelectedHomeTabIndex = rememberSaveable { mutableIntStateOf(0) }
    }

}