package com.yebali.template.service

import com.yebali.template.repository.CardRepository
import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
@Transactional
class CardService(
    private val cardRepository: CardRepository,
) {
    fun updateCardName(id: Long, name: String) {
        val card = cardRepository.findByIdOrNull(id)
            ?: throw IllegalArgumentException("Card not found")

        card.name = name
    }
}
