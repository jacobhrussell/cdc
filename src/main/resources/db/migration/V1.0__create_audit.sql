CREATE TABLE public.base_history
(
    hist_id          bigserial                          NOT NULL PRIMARY KEY,
    created          timestamptz DEFAULT now()          NOT NULL,
    dml_type         char(1)                            NOT NULL, -- I (insert), U (update), D (delete)
    txid             bigint      DEFAULT txid_current() NOT NULL,
    application_name text        DEFAULT current_setting('application_name'),
    db_session_user  text        DEFAULT session_user   NOT NULL,
    inet_client_addr inet        DEFAULT inet_client_addr(),
    pg_backend_pid   int         DEFAULT pg_backend_pid(),
    old_row_data     json,
    new_row_data     json,
    pk_value         UUID
);

CREATE OR REPLACE FUNCTION public.f_generic_history()
    RETURNS trigger AS
$$
DECLARE
    cc_dml_type        constant char(1)     := substring(TG_OP from 1 for 1);
    vc_insert_sql      constant text        := 'INSERT INTO public.' || TG_TABLE_NAME ||
                                               '_history (dml_type, old_row_data, new_row_data, pk_value) VALUES ($1, $2, $3, $4)';
    vc_pk_sql_template constant text        := 'SELECT ($1).%I';
    vc_pk_column_name  constant varchar(64) := TG_ARGV[0];
    uuid_pk_value               UUID        := null;
BEGIN
    IF TG_OP = 'INSERT' THEN
        IF vc_pk_column_name IS NOT NULL THEN
            EXECUTE format(vc_pk_sql_template, vc_pk_column_name) INTO uuid_pk_value USING NEW;
        END IF;

        EXECUTE vc_insert_sql USING cc_dml_type, NULL::json, row_to_json(NEW), uuid_pk_value;

    ELSIF (TG_OP = 'UPDATE' AND OLD IS DISTINCT FROM NEW) THEN
        IF vc_pk_column_name IS NOT NULL THEN
            EXECUTE format(vc_pk_sql_template, vc_pk_column_name) INTO uuid_pk_value USING NEW;
        END IF;

        EXECUTE vc_insert_sql USING cc_dml_type, row_to_json(OLD), row_to_json(NEW), uuid_pk_value;
    ELSIF TG_OP = 'DELETE' THEN
        IF vc_pk_column_name IS NOT NULL THEN
            EXECUTE format(vc_pk_sql_template, vc_pk_column_name) INTO uuid_pk_value USING OLD;
        END IF;

        EXECUTE vc_insert_sql USING cc_dml_type, row_to_json(OLD), NULL::json, uuid_pk_value;
    END IF;

    RETURN NULL;
END;
$$ LANGUAGE plpgsql;

