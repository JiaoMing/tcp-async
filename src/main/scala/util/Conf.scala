package util

import com.typesafe.config.ConfigFactory

object Conf {

  val config = ConfigFactory.load()
  config.checkValid(ConfigFactory.defaultReference())

  val dbUsername = config.getString("db.username")
  val dbPassword = config.getString("db.password")
  val dbPort = config.getInt("db.port")
  val dbName = config.getString("db.name")

  val dbPoolMaxObjects = config.getInt("db.pool.maxObjects")
  val dbPoolMaxIdle = config.getInt("db.pool.maxIdle")
  val dbPoolMaxQueueSize = config.getInt("db.pool.maxQueueSize")

  val appHostName = config.getString("app.hostname")
  val appPort = config.getInt("app.port")

}
