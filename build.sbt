import cit.build._
import Dependencies._
import AssemblyKeys._

import akka.sbt.AkkaKernelPlugin
import akka.sbt.AkkaKernelPlugin.{ Dist, outputDirectory, distJvmOptions, distMainClass }

lazy val root = (
  CitProject("cit", ".")
  aggregate(assets, jobServer)
)

lazy val assets = (
  CitProject("cit-assets", "assets")
  settings(
    libraryDependencies ++= Dependencies.assets
  )
)

lazy val jobServer = (
  CitProject("cit-job-server", "job-server")
  dependsOn(assets)
  settings((AkkaKernelPlugin.distSettings ++ Seq (
    libraryDependencies ++= Dependencies.jobServer,
    distJvmOptions in Dist := "-Xms512M -Xmx2048M -Xss1M -XX:MaxPermSize=512M -XX:+UseParallelGC",
    distMainClass in Dist := "akka.kernel.Main it.create.theater.JobServer",
    outputDirectory in Dist <<= target / "dist"
  )): _*)
)

