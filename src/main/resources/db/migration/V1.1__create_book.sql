CREATE TABLE public.book
(
    id    UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    title VARCHAR(500) NULL
);

CREATE TABLE public.book_history
(
    LIKE public.base_history INCLUDING ALL
);

CREATE INDEX idx_book_history_created ON public.book_history(created);
CREATE INDEX idx_book_history_dml_type ON public.book_history(dml_type);
CREATE INDEX idx_book_history_pk_value ON public.book_history(pk_value);
CREATE INDEX idx_book_history_pk_value_created ON public.book_history(pk_value, created DESC);

CREATE TRIGGER z_record_history
    AFTER INSERT OR UPDATE OR DELETE
    ON public.book
    FOR EACH ROW
EXECUTE PROCEDURE public.f_generic_history('id');