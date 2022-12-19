package com.yebali.template.entity

import javax.persistence.*

@Entity
class Team(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    var name: String,

    @OneToMany(mappedBy = "team", cascade = [CascadeType.PERSIST, CascadeType.REMOVE])
    val members: MutableList<Member> = mutableListOf()

) {
    fun addMember(member: Member) {
        this.members.add(member)
        member.team = this
    }

    fun expelMember(member: Member) {
        this.members.remove(member)
        member.team = null
    }
}
