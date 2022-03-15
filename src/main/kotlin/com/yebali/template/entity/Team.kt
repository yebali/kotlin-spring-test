package com.yebali.template.entity

import javax.persistence.Embedded
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
class Team(
    @Id @GeneratedValue
    val id: Long = 0,

    var name: String,

    @Embedded
    val embeddableMember: EmbeddableMember
) {
    fun addMember(member: Member) {
        embeddableMember.addMember(member)
    }
}
