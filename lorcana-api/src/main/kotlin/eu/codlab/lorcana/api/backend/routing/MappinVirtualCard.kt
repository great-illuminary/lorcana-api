package eu.codlab.lorcana.api.backend.routing

import eu.codlab.lorcana.abilities.Ability
import eu.codlab.lorcana.cards.CardThirdParty
import eu.codlab.lorcana.cards.CardTranslations
import eu.codlab.lorcana.cards.CardType
import eu.codlab.lorcana.cards.ClassificationHolder
import eu.codlab.lorcana.cards.ErratasClassification
import eu.codlab.lorcana.cards.InkColor
import eu.codlab.lorcana.cards.VariantRarity
import eu.codlab.lorcana.franchises.Franchise
import eu.codlab.lorcana.raw.Ravensburger
import eu.codlab.lorcana.raw.SetDescription
import kotlinx.serialization.SerialName

data class MappingVariantClassification(
    val set: SetDescription,
    val id: Int,
    val dreamborn: String,
    val ravensburger: Ravensburger,
    val rarity: VariantRarity,
    val illustrator: String? = null,
    // Language -> String
    val erratas: Map<String, ErratasClassification>? = null
)

data class MappingVirtualCard(
    val cost: Int = 0,
    val inkwell: Boolean = false,
    @SerialName("move_cost")
    val moveCost: Int? = null,
    val attack: Int? = null,
    val defence: Int? = null,
    val variants: List<MappingVariantClassification> = emptyList(),
    @Deprecated("Please move to the colors holder")
    val color: InkColor,
    val colors: List<InkColor>,
    val lore: Int? = null,
    val type: CardType,
    val classifications: List<ClassificationHolder> = emptyList(),
    val illustrator: String = "",
    val languages: CardTranslations,
    val actions: List<Ability> = emptyList(),
    val franchise: Franchise,
    @SerialName("third_party")
    val thirdParty: CardThirdParty? = null
)
