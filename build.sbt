name := "mechanize"

version := "0.3"

organization := "cn.orz.pascal"

scalaVersion := "2.9.2"

scalacOptions += "-deprecation"

publishTo := Some(Resolver.file("local-github-repos", file("../maven-repos")))

libraryDependencies += "org.scalatest" %% "scalatest" % "1.6.1" % "test"

libraryDependencies += "net.sourceforge.htmlunit" % "htmlunit" % "2.9"

libraryDependencies += "nu.validator.htmlparser" % "htmlparser" % "1.2.1"


