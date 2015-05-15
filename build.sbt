name := "sbt-script-args-parser"

version := "0.0.1-SNAPSHOT"

scalaVersion := "2.11.6"

organization := "tw.com.ehanlin"

organizationHomepage := Some(new URL("http://www.ehanlin.com.tw"))

homepage := Some(url("https://github.com/eHanlin/sbt-script-args-parser"))

licenses += ("MIT", url("http://opensource.org/licenses/MIT"))

description := "Scala Sbt script args parser"



publishMavenStyle := true

publishArtifact in Test := false

publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (isSnapshot.value)
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases"  at nexus + "service/local/staging/deploy/maven2")
}

sonatypeProfileName := "hotdog929"

pomExtra := (
    <scm>
      <url>git@github.com:eHanlin/sbt-script-args-parser.git</url>
      <connection>scm:git:git@github.com:eHanlin/sbt-script-args-parser.git</connection>
    </scm>
    <developers>
      <developer>
        <id>DdGWRv8u</id>
        <name>hotdog929</name>
      </developer>
    </developers>)
