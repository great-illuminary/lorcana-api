package eu.codlab.lorcana.rph.sync.extensions

import eu.codlab.lorcana.rph.event.Event

fun Event.toSyncEvent(
    cached: eu.codlab.lorcana.rph.sync.event.Event?
) = eu.codlab.lorcana.rph.sync.event.Event(
    id = id,
    fullHeaderImageUrl = fullHeaderImageUrl,
    startDatetimeISO = startDatetimeISO,
    startDatetime = startDateTime?.unixMillisLong,
    endDatetimeISO = endDatetime,
    day2StartDatetimeISO = day2StartDatetimeISO,
    day2StartDatetime = day2StartDateTime?.unixMillisLong,
    timerEndDatetime = timerEndDatetime,
    timerPausedAtDatetime = timerPausedAtDatetime,
    timerIsRunning = timerIsRunning,
    description = description,
// tournamentPhases: List<TournamentPhase>, -> computed list
    registeredUserCount = registeredUserCount,
    fullAddress = fullAddress,
    convention = convention,
    displayStatus = displayStatus?.name,
    name = name,
    pinnedByStore = pinnedByStore,
    useVerbatimName = useVerbatimName,
    queueStatus = queueStatus,
    gameType = gameType.name,
    source = source,
    eventStatus = eventStatus.name,
    eventFormat = eventFormat.name,
    eventType = eventType.name,
    pairingSystem = pairingSystem,
    rulesEnforcementLevel = rulesEnforcementLevel.name,
    timezone = timezone,
    eventAddressOverride = eventAddressOverride,
    eventIsOnline = eventIsOnline,
    latitude = latitude,
    longitude = longitude,
    costInCents = costInCents,
    currency = currency,
    capacity = capacity,
    url = url,
    numberOfRcInvites = numberOfRcInvites,
    topCutSize = topCutSize,
    numberOfRounds = numberOfRounds,
    numberOfDays = numberOfDays,
    isHeadliningEvent = isHeadliningEvent,
    isOnDemand = isOnDemand,
    preventSync = preventSync,
    headerImage = headerImage,
    startingTableNumber = startingTableNumber,
    endingTableNumber = endingTableNumber,
    adminListDisplayOrder = adminListDisplayOrder,
    prizesAwarded = prizesAwarded,
    isTestEvent = isTestEvent,
    isTemplate = isTemplate,
    taxEnabled = taxEnabled,
    polymorphicCtype = polymorphicCtype,
    game = game,
    productList = productList,
    eventFactoryCreatedBy = eventFactoryCreatedBy,
    eventConfigurationTemplate = eventConfigurationTemplate,
    bannerImage = bannerImage,
    phaseTemplateGroupId = phaseTemplateGroupId,

    // foreign keys
    settingsId = settings.id,
    storeId = store.id(),
    gameplayFormatId = gameplayFormat.id,

    updatedPostEvent = cached?.updatedPostEvent ?: false
)

fun eu.codlab.lorcana.rph.sync.event.Event.isEquals(other: Event): Boolean {
    if (id != other.id) return false
    if (timerIsRunning != other.timerIsRunning) return false
    if (registeredUserCount != other.registeredUserCount) return false
    if (pinnedByStore != other.pinnedByStore) return false
    if (useVerbatimName != other.useVerbatimName) return false
    if (eventIsOnline != other.eventIsOnline) return false
    if (latitude != other.latitude) return false
    if (longitude != other.longitude) return false
    if (costInCents != other.costInCents) return false
    if (capacity != other.capacity) return false
    if (numberOfRcInvites != other.numberOfRcInvites) return false
    if (topCutSize != other.topCutSize) return false
    if (numberOfRounds != other.numberOfRounds) return false
    if (numberOfDays != other.numberOfDays) return false
    if (isHeadliningEvent != other.isHeadliningEvent) return false
    if (isOnDemand != other.isOnDemand) return false
    if (preventSync != other.preventSync) return false
    if (startingTableNumber != other.startingTableNumber) return false
    if (endingTableNumber != other.endingTableNumber) return false
    if (adminListDisplayOrder != other.adminListDisplayOrder) return false
    if (prizesAwarded != other.prizesAwarded) return false
    if (isTestEvent != other.isTestEvent) return false
    if (isTemplate != other.isTemplate) return false
    if (taxEnabled != other.taxEnabled) return false
    if (polymorphicCtype != other.polymorphicCtype) return false
    if (game != other.game) return false
    if (bannerImage != other.bannerImage) return false
    if (settingsId != other.settings.id) return false
    if (storeId != other.store.id()) return false
    if (gameplayFormatId != other.gameplayFormat.id) return false
    //TODO update the images one day ?
    // if (fullHeaderImageUrl != other.fullHeaderImageUrl) return false
    if (startDatetimeISO != other.startDatetimeISO) return false
    if (startDatetime != other.startDateTime?.unixMillisLong) return false
    if (endDatetimeISO != other.endDatetime) return false
    if (day2StartDatetimeISO != other.day2StartDatetimeISO) return false
    if (day2StartDatetime != other.day2StartDateTime?.unixMillisLong) return false
    if (timerEndDatetime != other.timerEndDatetime) return false
    if (timerPausedAtDatetime != other.timerPausedAtDatetime) return false
    if (description != other.description) return false
    if (fullAddress != other.fullAddress) return false
    if (convention != other.convention) return false
    if (displayStatus != other.displayStatus?.name) return false
    if (name != other.name) return false
    if (queueStatus != other.queueStatus) return false
    if (gameType != other.gameType.name) return false
    if (source != other.source) return false
    if (eventStatus != other.eventStatus.name) return false
    if (eventFormat != other.eventFormat.name) return false
    if (eventType != other.eventType.name) return false
    if (pairingSystem != other.pairingSystem) return false
    if (rulesEnforcementLevel != other.rulesEnforcementLevel.name) return false
    if (timezone != other.timezone) return false
    if (eventAddressOverride != other.eventAddressOverride) return false
    if (currency != other.currency) return false
    if (url != other.url) return false
    if (headerImage != other.headerImage) return false
    if (productList != other.productList) return false
    if (eventFactoryCreatedBy != other.eventFactoryCreatedBy) return false
    if (eventConfigurationTemplate != other.eventConfigurationTemplate) return false
    if (phaseTemplateGroupId != other.phaseTemplateGroupId) return false

    return true
}

