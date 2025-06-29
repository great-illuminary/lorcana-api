package eu.codlab.lorcana.rph.sync.extensions

import eu.codlab.lorcana.rph.event.EventSettings

fun EventSettings.toSync(original: eu.codlab.lorcana.rph.sync.event.EventSettings? = null) =
    eu.codlab.lorcana.rph.sync.event.EventSettings(
        id = id,
        decklistStatus = decklistStatus,
        eventLifecycleStatus = eventLifecycleStatus,
        showRegistrationButton = showRegistrationButton,
        roundDurationInMinutes = roundDurationInMinutes,
        paymentInStore = paymentInStore,
        paymentOnSpicerack = paymentOnSpicerack,
        maximumNumberOfGameWinsPerMatch = maximumNumberOfGameWinsPerMatch,
        maximumNumberOfDrawsPerMatch = maximumNumberOfDrawsPerMatch,
        checkinMethodsCommaSeparated = checkinMethods.joinToString(","),
        stripePriceId = stripePriceId
    )

fun eu.codlab.lorcana.rph.sync.event.EventSettings.isEquals(other: EventSettings): Boolean {
    if (id != other.id) return false
    if (decklistStatus != other.decklistStatus) return false
    if (eventLifecycleStatus != other.eventLifecycleStatus) return false
    if (showRegistrationButton != other.showRegistrationButton) return false
    if (roundDurationInMinutes != other.roundDurationInMinutes) return false
    if (paymentInStore != other.paymentInStore) return false
    if (paymentOnSpicerack != other.paymentOnSpicerack) return false
    if (maximumNumberOfGameWinsPerMatch != other.maximumNumberOfGameWinsPerMatch) return false
    if (maximumNumberOfDrawsPerMatch != other.maximumNumberOfDrawsPerMatch) return false
    if (checkinMethodsCommaSeparated != other.checkinMethods.joinToString(",")) return false
    if (stripePriceId != other.stripePriceId) return false

    return true
}
