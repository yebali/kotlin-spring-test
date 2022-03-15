package com.yebali.template.repository

import com.yebali.template.entity.Team

interface CustomTeamRepository {
    fun findTeamById(teamId: Long): List<Team>
}