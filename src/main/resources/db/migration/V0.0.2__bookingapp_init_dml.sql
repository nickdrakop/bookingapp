INSERT INTO owner (id, name, created_at) VALUES (1, 'Nick', now());
INSERT INTO owner (id, name, created_at) VALUES (2, 'Maria', now());
INSERT INTO owner (id, name, created_at) VALUES (3, 'Anna', now());

INSERT INTO guest (id, name, created_at) VALUES (1, 'Guest 1', now());
INSERT INTO guest (id, name, created_at) VALUES (2, 'Guest 2', now());
INSERT INTO guest (id, name, created_at) VALUES (3, 'Guest 3', now());

INSERT INTO property (id, name, owner_id, created_at) VALUES (1, 'Athens Apartment', 1, now());
INSERT INTO property (id, name, owner_id, created_at) VALUES (2, 'Kalamata Cottage', 2, now());
INSERT INTO property (id, name, owner_id, created_at) VALUES (3, 'Thessaloniki Villa', 3, now());