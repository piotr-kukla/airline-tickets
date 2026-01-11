package pl.algocode.airline.infrastructure

import pl.algocode.airline.config.Sensitive
import pureconfig.ConfigReader

case class DBConfig(username: String, password: Sensitive, url: String, migrateOnStart: Boolean, driver: String) derives ConfigReader
