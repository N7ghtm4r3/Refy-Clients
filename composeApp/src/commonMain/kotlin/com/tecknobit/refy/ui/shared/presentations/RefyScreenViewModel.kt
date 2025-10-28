@file:OptIn(ExperimentalComposeApi::class)

package com.tecknobit.refy.ui.shared.presentations

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.ExperimentalComposeApi
import androidx.compose.runtime.MutableState
import com.tecknobit.equinoxcompose.session.sessionflow.SessionFlowState
import com.tecknobit.equinoxcompose.session.viewmodels.EquinoxViewModel
import com.tecknobit.equinoxcore.annotations.Structure

/**
 * The `RefyScreenViewModel` class is the support class used to handle the operations of a
 * [com.tecknobit.refy.ui.shared.presenters.RefyScreen], for example the requests to the backend
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see androidx.lifecycle.ViewModel
 * @see com.tecknobit.equinoxcompose.session.Retriever
 * @see EquinoxViewModel
 * @see SessionStateFlowConsumer
 */
@Structure
abstract class RefyScreenViewModel : EquinoxViewModel(
    snackbarHostState = SnackbarHostState()
), SessionStateFlowConsumer {

    /**
     *`keywords` the keywords used as filter
     */
    lateinit var keywords: MutableState<String>

    /**
     * `sessionFlowState` the state used to manage the session lifecycle in the screen
     */
    override lateinit var sessionFlowState: SessionFlowState

    /**
     * Method used to refresh the data displayed by the screen
     */
    abstract fun refresh()

    /**
     * Method used to reload the content related to data to retrieve that gone on error during the
     * retrieving
     */
    abstract fun reload()

}