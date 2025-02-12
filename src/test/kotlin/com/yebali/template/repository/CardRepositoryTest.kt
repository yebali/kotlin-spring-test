package com.yebali.template.repository

import com.yebali.template.SpringBootTestSupport
import com.yebali.template.entity.Card
import com.yebali.template.service.CardService
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull

class CardRepositoryTest : SpringBootTestSupport() {
    @Autowired
    lateinit var cardRepository: CardRepository

    @Autowired
    lateinit var cardService: CardService

    @Test
    fun `insert Card`() {
        val createdCard = cardRepository.save(Card(name = "card1"))

        Thread.sleep(1000)

        cardService.updateCardName(createdCard.id, "card2")

        val updatedCards = cardRepository.findByIdOrNull(createdCard.id)
            ?: throw IllegalArgumentException("Card not found")

        Assertions.assertThat(updatedCards.createdAt).isNotEqualTo(updatedCards.updatedAt)
    }
}
