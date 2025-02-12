package com.tecknobit.refy.helpers

import com.tecknobit.equinoxcompose.network.EquinoxRequester
import com.tecknobit.equinoxcore.annotations.Assembler
import com.tecknobit.equinoxcore.annotations.CustomParametersOrder
import com.tecknobit.equinoxcore.annotations.RequestPath
import com.tecknobit.equinoxcore.network.RequestMethod.DELETE
import com.tecknobit.equinoxcore.network.RequestMethod.GET
import com.tecknobit.equinoxcore.network.RequestMethod.PATCH
import com.tecknobit.equinoxcore.network.RequestMethod.POST
import com.tecknobit.equinoxcore.network.RequestMethod.PUT
import com.tecknobit.refy.ui.screens.collections.data.LinksCollection
import com.tecknobit.refy.ui.screens.customs.data.CustomRefyLink
import com.tecknobit.refy.ui.screens.links.data.RefyLink
import com.tecknobit.refy.ui.screens.teams.data.Team
import com.tecknobit.refy.ui.screens.teams.data.TeamMember
import com.tecknobit.refycore.COLLECTIONS_KEY
import com.tecknobit.refycore.COLLECTION_COLOR_KEY
import com.tecknobit.refycore.DESCRIPTION_KEY
import com.tecknobit.refycore.EXPIRED_TIME_KEY
import com.tecknobit.refycore.FIELDS_KEY
import com.tecknobit.refycore.LINKS_KEY
import com.tecknobit.refycore.MEMBERS_KEY
import com.tecknobit.refycore.OWNED_ONLY_KEY
import com.tecknobit.refycore.REFERENCE_LINK_KEY
import com.tecknobit.refycore.RESOURCES_KEY
import com.tecknobit.refycore.TAG_NAME_KEY
import com.tecknobit.refycore.TEAMS_KEY
import com.tecknobit.refycore.TEAM_ROLE_KEY
import com.tecknobit.refycore.TITLE_KEY
import com.tecknobit.refycore.UNIQUE_ACCESS_KEY
import com.tecknobit.refycore.enums.ExpiredTime
import com.tecknobit.refycore.enums.TeamRole
import com.tecknobit.refycore.helpers.RefyEndpointsSet.CUSTOM_LINKS_ENDPOINT
import com.tecknobit.refycore.helpers.RefyEndpointsSet.LEAVE_ENDPOINT
import com.tecknobit.refycore.helpers.RefyEndpointsSet.UPDATE_MEMBER_ROLE_ENDPOINT
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.put

/**
 * The **RefyRequester** class is useful to communicate with Refy's backend
 *
 * @param host The host address where is running the backend
 * @param userId The user identifier
 * @param userToken The user token
 *
 * @author N7ghtm4r3 - Tecknobit
 *
 */
class RefyRequester(
    host: String,
    userId: String? = null,
    userToken: String? = null
) : EquinoxRequester(
    host = host,
    userId = userId,
    userToken = userToken,
    connectionTimeout = 5000,
    connectionErrorMessage = DEFAULT_CONNECTION_ERROR_MESSAGE,
    byPassSSLValidation = true
) {

    /**
     * Method to create the payload for the [signUp] request.
     *
     * #### Usage example:
     *
     * ```
     * @CustomParametersOrder(order = ["currency"]) // optional
     * override fun getSignUpPayload(
     *         serverSecret: String,
     *         name: String,
     *         surname: String,
     *         email: String,
     *         password: String,
     *         language: String,
     *         vararg custom: Any?
     *     ): JsonObject {
     *         val payload = super.getSignUpPayload(serverSecret, name, surname, email, password, language, *custom).toMutableMap()
     *         payload["currency"] = Json.encodeToJsonElement(custom[0]!!.toString())
     *         return Json.encodeToJsonElement(payload).jsonObject
     *     }
     * ```
     *
     * @param serverSecret The secret of the personal Equinox's backend
     * @param name The name of the user
     * @param surname The surname of the user
     * @param email The email of the user
     * @param password The password of the user
     * @param language The language of the user
     * @param custom The custom parameters added in a customization of the equinox user to execute a customized sign-up
     *
     * @return the payload for the request as [JsonObject]
     *
     */
    @CustomParametersOrder(order = [TAG_NAME_KEY])
    override fun getSignUpPayload(
        serverSecret: String,
        name: String,
        surname: String,
        email: String,
        password: String,
        language: String,
        vararg custom: Any?
    ): JsonObject {
        val payload = super.getSignUpPayload(
            serverSecret = serverSecret,
            name = name,
            surname = surname,
            email = email,
            password = password,
            language = language,
            custom = custom
        ).toMutableMap()
        payload[TAG_NAME_KEY] = Json.encodeToJsonElement(custom[0]!!.toString())
        return Json.encodeToJsonElement(payload).jsonObject
    }

    /**
     * Function to execute the request to get the links of the user
     *
     * @param ownedOnly: whether to get only the links where the user is the owner
     *
     * @return the result of the request as [JsonObject]
     *
     */
    @RequestPath(path = "/api/v1/users/{user_id}/links", method = GET)
    suspend fun getLinks(
        ownedOnly: Boolean = false
    ): JsonObject {
        return execGet(
            endpoint = assembleLinksEndpointPath(),
            query = createOwnedOnlyQuery(
                ownedOnly = ownedOnly
            )
        )
    }

    /**
     * Function to execute the request to create a new link
     *
     * @param referenceLink The reference link value
     * @param description The description of the link
     *
     * @return the result of the request as [JsonObject]
     *
     */
    @RequestPath(path = "/api/v1/users/{user_id}/links", method = POST)
    suspend fun createLink(
        referenceLink: String,
        description: String
    ): JsonObject {
        val payload = createLinkPayload(
            referenceLink = referenceLink,
            description = description
        )
        return execPost(
            endpoint = assembleLinksEndpointPath(),
            payload = payload
        )
    }

    /**
     * Function to execute the request to edit a link
     *
     * @param link The link to edit
     * @param referenceLink The reference link value
     * @param description The description of the link
     *
     * @return the result of the request as [JsonObject]
     *
     */
    @RequestPath(path = "/api/v1/users/{user_id}/links/{link_id}", method = PATCH)
    suspend fun editLink(
        link: RefyLink,
        referenceLink: String,
        description: String
    ): JsonObject {
        return editLink(
            linkId = link.id,
            referenceLink = referenceLink,
            description = description
        )
    }

    /**
     * Function to execute the request to edit a link
     *
     * @param linkId The link identifier to edit
     * @param referenceLink The reference link value
     * @param description The description of the link
     *
     * @return the result of the request as [JsonObject]
     *
     */
    @RequestPath(path = "/api/v1/users/{user_id}/links/{link_id}", method = PATCH)
    suspend fun editLink(
        linkId: String,
        referenceLink: String,
        description: String
    ): JsonObject {
        val payload = createLinkPayload(
            referenceLink = referenceLink,
            description = description
        )
        return execPatch(
            endpoint = assembleLinksEndpointPath(
                subEndpoint = linkId
            ),
            payload = payload
        )
    }

    /**
     * Function to create a payload for the link creation/edit actions
     *
     * @param referenceLink The reference link value
     * @param description The description of the link
     *
     * @return the payload as [JsonObject]
     *
     */
    @Assembler
    private fun createLinkPayload(
        referenceLink: String,
        description: String
    ): JsonObject {
        return buildJsonObject {
            put(REFERENCE_LINK_KEY, referenceLink)
            put(DESCRIPTION_KEY, description)
        }
    }

    /**
     * Function to execute the request to manage the collections where the link is shared
     *
     * @param link The link where manage its collections
     * @param collections The list of collections where share the link
     *
     * @return the result of the request as [JsonObject]
     *
     */
    @RequestPath(path = "/api/v1/users/{user_id}/links/{link_id}/collections", method = PUT)
    suspend fun manageLinkCollections(
        link: RefyLink,
        collections: List<String>
    ): JsonObject {
        return manageLinkCollections(
            linkId = link.id,
            collections = collections
        )
    }

    /**
     * Function to execute the request to manage the collections where the link is shared
     *
     * @param linkId The link identifier where manage its collections
     * @param collections The list of collections where share the link
     *
     * @return the result of the request as [JsonObject]
     *
     */
    @RequestPath(path = "/api/v1/users/{user_id}/links/{link_id}/collections", method = PUT)
    suspend fun manageLinkCollections(
        linkId: String,
        collections: List<String>
    ): JsonObject {
        val payload = buildJsonObject {
            put(COLLECTIONS_KEY, Json.encodeToJsonElement(collections))
        }
        return execPut(
            endpoint = assembleLinksEndpointPath(
                subEndpoint = "$linkId/$COLLECTIONS_KEY"
            ),
            payload = payload
        )
    }

    /**
     * Function to execute the request to manage the teams where the link is shared
     *
     * @param link The link where manage its collections
     * @param teams The list of teams where share the link
     *
     * @return the result of the request as [JsonObject]
     *
     */
    @RequestPath(path = "/api/v1/users/{user_id}/links/{link_id}/teams", method = PUT)
    suspend fun manageLinkTeams(
        link: RefyLink,
        teams: List<String>
    ): JsonObject {
        return manageLinkTeams(
            linkId = link.id,
            teams = teams
        )
    }

    /**
     * Function to execute the request to manage the teams where the link is shared
     *
     * @param linkId The link identifier where manage its collections
     * @param teams The list of teams where share the link
     *
     * @return the result of the request as [JsonObject]
     *
     */
    @RequestPath(path = "/api/v1/users/{user_id}/links/{link_id}/teams", method = PUT)
    suspend fun manageLinkTeams(
        linkId: String,
        teams: List<String>
    ): JsonObject {
        val payload = buildJsonObject {
            put(TEAMS_KEY, Json.encodeToJsonElement(teams))
        }
        return execPut(
            endpoint = assembleLinksEndpointPath(
                subEndpoint = "$linkId/$TEAMS_KEY"
            ),
            payload = payload
        )
    }

    /**
     * Function to execute the request to delete a link
     *
     * @param link The link to delete
     *
     * @return the result of the request as [JsonObject]
     *
     */
    @RequestPath(path = "/api/v1/users/{user_id}/links/{link_id}", method = DELETE)
    suspend fun deleteLink(
        link: RefyLink
    ): JsonObject {
        return deleteLink(
            linkId = link.id
        )
    }

    /**
     * Function to execute the request to delete a link
     *
     * @param linkId The link identifier of the link to delete
     *
     * @return the result of the request as [JsonObject]
     *
     */
    @RequestPath(path = "/api/v1/users/{user_id}/links/{link_id}", method = DELETE)
    suspend fun deleteLink(
        linkId: String
    ): JsonObject {
        return execDelete(
            endpoint = assembleLinksEndpointPath(
                subEndpoint = linkId
            )
        )
    }

    /**
     * Function to assemble the endpoint to make the request to the links controller
     *
     * @param subEndpoint The sub-endpoint path of the url
     *
     * @return an endpoint to make the request as [String]
     */
    @Assembler
    private fun assembleLinksEndpointPath(
        subEndpoint: String = ""
    ): String {
        return assembleCustomEndpointPath(
            customEndpoint = LINKS_KEY,
            subEndpoint = subEndpoint
        )
    }

    /**
     * Function to execute the request to get the collections of the user
     *
     * @param ownedOnly: whether to get only the collections where the user is the owner
     *
     * @return the result of the request as [JsonObject]
     *
     */
    @RequestPath(path = "/api/v1/users/{user_id}/collections", method = GET)
    suspend fun getCollections(
        ownedOnly: Boolean = false
    ): JsonObject {
        return execGet(
            endpoint = assembleCollectionsEndpointPath(),
            query = createOwnedOnlyQuery(
                ownedOnly = ownedOnly
            )
        )
    }

    /**
     * Function to execute the request to create a collection
     *
     * @param color: color of the collection
     * @param title Title of the collection
     * @param description: description of the collection
     * @param links: list of links shared in a collection
     *
     * @return the result of the request as [JsonObject]
     *
     */
    @RequestPath(path = "/api/v1/users/{user_id}/collections", method = POST)
    suspend fun createCollection(
        color: String,
        title: String,
        description: String,
        links: List<String>
    ): JsonObject {
        val payload = createCollectionPayload(
            color = color,
            title = title,
            description = description,
            links = links
        )
        return execPost(
            endpoint = assembleCollectionsEndpointPath(),
            payload = payload
        )
    }

    /**
     * Function to execute the request to edit a collection
     *
     * @param collection The collection to edit
     * @param color: color of the collection
     * @param title Title of the collection
     * @param description: description of the collection
     * @param links: list of links shared in a collection
     *
     * @return the result of the request as [JsonObject]
     *
     */
    @RequestPath(path = "/api/v1/users/{user_id}/collections/{collection_id}", method = PATCH)
    suspend fun editCollection(
        collection: LinksCollection,
        color: String,
        title: String,
        description: String,
        links: List<String>
    ): JsonObject {
        return editCollection(
            collectionId = collection.id,
            color = color,
            title = title,
            description = description,
            links = links
        )
    }

    /**
     * Function to execute the request to edit a collection
     *
     * @param collectionId The identifier of the collection to edit
     * @param color: color of the collection
     * @param title Title of the collection
     * @param description: description of the collection
     * @param links: list of links shared in a collection
     *
     * @return the result of the request as [JsonObject]
     *
     */
    @RequestPath(path = "/api/v1/users/{user_id}/collections/{collection_id}", method = PATCH)
    suspend fun editCollection(
        collectionId: String,
        color: String,
        title: String,
        description: String,
        links: List<String>
    ): JsonObject {
        val payload = createCollectionPayload(
            color = color,
            title = title,
            description = description,
            links = links
        )
        return execPatch(
            endpoint = assembleCollectionsEndpointPath(
                subEndpoint = collectionId
            ),
            payload = payload
        )
    }

    /**
     * Function to create a payload for the collection creation/edit actions
     *
     * @param color: color of the collection
     * @param title Title of the collection
     * @param description: description of the collection
     * @param links: list of links shared in a collection
     *
     * @return the payload as [JsonObject]
     *
     */
    @Assembler
    private fun createCollectionPayload(
        color: String,
        title: String,
        description: String,
        links: List<String>
    ): JsonObject {
        return buildJsonObject {
            put(COLLECTION_COLOR_KEY, color)
            put(TITLE_KEY, title)
            put(DESCRIPTION_KEY, description)
            put(LINKS_KEY, Json.encodeToJsonElement(links))
        }
    }

    /**
     * Function to execute the request to manage the links shared with the collection
     *
     * @param collection The collection where manage the shared link
     * @param links The list of links shared with the collection
     *
     * @return the result of the request as [JsonObject]
     *
     */
    @RequestPath(path = "/api/v1/users/{user_id}/collections/{collection_id}/links", method = PUT)
    suspend fun manageCollectionLinks(
        collection: LinksCollection,
        links: List<String>
    ): JsonObject {
        return manageCollectionLinks(
            collectionId = collection.id,
            links = links
        )
    }

    /**
     * Function to execute the request to manage the links shared with the collection
     *
     * @param collectionId The identifier of the collection where manage the shared link
     * @param links The list of links shared with the collection
     *
     * @return the result of the request as [JsonObject]
     *
     */
    @RequestPath(path = "/api/v1/users/{user_id}/collections/{collection_id}/links", method = PUT)
    suspend fun manageCollectionLinks(
        collectionId: String,
        links: List<String>
    ): JsonObject {
        val payload = buildJsonObject {
            put(LINKS_KEY, Json.encodeToJsonElement(links))
        }
        return execPut(
            endpoint = assembleCollectionsEndpointPath(
                subEndpoint = "$collectionId/$LINKS_KEY"
            ),
            payload = payload
        )
    }

    /**
     * Function to execute the request to manage the teams where the collection is shared
     *
     * @param collection The collection where manage the teams list
     * @param teams The list of the teams where the collection is shared
     *
     * @return the result of the request as [JsonObject]
     *
     */
    @RequestPath(path = "/api/v1/users/{user_id}/collections/{collection_id}/teams", method = PUT)
    suspend fun manageCollectionTeams(
        collection: LinksCollection,
        teams: List<String>
    ): JsonObject {
        return manageCollectionTeams(
            collectionId = collection.id,
            teams = teams
        )
    }

    /**
     * Function to execute the request to manage the teams where the collection is shared
     *
     * @param collectionId The identifier of the collection where manage the teams list
     * @param teams The list of the teams where the collection is shared
     *
     * @return the result of the request as [JsonObject]
     *
     */
    @RequestPath(path = "/api/v1/users/{user_id}/collections/{collection_id}/teams", method = PUT)
    suspend fun manageCollectionTeams(
        collectionId: String,
        teams: List<String>
    ): JsonObject {
        val payload = buildJsonObject {
            put(TEAMS_KEY, Json.encodeToJsonElement(teams))
        }
        return execPut(
            endpoint = assembleCollectionsEndpointPath(
                subEndpoint = "$collectionId/$TEAMS_KEY"
            ),
            payload = payload
        )
    }

    /**
     * Function to execute the request to get a collection
     *
     * @param collection The collection to get
     *
     * @return the result of the request as [JsonObject]
     *
     */
    @RequestPath(path = "/api/v1/users/{user_id}/collections/{collection_id}", method = GET)
    suspend fun getCollection(
        collection: LinksCollection
    ): JsonObject {
        return getCollection(
            collectionId = collection.id
        )
    }

    /**
     * Function to execute the request to get a collection
     *
     * @param collectionId The identifier of the collection to get
     *
     * @return the result of the request as [JsonObject]
     *
     */
    @RequestPath(path = "/api/v1/users/{user_id}/collections/{collection_id}", method = GET)
    suspend fun getCollection(
        collectionId: String
    ): JsonObject {
        return execGet(
            endpoint = assembleCollectionsEndpointPath(
                subEndpoint = collectionId
            )
        )
    }

    /**
     * Function to execute the request to delete a collection
     *
     * @param collection The collection to delete
     *
     * @return the result of the request as [JsonObject]
     *
     */
    @RequestPath(path = "/api/v1/users/{user_id}/collections/{collection_id}", method = DELETE)
    suspend fun deleteCollection(
        collection: LinksCollection
    ): JsonObject {
        return deleteCollection(
            collectionId = collection.id
        )
    }

    /**
     * Function to execute the request to delete a collection
     *
     * @param collectionId The identifier of the  collection to delete
     *
     * @return the result of the request as [JsonObject]
     *
     */
    @RequestPath(path = "/api/v1/users/{user_id}/collections/{collection_id}", method = DELETE)
    suspend fun deleteCollection(
        collectionId: String
    ): JsonObject {
        return execDelete(
            endpoint = assembleCollectionsEndpointPath(
                subEndpoint = collectionId
            )
        )
    }

    /**
     * Function to assemble the endpoint to make the request to the collections controller
     *
     * @param subEndpoint The sub-endpoint path of the url
     *
     * @return an endpoint to make the request as [String]
     */
    @Assembler
    private fun assembleCollectionsEndpointPath(
        subEndpoint: String = ""
    ): String {
        return assembleCustomEndpointPath(
            customEndpoint = COLLECTIONS_KEY,
            subEndpoint = subEndpoint
        )
    }

    /**
     * Function to execute the request to get the teams where the user is a member
     *
     * @param ownedOnly: whether to get only the teams where the user is the owner
     *
     * @return the result of the request as [JsonObject]
     *
     */
    @RequestPath(path = "/api/v1/users/{user_id}/teams", method = GET)
    suspend fun getTeams(
        ownedOnly: Boolean = false
    ): JsonObject {
        return execGet(
            endpoint = assembleTeamsEndpointPath(),
            query = createOwnedOnlyQuery(
                ownedOnly = ownedOnly
            )
        )
    }

    /**
     * Function to execute the request to get the potential members to add in a team
     *
     * No-any params required
     *
     * @return the result of the request as [JsonObject]
     *
     */
    @RequestPath(path = "/api/v1/users/{user_id}/teams/members", method = GET)
    suspend fun getPotentialMembers(): JsonObject {
        return execGet(
            endpoint = assembleTeamsEndpointPath(
                subEndpoint = MEMBERS_KEY
            )
        )
    }

    /*
     * Function to execute the request to create a team
     *
     * @param title Title of the team
     * @param logoPic The logo of the team
     * @param description: description of the team
     * @param members: list of the members in the team
     *
     * @return the result of the request as [JsonObject]
     *
     *
    @RequestPath(path = "/api/v1/users/{user_id}/teams", method = POST)
    fun createTeam(
        title: String,
        logoPic: String,
        description: String,
        members: List<String>
    ) : JsonObject {
        val body = createTeamPayload(
            title = title,
            logoPic = File(logoPic),
            description = description,
            members = members
        )
        return execMultipartRequest(
            endpoint = assembleTeamsEndpointPath(),
            body = body
        )
    }

    /**
     * Function to execute the request to edit a team
     *
     * @param team The team to edit
     * @param title Title of the team
     * @param logoPic The logo of the team
     * @param description: description of the team
     * @param members: list of the members in the team
     *
     * @return the result of the request as [JsonObject]
     *
     */
    @RequestPath(path = "/api/v1/users/{user_id}/teams/{team_id}", method = POST)
    fun editTeam(
        team: Team,
        title: String,
        logoPic: String,
        description: String,
        members: List<String>
    ) : JsonObject {
        return editTeam(
            teamId = team.id,
            title = title,
            logoPic = logoPic,
            description = description,
            members = members
        )
    }


     * Function to execute the request to edit a team
     *
     * @param teamId The identifier of the team to edit
     * @param title Title of the team
     * @param logoPic The logo of the team
     * @param description: description of the team
     * @param members: list of the members in the team
     *
     * @return the result of the request as [JsonObject]
     *
     *
    @RequestPath(path = "/api/v1/users/{user_id}/teams/{team_id}", method = POST)
    suspend fun editTeam(
        teamId: String,
        title: String,
        logoPic: String,
        description: String,
        members: List<String>
    ) : JsonObject {
        val body = createTeamPayload(
            title = title,
            logoPic = if(logoPic.contains(teamId))
                null
            else
                File(logoPic),
            description = description,
            members = members
        )
        return execMultipartRequest(
            endpoint = assembleTeamsEndpointPath(
                subEndpoint = teamId
            ),
            body = body
        )
    }


     * Function to create a payload for the team creation/edit actions
     *
     * @param title Title of the team
     * @param logoPic The logo of the team
     * @param description: description of the team
     * @param members: list of the members in the team
     *
     * @return the payload as [JsonObject]
     *
     *
    @Assembler
    private fun createTeamPayload(
        title: String,
        logoPic: File?,
        description: String,
        members: List<String>
    ) : MultipartBody {
        val body = MultipartBody.Builder().setType(MultipartBody.FORM)
            .addFormDataPart(
                TITLE_KEY,
                title,
            )
            .addFormDataPart(
                DESCRIPTION_KEY,
                description,
            )
            .addFormDataPart(
                MEMBERS_KEY,
                JSONArray(members).toString(),
            )
        logoPic?.let {
            body.addFormDataPart(
                LOGO_PIC_KEY,
                logoPic.name,
                logoPic.readBytes().toRequestBody("*".toMediaType())
            )
        }
        return body.build()
    }*/

    /**
     * Function to execute the request to manage the links shared with the team
     *
     * @param team The team where manage the shared link
     * @param links The list of links shared with the team
     *
     * @return the result of the request as [JsonObject]
     *
     */
    @RequestPath(path = "/api/v1/users/{user_id}/teams/{team_id}/links", method = PUT)
    suspend fun manageTeamLinks(
        team: Team,
        links: List<String>
    ): JsonObject {
        return manageTeamLinks(
            teamId = team.id,
            links = links
        )
    }

    /**
     * Function to execute the request to manage the links shared with the team
     *
     * @param teamId The team identifier where manage the shared link
     * @param links The list of links shared with the team
     *
     * @return the result of the request as [JsonObject]
     *
     */
    @RequestPath(path = "/api/v1/users/{user_id}/teams/{team_id}/links", method = PUT)
    suspend fun manageTeamLinks(
        teamId: String,
        links: List<String>
    ): JsonObject {
        val payload = buildJsonObject {
            put(LINKS_KEY, Json.encodeToJsonElement(links))
        }
        return execPut(
            endpoint = assembleTeamsEndpointPath(
                subEndpoint = "$teamId/$LINKS_KEY"
            ),
            payload = payload
        )
    }

    /**
     * Function to execute the request to manage the collections shared with the team
     *
     * @param team The team where manage the shared collections
     * @param collections The list of collections shared with the team
     *
     * @return the result of the request as [JsonObject]
     *
     */
    @RequestPath(path = "/api/v1/users/{user_id}/teams/{team_id}/collections", method = PUT)
    suspend fun manageTeamCollections(
        team: Team,
        collections: List<String>
    ): JsonObject {
        return manageTeamCollections(
            teamId = team.id,
            collections = collections
        )
    }

    /**
     * Function to execute the request to manage the collections shared with the team
     *
     * @param teamId The team identifier where manage the shared collections
     * @param collections The list of collections shared with the team
     *
     * @return the result of the request as [JsonObject]
     *
     */
    @RequestPath(path = "/api/v1/users/{user_id}/teams/{team_id}/collections", method = PUT)
    suspend fun manageTeamCollections(
        teamId: String,
        collections: List<String>
    ): JsonObject {
        val payload = buildJsonObject {
            put(COLLECTIONS_KEY, Json.encodeToJsonElement(collections))
        }
        return execPut(
            endpoint = assembleTeamsEndpointPath(
                subEndpoint = "$teamId/$COLLECTIONS_KEY"
            ),
            payload = payload
        )
    }

    /**
     * Function to execute the request to get a team
     *
     * @param team The team to get
     *
     * @return the result of the request as [JsonObject]
     *
     */
    @RequestPath(path = "/api/v1/users/{user_id}/teams/{team_id}", method = GET)
    suspend fun getTeam(
        team: Team
    ): JsonObject {
        return getTeam(
            teamId = team.id
        )
    }

    /**
     * Function to execute the request to get a team
     *
     * @param teamId The identifier of the team to get
     *
     * @return the result of the request as [JsonObject]
     *
     */
    @RequestPath(path = "/api/v1/users/{user_id}/teams/{team_id}", method = GET)
    suspend fun getTeam(
        teamId: String
    ): JsonObject {
        return execGet(
            endpoint = assembleTeamsEndpointPath(
                subEndpoint = teamId
            )
        )
    }

    /**
     * Function to execute the request to change the role of a member
     *
     * @param team The team to where change the member role
     * @param member The member to change its role
     * @param role The role of the member
     *
     * @return the result of the request as [JsonObject]
     *
     */
    @RequestPath(
        path = "/api/v1/users/{user_id}/teams/{team_id}/members/{member_id}/updateRole",
        method = PATCH
    )
    suspend fun changeMemberRole(
        team: Team,
        member: TeamMember,
        role: TeamRole
    ): JsonObject {
        return changeMemberRole(
            teamId = team.id,
            memberId = member.id,
            role = role
        )
    }

    /**
     * Function to execute the request to change the role of a member
     *
     * @param teamId The identifier of the team to where change the member role
     * @param memberId The identifier of the member to change its role
     * @param role The role of the member
     *
     * @return the result of the request as [JsonObject]
     *
     */
    @RequestPath(
        path = "/api/v1/users/{user_id}/teams/{team_id}/members/{member_id}/updateRole",
        method = PATCH
    )
    suspend fun changeMemberRole(
        teamId: String,
        memberId: String,
        role: TeamRole
    ): JsonObject {
        val payload = buildJsonObject {
            put(TEAM_ROLE_KEY, Json.encodeToJsonElement(role))
        }
        return execPatch(
            endpoint = assembleTeamsEndpointPath(
                subEndpoint = "$teamId/$MEMBERS_KEY/$memberId$UPDATE_MEMBER_ROLE_ENDPOINT"
            ),
            payload = payload
        )
    }

    /**
     * Function to execute the request to remove a member from the team
     *
     * @param team The team to where remove the member
     * @param member The member to remove
     *
     * @return the result of the request as [JsonObject]
     *
     */
    @RequestPath(
        path = "/api/v1/users/{user_id}/teams/{team_id}/members/{member_id}",
        method = DELETE
    )
    suspend fun removeMember(
        team: Team,
        member: TeamMember
    ): JsonObject {
        return removeMember(
            teamId = team.id,
            memberId = member.id
        )
    }

    /**
     * Function to execute the request to remove a member from the team
     *
     * @param teamId The identifier of the team to where remove the member
     * @param memberId The identifier of the member to remove
     *
     * @return the result of the request as [JsonObject]
     *
     */
    @RequestPath(
        path = "/api/v1/users/{user_id}/teams/{team_id}/members/{member_id}",
        method = DELETE
    )
    suspend fun removeMember(
        teamId: String,
        memberId: String
    ): JsonObject {
        return execDelete(
            endpoint = assembleTeamsEndpointPath(
                subEndpoint = "$teamId/$MEMBERS_KEY/$memberId"
            )
        )
    }

    /**
     * Function to execute the request to leave from a team
     *
     * @param team The team from leave
     *
     * @return the result of the request as [JsonObject]
     *
     */
    @RequestPath(path = "/api/v1/users/{user_id}/teams/{team_id}/leave}", method = DELETE)
    suspend fun leave(
        team: Team
    ): JsonObject {
        return leave(
            teamId = team.id
        )
    }

    /**
     * Function to execute the request to leave from a team
     *
     * @param teamId The identifier of the team from leave
     *
     * @return the result of the request as [JsonObject]
     *
     */
    @RequestPath(path = "/api/v1/users/{user_id}/teams/{team_id}/leave}", method = DELETE)
    suspend fun leave(
        teamId: String
    ): JsonObject {
        return execDelete(
            endpoint = assembleTeamsEndpointPath(
                subEndpoint = "$teamId$LEAVE_ENDPOINT"
            )
        )
    }

    /**
     * Function to execute the request to delete a team
     *
     * @param team The team to delete
     *
     * @return the result of the request as [JsonObject]
     *
     */
    @RequestPath(path = "/api/v1/users/{user_id}/teams/{team_id}", method = DELETE)
    suspend fun deleteTeam(
        team: Team
    ): JsonObject {
        return deleteTeam(
            teamId = team.id
        )
    }

    /**
     * Function to execute the request to delete a team
     *
     * @param teamId The identifier of the team to delete
     *
     * @return the result of the request as [JsonObject]
     *
     */
    @RequestPath(path = "/api/v1/users/{user_id}/teams/{team_id}", method = DELETE)
    suspend fun deleteTeam(
        teamId: String
    ): JsonObject {
        return execDelete(
            endpoint = assembleTeamsEndpointPath(
                subEndpoint = teamId
            )
        )
    }

    /**
     * Function to assemble the endpoint to make the request to the teams controller
     *
     * @param subEndpoint The sub-endpoint path of the url
     *
     * @return an endpoint to make the request as [String]
     */
    @Assembler
    private fun assembleTeamsEndpointPath(
        subEndpoint: String = ""
    ): String {
        return assembleCustomEndpointPath(
            customEndpoint = TEAMS_KEY,
            subEndpoint = subEndpoint
        )
    }

    /**
     * Function to assemble the query to manage the owned only query
     *
     * @param ownedOnly: whether to get only the teams where the user is the owner
     *
     * @return an endpoint to make the request as [String]
     */
    @Assembler
    private fun createOwnedOnlyQuery(
        ownedOnly: Boolean
    ): JsonObject {
        return buildJsonObject {
            put(OWNED_ONLY_KEY, ownedOnly)
        }
    }

    /**
     * Function to execute the request to get the custom links of the user
     *
     * No-any params
     *
     * @return the result of the request as [JsonObject]
     *
     */
    @RequestPath(path = "/api/v1/users/{user_id}/customLinks", method = GET)
    suspend fun getCustomLinks(): JsonObject {
        return execGet(
            endpoint = assembleCustomLinksEndpointPath()
        )
    }

    /**
     * Function to execute the request to create a custom link
     *
     * @param title Title of the link
     * @param description: description of the link
     * @param resources The resources to share with the link
     * @param fields The fields used to protect the [resources] with a validation form
     * @param hasUniqueAccess: whether the link, when requested for the first time, must be deleted and no more accessible
     * @param expiredTime: when the link expires and automatically deleted
     *
     * @return the result of the request as [JsonObject]
     *
     */
    @RequestPath(path = "/api/v1/users/{user_id}/customLinks", method = POST)
    suspend fun createCustomLink(
        title: String,
        description: String,
        resources: Map<String, String>,
        fields: Map<String, String>,
        hasUniqueAccess: Boolean = false,
        expiredTime: ExpiredTime = ExpiredTime.NO_EXPIRATION
    ): JsonObject {
        val payload = createCustomLinkPayload(
            title = title,
            description = description,
            resources = resources,
            fields = fields,
            hasUniqueAccess = hasUniqueAccess,
            expiredTime = expiredTime
        )
        return execPost(
            endpoint = assembleCustomLinksEndpointPath(),
            payload = payload
        )
    }

    /**
     * Function to execute the request to edit a custom link
     *
     * @param link The link to edit
     * @param title Title of the link
     * @param description: description of the link
     * @param resources The resources to share with the link
     * @param fields The fields used to protect the [resources] with a validation form
     * @param hasUniqueAccess: whether the link, when requested for the first time, must be deleted and no more accessible
     * @param expiredTime: when the link expires and automatically deleted
     *
     * @return the result of the request as [JsonObject]
     *
     */
    @RequestPath(path = "/api/v1/users/{user_id}/customLinks/{link_id}", method = PATCH)
    suspend fun editCustomLink(
        link: CustomRefyLink,
        title: String,
        description: String,
        resources: Map<String, String>,
        fields: Map<String, String>,
        hasUniqueAccess: Boolean = false,
        expiredTime: ExpiredTime = ExpiredTime.NO_EXPIRATION
    ): JsonObject {
        return editCustomLink(
            linkId = link.id,
            title = title,
            description = description,
            resources = resources,
            fields = fields,
            hasUniqueAccess = hasUniqueAccess,
            expiredTime = expiredTime
        )
    }

    /**
     * Function to execute the request to edit a custom link
     *
     * @param linkId The identifier of the link to edit
     * @param title Title of the link
     * @param description: description of the link
     * @param resources The resources to share with the link
     * @param fields The fields used to protect the [resources] with a validation form
     * @param hasUniqueAccess: whether the link, when requested for the first time, must be deleted and no more accessible
     * @param expiredTime: when the link expires and automatically deleted
     *
     * @return the result of the request as [JsonObject]
     *
     */
    @RequestPath(path = "/api/v1/users/{user_id}/customLinks/{link_id}", method = PATCH)
    suspend fun editCustomLink(
        linkId: String,
        title: String,
        description: String,
        resources: Map<String, String>,
        fields: Map<String, String>,
        hasUniqueAccess: Boolean = false,
        expiredTime: ExpiredTime = ExpiredTime.NO_EXPIRATION
    ): JsonObject {
        val payload = createCustomLinkPayload(
            title = title,
            description = description,
            resources = resources,
            fields = fields,
            hasUniqueAccess = hasUniqueAccess,
            expiredTime = expiredTime
        )
        return execPatch(
            endpoint = assembleCustomLinksEndpointPath(
                subEndpoint = linkId
            ),
            payload = payload
        )
    }

    /**
     * Function to create a payload for the custom link creation/edit actions
     *
     * @param title Title of the link
     * @param description: description of the link
     * @param resources The resources to share with the link
     * @param fields The fields used to protect the [resources] with a validation form
     * @param hasUniqueAccess: whether the link, when requested for the first time, must be deleted and no more accessible
     * @param expiredTime: when the link expires and automatically deleted
     *
     * @return the payload as [JsonObject]
     *
     */
    @Assembler
    private fun createCustomLinkPayload(
        title: String,
        description: String,
        resources: Map<String, String>,
        fields: Map<String, String>,
        hasUniqueAccess: Boolean,
        expiredTime: ExpiredTime
    ): JsonObject {
        return buildJsonObject {
            put(TITLE_KEY, title)
            put(DESCRIPTION_KEY, description)
            put(RESOURCES_KEY, Json.encodeToJsonElement(resources))
            put(FIELDS_KEY, Json.encodeToJsonElement(fields))
            put(UNIQUE_ACCESS_KEY, hasUniqueAccess)
            put(EXPIRED_TIME_KEY, Json.encodeToJsonElement(expiredTime))
        }
    }

    /**
     * Function to execute the request to get a custom link
     *
     * @param link The link to get
     *
     * @return the result of the request as [JsonObject]
     *
     */
    @RequestPath(path = "/api/v1/users/{user_id}/customLinks/{link_id}", method = GET)
    suspend fun getCustomLink(
        link: CustomRefyLink
    ): JsonObject {
        return getCustomLink(
            linkId = link.id
        )
    }

    /**
     * Function to execute the request to get a custom link
     *
     * @param linkId The identifier of the link to get
     *
     * @return the result of the request as [JsonObject]
     *
     */
    @RequestPath(path = "/api/v1/users/{user_id}/customLinks/{link_id}", method = GET)
    suspend fun getCustomLink(
        linkId: String
    ): JsonObject {
        return execGet(
            endpoint = assembleCustomLinksEndpointPath(
                subEndpoint = linkId
            )
        )
    }

    /**
     * Function to execute the request to delete a custom link
     *
     * @param link The link to delete
     *
     * @return the result of the request as [JsonObject]
     *
     */
    @RequestPath(path = "/api/v1/users/{user_id}/customLinks/{link_id}", method = DELETE)
    suspend fun deleteCustomLink(
        link: CustomRefyLink
    ): JsonObject {
        return deleteCustomLink(
            linkId = link.id
        )
    }

    /**
     * Function to execute the request to delete a custom link
     *
     * @param linkId The identifier of the link to delete
     *
     * @return the result of the request as [JsonObject]
     *
     */
    @RequestPath(path = "/api/v1/users/{user_id}/customLinks/{link_id}", method = DELETE)
    suspend fun deleteCustomLink(
        linkId: String
    ): JsonObject {
        return execDelete(
            endpoint = assembleCustomLinksEndpointPath(
                subEndpoint = linkId
            )
        )
    }

    /**
     * Function to assemble the endpoint to make the request to the custom links controller
     *
     * @param subEndpoint The sub-endpoint path of the url
     *
     * @return an endpoint to make the request as [String]
     */
    private fun assembleCustomLinksEndpointPath(
        subEndpoint: String = ""
    ): String {
        return assembleCustomEndpointPath(
            customEndpoint = CUSTOM_LINKS_ENDPOINT,
            subEndpoint = subEndpoint
        )
    }

}