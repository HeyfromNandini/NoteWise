package project.app.notewise.domain.models

data class UserNotes(
    val author: String,
    val category: String,
    val content: String,
    val isEncrypted: Boolean,
    val priority: String,
    val title: String,
    val tags: List<String>,
    val timestamp: Long,
    val uId: String,
    val vectorIds: List<String>
) {
    constructor() : this(
        "",
        "",
        "",
        false,
        "",
        "",
        emptyList(),
        0,
        "",
        emptyList()
    )
}