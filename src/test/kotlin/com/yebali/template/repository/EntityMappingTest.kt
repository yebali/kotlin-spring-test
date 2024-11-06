package com.yebali.template.repository

import com.yebali.template.SpringBootTestSupport
import com.yebali.template.entity.Member
import com.yebali.template.entity.Team
import jakarta.persistence.EntityManager
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.annotation.Rollback
import org.springframework.transaction.annotation.Transactional

@Transactional
@Rollback
class EntityMappingTest : SpringBootTestSupport() {
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
            members = mutableListOf(),
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

        teamRepository.findById(team.id)
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

        val member = memberRepository.findByName("member1") ?: throw Exception()
        team.members.remove(member)
        em.flush()

        println("====== REMOVE MEMBER END ======")
    }

    @Test
    fun deleteMember() {
        println("====== SELECT MEMBER START ======")

        val member = memberRepository.findByName("member1") ?: throw Exception()
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
