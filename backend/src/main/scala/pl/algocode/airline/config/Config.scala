package pl.algocode.airline.config

import pl.algocode.airline.email.EmailConfig
import pl.algocode.airline.http.HttpConfig
import pl.algocode.airline.infrastructure.DBConfig
import pl.algocode.airline.logging.Logging
import pl.algocode.airline.passwordreset.PasswordResetConfig
import pl.algocode.airline.user.UserConfig
import pl.algocode.airline.version.BuildInfo
import pureconfig.{ConfigReader, ConfigSource}

import scala.collection.immutable.TreeMap

/** Maps to the `application.conf` file. Configuration for all modules of the application. */
case class Config(db: DBConfig, api: HttpConfig, email: EmailConfig, passwordReset: PasswordResetConfig, user: UserConfig)
    derives ConfigReader

object Config extends Logging:
  def log(config: Config): Unit =
    val baseInfo = s"""
                      |Airline configuration:
                      |-----------------------
                      |DB:             ${config.db}
                      |API:            ${config.api}
                      |Email:          ${config.email}
                      |Password reset: ${config.passwordReset}
                      |User:           ${config.user}
                      |
                      |Build & env info:
                      |-----------------
                      |""".stripMargin

    val info = TreeMap(BuildInfo.toMap.toSeq*).foldLeft(baseInfo) { case (str, (k, v)) =>
      str + s"$k: $v\n"
    }

    logger.info(info)
  end log

  def read: Config = ConfigSource.default.loadOrThrow[Config]
end Config
