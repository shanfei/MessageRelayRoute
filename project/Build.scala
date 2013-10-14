import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "depots"
  val appVersion      = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    // Add your project dependencies here,
    jdbc,
    anorm
  )


  val main = play.Project(appName, appVersion, appDependencies).settings(
    resolvers := Seq("typesafe" at "http://repo.typesafe.com/typesafe/repo")
  )

}
