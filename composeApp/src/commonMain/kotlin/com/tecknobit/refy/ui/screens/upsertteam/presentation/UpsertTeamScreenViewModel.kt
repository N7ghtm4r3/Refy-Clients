package com.tecknobit.refy.ui.screens.upsertteam.presentation

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.viewModelScope
import com.tecknobit.equinoxcompose.session.setHasBeenDisconnectedValue
import com.tecknobit.equinoxcompose.session.setServerOfflineValue
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

class UpsertTeamScreenViewModel(
    private val teamId: String?
) : UpsertScreenViewModel<Team>(
    itemId = teamId
) {

    lateinit var logoPic: MutableState<String?>

    lateinit var logoPicError: MutableState<Boolean>

    private var logoBytes: ByteArray = byteArrayOf()

    lateinit var teamName: MutableState<String>

    lateinit var teamNameError: MutableState<Boolean>

    lateinit var teamMembers: SnapshotStateList<TeamMember>

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

    val potentialMembers = PaginationState<Int, TeamMember>(
        initialPageKey = DEFAULT_PAGE,
        onRequestPage = { page ->
            loadPotentialMembers(
                page = page
            )
        }
    )

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