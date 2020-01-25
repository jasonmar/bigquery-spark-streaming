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

import com.google.api.gax.rpc.FixedHeaderProvider
import com.google.cloud.bigquery.{BigQuery, BigQueryOptions}

object BigQueryClient {

  @volatile private var bq: BigQuery = _

  def get(projectId: String): BigQuery = synchronized {
    if (bq == null) {
      val userAgent = FixedHeaderProvider.create("user-agent",
        "google-pso-tool/bigquery-spark-example/0.1")
      bq = BigQueryOptions.newBuilder()
        .setProjectId(projectId)
        .setHeaderProvider(userAgent)
        .build.getService
    }
    bq
  }
}
