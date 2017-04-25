name := "FlightPerformanceAnalysis"

version := "1.0"

organization := "com.github.streaming"

scalaVersion := "2.11.8"

scalacOptions in ThisBuild ++= Seq("-deprecation")


libraryDependencies += "org.apache.spark" % "spark-core_2.10" % "1.6.0"
libraryDependencies += "org.apache.spark" % "spark-sql_2.10" % "1.6.0" 
libraryDependencies += "org.apache.spark" % "spark-hive_2.10" % "1.6.0"
libraryDependencies += "org.apache.spark" % "spark-streaming_2.10" % "1.6.0"
libraryDependencies += "org.apache.spark" % "spark-streaming-kafka_2.10" % "1.6.0"
libraryDependencies += "com.github.scopt" %% "scopt" % "3.3.0"

libraryDependencies += "com.databricks" % "spark-csv_2.10" % "1.3.0"
libraryDependencies += "log4j" % "log4j" % "1.2.14"

fork := true
