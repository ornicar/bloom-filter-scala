import sbt._
import sbt.Keys._

object Settings {

  private lazy val build = Seq(
    scalaVersion := "3.2.1",
    autoCompilerPlugins := true,
    scalacOptions ++= scalacSettings,
    javaOptions += "-Xmx1G",
    organization := "com.github.alexandrnikitin"
  )

  lazy val root = build ++ Testing.settings ++ Publishing.noPublishSettings
  lazy val bloomfilter =
    build ++ Testing.settings ++ Dependencies.bloomfilter ++ Publishing.settings
  lazy val sandbox =
    build ++ Testing.settings ++ Dependencies.sandbox ++ Publishing.noPublishSettings
  lazy val sandboxApp =
    build ++ Dependencies.sandboxApp ++ Publishing.noPublishSettings
  lazy val tests =
    build ++ Testing.settings ++ Dependencies.tests ++ Publishing.noPublishSettings
  lazy val benchmarks =
    build ++ Dependencies.benchmarks ++ Publishing.noPublishSettings
  lazy val examples = build ++ Publishing.noPublishSettings

  val scalacSettings = Seq(
    "-rewrite",
    "-source:future-migration",
    "-indent",
    "-explaintypes",
    "-feature",
    "-language:postfixOps",
    "-Xtarget:12"
  )

}
