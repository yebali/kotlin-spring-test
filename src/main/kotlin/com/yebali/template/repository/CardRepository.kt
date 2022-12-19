package com.yebali.template.repository

import com.yebali.template.entity.Card
import org.springframework.data.jpa.repository.JpaRepository

interface CardRepository : JpaRepository<Card, Long>
