@file:OptIn(ExperimentalMultiplatform::class)

package com.tecknobit.refy.ui.shared.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecknobit.equinoxcompose.session.screens.EquinoxScreen
import com.tecknobit.equinoxcompose.utilities.ResponsiveContent
import com.tecknobit.equinoxcompose.viewmodels.EquinoxViewModel
import com.tecknobit.equinoxcore.annotations.Structure
import com.tecknobit.refy.displayFontFamily
import com.tecknobit.refy.ui.components.ProfilePic
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

@Structure
abstract class RefyScreen<V : EquinoxViewModel>(
    private val title: StringResource,
    viewModel: V
) : EquinoxScreen<V>(
    viewModel = viewModel
) {

    @Composable
    override fun ArrangeScreenContent() {
        Scaffold(
            snackbarHost = { SnackbarHost(viewModel.snackbarHostState!!) },
            floatingActionButton = {
                ResponsiveContent(
                    onExpandedSizeClass = { ExtendedFAB() },
                    onMediumSizeClass = { ExtendedFAB() },
                    onCompactSizeClass = {}
                )
            }
        ) {
            Column {
                TopBar()
                Column(
                    modifier = Modifier
                        .padding(
                            all = 16.dp
                        )
                ) {
                    Content()
                }
            }
        }
    }

    @Composable
    @NonRestartableComposable
    protected fun TopBar() {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(113.dp)
                    .padding(
                        horizontal = 16.dp
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ResponsiveContent(
                    onExpandedSizeClass = { ScreenTitle() },
                    onMediumSizeClass = { ScreenTitle() },
                    onCompactSizeClass = {
                        ListItem(
                            modifier = Modifier
                                .weight(2f),
                            supportingContent = {
                                Row(
                                    modifier = Modifier
                                        .clip(
                                            RoundedCornerShape(
                                                size = 5.dp
                                            )
                                        )
                                        .clickable { createAction() }
                                        .padding(
                                            horizontal = 4.dp
                                        ),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                                ) {
                                    Icon(
                                        imageVector = createIcon(),
                                        contentDescription = null
                                    )
                                    Text(
                                        text = stringResource(createText())
                                    )
                                }
                            },
                            headlineContent = {
                                ScreenTitle()
                            }
                        )
                        Column(
                            modifier = Modifier
                                .weight(1f),
                            horizontalAlignment = Alignment.End,
                            verticalArrangement = Arrangement.Center
                        ) {
                            ProfilePic(
                                size = 75.dp
                            )
                        }
                    }
                )
            }
            HorizontalDivider(
                color = MaterialTheme.colorScheme.primary
            )
        }
    }

    @Composable
    @NonRestartableComposable
    protected abstract fun Content()

    @Composable
    @NonRestartableComposable
    private fun ExtendedFAB() {
        ExtendedFloatingActionButton(
            onClick = { createAction() }
        ) {
            Text(
                text = stringResource(createText())
            )
            Icon(
                modifier = Modifier
                    .padding(
                        start = 5.dp
                    ),
                imageVector = createIcon(),
                contentDescription = null
            )
        }
    }

    protected abstract fun createIcon(): ImageVector

    protected abstract fun createText(): StringResource

    protected abstract fun createAction()

    @Composable
    @NonRestartableComposable
    private fun ScreenTitle() {
        Text(
            text = stringResource(title),
            fontSize = 28.sp,
            color = MaterialTheme.colorScheme.primary,
            fontFamily = displayFontFamily,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }

}