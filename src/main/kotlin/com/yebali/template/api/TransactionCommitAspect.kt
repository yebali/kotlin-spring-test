package com.yebali.template.api

import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.springframework.stereotype.Component
import org.springframework.transaction.support.TransactionSynchronization
import org.springframework.transaction.support.TransactionSynchronizationManager
import java.util.UUID

@Aspect
@Component
class TransactionCommitAspect {

    @Around("@annotation(org.springframework.transaction.annotation.Transactional)")
    fun aroundTransactionalMethod(joinPoint: ProceedingJoinPoint): Any? {
        val transactionId = UUID.randomUUID()

        println("[TransactionCommitAspect] Thread Name: ${Thread.currentThread().name}-${Thread.currentThread().threadId()}")
        println("[TransactionCommitAspect] Transaction Start. Transaction ID: $transactionId")

        // TransactionSynchronization 등록
        TransactionSynchronizationManager.registerSynchronization(object : TransactionSynchronization {
            override fun afterCommit() {
                println("[TransactionCommitAspect] Transaction committed successfully. $transactionId")
            }

            override fun afterCompletion(status: Int) {
                println("[TransactionCommitAspect] Transaction completed. $transactionId")
            }
        })

        return joinPoint.proceed()
    }
}
