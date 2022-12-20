package com.yebali.template

import com.yebali.template.entity.Card
import com.yebali.template.entity.Member
import com.yebali.template.entity.Team
import com.yebali.template.repository.CardRepository
import com.yebali.template.repository.MemberRepository
import com.yebali.template.repository.TeamRepository
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.core.BatchPreparedStatementSetter
import org.springframework.jdbc.core.JdbcTemplate
import java.sql.PreparedStatement
import javax.persistence.EntityManager
import javax.transaction.Transactional
import kotlin.system.measureTimeMillis

@SpringBootTest
@Transactional
class SelectPerformanceTest {
    @Autowired
    private lateinit var teamRepository: TeamRepository

    @Autowired
    private lateinit var memberRepository: MemberRepository

    @Autowired
    private lateinit var cardRepository: CardRepository

    @Autowired
    private lateinit var em: EntityManager

    @Autowired
    private lateinit var jdbcTemplate: JdbcTemplate

    @BeforeEach
    fun init() {
        val teamCount = 20
        val memberCountPerTeam = 100
        val cardCountPerMember = 50

        val teamsToInsert = (1..teamCount).map { Team(name = "team-$it") }

        jdbcTemplate.batchUpdate(
            """insert into team(name) values (?)""",
            object : BatchPreparedStatementSetter {
                override fun setValues(ps: PreparedStatement, i: Int) {
                    ps.setString(1, teamsToInsert[i].name)
                }

                override fun getBatchSize() = teamCount
            }
        )

        val teams = teamRepository.findAll()
        val memberToInsert = teams.flatMap { team ->
            (1..memberCountPerTeam).map {
                Member(name = "member-${team.id}-$it", team = team)
            }
        }

        jdbcTemplate.batchUpdate(
            """insert into member(name, team_id) values (?, ?)""",
            object : BatchPreparedStatementSetter {
                override fun setValues(ps: PreparedStatement, i: Int) {
                    ps.setString(1, memberToInsert[i].name)
                    ps.setLong(2, memberToInsert[i].team!!.id)
                }

                override fun getBatchSize() = teamCount * memberCountPerTeam
            }
        )

        val members = memberRepository.findAll()
        val cardToInsert = members.flatMap { member ->
            (1..cardCountPerMember).map { Card(name = "card-${member.id}-$it", member = member) }
        }

        jdbcTemplate.batchUpdate(
            """insert into card(name, member_id) values (?, ?)""",
            object : BatchPreparedStatementSetter {
                override fun setValues(ps: PreparedStatement, i: Int) {
                    ps.setString(1, cardToInsert[i].name)
                    ps.setLong(2, cardToInsert[i].member!!.id)
                }

                override fun getBatchSize() = teamCount * memberCountPerTeam * cardCountPerMember
            }
        )

        println("inserted team count:${teamRepository.findAll().count()}")
        println("inserted member count:${memberRepository.findAll().count()}")
        println("inserted card count:${cardRepository.findAll().count()}")

        em.clear()
    }

    @AfterEach
    fun deleteAll() {
        cardRepository.deleteAll()
        memberRepository.deleteAll()
        teamRepository.deleteAll()
    }

    @Test
    fun lazy_loading() {
        val measuredTime = measureTimeMillis {
            val teams = teamRepository.findAllWithoutFetch()

            teams.forEach { team ->
                team.members.forEach { member ->
                    member.cards.forEach { card ->
                        card.name
                    }
                }
            }
        }

        println("$measuredTime ms")
    }

    @Test
    fun fetch_join() {
        val measuredTime = measureTimeMillis {
            val teams = teamRepository.findAllWithFetch()

            teams.forEach { team ->
                team.members.forEach { member ->
                    member.cards.forEach { card ->
                        card.name
                    }
                }
            }
        }

        println("$measuredTime ms")
    }

    @Test
    fun entity_graph() {
        val measuredTime = measureTimeMillis {
            val teams = teamRepository.findAll()

            teams.forEach { team ->
                team.members.forEach { member ->
                    member.cards.forEach { card ->
                        card.name
                    }
                }
            }
        }

        println("$measuredTime ms")
    }
}
