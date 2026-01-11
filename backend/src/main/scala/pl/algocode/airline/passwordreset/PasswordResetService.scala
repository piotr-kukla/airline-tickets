package pl.algocode.airline.passwordreset

import com.augustnagro.magnum.DbTx
import pl.algocode.airline.Fail
import pl.algocode.airline.email.{EmailData, EmailScheduler, EmailSubjectContent, EmailTemplates}
import pl.algocode.airline.infrastructure.DB
import pl.algocode.airline.logging.Logging
import pl.algocode.airline.security.Auth
import pl.algocode.airline.user.{User, UserModel}
import pl.algocode.airline.util.*
import pl.algocode.airline.util.Strings.{Id, asId, toLowerCased}
import ox.either
import ox.either.*

class PasswordResetService(
    userModel: UserModel,
    passwordResetCodeModel: PasswordResetCodeModel,
    emailScheduler: EmailScheduler,
    emailTemplates: EmailTemplates,
    auth: Auth[PasswordResetCode],
    idGenerator: IdGenerator,
    config: PasswordResetConfig,
    clock: Clock,
    db: DB
) extends Logging:
  def forgotPassword(loginOrEmail: String)(using DbTx): Unit =
    userModel.findByLoginOrEmail(loginOrEmail.toLowerCased) match
      case None       => logger.debug(s"Could not find user with $loginOrEmail login/email")
      case Some(user) =>
        val pcr = createCode(user)
        sendCode(user, pcr)

  private def createCode(user: User)(using DbTx): PasswordResetCode =
    logger.debug(s"Creating password reset code for user: ${user.id}")
    val id = idGenerator.nextId[PasswordResetCode]()
    val validUntil = clock.now().plusMillis(config.codeValid.toMillis)
    val passwordResetCode = PasswordResetCode(id, user.id, validUntil)
    passwordResetCodeModel.insert(passwordResetCode)
    passwordResetCode

  private def sendCode(user: User, code: PasswordResetCode)(using DbTx): Unit =
    logger.debug(s"Scheduling e-mail with reset code for user: ${user.id}")
    emailScheduler.schedule(EmailData(user.emailLowerCase, prepareResetEmail(user, code)))

  private def prepareResetEmail(user: User, code: PasswordResetCode): EmailSubjectContent =
    val resetLink = String.format(config.resetLinkPattern, code.id)
    emailTemplates.passwordReset(user.login, resetLink)

  def resetPassword(code: String, newPassword: String): Either[Fail, Unit] = either {
    val userId = auth(code.asId[PasswordResetCode]).ok()
    logger.debug(s"Resetting password for user: $userId")
    db.transact(userModel.updatePassword(userId, User.hashPassword(newPassword)))
  }
end PasswordResetService
