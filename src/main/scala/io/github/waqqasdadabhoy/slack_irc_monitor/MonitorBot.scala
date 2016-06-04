package io.github.waqqasdadabhoy.slack_irc_monitor

import com.flyberrycapital.slack.SlackClient
import java.util.concurrent.TimeUnit
import scala.collection.JavaConverters._

import org.pircbotx.Configuration
import org.pircbotx.PircBotX
import org.pircbotx.hooks.ListenerAdapter
import org.pircbotx.hooks.types.GenericMessageEvent
import org.pircbotx.output.OutputIRC

import scala.util.Random

/**
  * Created by waqqas on 03/06/16.
  */
class MonitorBot(config: Config, slack_user_to_notify: String) {
  def check(): Unit = {
    val random_string: String = Random.alphanumeric.take(32).mkString

    // Connect to Slack & IRC
    val s = new SlackClient(config.token)

    val port_number: Int = if (config.ircOptions.asScala.contains("port")) config.ircOptions.asScala("port").toInt else 6667

    val ircConfig: Configuration = new Configuration.Builder()
      .addServer(config.server, port_number)
      .setName(config.nickname) //Nick of the bot. CHANGE IN YOUR CODE
      .setLogin(config.ircOptions.asScala("userName")) //Login part of hostmask, eg name:login@host
      .setServerPassword(config.ircOptions.asScala("password"))
      .setAutoNickChange(true) //Automatically change nick when the current one is in use
      .addAutoJoinChannels(config.channelMapping.values) //Join #pircbotx channel on connect
      .addListener(new IrcListener(random_string))
      .buildConfiguration() //Create an immutable configuration from this builder

    val i: PircBotX = new PircBotX(ircConfig)


    // Send a message on IRC & wait 10s for it to be received
    i.startBot()
    TimeUnit.SECONDS.sleep(10)

    // Check that message was received on Slack
    if (was_message_received(s, config, random_string)) {
      //Nothing to do
    } else {
      s.chat.postMessage(slack_user_to_notify, "The Slack-IRC bot may not be sending messages.")
    }

    // Disconnect Slack (IRC is already closed when startBot() above returns)
    // TODO find out how to close it

  }

  def run(): Unit = {
    while (true) {
      this.check()
      TimeUnit.DAYS.sleep(1)
    }
  }

  def was_message_received(s: SlackClient, config: Config, random_string: String): Boolean = {
    for (channel <- config.channelMapping.keySet().asScala) {
      if (s.im.history(channel).messages.exists(m => m.text.getOrElse("") == random_string)) {
        return true
      }
    }
    return false
  }
}

class IrcListener(random_string: String) extends ListenerAdapter {
  @Override def onJoin(event: GenericMessageEvent): Unit = {
    event.respond(random_string)
    new OutputIRC(event.getBot[PircBotX]).quitServer()
  }
}
