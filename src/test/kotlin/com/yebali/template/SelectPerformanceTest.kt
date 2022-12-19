package com.yebali.template

import com.yebali.template.entity.Card
import com.yebali.template.entity.Member
import com.yebali.template.entity.Team
import com.yebali.template.repository.CardRepository
import com.yebali.template.repository.MemberRepository
import com.yebali.template.repository.TeamRepository
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
        val teamCount = 100
        val memberCountPerTeam = 100
        val cardCountPerMember = 100

        val teamsToInsert = (0..teamCount).map { Team(name = "team-$it") }

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
            (0..memberCountPerTeam).map {
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
            (0..cardCountPerMember).map { Card(name = "card-${member.id}-$it", member = member) }
        }

        jdbcTemplate.batchUpdate(
            """insert into card(name, member_id) values (?, ?)""",
            object : BatchPreparedStatementSetter {
                override fun setValues(ps: PreparedStatement, i: Int) {
                    ps.setString(1, cardToInsert[i].name)
                    ps.setLong(2, cardToInsert[i].member!!.id)
                }

                override fun getBatchSize() = memberCountPerTeam * cardCountPerMember
            }
        )

        println("inserted team count:${teamRepository.findAll().count()}")
        println("inserted member count:${memberRepository.findAll().count()}")
        println("inserted card count:${cardRepository.findAll().count()}")

        em.clear()
    }

    @Test
    fun lazy_loading() {
        val measuredTime = measureTimeMillis {
            val teamIds = (1..100 step 5).map { it.toLong() }
            val teams = teamRepository.findAllByIdIn(teamIds)

            teams.forEach { team ->
                team.members.forEach { member ->
                    member.cards.forEach { card ->
                        println("card = ${card.name}")
                    }
                }
            }
        }

        println(measuredTime)
    }

    @Test
    fun fetch_join() {
        val measuredTime = measureTimeMillis {
            val teamIds = (1..100 step 5).map { it.toLong() }
            val teams = teamRepository.findTeamByIdWithFetch(teamIds)

            teams.forEach { team ->
                team.members.forEach { member ->
                    member.cards.forEach { card ->
                        println("card = ${card.name}")
                    }
                }
            }
        }

        println(measuredTime)
    }
}
