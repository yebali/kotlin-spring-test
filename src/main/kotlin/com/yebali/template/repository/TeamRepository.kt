package com.yebali.template.repository

import com.yebali.template.entity.Team
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository

interface TeamRepository : CustomTeamRepository, JpaRepository<Team, Long> {

    @EntityGraph(attributePaths = ["members", "members.cards"])
    override fun findAll(): List<Team>
}
