/*
 *  Copyright 2020 Google Inc.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package com.google.cloud.example

import scopt.OptionParser

object ExampleOptionParser extends OptionParser[ExampleOptions]("bigquery-spark-example") {
  def parse(args: Array[String]): Option[ExampleOptions] =
    parse(args, ExampleOptions())

  head("biqguery-spark-example", "0.1")

  help("help").text("prints this usage text")

  opt[String]("queryProject")
    .required()
    .action{(x,c) => c.copy(queryProject = x)}
    .text("BigQuery project for billing")

  opt[String]("project")
    .required()
    .action{(x,c) => c.copy(project = x)}
    .text("project of target BigQuery table")

  opt[String]("dataset")
    .required()
    .action{(x,c) => c.copy(dataset = x)}
    .text("dataset of target BigQuery table")

  opt[String]("table")
    .required()
    .action{(x,c) => c.copy(table = x)}
    .text("table of target BigQuery table")

  opt[String]("servers")
    .required()
    .action{(x,c) => c.copy(bootstrapServers = x)}
    .text("Kafka bootstrap servers")

  opt[String]("groupId")
    .required()
    .action{(x,c) => c.copy(groupId = x)}
    .text("Kafka groupId")

  opt[Seq[String]]("topics")
    .required()
    .action{(x,c) => c.copy(topics = x)}
    .text("Kafka topics")

  opt[Int]("batchSize")
    .optional()
    .action{(x,c) => c.copy(batchSize = x)}
    .text("(optional) BigQuery rows per insertAll request")

  opt[Int]("batchDuration")
    .optional()
    .action{(x,c) => c.copy(batchDuration = x)}
    .text("(optional) Apache Spark seconds per batch (default: 5)")

  opt[String]("appName")
    .optional()
    .action{(x,c) => c.copy(appName = x)}
    .text("(optional) Apache Spark application name")
}

