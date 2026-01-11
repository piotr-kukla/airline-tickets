package pl.algocode.airline.user

import pureconfig.ConfigReader

import scala.concurrent.duration.Duration

case class UserConfig(defaultApiKeyValid: Duration) derives ConfigReader
