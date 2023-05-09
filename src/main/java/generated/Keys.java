/*
 * This file is generated by jOOQ.
 */
package generated;


import generated.tables.Accounttitles;
import generated.tables.EntryDetails;
import generated.tables.JournalEntries;
import generated.tables.MonthlyBalances;
import generated.tables.SubAccounttitles;
import generated.tables.Users;
import generated.tables.records.AccounttitlesRecord;
import generated.tables.records.EntryDetailsRecord;
import generated.tables.records.JournalEntriesRecord;
import generated.tables.records.MonthlyBalancesRecord;
import generated.tables.records.SubAccounttitlesRecord;
import generated.tables.records.UsersRecord;

import org.jooq.ForeignKey;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.Internal;


/**
 * A class modelling foreign key relationships and constraints of tables in
 * accountbook_test.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Keys {

    // -------------------------------------------------------------------------
    // UNIQUE and PRIMARY KEY definitions
    // -------------------------------------------------------------------------

    public static final UniqueKey<AccounttitlesRecord> KEY_ACCOUNTTITLES_PRIMARY = Internal.createUniqueKey(Accounttitles.ACCOUNTTITLES, DSL.name("KEY_accounttitles_PRIMARY"), new TableField[] { Accounttitles.ACCOUNTTITLES.ACCOUNTTITLE_ID }, true);
    public static final UniqueKey<JournalEntriesRecord> KEY_JOURNAL_ENTRIES_PRIMARY = Internal.createUniqueKey(JournalEntries.JOURNAL_ENTRIES, DSL.name("KEY_journal_entries_PRIMARY"), new TableField[] { JournalEntries.JOURNAL_ENTRIES.ENTRY_ID }, true);
    public static final UniqueKey<SubAccounttitlesRecord> KEY_SUB_ACCOUNTTITLES_PRIMARY = Internal.createUniqueKey(SubAccounttitles.SUB_ACCOUNTTITLES, DSL.name("KEY_sub_accounttitles_PRIMARY"), new TableField[] { SubAccounttitles.SUB_ACCOUNTTITLES.SUB_ACCOUNTTITLE_ID, SubAccounttitles.SUB_ACCOUNTTITLES.ACCOUNTTITLE_ID, SubAccounttitles.SUB_ACCOUNTTITLES.USER_ID }, true);
    public static final UniqueKey<UsersRecord> KEY_USERS_PRIMARY = Internal.createUniqueKey(Users.USERS, DSL.name("KEY_users_PRIMARY"), new TableField[] { Users.USERS.USER_ID }, true);

    // -------------------------------------------------------------------------
    // FOREIGN KEY definitions
    // -------------------------------------------------------------------------

    public static final ForeignKey<EntryDetailsRecord, JournalEntriesRecord> ENTRY_DETAILS_IBFK_1 = Internal.createForeignKey(EntryDetails.ENTRY_DETAILS, DSL.name("entry_details_ibfk_1"), new TableField[] { EntryDetails.ENTRY_DETAILS.ENTRY_ID }, Keys.KEY_JOURNAL_ENTRIES_PRIMARY, new TableField[] { JournalEntries.JOURNAL_ENTRIES.ENTRY_ID }, true);
    public static final ForeignKey<EntryDetailsRecord, AccounttitlesRecord> ENTRY_DETAILS_IBFK_2 = Internal.createForeignKey(EntryDetails.ENTRY_DETAILS, DSL.name("entry_details_ibfk_2"), new TableField[] { EntryDetails.ENTRY_DETAILS.ACCOUNTTITLE_ID }, Keys.KEY_ACCOUNTTITLES_PRIMARY, new TableField[] { Accounttitles.ACCOUNTTITLES.ACCOUNTTITLE_ID }, true);
    public static final ForeignKey<JournalEntriesRecord, UsersRecord> JOURNAL_ENTRIES_IBFK_1 = Internal.createForeignKey(JournalEntries.JOURNAL_ENTRIES, DSL.name("journal_entries_ibfk_1"), new TableField[] { JournalEntries.JOURNAL_ENTRIES.USER_ID }, Keys.KEY_USERS_PRIMARY, new TableField[] { Users.USERS.USER_ID }, true);
    public static final ForeignKey<MonthlyBalancesRecord, AccounttitlesRecord> MONTHLY_BALANCES_IBFK_1 = Internal.createForeignKey(MonthlyBalances.MONTHLY_BALANCES, DSL.name("monthly_balances_ibfk_1"), new TableField[] { MonthlyBalances.MONTHLY_BALANCES.ACCOUNTTITLE_ID }, Keys.KEY_ACCOUNTTITLES_PRIMARY, new TableField[] { Accounttitles.ACCOUNTTITLES.ACCOUNTTITLE_ID }, true);
    public static final ForeignKey<MonthlyBalancesRecord, UsersRecord> MONTHLY_BALANCES_IBFK_2 = Internal.createForeignKey(MonthlyBalances.MONTHLY_BALANCES, DSL.name("monthly_balances_ibfk_2"), new TableField[] { MonthlyBalances.MONTHLY_BALANCES.USER_ID }, Keys.KEY_USERS_PRIMARY, new TableField[] { Users.USERS.USER_ID }, true);
    public static final ForeignKey<SubAccounttitlesRecord, AccounttitlesRecord> SUB_ACCOUNTTITLES_IBFK_1 = Internal.createForeignKey(SubAccounttitles.SUB_ACCOUNTTITLES, DSL.name("sub_accounttitles_ibfk_1"), new TableField[] { SubAccounttitles.SUB_ACCOUNTTITLES.ACCOUNTTITLE_ID }, Keys.KEY_ACCOUNTTITLES_PRIMARY, new TableField[] { Accounttitles.ACCOUNTTITLES.ACCOUNTTITLE_ID }, true);
    public static final ForeignKey<SubAccounttitlesRecord, UsersRecord> SUB_ACCOUNTTITLES_IBFK_2 = Internal.createForeignKey(SubAccounttitles.SUB_ACCOUNTTITLES, DSL.name("sub_accounttitles_ibfk_2"), new TableField[] { SubAccounttitles.SUB_ACCOUNTTITLES.USER_ID }, Keys.KEY_USERS_PRIMARY, new TableField[] { Users.USERS.USER_ID }, true);
}
