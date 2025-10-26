package com.tecknobit.refy.ui.screens.home.presenter

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Groups3
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.tecknobit.equinoxcompose.session.screens.EquinoxNoModelScreen
import com.tecknobit.equinoxcompose.utilities.ResponsiveClass.EXPANDED_CONTENT
import com.tecknobit.equinoxcompose.utilities.ResponsiveClass.MEDIUM_CONTENT
import com.tecknobit.equinoxcompose.utilities.ResponsiveClassComponent
import com.tecknobit.equinoxnavigation.I18nNavigationTab
import com.tecknobit.equinoxnavigation.NavigatorScreen
import com.tecknobit.refy.ui.components.ProfilePic
import com.tecknobit.refy.ui.icons.Collection
import com.tecknobit.refy.ui.icons.Link45deg
import com.tecknobit.refy.ui.icons.TempPreferencesCustom
import com.tecknobit.refy.ui.screens.collections.presenter.CollectionsScreen
import com.tecknobit.refy.ui.screens.customs.presenter.CustomLinksScreen
import com.tecknobit.refy.ui.screens.links.presenter.LinksScreen
import com.tecknobit.refy.ui.screens.teams.presenter.TeamsScreen
import com.tecknobit.refy.ui.theme.AppTypography
import org.jetbrains.compose.resources.stringResource
import refy.composeapp.generated.resources.Res
import refy.composeapp.generated.resources.app_version
import refy.composeapp.generated.resources.collections
import refy.composeapp.generated.resources.custom
import refy.composeapp.generated.resources.links
import refy.composeapp.generated.resources.teams

/**
 * The [HomeScreen] class is used to display the main screen of the application allowing the user
 * to navigate in the screens of that application
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see EquinoxNoModelScreen
 */
// TODO: TO DOCU SINCE
class HomeScreen : NavigatorScreen<I18nNavigationTab>() {

    @Composable
    override fun ArrangeScreenContent() {
        NavigationContent()
    }

    @Composable
    @ResponsiveClassComponent(
        classes = [EXPANDED_CONTENT, MEDIUM_CONTENT]
    )
    override fun ColumnScope.SideNavigationHeaderContent() {
        ProfilePic(
            modifier = Modifier.padding(
                vertical = 16.dp
            ),
            size = 100.dp
        )
        HorizontalDivider(
            color = MaterialTheme.colorScheme.primary
        )
    }

    @Composable
    @ResponsiveClassComponent(
        classes = [EXPANDED_CONTENT, MEDIUM_CONTENT]
    )
    override fun ColumnScope.SideNavigationFooterContent() {
        Text(
            modifier = Modifier
                .fillMaxWidth(),
            text = "v. ${stringResource(Res.string.app_version)}",
            style = AppTypography.labelMedium,
            textAlign = TextAlign.Center
        )
    }

    /**
     * Method used to retrieve the tabs to assign to the [tabs] array
     *
     * @return the tabs used by the [NavigatorScreen] as [Array] of [T]
     */
    override fun navigationTabs(): Array<I18nNavigationTab> {
        return arrayOf(
            I18nNavigationTab(
                title = Res.string.links,
                icon = Link45deg
            ),
            I18nNavigationTab(
                title = Res.string.collections,
                icon = Collection
            ),
            I18nNavigationTab(
                title = Res.string.teams,
                icon = Icons.Default.Groups3
            ),
            I18nNavigationTab(
                title = Res.string.custom,
                icon = TempPreferencesCustom
            )
        )
    }

    /**
     * Method used to instantiate the related screen based on the current [activeNavigationTabIndex]
     *
     * @return the screen as [EquinoxNoModelScreen]
     */
    override fun Int.tabContent(): EquinoxNoModelScreen {
        return when (this) {
            0 -> LinksScreen()
            1 -> CollectionsScreen()
            2 -> TeamsScreen()
            else -> CustomLinksScreen()
        }
    }

}