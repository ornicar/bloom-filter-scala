import sbt._
import sbt.Keys._

object Settings {

  private lazy val build = Seq(
    version := "0.13.1_lila-1",
    scalaVersion := "3.2.1",
    autoCompilerPlugins := true,
    scalacOptions ++= scalacSettings,
    javaOptions += "-Xmx1G",
    organization := "com.github.alexandrnikitin"
  )

  lazy val root = build ++ Testing.settings
  lazy val bloomfilter =
    build ++ Testing.settings ++ Dependencies.bloomfilter
  lazy val sandbox =
    build ++ Testing.settings ++ Dependencies.sandbox
  lazy val sandboxApp =
    build ++ Dependencies.sandboxApp
  lazy val tests =
    build ++ Testing.settings ++ Dependencies.tests
  lazy val benchmarks =
    build ++ Dependencies.benchmarks
  lazy val examples = build

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
