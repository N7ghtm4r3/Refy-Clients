package com.tecknobit.refy.ui.screens.profile.util

import com.tecknobit.equinoxcompose.components.stepper.Step
import com.tecknobit.refy.ui.screens.profile.presentation.ProfileScreenViewModel

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
    return commonSteps
}