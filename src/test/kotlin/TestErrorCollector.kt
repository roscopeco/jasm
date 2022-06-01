import com.roscopeco.jasm.ErrorForTestsException
import com.roscopeco.jasm.errors.BaseError
import com.roscopeco.jasm.errors.ErrorCollector

/**
 * An ErrorCollector implementation that just throws SyntaxErrorException.
 *
 * Only for use in test code.
 */
class TestErrorCollector : ErrorCollector {
    override fun addError(error: BaseError) {
        throw ErrorForTestsException(error.toString())
    }

    override fun hasErrors(): Boolean = false

    override fun getErrors(): List<BaseError> = emptyList()
}