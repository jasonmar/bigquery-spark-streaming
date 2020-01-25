# BigQuery Spark Example

## Purpose

This repository provides an example of writing to BigQuery  
from a Kafka subscription with Apache Spark Streaming.

## Usage

Build by running `sbt assembly`

Submit assembly jar as a Spark application, providing the following arguments:

```
  --queryProject <value>   BigQuery project for billing
  --project <value>        project of target BigQuery table
  --dataset <value>        dataset of target BigQuery table
  --table <value>          table of target BigQuery table
  --servers <value>        Kafka bootstrap servers
  --groupId <value>        Kafka groupId
  --topics <value>         Kafka topics
  --batchSize <value>      (optional) BigQuery rows per insertAll request
  --batchDuration <value>  (optional) Apache Spark seconds per batch (default: 5)
  --appName <value>        (optional) Apache Spark application name
```

## Disclaimer

This is not an officially supported Google product.
