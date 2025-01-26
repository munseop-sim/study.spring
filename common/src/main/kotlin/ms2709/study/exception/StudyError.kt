package ms2709.study.exception

open class StudyError(
    override val message: String,
) : RuntimeException(message)
