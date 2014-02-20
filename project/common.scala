package cit

import sbt._
import Keys._

package object build {
  val commonSettings = Seq (
    organization := "it.create",
    scalaVersion := "2.10.3",
    sourceDirectory <<= baseDirectory(identity),
    initialCommands in (Compile, consoleQuick) <<= initialCommands in Compile,
    initialCommands in Compile in console += """
      import scalax.hash._
    """
  )
}
