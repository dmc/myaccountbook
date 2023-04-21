#ユーザテーブル
CREATE TABLE users (
	user_id varchar(32) PRIMARY KEY,
    hashed_password varchar(128) NOT NULL,
    mail_address varchar(32) NOT NULL,
    is_active boolean NOT NULL
);

#勘定科目テーブル
CREATE TABLE accounttitles (
	accounttitle_id varchar(3) PRIMARY KEY, 
	accounttitle_name varchar(32) NOT NULL,
    accounting_type varchar(16) NOT NULL,
	loan_type varchar(8) NOT NULL,
	summary_type varchar(8) NOT NULL
);

#補助科目テーブル
CREATE TABLE sub_accounttitles (
	sub_accounttitle_id varchar(1) NOT NULL,
    accounttitle_id varchar(3) NOT NULL,
    user_id varchar(32) NOT NULL,
    sub_accounttitle_name varchar(32) NOT NULL,
    PRIMARY KEY (sub_accounttitle_id, accounttitle_id, user_id),
    FOREIGN KEY (accounttitle_id) REFERENCES accounttitles (accounttitle_id),
    FOREIGN KEY (user_id) REFERENCES users (user_id)
);

#仕訳テーブル
CREATE TABLE journal_entries (
	entry_id varchar(36) PRIMARY KEY,
    deal_date date NOT NULL,
    entry_description varchar(64) NOT NULL,
    fiscal_yearmonth varchar(6) NOT NULL,
    user_id varchar(32) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (user_id)
);

#仕訳明細テーブル
CREATE TABLE entry_details (
	entry_id varchar(36),
	accounttitle_id varchar(3) NOT NULL,
    sub_accounttitle_id varchar(1) NOT NULL,
    loan_type varchar(8) NOT NULL,
    amount int NOT NULL,
    FOREIGN KEY (entry_id) REFERENCES journal_entries (entry_id),
    FOREIGN KEY (accounttitle_id) REFERENCES accounttitles (accounttitle_id)
);