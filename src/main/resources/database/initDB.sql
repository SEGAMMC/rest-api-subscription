-- DROP TABLE IF EXISTS subscriptions;
-- DROP SEQUENCE IF EXISTS subscription_id_seq;
-- DROP TABLE IF EXISTS transport;
-- DROP SEQUENCE IF EXISTS transport_id_seq;
-- DROP SEQUENCE IF EXISTS transport_seq;


CREATE TABLE IF NOT EXISTS transport (
    id INTEGER PRIMARY KEY NOT NULL ,
    type VARCHAR (30) NOT NULL,
    endpoint VARCHAR (80) NOT NULL UNIQUE
);

CREATE SEQUENCE IF NOT EXISTS transport_id_seq START WITH 1 INCREMENT BY 1;


CREATE TABLE IF NOT EXISTS subscriptions (
    subscription_id VARCHAR (80) PRIMARY KEY,
    created timestamp NOT NULL,
    expires timestamp NOT NULL,
    status VARCHAR (8) NOT NULL,
    transport_id int NOT NULL ,
    FOREIGN KEY (transport_id) references webhook.public.transport(id)
);
-- CREATE SEQUENCE subscription_id_seq START WITH 3 INCREMENT BY 1;
-- CREATE SEQUENCE transport_seq START WITH 3 INCREMENT BY 1;
-- @SequenceGenerator(name = "id", sequenceName = "transport_seq", allocationSize = 1)
