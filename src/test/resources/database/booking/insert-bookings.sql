DELETE FROM booking;
ALTER TABLE booking ALTER COLUMN id RESTART WITH 2001;

INSERT INTO booking (id, check_in_date, check_out_date, accommodation_id, user_id, status)
VALUES
    (2001, '2025-10-01', '2025-10-03', 1, 1001, 'PENDING'),
    (2002, '2025-11-01', '2025-11-02', 1, 1001, 'PENDING');

ALTER TABLE booking ALTER COLUMN id RESTART WITH 2003;
