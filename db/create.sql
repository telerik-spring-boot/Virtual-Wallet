CREATE SCHEMA virtual_wallet;


DROP TABLE IF EXISTS virtual_wallet.users CASCADE;
DROP TABLE IF EXISTS virtual_wallet.roles CASCADE;
DROP TABLE IF EXISTS virtual_wallet.user_roles CASCADE;
DROP TABLE IF EXISTS virtual_wallet.users_wallets CASCADE;
DROP TABLE IF EXISTS virtual_wallet.cards CASCADE;
DROP TABLE IF EXISTS virtual_wallet.wallets CASCADE;
DROP TABLE IF EXISTS virtual_wallet.referrals CASCADE;
DROP TABLE IF EXISTS virtual_wallet.transactions CASCADE;
DROP TABLE IF EXISTS virtual_wallet.transfers CASCADE;
DROP TABLE IF EXISTS virtual_wallet.transaction_categories CASCADE;
DROP TABLE IF EXISTS virtual_wallet.stocks CASCADE;
DROP TABLE IF EXISTS virtual_wallet.verifications CASCADE;
DROP TABLE IF EXISTS virtual_wallet.investments CASCADE;
DROP TABLE IF EXISTS virtual_wallet.exchange_rates CASCADE;
DROP TABLE IF EXISTS virtual_wallet.articleResponses CASCADE;

CREATE TABLE virtual_wallet.wallets
(
    wallet_id INT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    balance   DECIMAL(15, 2)     NOT NULL,
    currency  VARCHAR(30)        NOT NULL
);

CREATE TABLE virtual_wallet.users
(
    user_id        INT AUTO_INCREMENT PRIMARY KEY,
    full_name      VARCHAR(50)                          NOT NULL,
    username       VARCHAR(20)                          NOT NULL UNIQUE,
    password       VARCHAR(70)                          NOT NULL,
    email          VARCHAR(50)                          NOT NULL UNIQUE,
    phone_number   VARCHAR(20)                          NOT NULL UNIQUE,
    is_blocked     TINYINT(1) DEFAULT 0                 NOT NULL,
    main_wallet_id INT                                  NOT NULL,
    created_at     TIMESTAMP  DEFAULT CURRENT_TIMESTAMP NOT NULL,
    last_online    TIMESTAMP  DEFAULT CURRENT_TIMESTAMP NOT NULL,
    FOREIGN KEY (main_wallet_id) REFERENCES virtual_wallet.wallets (wallet_id)
);

CREATE TABLE virtual_wallet.wallet_creators
(
    wallet_id  INT NOT NULL PRIMARY KEY,
    creator_id INT NOT NULL,
    FOREIGN KEY (wallet_id) REFERENCES virtual_wallet.wallets (wallet_id) ON DELETE CASCADE,
    FOREIGN KEY (creator_id) REFERENCES virtual_wallet.users (user_id) ON DELETE CASCADE
);

CREATE TABLE virtual_wallet.roles
(
    role_id   INT AUTO_INCREMENT PRIMARY KEY,
    role_name VARCHAR(30) UNIQUE NOT NULL
);


CREATE TABLE virtual_wallet.user_roles
(
    user_id INT NOT NULL,
    role_Id INT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES virtual_wallet.users (user_id),
    FOREIGN KEY (role_id) REFERENCES virtual_wallet.roles (role_id)
);


CREATE TABLE virtual_wallet.cards
(
    card_id      INT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    card_number  VARCHAR(16)        NOT NULL,
    card_holder  VARCHAR(30)        NOT NULL,
    expiry_month CHAR(2)            NOT NULL CHECK (expiry_month BETWEEN '01' AND '12'),
    expiry_year  CHAR(2)            NOT NULL CHECK (expiry_year BETWEEN '00' AND '99'),
    card_cvv     VARCHAR(3)         NOT NULL,
    user_id      INT                NOT NULL,
    FOREIGN KEY (user_id) REFERENCES virtual_wallet.users (user_id) ON DELETE CASCADE
);

CREATE TABLE virtual_wallet.users_wallets
(
    id        INT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    wallet_id INT                NOT NULL,
    user_id   INT                NOT NULL,
    FOREIGN KEY (user_id) REFERENCES virtual_wallet.users (user_id) ON DELETE CASCADE,
    FOREIGN KEY (wallet_id) REFERENCES virtual_wallet.wallets (wallet_id) ON DELETE CASCADE
);

CREATE TABLE virtual_wallet.referrals
(
    id          INT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    referrer_id INT                NOT NULL,
    referee_id  INT                NOT NULL,
    FOREIGN KEY (referrer_id) REFERENCES virtual_wallet.users (user_id) ON DELETE CASCADE,
    FOREIGN KEY (referee_id) REFERENCES virtual_wallet.users (user_id) ON DELETE CASCADE
);

CREATE TABLE virtual_wallet.stocks
(
    id           INT AUTO_INCREMENT                  NOT NULL PRIMARY KEY,
    buyer_id     INT                                 NOT NULL,
    purchased_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    stock_symbol VARCHAR(30)                         NOT NULL,
    value        DECIMAL(15, 5)                      NOT NULL,
    amount       DECIMAL(15, 5)                      NOT NULL,
    FOREIGN KEY (buyer_id) REFERENCES virtual_wallet.users (user_id) ON DELETE CASCADE
);


CREATE TABLE virtual_wallet.verifications
(
    id                INT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    user_id           INT                NOT NULL,
    pictures_verified BOOLEAN            NOT NULL,
    email_verified    BOOLEAN            NOT NULL,
    verified_at       TIMESTAMP          NULL,
    FOREIGN KEY (user_id) REFERENCES virtual_wallet.users (user_id) ON DELETE CASCADE
);

CREATE TABLE virtual_wallet.transaction_categories
(
    id   INT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    name VARCHAR(50)        NOT NULL
);

CREATE TABLE virtual_wallet.transactions
(
    id                 INT AUTO_INCREMENT                  NOT NULL PRIMARY KEY,
    transaction_time   TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    amount             DECIMAL(15, 2)                      NOT NULL,
    user_sender_id     INT                                 NOT NULL,
    wallet_sender_id   INT                                 NOT NULL,
    wallet_receiver_id INT                                 NOT NULL,
    message            VARCHAR(50)                         NOT NULL,
    category_id        INT                                 NOT NULL,
    FOREIGN KEY (user_sender_id) REFERENCES virtual_wallet.users (user_id) ON DELETE CASCADE,
    FOREIGN KEY (wallet_sender_id) REFERENCES virtual_wallet.wallets (wallet_id) ON DELETE CASCADE,
    FOREIGN KEY (wallet_receiver_id) REFERENCES virtual_wallet.wallets (wallet_id) ON DELETE CASCADE,
    FOREIGN KEY (category_id) REFERENCES virtual_wallet.transaction_categories (id) ON DELETE CASCADE
);

CREATE TABLE virtual_wallet.transfers
(
    id                 INT AUTO_INCREMENT                  NOT NULL PRIMARY KEY,
    transfer_time      TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    amount             DECIMAL(15, 2)                      NOT NULL,
    card_sender_id     INT                                 NOT NULL,
    wallet_receiver_id INT                                 NOT NULL,
    FOREIGN KEY (card_sender_id) REFERENCES virtual_wallet.cards (card_id) ON DELETE CASCADE,
    FOREIGN KEY (wallet_receiver_id) REFERENCES virtual_wallet.wallets (wallet_id) ON DELETE CASCADE

);


CREATE TABLE virtual_wallet.investments
(
    id           INT AUTO_INCREMENT                  NOT NULL PRIMARY KEY,
    user_id      INT                                 NOT NULL,
    purchased_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    symbols      TEXT                                NOT NULL,
    quantities   TEXT                                NOT NULL,
    stock_values TEXT                                NOT NULL,
    total_value  decimal(15, 5)                      NOT NULL,
    type         varchar(5)                          NOT NULL,
    FOREIGN KEY (user_id) REFERENCES virtual_wallet.users (user_id) ON DELETE CASCADE
);

CREATE TABLE virtual_wallet.exchange_rates(
    id INT AUTO_INCREMENT PRIMARY KEY,
    from_currency VARCHAR(30) NOT NULL,
    to_currency VARCHAR(30) NOT NULL,
    rate DECIMAL(15, 5) NOT NULL,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);

CREATE TABLE virtual_wallet.articles(
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(256) NOT NULL,
    description TEXT NOT NULL,
    url VARCHAR(256) NOT NULL,
    published_on TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);


