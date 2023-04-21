#ユーザテーブルにデータを投入
INSERT INTO users (
	user_id,
	hashed_password,
	mail_address,
    is_active
)
VALUES (
	'TEST_USER',
    'testpassword',
    'test@example.com',
    1
);

#勘定科目テーブルにデータを投入
INSERT INTO accounttitles (
	accounttitle_id,
    accounttitle_name,
    accounting_type,
    loan_type,
    summary_type
)
VALUES (
	'101',
    '現金',
    'ASSETS',
    'DEBIT',
    'BS'
);

INSERT INTO accounttitles (
	accounttitle_id,
    accounttitle_name,
    accounting_type,
    loan_type,
    summary_type
)
VALUES (
	'102',
    '普通預金',
    'ASSETS',
    'DEBIT',
    'BS'
);

INSERT INTO accounttitles (
	accounttitle_id,
    accounttitle_name,
    accounting_type,
    loan_type,
    summary_type
)
VALUES (
	'201',
    '未払金',
    'LIABILITIES',
    'CREDIT',
    'BS'
);

INSERT INTO accounttitles (
	accounttitle_id,
    accounttitle_name,
    accounting_type,
    loan_type,
    summary_type
)
VALUES (
	'205',
    '借入金',
    'LIABILITIES',
    'CREDIT',
    'BS'
);

INSERT INTO accounttitles (
	accounttitle_id,
    accounttitle_name,
    accounting_type,
    loan_type,
    summary_type
)
VALUES (
	'301',
    '貯蓄',
    'NETASSETS',
    'CREDIT',
    'BS'
);

INSERT INTO accounttitles (
	accounttitle_id,
    accounttitle_name,
    accounting_type,
    loan_type,
    summary_type
)
VALUES (
	'401',
    '食費',
    'EXPENSES',
    'DEBIT',
    'PL'
);

INSERT INTO accounttitles (
	accounttitle_id,
    accounttitle_name,
    accounting_type,
    loan_type,
    summary_type
)
VALUES (
	'501',
    '給与',
    'REVENUE',
    'CREDIT',
    'PL'
);

#補助科目テーブルにデータを投入
INSERT INTO sub_accounttitles (
	sub_accounttitle_id,
    accounttitle_id,
    user_id,
    sub_accounttitle_name
)
VALUES (
	'0',
    '102',
    'TEST_USER',
    'その他'
);

INSERT INTO sub_accounttitles (
	sub_accounttitle_id,
    accounttitle_id,
    user_id,
    sub_accounttitle_name
)
VALUES (
	'1',
    '102',
    'TEST_USER',
    '第四北越銀行'
);

INSERT INTO sub_accounttitles (
	sub_accounttitle_id,
    accounttitle_id,
    user_id,
    sub_accounttitle_name
)
VALUES (
	'0',
    '401',
    'TEST_USER',
    'その他'
);

INSERT INTO sub_accounttitles (
	sub_accounttitle_id,
    accounttitle_id,
    user_id,
    sub_accounttitle_name
)
VALUES (
	'1',
    '401',
    'TEST_USER',
    '食料品'
);

INSERT INTO sub_accounttitles (
	sub_accounttitle_id,
    accounttitle_id,
    user_id,
    sub_accounttitle_name
)
VALUES (
	'2',
    '401',
    'TEST_USER',
    '外食'
);