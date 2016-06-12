[slack-irc-monitor](https://github.com/waqqasdadabhoy/slack-irc-monitor)
=================

This is a program to monitor [slack-irc](https://github.com/ekmartin/slack-irc) and
send out notifications.

It works by sending a message once every 24 hours to one of the connected channels,
and checks if the message was received on any of the Slack channels.
If the message was not received, it sends a notification message to the
user specified on the command line.

Homepage
------------
https://github.com/waqqasdadabhoy/slack-irc-monitor

Configuration
-------------
The configuration for slack-irc can be used for this program.

Build
---------
Download the source and build using `sbt compile`.

Run
---------
Build, then run
`java -jar <jar filename> <config filename> <slack username to notify>`

Questions & Contibuting
-----------
Pull requests are welcome.
For any questions or feature requests,
message me on [Gitter](https://gitter.im/) using my GitHub username.
