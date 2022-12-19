package com.yebali.template.repository

import com.yebali.template.entity.Team

interface CustomTeamRepository {
    fun findTeamByIdWithFetch(teamIds: List<Long>): List<Team>
}
