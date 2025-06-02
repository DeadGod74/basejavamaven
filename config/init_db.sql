CREATE TABLE resume
(
    uuid CHAR(36) PRIMARY KEY NOT NULL,
    full_name TEXT NOT NULL
);

CREATE TABLE contact
(
    id SERIAL,
    type TEXT NOT NULL,
    value TEXT NOT NULL,
    resume_uuid CHAR(36) NOT NULL,
    CONSTRAINT contact_resume_uuid_fk FOREIGN KEY (resume_uuid) REFERENCES resume (uuid)
);

CREATE UNIQUE INDEX contact_uuid_type_index ON public.contact (resume_uuid, type);