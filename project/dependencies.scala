package cit
package build

import sbt._

object Dependencies {
  import Dependency._

  val jobServer = Seq(
    kernel, remote, contrib, slf4j, logback
  )
  val assets = Seq(
    actor, commonsBeanutils
  )
}

object Dependency {
  object V {
    val Akka = "2.2.3"
  }

  val actor   = "com.typesafe.akka"        %% "akka-actor"      % V.Akka
  val contrib = "com.typesafe.akka"        %% "akka-contrib"    % V.Akka
  val remote  = "com.typesafe.akka"        %% "akka-remote"     % V.Akka
  val kernel  = "com.typesafe.akka"        %% "akka-kernel"     % V.Akka
  val slf4j   = "com.typesafe.akka"        %% "akka-slf4j"      % V.Akka

  val logback = "ch.qos.logback"           %  "logback-classic" % "1.0.13"
  val specs   = "org.specs2"               %% "specs2-core"     % "2.3.7"

  val commonsBeanutils  = "commons-beanutils"     % "commons-beanutils"  % "1.8.3"
}
