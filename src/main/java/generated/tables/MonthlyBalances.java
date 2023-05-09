/*
 * This file is generated by jOOQ.
 */
package generated.tables;


import generated.AccountbookTest;
import generated.Indexes;
import generated.Keys;
import generated.tables.records.MonthlyBalancesRecord;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Function5;
import org.jooq.Index;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Records;
import org.jooq.Row5;
import org.jooq.Schema;
import org.jooq.SelectField;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class MonthlyBalances extends TableImpl<MonthlyBalancesRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>accountbook_test.monthly_balances</code>
     */
    public static final MonthlyBalances MONTHLY_BALANCES = new MonthlyBalances();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<MonthlyBalancesRecord> getRecordType() {
        return MonthlyBalancesRecord.class;
    }

    /**
     * The column
     * <code>accountbook_test.monthly_balances.accounttitle_id</code>.
     */
    public final TableField<MonthlyBalancesRecord, String> ACCOUNTTITLE_ID = createField(DSL.name("accounttitle_id"), SQLDataType.VARCHAR(3), this, "");

    /**
     * The column
     * <code>accountbook_test.monthly_balances.sub_accounttitle_id</code>.
     */
    public final TableField<MonthlyBalancesRecord, String> SUB_ACCOUNTTITLE_ID = createField(DSL.name("sub_accounttitle_id"), SQLDataType.VARCHAR(1), this, "");

    /**
     * The column
     * <code>accountbook_test.monthly_balances.fiscal_yearmonth</code>.
     */
    public final TableField<MonthlyBalancesRecord, String> FISCAL_YEARMONTH = createField(DSL.name("fiscal_yearmonth"), SQLDataType.VARCHAR(6), this, "");

    /**
     * The column <code>accountbook_test.monthly_balances.user_id</code>.
     */
    public final TableField<MonthlyBalancesRecord, String> USER_ID = createField(DSL.name("user_id"), SQLDataType.VARCHAR(32), this, "");

    /**
     * The column <code>accountbook_test.monthly_balances.balance</code>.
     */
    public final TableField<MonthlyBalancesRecord, Integer> BALANCE = createField(DSL.name("balance"), SQLDataType.INTEGER, this, "");

    private MonthlyBalances(Name alias, Table<MonthlyBalancesRecord> aliased) {
        this(alias, aliased, null);
    }

    private MonthlyBalances(Name alias, Table<MonthlyBalancesRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>accountbook_test.monthly_balances</code> table
     * reference
     */
    public MonthlyBalances(String alias) {
        this(DSL.name(alias), MONTHLY_BALANCES);
    }

    /**
     * Create an aliased <code>accountbook_test.monthly_balances</code> table
     * reference
     */
    public MonthlyBalances(Name alias) {
        this(alias, MONTHLY_BALANCES);
    }

    /**
     * Create a <code>accountbook_test.monthly_balances</code> table reference
     */
    public MonthlyBalances() {
        this(DSL.name("monthly_balances"), null);
    }

    public <O extends Record> MonthlyBalances(Table<O> child, ForeignKey<O, MonthlyBalancesRecord> key) {
        super(child, key, MONTHLY_BALANCES);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : AccountbookTest.ACCOUNTBOOK_TEST;
    }

    @Override
    public List<Index> getIndexes() {
        return Arrays.asList(Indexes.MONTHLY_BALANCES_ACCOUNTTITLE_ID, Indexes.MONTHLY_BALANCES_USER_ID);
    }

    @Override
    public List<ForeignKey<MonthlyBalancesRecord, ?>> getReferences() {
        return Arrays.asList(Keys.MONTHLY_BALANCES_IBFK_1, Keys.MONTHLY_BALANCES_IBFK_2);
    }

    private transient Accounttitles _accounttitles;
    private transient Users _users;

    /**
     * Get the implicit join path to the
     * <code>accountbook_test.accounttitles</code> table.
     */
    public Accounttitles accounttitles() {
        if (_accounttitles == null)
            _accounttitles = new Accounttitles(this, Keys.MONTHLY_BALANCES_IBFK_1);

        return _accounttitles;
    }

    /**
     * Get the implicit join path to the <code>accountbook_test.users</code>
     * table.
     */
    public Users users() {
        if (_users == null)
            _users = new Users(this, Keys.MONTHLY_BALANCES_IBFK_2);

        return _users;
    }

    @Override
    public MonthlyBalances as(String alias) {
        return new MonthlyBalances(DSL.name(alias), this);
    }

    @Override
    public MonthlyBalances as(Name alias) {
        return new MonthlyBalances(alias, this);
    }

    @Override
    public MonthlyBalances as(Table<?> alias) {
        return new MonthlyBalances(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    public MonthlyBalances rename(String name) {
        return new MonthlyBalances(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public MonthlyBalances rename(Name name) {
        return new MonthlyBalances(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    public MonthlyBalances rename(Table<?> name) {
        return new MonthlyBalances(name.getQualifiedName(), null);
    }

    // -------------------------------------------------------------------------
    // Row5 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row5<String, String, String, String, Integer> fieldsRow() {
        return (Row5) super.fieldsRow();
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Function)}.
     */
    public <U> SelectField<U> mapping(Function5<? super String, ? super String, ? super String, ? super String, ? super Integer, ? extends U> from) {
        return convertFrom(Records.mapping(from));
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Class,
     * Function)}.
     */
    public <U> SelectField<U> mapping(Class<U> toType, Function5<? super String, ? super String, ? super String, ? super String, ? super Integer, ? extends U> from) {
        return convertFrom(toType, Records.mapping(from));
    }
}
