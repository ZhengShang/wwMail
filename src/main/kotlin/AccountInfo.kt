import java.io.File
import java.util.*

object AccountInfo {
    var mailName: String = ""
    var mailPw: String = ""

    fun loadAccountInfo() {
        if (file.exists().not()) {
            file.parentFile.mkdirs()
            file.createNewFile()
        }
        val properties = Properties().apply {
            file.inputStream().buffered().use { input ->
                load(input)
            }
        }
        properties.getProperty(EMAIL_NAME)?.let {
            mailName = String(Base64.getDecoder().decode(it.trim()))
        }
        mailPw = properties.getProperty(EMAIL_PW) ?: ""
    }

    fun saveAccountInfo(mailName: String, mailPw: String) {
        val properties = Properties()
        properties[EMAIL_NAME] = Base64.getEncoder().encodeToString(mailName.trim().toByteArray())
        properties[EMAIL_PW] = mailPw
        file.outputStream().buffered().use { output ->
            properties.store(output, null)
        }
    }

    private val file =
        File(System.getProperty("user.home") + File.separator + ".wwmail" + File.separator + "account.ini")
    private const val EMAIL_NAME = "EMAIL_NAME"
    private const val EMAIL_PW = "EMAIL_PW"
}
