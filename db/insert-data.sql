-- Insert Wallets
INSERT INTO virtual_wallet.wallets (balance, currency)
VALUES (500.00, 'USD'),
       (1500.00, 'USD'),
       (300.75, 'USD'),
       (780.50, 'USD'),
       (1250.30, 'USD'),
       (60.00, 'USD'),
       (900.00, 'USD'),
       (175.45, 'USD'),
       (420.80, 'USD'),
       (999.99, 'USD'),
       (123.55, 'GBP'),
       (52.75, 'EUR'),
       (78.50, 'EUR'),
       (34.00, 'USD'),
       (25.00, 'USD'),
       (26.75, 'USD'),
       (632.50, 'USD'),
       (345.30, 'USD'),
       (256.00, 'USD'),
       (34.00, 'USD'),
       (55.45, 'USD'),
       (32.80, 'USD'),
       (44.99, 'USD'),
       (0.00, 'USD');;


-- Insert Users
INSERT INTO virtual_wallet.users (full_name, username, password, email, phone_number, main_wallet_id, created_at)
VALUES ('Alice Johnson', 'alice', '$2a$12$ZeD0iIW6ODU1s.pGjuXL.u7cJSddHBRiaupNIUXo160abY45Zpvsa', 'alice@example.com',
        '+1234567890', 1, '2024-04-01 11:30:43'),
       ('Bob Bober', 'bobcho', '$2a$12$eU62Y9k/7vV8hjpbQUfIz.Jca6hqR4ZRcp1hu9GgqxWglBScuIf3e', 'bob@example.com',
        '+1234567891', 2, '2024-05-15 11:30:43'),
       ('Charlie Sheen', 'charlie', '$2a$12$N4fImZBaMeBxKgqJXlIkfeRVsiX4WxwpYRiMb3kkpAdS1DBwR6d7m',
        'charlie@example.com',
        '+1234567892', 3, '2024-06-20 11:30:43'),
       ('David Rodriguez', 'david321', '$2a$12$.k28sG4V5tFxjxe7VHB4Lu.LuH6V2/h1NCNbWyNqE.pibDZTfwp7i',
        'david@example.com', '+1234567893', 4, '2024-07-25 11:30:43'),
       ('Eve Eve', 'eve654', '$2a$12$iOq2o6h6kUA30QhJJuFS9OXZcwxMaIRK7eGP5j3NbVC8Mph.BqDae', 'eve@example.com',
        '+1234567894', 5, '2024-08-10 11:30:43'),
       ('Frank Sinatra', 'frank987', '$2a$12$4fLXwiC37ynhXgF70YgXHO4wlitKIxobC.mDmYNi9v0CZl43WI/vS',
        'frank@example.com', '+1234567895', 6, '2024-09-15 11:30:43'),
       ('Your Grace', 'grace111', '$2a$12$6bwtx42oUJ5RgWRFeS0gD.qfioQFcwRTgTW4hfzOdoxhacSAZp1Ei', 'grace@example.com',
        '+1234567896', 7, '2024-10-05 11:30:43'),
       ('Hannah Montana', 'hannah222', '$2a$12$VnYEfX33l25JLk42GCOjj..IXCT2TLQm4FhUOKh0F47DXA1re7bwa',
        'hannah@example.com',
        '+1234567897', 8, '2024-11-18 11:30:43'),
       ('Ian Wolf', 'ian333', '$2a$12$g/iw1XfDYeQ25gzoRmCBze0iZK/liH4QDK7/SlhzLmyz3B.8EFhrS', 'ian@example.com',
        '+1234567898', 9, '2024-12-03 11:30:43'),
       ('Jack Russel', 'jack444', '$2a$12$id0a4ReIhm/E4VTspyGDuu/3VEcPVZJOBkOirknGWaDhCfjvdjBvu', 'jack@example.com',
        '+1234567899', 10, '2025-01-08 11:30:43'),
       ('Lily Allen', 'lily222', '$2a$12$ZeD0iIW6ODU1s.pGjuXL.u7cJSddHBRiaupNIUXo160abY45Zpvsa', 'lily@example.com',
        '+1934567810', 14, '2024-02-10 11:30:43'),
       ('Mason Lee', 'mason333', '$2a$12$ZeD0iIW6ODU1s.pGjuXL.u7cJSddHBRiaupNIUXo160abY45Zpvsa', 'mason@example.com',
        '+1934567811', 15, '2024-03-01 11:30:43'),
       ('Nina Rose', 'nina444', '$2a$12$ZeD0iIW6ODU1s.pGjuXL.u7cJSddHBRiaupNIUXo160abY45Zpvsa', 'nina@example.com',
        '+1934567812', 16, '2024-04-07 11:30:43'),
       ('Oscar Wilde', 'oscar555', '$2a$12$ZeD0iIW6ODU1s.pGjuXL.u7cJSddHBRiaupNIUXo160abY45Zpvsa', 'oscar@example.com',
        '+1934567813', 17, '2024-05-22 11:30:43'),
       ('Paulina Snow', 'paulina666', '$2a$12$ZeD0iIW6ODU1s.pGjuXL.u7cJSddHBRiaupNIUXo160abY45Zpvsa',
        'paulina@example.com', '+1934567814', 18, '2024-06-17 11:30:43'),
       ('Quincy Adams', 'quincy777', '$2a$12$ZeD0iIW6ODU1s.pGjuXL.u7cJSddHBRiaupNIUXo160abY45Zpvsa',
        'quincy@example.com', '+1294567815', 19, '2024-07-04 11:30:43'),
       ('Rachel Green', 'rachel888', '$2a$12$ZeD0iIW6ODU1s.pGjuXL.u7cJSddHBRiaupNIUXo160abY45Zpvsa',
        'rachel@example.com', '+1294567816', 20, '2024-08-14 11:30:43'),
       ('Sammy Clark', 'sammy999', '$2a$12$ZeD0iIW6ODU1s.pGjuXL.u7cJSddHBRiaupNIUXo160abY45Zpvsa', 'sammy@example.com',
        '+1294597817', 21, '2024-09-29 11:30:43'),
       ('Tina Turner', 'tina000', '$2a$12$ZeD0iIW6ODU1s.pGjuXL.u7cJSddHBRiaupNIUXo160abY45Zpvsa', 'tina@example.com',
        '+1294567818', 22, '2024-10-13 11:30:43'),
       ('Victor Hugo', 'victor111', '$2a$12$ZeD0iIW6ODU1s.pGjuXL.u7cJSddHBRiaupNIUXo160abY45Zpvsa',
        'victor@example.com', '+1294567819', 23, '2024-11-28 11:30:43'),
       ('Hugh Grant', 'hughG', '$2a$12$ZeD0iIW6ODU1s.pGjuXL.u7cJSddHBRiaupNIUXo160abY45Zpvsa',
        'hugh@example.com', '+1294557819', 24, '2024-11-28 11:30:43');

-- Insert Wallet Creators
INSERT INTO virtual_wallet.wallet_creators (wallet_id, creator_id)
VALUES (1, 1),
       (2, 2),
       (3, 3),
       (4, 4),
       (5, 5),
       (6, 6),
       (7, 7),
       (8, 8),
       (9, 9),
       (10, 10),
       (11, 1),
       (12, 1),
       (13, 2),
       (14, 11),
       (15, 12),
       (16, 13),
       (17, 14),
       (18, 15),
       (19, 16),
       (20, 17),
       (21, 18),
       (22, 19),
       (23, 20),
       (24, 21);


-- Insert Admins (Making first two users admins)

INSERT INTO virtual_wallet.roles(role_name)
VALUES ('ROLE_ADMIN');

INSERT INTO virtual_wallet.user_roles(user_id, role_id)
VALUES (1, 1),
       (10, 1);

-- Assign Users to Wallets
INSERT INTO virtual_wallet.users_wallets (wallet_id, user_id)
VALUES (1, 1),
       (2, 2),
       (3, 3),
       (4, 4),
       (5, 5),
       (6, 6),
       (7, 7),
       (8, 8),
       (9, 9),
       (10, 10),
       (11, 1),
       (11, 2),
       (12, 1),
       (12, 2),
       (12, 3),
       (12, 4),
       (12, 5),
       (13, 2),
       (13, 7),
       (14, 11),
       (15, 12),
       (16, 13),
       (17, 14),
       (18, 15),
       (19, 16),
       (20, 17),
       (21, 18),
       (22, 19),
       (23, 20),
       (24, 21);

-- Insert Cards
INSERT INTO virtual_wallet.cards (card_number, card_holder, expiry_month, expiry_year, card_cvv, user_id)
VALUES ('1111222233334444', 'Alice Smith', '02', '27', '123', 1),
       ('2222333344445555', 'Bob Smith', '03', '28', '234', 2),
       ('3333444455556666', 'Charlie Smith', '04', '25', '345', 3),
       ('4444555566667777', 'David Smith', '05', '26', '456', 4),
       ('5555666677778888', 'Eve Smith', '01', '27', '567', 5),
       ('6666777788889999', 'Frank Smith', '12', '25', '678', 6),
       ('7777888899990000', 'Grace Smith', '11', '26', '789', 7),
       ('8888999900001111', 'Hannah Smith', '10', '26', '890', 8),
       ('9999000011112222', 'Ian Smith', '07', '27', '901', 9),
       ('0000111122223333', 'Jack Smith', '02', '26', '012', 10),
       ('2111222253334324', 'Gary Oldman', '07', '27', '123', 1),
       ('2546333344423555', 'Jason Statham', '11', '28', '234', 2),
       ('3333552624766666', 'Julia Roberts', '12', '26', '345', 3);


-- Insert Referrals
INSERT INTO virtual_wallet.referrals (referrer_id, referee_id)
VALUES (1, 2),
       (1, 3),
       (2, 11),
       (2, 12),
       (2, 13),
       (2, 14),
       (2, 15),
       (1, 16),
       (2, 17),
       (1, 18),
       (5, 19),
       (3, 20),
       (3, 21);

-- Insert Stocks
INSERT INTO virtual_wallet.stocks (buyer_id, stock_symbol, value, amount)
VALUES (1, 'AAPL', 150.00, 0.3),
       (1, 'TSLA', 900.00, 0.5),
       (2, 'AAPL', 180.00, 1.5),
       (2, 'GOOGL', 2800.00, 1),
       (3, 'META', 350.00, 3),
       (3, 'TSLA', 850.00, 0.2),
       (4, 'AMZN', 3500.00, 1),
       (4, 'NFLX', 650.00, 3),
       (5, 'AMZN', 3500.00, 1),
       (5, 'TSLA', 900.00, 0.05);


-- Insert Investments
INSERT INTO virtual_wallet.investments (user_id, purchased_at, symbols, quantities, stock_values, total_value, type)
VALUES (1, '2025-01-01 12:00:00', 'AAPL,TSLA', '1,1', '100,1000', '1100', 'BUY'),
       (1, '2025-02-01 14:00:00', 'AAPL,TSLA', '1,1', '200,800', '1000', 'BUY'),
       (1, '2025-03-01 16:00:00', 'AAPL,TSLA', '1.7,1.5', '200,1000', '1840', 'SELL'),
       (2, '2025-01-01 12:00:00', 'AAPL,GOOGL', '3,2', '180,2800', '6140', 'BUY'),
       (2, '2025-02-01 14:00:00', 'AAPL,GOOGL', '2,1', '200,3000', '3400', 'SELL'),
       (3, '2025-01-01 12:00:00', 'META,TSLA', '3,0.2', '350,850', '1220', 'BUY'),
       (4, '2025-01-01 12:00:00', 'AMZN,NFLX', '1,3', '3500,650', '5450', 'BUY'),
       (5, '2025-01-01 12:00:00', 'AMZN,TSLA', '1,0.1', '3500,900', '3590', 'BUY'),
       (5, '2025-02-01 14:00:00', 'TSLA', '0.05', '900', '45', 'SELL'),

       (1, '2025-03-05 12:30:00', 'AAPL,TSLA', '2,1', '200,850', '1050', 'BUY'),
       (2, '2025-03-10 14:45:00', 'AAPL,TSLA', '1,1', '180,900', '1080', 'BUY'),
       (3, '2025-03-15 11:00:00', 'AAPL,TSLA', '1,0.5', '220,900', '710', 'SELL'),
       (4, '2025-02-20 13:10:00', 'AAPL,TSLA', '3,0.7', '200,850', '1510', 'BUY'),
       (5, '2025-03-20 15:30:00', 'AAPL,TSLA', '1,0.3', '220,900', '570', 'SELL');

-- Insert Verifications
INSERT INTO virtual_wallet.verifications (user_id, pictures_verified, email_verified, verified_at)
VALUES (1, TRUE, TRUE, '2024-04-01 11:30:43'),
       (2, TRUE, TRUE, '2024-05-15 11:30:43'),
       (3, TRUE, TRUE, '2024-06-20 11:30:43'),
       (4, TRUE, TRUE, '2024-07-25 11:30:43'),
       (5, TRUE, TRUE, '2024-08-10 11:30:43'),
       (6, TRUE, TRUE, '2024-09-15 11:30:43'),
       (7, TRUE, TRUE, '2024-10-05 11:30:43'),
       (8, TRUE, TRUE, '2024-11-18 11:30:43'),
       (9, TRUE, TRUE, '2024-12-03 11:30:43'),
       (10, TRUE, TRUE, '2025-01-08 11:30:43'),

       (11, TRUE, TRUE, '2024-02-10 11:30:43'),
       (12, TRUE, TRUE, '2024-03-01 11:30:43'),
       (13, TRUE, TRUE, '2024-04-07 11:30:43'),
       (14, TRUE, TRUE, '2024-05-22 11:30:43'),
       (15, TRUE, TRUE, '2024-06-17 11:30:43'),
       (16, TRUE, TRUE, '2024-07-04 11:30:43');
INSERT
INTO virtual_wallet.verifications (user_id, pictures_verified, email_verified)
VALUES (17, FALSE, TRUE),
       (18, FALSE, TRUE),
       (19, FALSE, TRUE),
       (20, FALSE, TRUE),
       (21, FALSE, TRUE);

-- Insert Transaction Categories
INSERT INTO virtual_wallet.transaction_categories (name)
VALUES ('Shopping'),
       ('Food'),
       ('Bills'),
       ('Entertainment'),
       ('Investment'),
       ('Other');

-- Insert Transactions (Transferring between wallets)
INSERT INTO virtual_wallet.transactions (amount, user_sender_id, wallet_sender_id, wallet_receiver_id, category_id,
                                         message, transaction_time)
VALUES (50.00, 1, 1, 2, 1, 'Lunch split', '2025-03-19 14:23:45'),
       (200.00, 2, 2, 3, 2, 'Rent contribution', '2025-02-21 09:30:12'),
       (30.00, 3, 3, 4, 3, 'Coffee reimbursement', '2025-01-12 18:45:27'),
       (500.00, 4, 4, 5, 4, 'Freelance payment', '2025-03-05 22:10:33'),
       (25.00, 10, 10, 1, 1, 'Dinner bill share', '2025-03-14 21:50:42'),

       (40.00, 1, 1, 3, 2, 'Uber fare split', '2025-03-05 13:12:10'),
       (150.75, 2, 2, 4, 3, 'Subscription payment', '2025-02-25 10:40:50'),
       (22.30, 3, 3, 5, 1, 'Gym membership', '2025-01-20 07:55:35'),
       (110.00, 9, 9, 1, 4, 'Concert tickets', '2025-01-12 22:15:40'),

       -- New Transactions for user_sender_id 1
       (35.00, 1, 1, 5, 3, 'Breakfast share', '2024-04-10 08:30:15'),
       (80.00, 1, 1, 6, 2, 'Cinema tickets', '2024-05-12 19:45:30'),
       (95.25, 1, 1, 7, 4, 'Restaurant bill', '2024-06-18 21:30:45'),
       (40.50, 1, 1, 8, 5, 'Transport fee', '2024-07-25 10:15:20'),
       (30.00, 1, 1, 2, 3, 'Shopping cost', '2024-10-16 12:45:25'),
       (110.50, 1, 1, 3, 4, 'Home supplies', '2024-11-22 15:30:40'),
       (85.50, 1, 1, 6, 2, 'Gas refill', '2025-02-07 09:30:15'),
       (95.00, 1, 1, 7, 3, 'Vacation planning', '2025-03-14 16:40:30'),

       -- New Transactions for user_sender_id 2
       (45.00, 2, 2, 5, 3, 'Brunch outing', '2024-04-05 09:15:10'),
       (120.00, 2, 2, 6, 2, 'Hotel stay', '2024-05-20 18:30:40'),
       (150.50, 2, 2, 7, 4, 'Work supplies', '2024-06-08 20:15:55'),
       (80.75, 2, 2, 8, 5, 'Holiday gifts', '2024-07-14 11:50:30'),
       (35.25, 2, 2, 9, 1, 'Coffee meetup', '2024-08-23 14:40:20'),
       (110.00, 2, 2, 10, 2, 'Furniture payment', '2024-09-11 21:05:10'),
       (310.00, 2, 2, 4, 5, 'Health insurance', '2024-12-08 17:55:50'),
       (95.75, 2, 2, 7, 3, 'Team dinner', '2025-03-07 15:10:50'),


       -- Wallet sender ID 11 (user_sender_id: 1 or 2)
       (72.00, 1, 11, 1, 1, 'Grocery split', '2025-02-10 09:25:10'),
       (40.00, 2, 11, 2, 2, 'Shared lunch', '2025-03-03 18:40:25'),
       (98.50, 1, 11, 3, 3, 'Movie ticket share', '2025-01-25 14:15:35'),
       (65.75, 1, 11, 5, 5, 'Concert expenses', '2025-03-18 17:00:05'),

       -- Wallet sender ID 12 (user_sender_id: 1, 2, 3, 4, or 5)
       (58.25, 1, 12, 6, 1, 'Gym subscription', '2024-04-10 10:30:15'),
       (122.00, 2, 12, 7, 2, 'Business expense', '2024-06-17 15:45:30'),
       (45.50, 4, 12, 9, 4, 'Coffee date', '2024-12-02 11:20:40'),
       (210.00, 5, 12, 10, 5, 'Weekend getaway', '2025-02-19 22:10:55'),

       -- Wallet sender ID 13 (user_sender_id: 2 or 7)
       (80.00, 2, 13, 1, 1, 'Shared dinner', '2024-05-14 18:35:25'),
       (140.50, 7, 13, 2, 2, 'Project payment', '2024-08-03 13:20:50'),
       (65.20, 2, 13, 3, 3, 'Food delivery', '2024-10-18 21:05:15'),
       (92.00, 2, 13, 5, 5, 'Birthday present', '2025-03-06 08:40:30'),

-- Wallet receiver ID 11 (wallet sender id between 1 and 10)
       (50.00, 1, 1, 11, 1, 'Lunch split', '2025-03-23 14:25:10'),
       (88.75, 3, 3, 11, 3, 'Shared gift expense', '2025-03-10 11:35:30'),

-- Wallet receiver ID 12 (wallet sender id between 1 and 10)
       (64.50, 2, 2, 12, 1, 'Shared lunch cost', '2025-03-21 20:10:25'),
       (55.25, 6, 6, 12, 3, 'Concert expenses', '2025-02-28 17:55:40'),

-- Wallet receiver ID 13 (wallet sender id between 1 and 10)
       (70.00, 7, 7, 13, 1, 'Event ticket purchase', '2025-03-08 09:30:15'),
       (90.00, 2, 2, 13, 3, 'Food delivery cost', '2025-02-10 14:10:25');


-- Insert Transfers (Transferring between card and wallet)
INSERT INTO virtual_wallet.transfers (amount, card_sender_id, wallet_receiver_id, transfer_time)
VALUES (50.00, 1, 1, '2025-03-10 14:20:33'),
       (200.00, 2, 2, '2025-02-21 09:15:45'),
       (30.00, 3, 3, '2025-01-18 18:40:25'),
       (500.00, 4, 4, '2025-03-07 22:05:30'),
       (15.75, 5, 5, '2025-02-12 20:35:12'),

       (65.00, 11, 1, '2025-03-02 10:30:12'),
       (200.00, 11, 1, '2025-01-09 19:20:05'),
       (75.00, 12, 2, '2025-03-22 11:10:50'),
       (220.00, 12, 2, '2025-01-05 20:55:40'),
       (80.00, 13, 3, '2025-03-18 09:25:18'),
       (155.75, 13, 3, '2025-02-25 13:40:55'),

       -- New Transfers for card_sender_id 1 (Randomized Amounts)
       (47.80, 1, 1, '2024-04-12 08:15:25'),
       (93.25, 1, 1, '2024-05-10 18:40:30'),
       (142.10, 1, 1, '2024-06-22 21:30:10'),
       (37.65, 1, 1, '2024-07-15 10:05:45'),
       (28.30, 1, 1, '2024-08-05 14:55:20'),
       (75.90, 1, 1, '2024-09-09 22:10:33'),
       (19.45, 1, 1, '2024-10-14 12:45:15'),
       (123.00, 1, 1, '2024-11-20 15:30:50'),
       (255.75, 1, 1, '2024-12-29 17:20:55'),
       (138.50, 1, 1, '2025-01-11 11:55:30'),
       (92.15, 1, 1, '2025-02-07 09:25:15'),
       (88.40, 1, 1, '2025-03-14 16:30:10'),

       -- New Transfers for card_sender_id 2 (Randomized Amounts)
       (51.35, 2, 2, '2024-04-09 09:30:40'),
       (134.90, 2, 2, '2024-05-22 19:20:55'),
       (175.40, 2, 2, '2024-06-08 20:10:30'),
       (83.65, 2, 2, '2024-07-14 11:40:15'),
       (29.25, 2, 2, '2024-08-28 14:50:20'),
       (118.75, 2, 2, '2024-09-16 21:05:30'),
       (212.30, 2, 2, '2024-10-25 13:10:55'),
       (241.65, 2, 2, '2024-11-11 16:30:40'),
       (328.90, 2, 2, '2024-12-07 17:55:25'),
       (169.75, 2, 2, '2025-01-29 10:05:45'),
       (145.20, 2, 2, '2025-02-18 12:15:30'),
       (107.85, 2, 2, '2025-03-08 15:40:10');



INSERT INTO virtual_wallet.exchange_rates(from_currency, to_currency, rate)
VALUES ('EUR', 'USD', 1.09),
       ('EUR', 'GBP', 0.84),
       ('GBP', 'USD', 1.30);



INSERT INTO virtual_wallet.articles(title, description, url)
VALUES ('The No. 1 mistake I see job seekers making in 2025, says career expert: It can ‘destroy your chances’',
        'Here''s what to do instead to help you stand out and land the role, says Jeremy Schifeling, founder of The Job Insiders and author of \"Career Coach GPT.\"',
        'https://www.cnbc.com/2025/03/24/the-no-1-mistake-job-seekers-are-making-in-2025-it-can-destroy-your-chances.html'),
       ('What''s hot in ETFs? Bond funds are in demand as investors flee the Nasdaq ''QQQ''',
        'There have been strong inflows into fixed income, especially ultrashort funds. Precious metal funds have seen surprisingly light inflows.',
        'https://www.cnbc.com/2025/03/24/etf-conference-draws-2000-managers-and-advisors-to-air-latest-trends.html'),
       ('Gain influence at work by building ''social fitness,'' researchers say: 2 easy strategies to get better',
        'Building social endurance can help you gain influence and get ahead at work, workplace experts and performance coaches Henna Pryor and Shane Hatton say.',
        'https://www.cnbc.com/2025/03/24/gain-influence-at-work-by-building-social-fitness-easy-strategies.html'),
       ('The ''fatal mistake'' most people make starting a business, says Stanford professor who co-founded 4 startups: I''ve ''seen this a million times''',
        'Not finding out what prospective customers want and need is a crucial mistake that often dooms founders to failure, says entrepreneurship expert Steve Blank.',
        'https://www.cnbc.com/2025/03/24/steve-blank-most-people-make-this-fatal-mistake-starting-a-business.html'),
       ('Tax revenue collected by the IRS set to plummet, report says',
        'IRS officials are expecting tax revenue to drop by more than 10% by April 15, the Washington Post reported.',
        'https://www.cnbc.com/2025/03/24/tax-revenue-collected-by-the-irs-set-to-plummet-report-says.html');