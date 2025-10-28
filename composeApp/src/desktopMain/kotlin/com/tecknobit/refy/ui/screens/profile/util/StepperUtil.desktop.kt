package com.tecknobit.refy.ui.screens.profile.util

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecknobit.equinoxcompose.components.stepper.Step
import com.tecknobit.equinoxcompose.components.stepper.StepContent
import com.tecknobit.equinoxcore.annotations.RequiresDocumentation
import com.tecknobit.refy.ui.screens.profile.presentation.ProfileScreenViewModel
import org.jetbrains.compose.resources.stringResource
import refy.composeapp.generated.resources.Res
import refy.composeapp.generated.resources.close_application_on_link_open
import refy.composeapp.generated.resources.close_application_on_link_open_title

/**
 * Method used to merge the default settings steps of the [com.tecknobit.refy.ui.screens.profile.presenter.ProfileScreen] with
 * specific platform settings of each platform
 *
 * @param viewModel The support viewmodel for the screen
 * @param commonSteps The steps in common between all platforms
 *
 * @return the complete steps array as [Array] of [Step]
 *
 * @since 1.1.0
 */
actual fun platformSpecificSettingSteps(
    viewModel: ProfileScreenViewModel,
    commonSteps: Array<Step>,
): Array<Step> {
    val closeOnLinkOpenStep = Step(
        stepIcon = Icons.Default.PowerSettingsNew,
        title = Res.string.close_application_on_link_open_title,
        content = {
            CloseApplicationOnLinkOpen(
                viewModel = viewModel
            )
        },
        dismissAction = { visible -> visible.value = false },
        confirmAction = { visible ->
            viewModel.changeCloseOnLinkOpen(
                onChange = { visible.value = false }
            )
        }
    )
    return arrayOf(*commonSteps, closeOnLinkOpenStep)
}

@RequiresDocumentation(
    additionalNotes = "TODO: INCLUDE VERSION"
)
@StepContent(
    number = 6
)
@Composable
private fun CloseApplicationOnLinkOpen(
    viewModel: ProfileScreenViewModel,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 16.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(Res.string.close_application_on_link_open)
        )
        Checkbox(
            checked = viewModel.closeApplicationOnLinkOpen.value,
            onCheckedChange = { checked ->
                viewModel.closeApplicationOnLinkOpen.value = checked
            }
        )
    }
}