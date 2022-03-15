package com.yebali.template.entity

import javax.persistence.CascadeType
import javax.persistence.Embeddable
import javax.persistence.JoinColumn
import javax.persistence.OneToMany

@Embeddable
class EmbeddableMember(

    val embeddableValue: String,

    @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true)
    @JoinColumn(name = "team_id")
    val members: MutableList<Member> = mutableListOf(),
) {
    fun addMember(member: Member) {
        members.add(member)
    }
}
