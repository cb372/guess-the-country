# Guess the country

Twitter streaming API -> Twitter4j -> Fluentd -> S3 -> Spark -> RDS (Postgres) -> Rails

Collects (first name, second name, country) tuples using geo-tagged tweets from the Twitter streaming sample API.

Uses this information to build a database of name -> country info, allowing us to guess where a person with a particular name comes from.

Spark is used merely to clean up the data and remove duplicate users.

## Numbers

Leaving the collector running for about 10 days, I collected 205,798 distinct Twitter users and their locations.

## Caveats

* Since the original data is geo-tagged tweets, strictly speaking we only know where a user _is_, not where they come from.

* Makes no attempt to validate that people are using their real names. This results in a noisy dataset, but doesn't affect the accuracy when you're searching for a real name such as 'John'.


