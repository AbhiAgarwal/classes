name := "hw02"

version := "2.0"

scalaVersion := "2.11.7"

libraryDependencies += "org.scalatest" % "scalatest_2.11" % "2.2.4" % "test"

libraryDependencies += "org.scala-lang.modules" %% "scala-parser-combinators" % "1.0.4"

libraryDependencies += "com.googlecode.kiama" % "kiama_2.11" % "1.8.0"

parallelExecution in Test := false
