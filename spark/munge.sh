#!/bin/sh
# Munge the tweet data using Spark, then write it to Postgresql

# For some reason Spark isn't reading spark-env.sh
export SPARK_LOCAL_IP=127.0.0.1

# Delete previous output file
rm -rf tweetmunger/people.tsv

# Run the Spark script
(cd tweetmunger && sbt run)

# Write the resulting people.tsv to Postgresql (this will display a password prompt unless $PGPASSWORD is set)
cat tweetmunger/people.tsv/part-00000 | psql -h guesscountry.cezvpwtdc4j8.ap-northeast-1.rds.amazonaws.com guesscountry -c "begin; delete from people; copy people(first_name, second_name, country) from stdin; commit;"
