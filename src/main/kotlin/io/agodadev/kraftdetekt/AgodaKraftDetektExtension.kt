package io.agodadev.kraftdetekt

import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.RuleSet
import io.gitlab.arturbosch.detekt.api.RuleSetProvider

class AgodaKraftDetektExtension : RuleSetProvider {
    override val ruleSetId: String = "agoda-kraft"

    override fun instance(config: Config): RuleSet {
        return RuleSet(
            ruleSetId,
            listOf(
                IgnoredReturnValueRule(config)
                // Add other rules here when there is new ones
            )
        )
    }
}