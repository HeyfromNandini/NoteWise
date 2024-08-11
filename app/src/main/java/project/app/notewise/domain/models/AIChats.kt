package project.app.notewise.domain.models

import kotlinx.serialization.Serializable

data class AIChats(
    val conversations: List<Conversations>,
    val userId: String
) {
    constructor() : this(
        emptyList(),
        ""
    )
}

data class Conversations(
    val question: String,
    val answer: String,
    val timestamp: Long
) {
    constructor() : this(
        "",
        "",
        0
    )
}