# Change Data Capture (CDC)

## Running Locally

These instructions assume you are using IntelliJ as your IDE.

Set your debug profile as `local` and start the local postgres container with `docker-compose up -d`.

## Endpoints

If you are using IntelliJ you can use the `http` directory for initiating local requests.

Create:

```shell
curl -X POST http://localhost:8080/book \
     -H "Content-Type: application/json" \
     -d '{
           "title": "The Great Gatsby"
         }' | jq
```

List all:

```shell
curl http://localhost:8080/book | jq
```

Find by id:

```shell
curl http://localhost:8080/book/1 | jq
```

Update:

```shell
curl -X PUT http://localhost:8080/book/1 \
     -H "Content-Type: application/json" \
     -d '{
           "title": "The Great Gatsby - Updated"
         }' | jq

```

Delete:

```shell
curl -X DELETE http://localhost:8080/book/1 | jq
```

## Connecting to Aurora DSQL

User your preferred RDMBS tool to connect via the Aurora DSQL endpoint found in the AWS Console. You can use the AWS
Console to create a short-lived token to use as a password. You will need to change the password by generating another
token through the console when it expires.

Schema visualization does not work, so you will need to query the Postgres system tables in order to see database
information:

- see all schemas: `select * from pg_namespace;`
- see all tables: `select * from pg_tables where schemaname = 'public';`

## Resources

- [Amazon Aurora DSQL User Guide (PDF)](https://docs.aws.amazon.com/pdfs/aurora-dsql/latest/userguide/aurora-dsql-ug.pdf)