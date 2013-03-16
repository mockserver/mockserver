import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "mockserver"
  val appVersion      = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    // Add your project dependencies here,
    "com.typesafe.slick" %% "slick" % "1.0.0",
    jdbc,
    anorm
  )

  val main = play.Project(appName, appVersion, appDependencies).settings(
    // Add your own project settings here      
    resolvers += "Sonatype OSS releases" at "https://oss.sonatype.org/content/repositories/releases"
  )

}