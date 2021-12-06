import java.io.File
import java.util.*
import javax.mail.Message
import javax.mail.PasswordAuthentication
import javax.mail.Session
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeBodyPart
import javax.mail.internet.MimeMessage
import javax.mail.internet.MimeMultipart

object SendTask {
    fun send(subject: String, receivers: String, contents: String, attachmentFile: File?): String {

//        val receiverList = receivers.split("\\r?\\n")

        //分号分隔
        val receiverList = receivers.split(";")

        val props = Properties().apply {
            putIfAbsent("mail.smtp.host", "smtp.qq.com")
            putIfAbsent("mail.smtp.port", "587")
            putIfAbsent("mail.smtp.auth", "true")
            putIfAbsent("mail.smtp.starttls.enable", "false")
        }

        val sender = AccountInfo.mailName
        val password = AccountInfo.mailPw

        if (sender.isNotBlank() || password.isNotBlank()) {
            return "邮箱账号或者密码每天, 请点击菜单中的[设置]进行填写"
        }

        val session = Session.getDefaultInstance(props, object : javax.mail.Authenticator() {
            override fun getPasswordAuthentication(): PasswordAuthentication {
                return PasswordAuthentication(sender, password)
            }
        })

        session.debug = true

        val bodyPart = MimeBodyPart().apply {
            setText(contents)
        }

        val multipart = MimeMultipart().apply {
            addBodyPart(bodyPart)
            if (attachmentFile != null) {
                val attachBody = MimeBodyPart()
                attachBody.attachFile(attachmentFile)
                addBodyPart(attachBody)
            }
        }

        try {
            var sendCount = 0
            session.getTransport("smtp").apply {
                connect()
                receiverList.forEach { address ->
                    val mimeMessage = MimeMessage(session).apply {
                        setFrom(InternetAddress(sender))
                        setRecipients(Message.RecipientType.TO, InternetAddress.parse(address, false))
                        setRecipients(Message.RecipientType.BCC, InternetAddress.parse(sender, false))
                        //attachment
                        setContent(multipart, "text/html; charset=utf-8")

                        this.subject = subject
                        sentDate = Date()
                    }
                    sendMessage(mimeMessage, mimeMessage.allRecipients)
                    sendCount++
                }
                close()
            }
            return "发送成功(${sendCount}个)"
        } catch (e: Exception) {
            e.printStackTrace()
            return e.message.toString()
        }
    }
}