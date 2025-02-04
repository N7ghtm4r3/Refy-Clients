@file:OptIn(ExperimentalMultiplatform::class)

package com.tecknobit.refy.ui.screens.shared.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
            TopBar()
        }
    }

    @Composable
    @NonRestartableComposable
    private fun ExtendedFAB() {
        ExtendedFloatingActionButton(
            onClick = {

            }
        ) {
            Text(
                text = "AGGIUNGI"
            )
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null
            )
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
                Text(
                    text = stringResource(title),
                    fontSize = 30.sp,
                    color = MaterialTheme.colorScheme.primary,
                    fontFamily = displayFontFamily
                )
                ResponsiveContent(
                    onExpandedSizeClass = {},
                    onMediumSizeClass = {},
                    onCompactSizeClass = {
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

}