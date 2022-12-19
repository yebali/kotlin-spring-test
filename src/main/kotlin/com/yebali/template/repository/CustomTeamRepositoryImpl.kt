package com.yebali.template.repository

import com.querydsl.jpa.impl.JPAQueryFactory
import com.yebali.template.entity.QCard.card
import com.yebali.template.entity.QMember.member
import com.yebali.template.entity.QTeam.team
import com.yebali.template.entity.Team

class CustomTeamRepositoryImpl(
    private val jpaQueryFactory: JPAQueryFactory
) : CustomTeamRepository {
    override fun findTeamByIdWithFetch(teamIds: List<Long>): List<Team> {
        return jpaQueryFactory.selectFrom(team)
            .innerJoin(team.members, member).fetchJoin()
            .innerJoin(member.cards, card).fetchJoin()
            .where(team.id.`in`(teamIds))
            .fetch()
    }
}
