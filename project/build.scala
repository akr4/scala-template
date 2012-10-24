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
    crossScalaVersions := Seq("2.9.2"),
    scalaVersion := "2.9.2",
    scalacOptions ++= Seq("-unchecked", "-deprecation"),
    resolvers ++= Seq(
      "typesafe" at "http://repo.typesafe.com/typesafe/releases/",
      "sonatype-public" at "https://oss.sonatype.org/content/groups/public"
    )
  )

  object Dependency {

    val basic = {
      Seq(
        "org.scalaj" %% "scalaj-time" % "0.6",
        "com.github.scopt" %% "scopt" % "2.1.0"
      )
    }

    val io = {
      val version = "0.4.1"
      Seq(
        "com.github.scala-incubator.io" %% "scala-io-core" % version,
        "com.github.scala-incubator.io" %% "scala-io-file" % version
      )
    }

    val logging = Seq(
      "ch.qos.logback" % "logback-classic" % "1.0.1",
      "org.codehaus.groovy" % "groovy" % "1.8.6",
      "org.slf4j" % "slf4j-api" % "1.6.4",
      "org.clapper" %% "grizzled-slf4j" % "0.6.10"
    )

    val test = Seq(
      "org.scalatest" %% "scalatest" % "1.6.1",
      "org.scalamock" %% "scalamock-scalatest-support" % "latest.integration"
    ).map { _ % "test" }

    val unfiltered = {
      val version = "0.6.1"
      Seq(
        "net.databinder" %% "unfiltered-filter" % version,
        "net.databinder" %% "unfiltered-jetty" % version
      )
    }

    val scalaQuery = Seq(
      "org.scalaquery" % "scalaquery_2.9.0-1" % "0.9.5",
      "com.h2database" % "h2" % "1.3.157"
    )

    val dispatch = Seq(
      "net.databinder.dispatch" %% "dispatch-core" % "0.9.2"
    )

    val liftJson = Seq(
      "net.liftweb" % "lift-json_2.9.1" % "2.4"
    )

    val default = basic ++ io ++ logging ++ test
  }

  lazy val main = Project(groupName, file("."),
    settings = defaultSettings ++ Seq(
      libraryDependencies := Dependency.default,
      initialCommands := """
          |import scalax.io._
          |import scalax.file._
          |import org.scala_tools.time.Imports._
        """.stripMargin
    )
  ) aggregate(sub1, sub2)

  lazy val sub1 = Project(id("sub1"), file("sub1"),
    settings = defaultSettings ++ Seq(
      libraryDependencies := Dependency.default
    )
  )

  lazy val sub2 = Project(id("sub2"), file("sub2"),
    settings = defaultSettings ++ Seq(
      libraryDependencies := Dependency.default
    )
  ) dependsOn(sub1)
}

