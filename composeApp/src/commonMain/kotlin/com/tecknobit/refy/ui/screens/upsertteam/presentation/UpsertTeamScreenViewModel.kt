package com.tecknobit.refy.ui.screens.upsertteam.presentation

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.viewModelScope
import com.tecknobit.equinoxcore.pagination.PaginatedResponse.Companion.DEFAULT_PAGE
import com.tecknobit.equinoxcore.time.TimeFormatter
import com.tecknobit.refy.ui.screens.teams.data.Team
import com.tecknobit.refy.ui.screens.teams.data.TeamMember
import com.tecknobit.refy.ui.shared.data.RefyUser
import com.tecknobit.refy.ui.shared.presentations.UpsertScreenViewModel
import com.tecknobit.refycore.enums.TeamRole
import com.tecknobit.refycore.helpers.RefyInputsValidator.isTitleValid
import io.github.ahmad_hamwi.compose.pagination.PaginationState
import io.github.vinceglb.filekit.core.PlatformFile
import kotlinx.coroutines.launch
import kotlin.random.Random

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
        // TODO: MAKE THE REQUEST THEN
        _item.value = Team(
            id = Random.nextLong().toString(),
            logoPic = "https://storage.googleapis.com/pod_public/1300/169941.jpg",
            owner = RefyUser.RefyUserImpl(
                id = Random.nextLong().toString(),
                name = "Name",
                surname = "Name",
                email = "email@email.com",
                profilePic = "",
                tagName = "@prova"
            ),
            title = "gaggaggaggaggaggaggaggaggaggag",
            description = if (Random.nextBoolean()) {
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed convallis dolor eu eros tristique gravida. Fusce nec fermentum dui, vitae suscipit eros. Aliquam rhoncus nunc a est sollicitudin blandit. Suspendisse dictum lorem sed vestibulum scelerisque. Morbi ac velit vel lorem molestie venenatis. Donec vitae mauris convallis, efficitur elit ac, hendrerit erat. Suspendisse nisi nunc, faucibus sit amet tellus dignissim, accumsan imperdiet nibh. Duis sapien dolor, dapibus a suscipit in, molestie eu metus. Nam volutpat lacus ut ante auctor sagittis. Donec vitae commodo sem. Curabitur at molestie nunc, quis sodales dui. Vestibulum sed dictum purus. Etiam commodo nibh vitae ex euismod commodo. Morbi egestas massa felis, vitae semper risus rutrum hendrerit. Donec facilisis eget mauris at condimentum.\n" +
                        "\n" +
                        "Pellentesque dignissim tincidunt interdum. Suspendisse egestas risus nec varius rutrum. Cras nisl ligula, consequat vel scelerisque imperdiet, lobortis at diam. Maecenas purus mauris, facilisis hendrerit viverra vel, ullamcorper in mauris. Ut consequat convallis nunc, et molestie lorem auctor sed. Duis consequat placerat sapien, at pharetra ex consectetur a. Sed nulla libero, pretium nec elit vitae, ornare tincidunt augue. Maecenas vel aliquam eros, vel dignissim magna. Proin lacinia, quam eu molestie malesuada, dolor augue tempus lectus, in dictum neque nisi in libero. Phasellus ullamcorper sapien eu nisi hendrerit, sit amet faucibus metus sagittis. Ut vitae nisi at quam venenatis malesuada vitae eu nisi. Vivamus eu elit et purus euismod rutrum. Sed a ipsum semper, facilisis quam non, blandit odio. Pellentesque maximus fringilla hendrerit.\n" +
                        "\n" +
                        "Etiam ac pellentesque nunc. Phasellus sapien elit, consequat non metus ac, viverra hendrerit erat. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Ut malesuada tellus eget eros maximus, a ornare eros vulputate. Nam suscipit tempus lorem, quis ornare enim ornare id. Fusce quis congue ex. Quisque tincidunt enim ut mollis facilisis.\n" +
                        "\n" +
                        "Cras at mauris convallis, porttitor sem euismod, placerat libero. Sed quam mauris, dictum sit amet pharetra ut, rutrum quis nisl. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Aenean condimentum augue venenatis posuere malesuada. Fusce porttitor est quis interdum blandit. In fermentum ante dapibus diam vulputate efficitur. Fusce placerat leo nec ullamcorper sollicitudin. In accumsan scelerisque eros nec feugiat. Duis odio urna, maximus ut leo quis, venenatis euismod est. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Maecenas vel pretium tortor. In suscipit massa laoreet ligula varius, eget lobortis tellus suscipit. Maecenas viverra nibh consectetur, convallis lectus id, dictum tellus.\n" +
                        "\n" +
                        "Donec eros ex, tincidunt sit amet dui et, consequat tempus nisl. Proin justo quam, tempus malesuada libero nec, malesuada pharetra lorem. In ac nisi vel tellus lobortis accumsan. Nulla facilisi. Donec eget lorem viverra, tempus ex congue, finibus orci. In id vehicula sem. Nullam vulputate leo sed sapien dapibus efficitur. Nunc tincidunt, nibh id tincidunt facilisis, nisl nunc sagittis leo, et venenatis eros purus in justo. Donec bibendum sodales efficitur. Donec at odio a velit euismod venenatis in quis tortor. Fusce imperdiet in felis in eleifend. Morbi eget posuere urna. Aliquam interdum vel lacus et suscipit. Cras a suscipit urna, vel dapibus tortor. Duis semper porttitor finibus."

            } else "gag",
            date = TimeFormatter.currentTimestamp(),
            members = listOf(
                TeamMember(
                    id = Random.nextLong().toString(),
                    name = "Name",
                    surname = "Name",
                    email = "email@email.com",
                    profilePic = "",
                    tagName = "@prova",
                    role = TeamRole.VIEWER
                )
            )
        )
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
        // TODO: MAKE THE REQUEST THEN
        val members = listOf(
            TeamMember(
                id = Random.nextLong().toString(),
                name = "Name",
                surname = "Name",
                email = "email@email.com",
                profilePic = "",
                tagName = "@prova",
                role = TeamRole.VIEWER
            )
        )
        potentialMembers.appendPage(
            items = if (Random.nextBoolean())
                members
            else
                emptyList(), // TODO: TO USE THE REAL DATA
            nextPageKey = page + 1, // TODO: TO USE THE REAL DATA
            isLastPage = Random.nextBoolean() // TODO: TO USE THE REAL DATA
        )
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
        // TODO: MAKE THE REQUEST THEN
        onInsert()
    }

    override fun update(
        onUpdate: () -> Unit
    ) {
        // TODO: MAKE THE REQUEST THEN
        onUpdate()
    }

}