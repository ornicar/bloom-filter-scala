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

  lazy val root = build ++ Testing.settings ++ noPublish
  lazy val bloomfilter =
    build ++ Testing.settings ++ Dependencies.bloomfilter ++ Seq(
      publishTo := Some(
        Resolver.file("file", new File(sys.props.getOrElse("publishTo", "")))
      )
    )
  lazy val sandbox =
    build ++ Testing.settings ++ Dependencies.sandbox ++ noPublish
  lazy val sandboxApp =
    build ++ Dependencies.sandboxApp ++ noPublish
  lazy val tests =
    build ++ Testing.settings ++ Dependencies.tests ++ noPublish
  lazy val benchmarks =
    build ++ Dependencies.benchmarks ++ noPublish
  lazy val examples = build ++ noPublish

  val noPublish = publish / skip := true

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
