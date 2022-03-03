CREATE TABLE IF NOT EXISTS owner
(
    id INTEGER NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    created_at DATETIME(6) NOT NULL,
    CONSTRAINT property_owner_id_pkey PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS property
(
    id INTEGER NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    owner_id INTEGER NOT NULL,
    created_at DATETIME(6) NOT NULL,
    CONSTRAINT property_id_pkey PRIMARY KEY (id),
    CONSTRAINT property_owner_id_fk FOREIGN KEY (owner_id) REFERENCES owner (id)
);

CREATE TABLE IF NOT EXISTS guest
(
    id INTEGER NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    created_at DATETIME(6) NOT NULL,
    CONSTRAINT guest_id_pkey PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS booking
(
    id INTEGER NOT NULL AUTO_INCREMENT,
    property_id INTEGER NOT NULL,
    guest_id INTEGER NOT NULL,
    start_date DATETIME(6) NOT NULL,
    end_date DATETIME(6) NOT NULL,
    status VARCHAR(255) NOT NULL,
    created_at DATETIME(6) NOT NULL,
    CONSTRAINT booking_id_pkey PRIMARY KEY (id),
    CONSTRAINT booking_property_id_fk FOREIGN KEY (property_id) REFERENCES property (id),
    CONSTRAINT booking_guest_id_fk FOREIGN KEY (guest_id) REFERENCES guest (id)
);

CREATE TABLE IF NOT EXISTS block
(
    id INTEGER NOT NULL AUTO_INCREMENT,
    property_id INTEGER NOT NULL,
    start_date DATETIME(6) NOT NULL,
    end_date DATETIME(6) NOT NULL,
    created_at DATETIME(6) NOT NULL,
    CONSTRAINT block_id_pkey PRIMARY KEY (id),
    CONSTRAINT block_property_id_fk FOREIGN KEY (property_id) REFERENCES property (id)
);