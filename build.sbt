/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

name := "bigquery-spark"

version := "0.1.0"

scalaVersion := "2.12.10"

val exGuava = ExclusionRule(organization = "com.google.guava")

libraryDependencies ++= Seq(
  "com.github.scopt" %% "scopt" % "3.7.1",
  "com.google.guava" % "guava" % "28.0-jre",
  "com.google.auto.value" % "auto-value-annotations" % "1.7"
)

libraryDependencies ++= Seq(
  "com.google.cloud" % "google-cloud-bigquery" % "1.104.0",
  "org.scalatest" %% "scalatest" % "3.0.4" % "test"
).map(_ excludeAll exGuava)

val sparkVersion = "2.4.4"
libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core",
  "org.apache.spark" %% "spark-sql",
  "org.apache.spark" %% "spark-streaming",
  "org.apache.spark" %% "spark-streaming-kafka-0-10"
).map(_ % sparkVersion).map(_ % Provided)

mainClass in assembly := Some("com.google.cloud.example.BigQueryExample")

assemblyOption in assembly := (assemblyOption in assembly).value.copy(includeScala = false)

assemblyMergeStrategy in assembly := {
  case PathList("META-INF", _) => MergeStrategy.discard
  case _ => MergeStrategy.first
}

assemblyShadeRules in assembly := Seq(
  ShadeRule.rename("com.google.common.**" -> "shadegooglecommon.@1").inAll,
  ShadeRule.rename("com.google.protobuf.*" -> "shadedproto.@1").inAll
)
