-- Insert Users
INSERT INTO virtual_wallet.users (username, password, email, phone_number)
VALUES ('alice', '$2a$12$ZeD0iIW6ODU1s.pGjuXL.u7cJSddHBRiaupNIUXo160abY45Zpvsa', 'alice@example.com', '+1234567890'),
       ('bober', '$2a$12$eU62Y9k/7vV8hjpbQUfIz.Jca6hqR4ZRcp1hu9GgqxWglBScuIf3e', 'bob@example.com', '+1234567891'),
       ('charlie', '$2a$12$N4fImZBaMeBxKgqJXlIkfeRVsiX4WxwpYRiMb3kkpAdS1DBwR6d7m', 'charlie@example.com', '+1234567892'),
       ('david321', '$2a$12$.k28sG4V5tFxjxe7VHB4Lu.LuH6V2/h1NCNbWyNqE.pibDZTfwp7i', 'david@example.com', '+1234567893'),
       ('eve654', '$2a$12$iOq2o6h6kUA30QhJJuFS9OXZcwxMaIRK7eGP5j3NbVC8Mph.BqDae', 'eve@example.com', '+1234567894'),
       ('frank987', '$2a$12$4fLXwiC37ynhXgF70YgXHO4wlitKIxobC.mDmYNi9v0CZl43WI/vS', 'frank@example.com', '+1234567895'),
       ('grace111', '$2a$12$6bwtx42oUJ5RgWRFeS0gD.qfioQFcwRTgTW4hfzOdoxhacSAZp1Ei', 'grace@example.com', '+1234567896'),
       ('hannah222', '$2a$12$VnYEfX33l25JLk42GCOjj..IXCT2TLQm4FhUOKh0F47DXA1re7bwa', 'hannah@example.com', '+1234567897'),
       ('ian333', '$2a$12$g/iw1XfDYeQ25gzoRmCBze0iZK/liH4QDK7/SlhzLmyz3B.8EFhrS', 'ian@example.com', '+1234567898'),
       ('jack444', '$2a$12$id0a4ReIhm/E4VTspyGDuu/3VEcPVZJOBkOirknGWaDhCfjvdjBvu', 'jack@example.com', '+1234567899');

-- Insert Admins (Making first two users admins)

INSERT INTO virtual_wallet.roles(role_name) VALUES ('ROLE_ADMIN');

INSERT INTO virtual_wallet.user_roles(user_id, role_id) VALUES (1, 1),
                                                               (10, 1);

-- Insert Wallets
INSERT INTO virtual_wallet.wallets (balance, currency)
VALUES (500.00, 'USD'),
       (1500.00, 'USD'),
       (300.75, 'EUR'),
       (780.50, 'GBP'),
       (1250.30, 'USD'),
       (60.00, 'EUR'),
       (900.00, 'USD'),
       (175.45, 'GBP'),
       (420.80, 'USD'),
       (999.99, 'USD');

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
       (10, 10);

-- Insert Cards
INSERT INTO virtual_wallet.cards (card_number, card_holder, card_cvv, user_id)
VALUES ('1111222233334444', 'Alice Smith', '123', 1),
       ('2222333344445555', 'Bob Smith', '234', 2),
       ('3333444455556666', 'Charlie Smith', '345', 3),
       ('4444555566667777', 'David Smith', '456', 4),
       ('5555666677778888', 'Eve Smith', '567', 5),
       ('6666777788889999', 'Frank Smith', '678', 6),
       ('7777888899990000', 'Grace Smith', '789', 7),
       ('8888999900001111', 'Hannah Smith', '890', 8),
       ('9999000011112222', 'Ian Smith', '901', 9),
       ('0000111122223333', 'Jack Smith', '012', 10);

-- Insert Referrals
INSERT INTO virtual_wallet.referrals (referrer_id, referee_id)
VALUES (1, 2),
       (1, 3);

-- Insert Portfolios (Users purchasing stocks)
INSERT INTO virtual_wallet.stocks (buyer_id, stock_index, value, amount)
VALUES (1, 'AAPL', 150.00, 0.3),
       (2, 'GOOGL', 2800.00, 1),
       (3, 'MSFT', 300.00, 5),
       (4, 'TSLA', 900.00, 0.02),
       (5, 'AMZN', 3500.00, 1),
       (6, 'FB', 375.00, 2),
       (7, 'NFLX', 650.00, 3),
       (8, 'NVDA', 225.00, 4),
       (9, 'PYPL', 220.00, 2),
       (10, 'DIS', 170.00, 2);

-- Insert Verifications
INSERT INTO virtual_wallet.verifications (user_id, pictures_verified, email_verified)
VALUES (1, TRUE, TRUE),
       (2, TRUE, TRUE),
       (3, TRUE, TRUE),
       (4, TRUE, TRUE),
       (5, TRUE, TRUE),
       (6, FALSE, TRUE),
       (7, TRUE, FALSE),
       (8, FALSE, FALSE),
       (9, TRUE, TRUE),
       (10, TRUE, FALSE);

-- Insert Transaction Categories
INSERT INTO virtual_wallet.transaction_categories (name)
VALUES ('Shopping'),
       ('Food'),
       ('Bills'),
       ('Entertainment'),
       ('Investment');

-- Insert Transactions (Transferring between wallets)
INSERT INTO virtual_wallet.transactions (amount, wallet_sender_id, wallet_receiver_id, category_id)
VALUES (50.00, 1, 2, 1),
       (200.00, 2, 3, 2),
       (30.00, 3, 4, 3),
       (500.00, 4, 5, 4),
       (15.75, 5, 6, 1),
       (90.00, 6, 7, 2),
       (120.50, 7, 8, 3),
       (300.00, 8, 9, 4),
       (75.25, 9, 10, 5),
       (25.00, 10, 1, 1);
