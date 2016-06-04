package io.github.waqqasdadabhoy.slack_irc_monitor

import com.google.gson.Gson

/**
  * Created by waqqas on 31/05/16.
  */
class Config(
              val nickname: String,
              val server: String,
              val token: String,
              val avatarUrl: String,
              val autoSendCommands: Array[Array[String]],
              val channelMapping: java.util.Map[String, String],
              val ircOptions: java.util.Map[String, String],
              val commandCharacters: Array[String],
              val muteSlackbot: Boolean,
              val ircStatusNotices: java.util.Map[String, Boolean]
            ) {


}

object Config {
  def readConfigFile(fileContents: String): Array[Config] = {
    if (fileContents.trim().startsWith("[")) {
      return new Gson().fromJson(fileContents, classOf[Array[Config]])
    } else if (fileContents.trim().startsWith("{")){
      val config_array = new Array[Config](1)
      config_array(0) = new Gson().fromJson(fileContents, classOf[Config])
      return config_array
    } else {
      throw new Exception("Invalid config file")
    }
  }

}
