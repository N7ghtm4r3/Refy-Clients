@file:OptIn(ExperimentalComposeApi::class)

package com.tecknobit.refy.ui.shared.presentations

import androidx.compose.runtime.ExperimentalComposeApi
import com.tecknobit.equinoxcompose.session.sessionflow.SessionFlowState
import com.tecknobit.equinoxcore.annotations.FutureEquinoxApi

@FutureEquinoxApi(
    releaseVersion = "1.1.4",
    additionalNotes = """
        - Think how to keep clean implementable
    """
)
interface SessionStateFlowConsumer {

    /**
     * `sessionFlowState` the state used to manage the session lifecycle in the screen
     */
    var sessionFlowState: SessionFlowState

    /**
     * Routine to perform when the server is currently offline
     */
    fun performOnServerOffline()

    fun notifyServerOffline() {
        sessionFlowState.notifyServerOffline()
        performOnServerOffline()
    }

}