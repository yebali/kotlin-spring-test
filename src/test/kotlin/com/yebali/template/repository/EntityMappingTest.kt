package com.yebali.template.repository

import com.yebali.template.entity.EmbeddableMember
import com.yebali.template.entity.Member
import com.yebali.template.entity.Team
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.test.annotation.Rollback
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager

@SpringBootTest
@Transactional
@Rollback(false)
class EntityMappingTest {
    @Autowired
    private lateinit var teamRepository: TeamRepository

    @Autowired
    private lateinit var memberRepository: MemberRepository

    @Autowired
    private lateinit var em: EntityManager

    private lateinit var team: Team

    @BeforeEach
    fun setupEntities() {
        println("====== SET UP START======")
        team = Team(
            name = "my team",
            embeddableMember = EmbeddableMember("embeddable")
        )

        team.addMember(member = Member(name = "member1"))
        team.addMember(member = Member(name = "member2"))
        team.addMember(member = Member(name = "member3"))
        team.addMember(member = Member(name = "member4"))
        team.addMember(member = Member(name = "member5"))

        teamRepository.saveAndFlush(team)

        println("====== SET UP END ======")
    }

    @Test
    fun selectTeam() {
        println("====== SELECT TEAM START ======")

        teamRepository.findTeamById(team.id)
        em.flush()

        println("====== SELECT TEAM END ======")
    }

    @Test
    fun deleteTeam() {
        println("====== DELETE TEAM START ======")

        teamRepository.delete(team)
        em.flush()

        println("====== DELETE TEAM END ======")
    }

    @Test
    fun removeMember() {
        println("====== REMOVE MEMBER START ======")

        val member = memberRepository.findByIdOrNull(2L) ?: throw Exception()
        team.embeddableMember.members.remove(member)
        em.flush()

        println("====== REMOVE MEMBER END ======")
    }

    @Test
    fun deleteMember() {
        println("====== SELECT MEMBER START ======")

        val member = memberRepository.findByIdOrNull(2L) ?: throw Exception()
        memberRepository.delete(member)
        em.flush()

        println("====== SELECT MEMBER END ======")
    }

    @Test
    fun updateTeam() {
        println("====== UPDATE TEAM START ======")

        team.name = "your team"
        em.flush()

        println("====== UPDATE TEAM END ======")
    }
}
