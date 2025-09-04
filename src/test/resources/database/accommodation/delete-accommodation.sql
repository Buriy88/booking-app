DELETE FROM accommodation_amenities;
delete from accommodation;
ALTER TABLE accommodation ALTER COLUMN id RESTART WITH 1;