package com.yebali.template.repository

import com.querydsl.jpa.impl.JPAQueryFactory
import com.yebali.template.entity.QTeam.team
import com.yebali.template.entity.Team

class CustomTeamRepositoryImpl(
    private val jpaQueryFactory: JPAQueryFactory
) : CustomTeamRepository {
    override fun findTeamById(teamId: Long): List<Team> {
        return jpaQueryFactory.selectFrom(team)
            .leftJoin(team.embeddableMember.members).fetchJoin()
            .where(team.id.eq(teamId))
            .fetch()
    }
}
