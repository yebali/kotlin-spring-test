package com.yebali.template.entity

import javax.persistence.*

@Entity
class Card(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    val name: String,

    @ManyToOne
    @JoinColumn(name = "member_id")
    var member: Member? = null
)
