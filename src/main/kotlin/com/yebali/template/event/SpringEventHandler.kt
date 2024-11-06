package com.yebali.template.event

import com.yebali.template.event.message.AsyncEventListenerEvent
import com.yebali.template.event.message.AsyncTransactionalEventListenerBeforeCommitEvent
import com.yebali.template.event.message.EventListenerEvent
import com.yebali.template.event.message.TransactionalEventListenerAfterCommitEvent
import com.yebali.template.event.message.TransactionalEventListenerBeforeCommitEvent
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase.AFTER_COMMIT
import org.springframework.transaction.event.TransactionPhase.BEFORE_COMMIT
import org.springframework.transaction.event.TransactionalEventListener

@Component
class SpringEventHandler {

    @EventListener
    fun handleByEventListener(event: EventListenerEvent) {
        println("[EventListener] Thread Name: ${Thread.currentThread().name}-${Thread.currentThread().threadId()}")
        println("[EventListener] Error Occurred")
        throw RuntimeException()
    }

    @Async
    @EventListener
    fun handleByAsyncEventListener(event: AsyncEventListenerEvent) {
        println("[AsyncEventListener] Thread Name: ${Thread.currentThread().name}-${Thread.currentThread().threadId()}")
        println("[AsyncEventListener] Error Occurred")
        throw RuntimeException()
    }

    @TransactionalEventListener(phase = BEFORE_COMMIT)
    fun handleByTransactionalEventListenerBeforeCommit(event: TransactionalEventListenerBeforeCommitEvent) {
        println("[TransactionalEventListenerBeforeCommit] Thread Name: ${Thread.currentThread().name}-${Thread.currentThread().threadId()}")
        println("[TransactionalEventListenerBeforeCommit] Error Occurred")
        throw RuntimeException()
    }

    @Async
    @TransactionalEventListener(phase = BEFORE_COMMIT)
    fun handleByAsyncTransactionalEventListenerBeforeCommit(event: AsyncTransactionalEventListenerBeforeCommitEvent) {
        println("[AsyncTransactionalEventListenerBeforeCommit] Thread Name: ${Thread.currentThread().name}-${Thread.currentThread().threadId()}")
        println("[AsyncTransactionalEventListenerBeforeCommit] Error Occurred")
        throw RuntimeException()
    }

    @TransactionalEventListener(phase = AFTER_COMMIT)
    fun handleByTransactionalEventListenerAfterCommit(event: TransactionalEventListenerAfterCommitEvent) {
        println("[TransactionalEventListenerAfterCommit] Thread Name: ${Thread.currentThread().name}-${Thread.currentThread().threadId()}")
        println("[TransactionalEventListenerAfterCommit] Error Occurred")
        throw RuntimeException()
    }
}
