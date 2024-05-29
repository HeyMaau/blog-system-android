package top.manpok.blog.pojo

data class FeedbackRequest(
    val content: String,
    val email: String,
    val title: String
)