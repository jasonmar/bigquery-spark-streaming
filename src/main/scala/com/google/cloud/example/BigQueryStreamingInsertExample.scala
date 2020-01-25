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

import com.google.cloud.bigquery.{InsertAllRequest, TableId}
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession
import org.apache.spark.streaming.{Seconds, StreamingContext, Time}
import org.apache.spark.streaming.dstream.InputDStream
import org.apache.spark.streaming.kafka010.{ConsumerStrategies, KafkaUtils, LocationStrategies}

object BigQueryStreamingInsertExample {
  type KMsg = ConsumerRecord[String,Array[Byte]]
  type BQRow = java.util.Map[String,Any]

  /** Extracts data fields from Kafka messages
    * @param kMsg Kafka Message
    * @return row to be added to BigQuery InsertAllRequest
    */
  def msg2Row(kMsg: KMsg, t: Time): BQRow = {
    val m = new java.util.HashMap[String,Any]()
    m.put("t", t.milliseconds)
    m.put("key", kMsg.key)
    m.put("value", new String(kMsg.value))
    m
  }

  /** Apache Spark Streaming Kafka to BigQuery example
    * Reads messages from Apache Kafka
    * Uses BigQuery streaming insert
    */
  def main(args: Array[String]): Unit = {
    ExampleOptionParser.parse(args) match {
      case Some(opts) =>
        val spark = SparkSession.builder
          .appName(opts.appName)
          .getOrCreate()

        run(spark, opts)

      case _ =>
    }
  }

  /** Inserts rows into BigQuery using streaming insert
    * @param batchSize rows per batch
    * @param tableId BigQuery table to insert into
    * @param project GCP Project used for BigQuery client
    * @param kMsgs RDD containing messages
    * @param t Time of batch window
    */
  def streamingInsert(batchSize: Int,
                      tableId: TableId,
                      project: String,
                      kMsgs: RDD[KMsg],
                      t: Time): Unit = {
    kMsgs.foreachPartition{msgs =>
      val bq = BigQueryClient.get(project)
      for (batch <- msgs.grouped(batchSize)){
        val req = InsertAllRequest.newBuilder(tableId)
        batch.foreach(msg => req.addRow(msg2Row(msg, t)))
        bq.insertAll(req.build)
      }
    }
  }

  def run(spark: SparkSession, opts: ExampleOptions): Unit = {
    val ssc = new StreamingContext(spark.sparkContext, Seconds(opts.batchDuration))

    val stream: InputDStream[KMsg] =
      KafkaUtils.createDirectStream[String,Array[Byte]](ssc,
        LocationStrategies.PreferConsistent,
        ConsumerStrategies.Subscribe[String,Array[Byte]](opts.topics, opts.kafkaParams))

    stream.foreachRDD{(rdd,t) =>
      streamingInsert(opts.batchSize, opts.tableId, opts.project, rdd, t)
    }
  }
}
