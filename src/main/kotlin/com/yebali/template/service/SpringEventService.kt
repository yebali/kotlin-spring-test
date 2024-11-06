package com.yebali.template.service

import com.yebali.template.entity.Team
import com.yebali.template.event.message.AsyncEventListenerEvent
import com.yebali.template.event.message.AsyncTransactionalEventListenerBeforeCommitEvent
import com.yebali.template.event.message.EventListenerEvent
import com.yebali.template.event.message.TransactionalEventListenerAfterCommitEvent
import com.yebali.template.event.message.TransactionalEventListenerBeforeCommitEvent
import com.yebali.template.repository.TeamRepository
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class SpringEventService(
    private val teamRepository: TeamRepository,
    private val applicationEventPublisher: ApplicationEventPublisher,
) {
    @Transactional
    fun publish(isTransactionalListener: Boolean, isBeforeCommit: Boolean, isAsync: Boolean) {
        println("[Publish] Thread Name: ${Thread.currentThread().name}-${Thread.currentThread().threadId()}")

        // Create a new team
        teamRepository.save(
            Team(name = "New Team"),
        )

        when {
            !isTransactionalListener && !isAsync -> applicationEventPublisher.publishEvent(
                EventListenerEvent(),
            )
            !isTransactionalListener && isAsync -> applicationEventPublisher.publishEvent(
                AsyncEventListenerEvent(),
            )
            isBeforeCommit && !isAsync -> applicationEventPublisher.publishEvent(
                TransactionalEventListenerBeforeCommitEvent(),
            )
            isBeforeCommit && isAsync -> applicationEventPublisher.publishEvent(
                AsyncTransactionalEventListenerBeforeCommitEvent(),
            )
            !isBeforeCommit -> applicationEventPublisher.publishEvent(
                TransactionalEventListenerAfterCommitEvent(),
            )

            else -> {}
        }
    }

    fun publishWithoutTx() {
        println("[PublishWithoutTx] Thread Name: ${Thread.currentThread().name}-${Thread.currentThread().threadId()}")

        // Create a new team
        teamRepository.save(
            Team(name = "New Team"),
        )

        applicationEventPublisher.publishEvent(
            EventListenerEvent(),
        )
    }
}
