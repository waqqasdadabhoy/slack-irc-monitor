package io.github.waqqasdadabhoy.slack_irc_monitor

import io.github.waqqasdadabhoy.slack_irc_monitor.MonitorBot
import io.github.waqqasdadabhoy.slack_irc_monitor.Config

import scala.collection.parallel.mutable.ParArray

/**
  * Created by waqqas on 30/05/16.
  */
object Main {
  def main(args: Array[String]): Unit = {
    if (args.length != 2) {
      println("Arguments: <config file name> <Slack user to notify>")
      sys.exit(1)
    }

    val configFileName = args(0)
    val slack_user_to_notify = args(1)

    val configs: Array[Config] = Config.readConfigFile(scala.io.Source.fromFile(configFileName).mkString)
    val bots: ParArray[MonitorBot] = configs.par.map(x => new MonitorBot(x, slack_user_to_notify))
    bots.par.foreach(b => b.run())
  }
}
