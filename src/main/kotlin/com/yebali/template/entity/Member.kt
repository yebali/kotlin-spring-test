package com.yebali.template.entity

import javax.persistence.*

@Entity
class Member(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    var name: String,

    @ManyToOne
    @JoinColumn(name = "team_id")
    var team: Team? = null,

    @OneToMany(mappedBy = "member", cascade = [CascadeType.PERSIST])
    val cards: MutableSet<Card> = mutableSetOf()
) {
    fun addCard(card: Card) {
        this.cards.add(card)
        card.member = this
    }

    fun removeCard(card: Card) {
        this.cards.remove(card)
    }
}
