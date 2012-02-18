import sbt._
import sbt.Keys._

object MyBuild extends Build {

  val groupName = "myproject"

  def id(name: String) = "%s-%s" format(groupName, name)

  override val settings = super.settings :+ 
    (shellPrompt := { s => Project.extract(s).currentProject.id + "> " })

  val defaultSettings = Defaults.defaultSettings ++ Seq(
    version := "0.1",
    organization := "net.physalis",
    crossScalaVersions := Seq("2.9.0", "2.9.0-1", "2.9.1"),
    scalaVersion := "2.9.1"
  )

  object Dependency {
    val scalaIoVersion = "0.3.0"

    val basic = Seq(
      "com.github.scala-incubator.io" %% "scala-io-core" % scalaIoVersion,
      "com.github.scala-incubator.io" %% "scala-io-file" % scalaIoVersion,
      "org.scala-tools.time" %% "time" % "0.5"
    )

    val logging = Seq(
      "ch.qos.logback" % "logback-classic" % "0.9.25",
      "org.codehaus.groovy" % "groovy" % "1.8.0",
      "org.slf4j" % "slf4j-api" % "1.6.2",
      "org.clapper" %% "grizzled-slf4j" % "0.6.6"
    )

    val test = Seq(
      "org.scalatest" %% "scalatest" % "1.6.1" % "test",
      "org.scalamock" %% "scalamock-scalatest-support" % "latest.integration" % "test"
    )
  }

  lazy val main = Project(groupName, file("."),
    settings = defaultSettings
  ) aggregate(sub1, sub2)

  lazy val sub1 = Project(id("sub1"), file("sub1"),
    settings = defaultSettings ++ Seq(
      libraryDependencies := Dependency.logging ++ Dependency.test
    )
  )

  lazy val sub2 = Project(id("sub2"), file("sub2"),
    settings = defaultSettings ++ Seq(
      libraryDependencies := Dependency.logging ++ Dependency.test
    )
  ) dependsOn(sub1)
}

