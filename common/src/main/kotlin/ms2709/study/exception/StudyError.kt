package ms2709.study.exception

class StudyError(
    override val message: String,
) : RuntimeException(message)
