
# This project

This project is an exploration of how to perform basic data analytics efficiently
using  Apache Spark Streaming directly through Scala code.
The project described there simply runs quite basic Spark Streaming with both the core input sources like files and additional sources like kafka against a well
known data set (described below) on US airline flight data. The original queries are
reproduced here, reformatted and commented rather more pedantically to emphasize what is
being computed.

The project goals are:


1. Explore techniques for writing Spark Streaming code for both input core sources like files & akka actor and additional sources like kafka,flume.
2. In this project will see example for local files and integration with Kafka.

# The data

## Official site

These examples analyze the data published by the US Government (specifically,
the Bureau of Transport Statistics in the Department of Transportation) as
[Airline On-Time Performance and Causes of Flight Delays: On_Time Data]
(https://catalog.data.gov/dataset/airline-on-time-performance-and-causes-of-flight-delays).


## Interesting properties

The data spans from 1987 to 2015. There are 162,212,419 rows.
It appears to contain all the columns in the official data set.

| Statistic | Value |
| --------- | -----:|
| Flights | 162,212,419 |
| Aircraft Tail Numbers | 14,858 |
| Records with missing or unknown tail numbers | 38,687,083 |
| Maximum number of flights for a single tail number | 43,989 |
| Canceled Flights | |
| Delayed Flights > 1 hour | |
| Airports | 388 |
| Airlines (Unique Carrier) | 31 |
| Airport (Origin,Destination) pairs with flights | 9554 |
| Airport pair with the most flights | SFO to LAX (426,506) |

# Problem statement
Find out no. of cancellations for airlines.

# Building and running

Core input source - Local file

sbt "run-main com.github.streaming.sparkflights.experiments.FlightsMainStream /home/cloudera/Downloads/streaming_data/On_Time_On_Time_Performance_2017_1.csv"

Additional Source - Kafka

Ingest data by running KafkaFlightProducer
sbt "run-main impetus.example.kafka.KafkaFlightDataProducer localhost:9092 cancellation1 10 10"

Simultaneously in other cli run TopAirlineCancellation to calculate no. of cancellation 
sbt "run-main impetus.example.kafka.TopAirlineCancellation localhost:2181 airlines-flight cancellation1 1"

# Finding the Output

As the framework executes the classes, it produces a directory tree with
summary results as well as the actual output of the various Spark computations.
The tree looks like this:

    <specified output directory>
    |
    |-- <date and time at job start>
        |
        |-- summary
        |   |
        |   |-- executions
        |   |
        |   |-- unknown
        |
        |-- 00001_<Experiment Name 1>
        |   |
        |   |-- <Result 1>
        |   |
        |   |-- ...
        |   |
        |   |-- <Result n_1>
        |
        |-- ...
        |
        |-- 0000k_<Experiment Name k>
            |
            |-- <Result 1>
            |
            |-- ...
            |
            |-- <Result n_k>

Each of the leaf nodes int he tree are HDFS "files",
with their various underlying "part", "SUCCESS" and ".crc" files.

The "executions" node contains a summary of what ran when, how
long it took, and whether it succeeded. Stack traces are included
in case of a failure.

The "unknown" node lists experiments explicitly specified ont he command line that
could not be found int he registry.

The experiment nodes are numbered to show the sequence in which they ran and to
accommodate the same experiment being run more than once.

The number of result nodes under an experiment varies,
and is determined by the implementation of each individual experiment.