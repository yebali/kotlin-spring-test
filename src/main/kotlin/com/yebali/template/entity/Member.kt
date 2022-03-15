package com.yebali.template.entity

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
class Member(
    @Id @GeneratedValue
    val id: Long = 0,

    var name: String,
)
