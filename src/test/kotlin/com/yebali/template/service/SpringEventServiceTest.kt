package com.yebali.template.service

import com.yebali.template.SpringBootTestSupport
import com.yebali.template.repository.TeamRepository
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class SpringEventServiceTest : SpringBootTestSupport() {
    @Autowired
    private lateinit var springEventService: SpringEventService

    @Autowired
    private lateinit var teamRepository: TeamRepository

    @BeforeEach
    fun init() {
        teamRepository.deleteAll()
    }

    @Nested
    // @EventListener는 기본적으로는 Tx에 참여하지 않고 동기적으로 바로 호출된다.
    inner class EventListenerTest {
        @Test
        fun `Transaction 밖에서 호출된 @EventListener은 Transacntion에 참여하지 않는다`() {
            runCatching { springEventService.publishWithoutTx() }

            // Tx에 참여하지 않았기 때문에 team이 생성된다.
            Assertions.assertThat(teamRepository.findAll()).isNotEmpty()
        }

        @Test
        fun `Transaction 내에서 호출된 @EventListener은 Transaction에 참여한다`() {
            runCatching { springEventService.publish(isTransactionalListener = false, isBeforeCommit = false, isAsync = false) }

            // Tx 안에서 이벤트를 발행하면 Tx에 참여하게 된다. 따라서 team은 생성되지 않는다.
            Assertions.assertThat(teamRepository.findAll()).isEmpty()
        }

        @Test
        fun `Transaction 내에서 호출된 @Async + @EventListener은 Transaction에 참여하지 않는다`() {
            runCatching { springEventService.publish(isTransactionalListener = false, isBeforeCommit = false, isAsync = true) }

            // Tx은 Thread 종속적이기 떄문에, @Async로 인해 이벤트 핸들링이 다른 스레드에서 일어나 Tx에 참여하지 못한다.
            // 그렇기 때문에 team은 생성된다.
            Assertions.assertThat(teamRepository.findAll()).isNotEmpty()
        }
    }

    @Nested
    inner class TransactionEventListenerTestBeforeCommit {
        @Test
        fun `@TransactionalEventListener(phase = BEFORE_COMMIT)은 Transaction에 참여한다`() {
            runCatching { springEventService.publish(isTransactionalListener = true, isBeforeCommit = true, isAsync = false) }

            // Tx에 참여했기 때문에 team은 생성되지 못한다.
            Assertions.assertThat(teamRepository.findAll()).isEmpty()
        }

        @Test
        fun `@Async + @TransactionalEventListener(phase = BEFORE_COMMIT)은 Transaction에 참여하지 않는다`() {
            runCatching { springEventService.publish(isTransactionalListener = true, isBeforeCommit = true, isAsync = true) }

            // Tx은 Thread 종속적이기 떄문에, @Async로 인해 이벤트 핸들링이 다른 스레드에서 일어나 Tx에 참여하지 못한다.
            // 그렇기 때문에 team은 생성된다.
            Assertions.assertThat(teamRepository.findAll()).isNotEmpty()
        }
    }

    @Nested
    inner class TransactionEventListenerTestAfterCommit {
        @Test
        fun `@TransactionalEventListener(phase = AFTER_COMMIT)은 Transacntion에 참여하지 않는다`() {
            runCatching { springEventService.publish(isTransactionalListener = true, isBeforeCommit = false, isAsync = false) }

            // Tx에 참여하지 않았기 때문에 team은 생성된다.
            Assertions.assertThat(teamRepository.findAll()).isNotEmpty()
        }
    }
}
