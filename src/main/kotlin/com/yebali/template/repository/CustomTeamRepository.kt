package com.yebali.template.repository

import com.yebali.template.entity.Team

interface CustomTeamRepository {

    fun findAllWithoutFetch(): List<Team>

    fun findAllWithFetch(): List<Team>
}
