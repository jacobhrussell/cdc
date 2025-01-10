-- See schemas
SELECT *
FROM pg_namespace;

-- See tables in the public schema
select *
from pg_tables
where schemaname = 'public';