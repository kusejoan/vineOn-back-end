
INSERT into roles values (1,'regular') ON CONFLICT DO NOTHING;
INSERT  into roles values (2,'shop') ON CONFLICT DO NOTHING;