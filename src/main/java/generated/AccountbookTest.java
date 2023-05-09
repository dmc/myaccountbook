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

import java.util.Arrays;
import java.util.List;

import org.jooq.Catalog;
import org.jooq.Table;
import org.jooq.impl.SchemaImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class AccountbookTest extends SchemaImpl {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>accountbook_test</code>
     */
    public static final AccountbookTest ACCOUNTBOOK_TEST = new AccountbookTest();

    /**
     * The table <code>accountbook_test.accounttitles</code>.
     */
    public final Accounttitles ACCOUNTTITLES = Accounttitles.ACCOUNTTITLES;

    /**
     * The table <code>accountbook_test.entry_details</code>.
     */
    public final EntryDetails ENTRY_DETAILS = EntryDetails.ENTRY_DETAILS;

    /**
     * The table <code>accountbook_test.journal_entries</code>.
     */
    public final JournalEntries JOURNAL_ENTRIES = JournalEntries.JOURNAL_ENTRIES;

    /**
     * The table <code>accountbook_test.monthly_balances</code>.
     */
    public final MonthlyBalances MONTHLY_BALANCES = MonthlyBalances.MONTHLY_BALANCES;

    /**
     * The table <code>accountbook_test.sub_accounttitles</code>.
     */
    public final SubAccounttitles SUB_ACCOUNTTITLES = SubAccounttitles.SUB_ACCOUNTTITLES;

    /**
     * The table <code>accountbook_test.users</code>.
     */
    public final Users USERS = Users.USERS;

    /**
     * No further instances allowed
     */
    private AccountbookTest() {
        super("accountbook_test", null);
    }


    @Override
    public Catalog getCatalog() {
        return DefaultCatalog.DEFAULT_CATALOG;
    }

    @Override
    public final List<Table<?>> getTables() {
        return Arrays.asList(
            Accounttitles.ACCOUNTTITLES,
            EntryDetails.ENTRY_DETAILS,
            JournalEntries.JOURNAL_ENTRIES,
            MonthlyBalances.MONTHLY_BALANCES,
            SubAccounttitles.SUB_ACCOUNTTITLES,
            Users.USERS
        );
    }
}
