package com.yebali.template.repository

import com.yebali.template.entity.Team
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface TeamRepository : CustomTeamRepository, JpaRepository<Team, Long> {

    @EntityGraph(attributePaths = ["members", "members.cards"])
    override fun findAll(): List<Team>

    @Query("select t from Team t")
    fun findAllByQueryAnnotation(): List<Team>
}
