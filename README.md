# AWS Spring Boot Native DSQL

## Running Locally

These instructions assume you are using IntelliJ as your IDE.

Set your debug profile as `local` and start the local postgres container with `docker-compose up -d`.

If you want to connect to the Aurora DSQL database you will need to make sure you have your `default` AWS credentials
set via `aws configure`. You may also need to delete the `aws_session_token` still associated with your default
profile if it exists. Once your credentials are set, make sure you change your profile from `local` to something else.
It may also be helpful to create additional `application.properties` files, such as `application-dev.properties` with
the `spring.datasource.cluster-endpoint` parameter set to the Aurora DSQL cluster endpoint you are wanting to connect
to. Be careful, because `spring.jpa.hibernate.ddl-auto=update` will run migrations against your schema based on the JPA
entities you have defined.

You should now be able to run/debug as normal with your IDE.

## Deploying

To run these steps in one command you can use the `deploy.sh` script:

```shell
# chmod +x deploy.sh
./deploy.sh
```

Create Docker image we will build the application in

```shell
docker build --platform linux/amd64 -t al2023-graalvm21:native-web . --no-cache
```

Build the application

```shell
docker run --platform linux/amd64 -it -v `pwd`:`pwd` -w `pwd` -v ~/.m2:/root/.m2 al2023-graalvm21:native-web ./mvnw clean -Pnative package -DskipTests
```

Deploy via AWS SAM

```shell
sam deploy --guided --profile your-profile
```

Delete infrastructure

```shell
sam delete
```

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