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
    crossScalaVersions := Seq("2.10.1"),
    scalaVersion := "2.10.1",
    scalacOptions ++= Seq("-unchecked", "-deprecation"),
    resolvers ++= Seq(
      "typesafe" at "http://repo.typesafe.com/typesafe/releases/",
      "sonatype-public" at "https://oss.sonatype.org/content/groups/public"
    )
  )

  object Dependency {

    val basic = {
      Seq(
        "com.github.nscala-time" %% "nscala-time" % "0.4.0",
        "com.github.scopt" %% "scopt" % "2.1.0"
      )
    }

    val logging = Seq(
      "ch.qos.logback" % "logback-classic" % "1.0.12",
      "org.codehaus.groovy" % "groovy" % "2.1.3",
      "org.slf4j" % "slf4j-api" % "1.7.5",
      "org.clapper" %% "grizzled-slf4j" % "1.0.1"
    )

    val test = Seq(
      "org.scalatest" %% "scalatest" % "1.9.1",
      "org.scalamock" %% "scalamock-scalatest-support" % "3.0.1"
    ).map { _ % "test" }

    val default = basic ++ logging ++ test
  }

  lazy val main = Project(groupName, file("."),
    settings = defaultSettings ++ Seq(
      libraryDependencies := Dependency.default,
      initialCommands := """
          |import com.github.nscala_time.time.Imports._
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

