package com.tecknobit.refy.ui.screens.upsertteam.presentation

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.viewModelScope
import com.tecknobit.equinoxcompose.session.setHasBeenDisconnectedValue
import com.tecknobit.equinoxcompose.session.setServerOfflineValue
import com.tecknobit.equinoxcompose.viewmodels.EquinoxViewModel
import com.tecknobit.equinoxcore.network.Requester.Companion.sendPaginatedRequest
import com.tecknobit.equinoxcore.network.Requester.Companion.sendRequest
import com.tecknobit.equinoxcore.network.Requester.Companion.toResponseData
import com.tecknobit.equinoxcore.pagination.PaginatedResponse.Companion.DEFAULT_PAGE
import com.tecknobit.refy.requester
import com.tecknobit.refy.ui.screens.teams.data.Team
import com.tecknobit.refy.ui.screens.teams.data.TeamMember
import com.tecknobit.refy.ui.shared.presentations.UpsertScreenViewModel
import com.tecknobit.refycore.helpers.RefyInputsValidator.isTitleValid
import io.github.ahmad_hamwi.compose.pagination.PaginationState
import io.github.vinceglb.filekit.core.PlatformFile
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement

/**
 * The `UpsertTeamScreenViewModel` class is the support class used by the [com.tecknobit.refy.ui.screens.upsertteam.presenter.UpsertTeamScreen]
 *
 * @param teamId The identifier of the item to update
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see androidx.lifecycle.ViewModel
 * @see com.tecknobit.equinoxcompose.session.Retriever
 * @see EquinoxViewModel
 * @see UpsertScreenViewModel
 *
 */
class UpsertTeamScreenViewModel(
    private val teamId: String?
) : UpsertScreenViewModel<Team>(
    itemId = teamId
) {

    /**
     *`logoPic` the logo of the team
     */
    lateinit var logoPic: MutableState<String?>

    /**
     * `logoPicError` whether the [logoPic] field is not valid
     */
    lateinit var logoPicError: MutableState<Boolean>

    /**
     * `logoBytes` the array of bytes made up the [logoPic]
     */
    private var logoBytes: ByteArray = byteArrayOf()

    /**
     *`teamName` the name of the team
     */
    lateinit var teamName: MutableState<String>

    /**
     * `teamNameError` whether the [teamName] field is not valid
     */
    lateinit var teamNameError: MutableState<Boolean>

    /**
     * `teamMembers` the members of the team
     */
    lateinit var teamMembers: SnapshotStateList<TeamMember>

    /**
     * Method to retrieve the information of the item to display
     */
    override fun retrieveItem() {
        if (teamId == null)
            return
        viewModelScope.launch {
            requester.sendRequest(
                request = {
                    getTeam(
                        teamId = teamId
                    )
                },
                onSuccess = {
                    setServerOfflineValue(false)
                    _item.value = Json.decodeFromJsonElement(it.toResponseData())
                },
                onFailure = { setHasBeenDisconnectedValue(true) },
                onConnectionError = { setServerOfflineValue(true) }
            )
        }
    }

    /**
     * Method to pick the logo of the team
     */
    fun pickTeamLogo(
        logoAsset: PlatformFile?
    ) {
        logoAsset?.let {
            viewModelScope.launch {
                logoBytes = logoAsset.readBytes()
                logoPic.value = logoAsset.path
                logoPicError.value = false
            }
        }
    }

    /**
     *`potentialMembers` the state used to handle the pagination of the potential members to add
     * in the team list
     */
    val potentialMembers = PaginationState<Int, TeamMember>(
        initialPageKey = DEFAULT_PAGE,
        onRequestPage = { page ->
            loadPotentialMembers(
                page = page
            )
        }
    )

    /**
     * Method used to load and retrieve the potential members to append to the [potentialMembers]
     *
     * @param page The page to request
     */
    private fun loadPotentialMembers(
        page: Int
    ) {
        viewModelScope.launch {
            requester.sendPaginatedRequest(
                request = {
                    getPotentialMembers(
                        page = page
                    )
                },
                serializer = TeamMember.serializer(),
                onSuccess = { paginatedResponse ->
                    potentialMembers.appendPage(
                        items = paginatedResponse.data,
                        nextPageKey = paginatedResponse.nextPage,
                        isLastPage = paginatedResponse.isLastPage
                    )
                },
                onFailure = { showSnackbarMessage(it) }
            )
        }
    }

    /**
     * Method to check the validity of the form data to insert or update an item
     *
     * @return the validity of the form as [Boolean]
     */
    override fun validForm(): Boolean {
        if (logoPic.value.isNullOrBlank()) {
            logoPicError.value = true
            return false
        }
        if (!isTitleValid(teamName.value)) {
            teamNameError.value = true
            return false
        }
        return super.validForm()
    }

    /**
     * Method to insert a new item
     *
     * @param onInsert The action to execute after the item inserted
     */
    override fun insert(
        onInsert: () -> Unit
    ) {
        viewModelScope.launch {
            requester.sendRequest(
                request = {
                    createTeam(
                        title = teamName.value,
                        logoPicName = logoPic.value!!,
                        logoPicBytes = logoBytes,
                        description = itemDescription.value,
                        membersRaw = teamMembers
                    )
                },
                onSuccess = { onInsert() },
                onFailure = { showSnackbarMessage(it) }
            )
        }
    }

    /**
     * Method to update an existing item
     *
     * @param onUpdate The action to execute after the item updated
     */
    override fun update(
        onUpdate: () -> Unit
    ) {
        viewModelScope.launch {
            requester.sendRequest(
                request = {
                    editTeam(
                        teamId = teamId!!,
                        title = teamName.value,
                        logoPicName = logoPic.value!!,
                        logoPicBytes = logoBytes,
                        description = itemDescription.value,
                        membersRaw = teamMembers
                    )
                },
                onSuccess = { onUpdate() },
                onFailure = { showSnackbarMessage(it) }
            )
        }
    }

}