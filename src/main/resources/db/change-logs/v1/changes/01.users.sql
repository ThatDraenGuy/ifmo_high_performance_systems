CREATE TABLE users (
    user_id SERIAL8 PRIMARY KEY,
    username VARCHAR NOT NULL,
    password VARCHAR NOT NULL,
    del_date TIMESTAMP WITH TIME ZONE NULL,
    del_user VARCHAR NULL,
    CONSTRAINT user_name_chk EXCLUDE (username WITH =) WHERE (del_date IS NULL)
);

CREATE TABLE user_roles (
    user_user_id INT8 NOT NULL,
    role VARCHAR NOT NULL,
    CONSTRAINT user_roles_pk PRIMARY KEY (user_user_id, role),
    CONSTRAINT user_roles_fk FOREIGN KEY (user_user_id) REFERENCES users(user_id),
    CONSTRAINT user_roles_chk CHECK (role::text = ANY (ARRAY['GUEST', 'CLIENT', 'OPERATOR' 'ADMIN']::text[]))
);
