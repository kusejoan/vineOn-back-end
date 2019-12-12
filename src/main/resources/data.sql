
INSERT into roles values (1,'customer') ON CONFLICT DO NOTHING;
INSERT  into roles values (2,'store') ON CONFLICT DO NOTHING;