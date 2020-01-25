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

import com.google.cloud.bigquery.TableId
import org.apache.kafka.common.serialization.StringDeserializer

case class ExampleOptions(queryProject: String = "",
                          project: String = "",
                          dataset: String = "",
                          table: String = "",
                          appName: String = "",
                          bootstrapServers: String = "localhost:9092",
                          groupId: String = "",
                          topics: Seq[String] = Seq.empty,
                          batchSize: Int = 10000,
                          batchDuration: Int = 5
                         ) {
  def tableId: TableId = TableId.of(project, dataset, table)
  def kafkaParams: Map[String,Object] = Map(
    "auto.offset.reset" -> "latest",
    "bootstrap.servers" -> bootstrapServers,
    "group.id" -> groupId,
    "key.deserializer" -> classOf[StringDeserializer],
    "value.deserializer" -> classOf[StringDeserializer],
    "enable.auto.commit" -> (false: java.lang.Boolean)
  )
}
