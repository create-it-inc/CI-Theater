package cit
package build

import sbt._
import Keys._

object CitProject {
  def apply(name: String, path: String): Project = (
    Project(name, file(path))
    settings(commonSettings: _*)
  )
}
