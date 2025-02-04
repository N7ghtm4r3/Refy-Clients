@file:OptIn(ExperimentalMultiplatform::class)

package com.tecknobit.refy.ui.screens.home.presenter

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
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
import androidx.compose.material.icons.filled.CollectionsBookmark
import androidx.compose.material.icons.filled.Groups3
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecknobit.equinoxcompose.session.screens.EquinoxNoModelScreen
import com.tecknobit.equinoxcompose.utilities.ResponsiveContent
import com.tecknobit.refy.ui.components.ProfilePic
import com.tecknobit.refy.ui.icons.Link45deg
import com.tecknobit.refy.ui.icons.TempPreferencesCustom
import com.tecknobit.refy.ui.screens.collections.presenter.CollectionsScreen
import com.tecknobit.refy.ui.screens.customs.presenter.CustomLinksScreen
import com.tecknobit.refy.ui.screens.home.components.AnimatedBottomNavigationBar
import com.tecknobit.refy.ui.screens.home.components.SideNavigationItem
import com.tecknobit.refy.ui.screens.home.data.NavigationTab
import com.tecknobit.refy.ui.screens.links.presenter.LinksScreen
import com.tecknobit.refy.ui.screens.teams.presenter.TeamsScreen
import org.jetbrains.compose.resources.stringResource
import refy.composeapp.generated.resources.Res
import refy.composeapp.generated.resources.app_version
import refy.composeapp.generated.resources.collections
import refy.composeapp.generated.resources.custom
import refy.composeapp.generated.resources.links
import refy.composeapp.generated.resources.teams

class HomeScreen : EquinoxNoModelScreen() {

    companion object {

        private val tabs = arrayOf(
            NavigationTab(
                title = Res.string.links,
                icon = Link45deg,
                screen = LinksScreen()
            ),
            NavigationTab(
                title = Res.string.collections,
                icon = Icons.Default.CollectionsBookmark,
                screen = CollectionsScreen()
            ),
            NavigationTab(
                title = Res.string.teams,
                icon = Icons.Default.Groups3,
                screen = TeamsScreen()
            ),
            NavigationTab(
                title = Res.string.custom,
                icon = TempPreferencesCustom,
                screen = CustomLinksScreen()
            )
        )

    }

    private lateinit var currentSelectedTab: MutableState<NavigationTab>

    /**
     * Method to arrange the content of the screen to display
     */
    @Composable
    override fun ArrangeScreenContent() {
        NavigationBar()
    }

    @Composable
    @NonRestartableComposable
    private fun NavigationBar() {
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
                tabs.forEach { tab ->
                    SideNavigationItem(
                        tab = tab,
                        selected = tab == currentSelectedTab.value,
                        onClick = { currentSelectedTab.value = tab}
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
                    .align(Alignment.BottomCenter),
                tabs = tabs,
                currentSelectedTab = currentSelectedTab
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
                targetState = currentSelectedTab.value,
                transitionSpec = spring(stiffness = Spring.StiffnessMediumLow)
            ) { tab ->
                tab.screen.ShowContent()
            }
        }
    }

    /**
     * Method to collect or instantiate the states of the screen
     */
    @Composable
    override fun CollectStates() {
        currentSelectedTab = remember { mutableStateOf(tabs[0]) }
    }

}