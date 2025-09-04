
DELETE FROM accommodation_amenities;
DELETE FROM accommodation;
ALTER TABLE accommodation ALTER COLUMN id RESTART WITH 1;


INSERT INTO accommodation (type, location, size, daily_rate, availability)
VALUES ('APARTMENT', 'Kyiv, Khreshchatyk St. 10', '1 Bedroom', 30.00, 5),
       ('HOUSE', 'Lviv, Franka St. 25', '3 Bedroom', 120.00, 2),
       ('CONDO', 'Odessa, Deribasivska St. 5', 'Studio', 50.00, 10);


INSERT INTO accommodation_amenities (accommodation_id, amenity)
VALUES (1, 'WiFi'),
       (1, 'Air Conditioning'),
       (2, 'Garden'),
       (2, 'Parking'),
       (3, 'Sea View'),
       (3, 'WiFi');
