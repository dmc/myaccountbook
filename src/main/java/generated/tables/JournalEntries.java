/*
 * This file is generated by jOOQ.
 */
package generated.tables;


import generated.Accountbook;
import generated.Indexes;
import generated.Keys;
import generated.tables.records.JournalEntriesRecord;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Function6;
import org.jooq.Index;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Records;
import org.jooq.Row6;
import org.jooq.Schema;
import org.jooq.SelectField;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class JournalEntries extends TableImpl<JournalEntriesRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>accountbook.journal_entries</code>
     */
    public static final JournalEntries JOURNAL_ENTRIES = new JournalEntries();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<JournalEntriesRecord> getRecordType() {
        return JournalEntriesRecord.class;
    }

    /**
     * The column <code>accountbook.journal_entries.entry_id</code>.
     */
    public final TableField<JournalEntriesRecord, String> ENTRY_ID = createField(DSL.name("entry_id"), SQLDataType.VARCHAR(36).nullable(false), this, "");

    /**
     * The column <code>accountbook.journal_entries.deal_date</code>.
     */
    public final TableField<JournalEntriesRecord, LocalDate> DEAL_DATE = createField(DSL.name("deal_date"), SQLDataType.LOCALDATE.nullable(false), this, "");

    /**
     * The column <code>accountbook.journal_entries.entry_description</code>.
     */
    public final TableField<JournalEntriesRecord, String> ENTRY_DESCRIPTION = createField(DSL.name("entry_description"), SQLDataType.VARCHAR(64).nullable(false), this, "");

    /**
     * The column <code>accountbook.journal_entries.fiscal_yearmonth</code>.
     */
    public final TableField<JournalEntriesRecord, String> FISCAL_YEARMONTH = createField(DSL.name("fiscal_yearmonth"), SQLDataType.VARCHAR(7).nullable(false), this, "");

    /**
     * The column <code>accountbook.journal_entries.total_amount</code>.
     */
    public final TableField<JournalEntriesRecord, Integer> TOTAL_AMOUNT = createField(DSL.name("total_amount"), SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>accountbook.journal_entries.user_id</code>.
     */
    public final TableField<JournalEntriesRecord, String> USER_ID = createField(DSL.name("user_id"), SQLDataType.VARCHAR(32).nullable(false), this, "");

    private JournalEntries(Name alias, Table<JournalEntriesRecord> aliased) {
        this(alias, aliased, null);
    }

    private JournalEntries(Name alias, Table<JournalEntriesRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>accountbook.journal_entries</code> table
     * reference
     */
    public JournalEntries(String alias) {
        this(DSL.name(alias), JOURNAL_ENTRIES);
    }

    /**
     * Create an aliased <code>accountbook.journal_entries</code> table
     * reference
     */
    public JournalEntries(Name alias) {
        this(alias, JOURNAL_ENTRIES);
    }

    /**
     * Create a <code>accountbook.journal_entries</code> table reference
     */
    public JournalEntries() {
        this(DSL.name("journal_entries"), null);
    }

    public <O extends Record> JournalEntries(Table<O> child, ForeignKey<O, JournalEntriesRecord> key) {
        super(child, key, JOURNAL_ENTRIES);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : Accountbook.ACCOUNTBOOK;
    }

    @Override
    public List<Index> getIndexes() {
        return Arrays.asList(Indexes.JOURNAL_ENTRIES_USER_ID);
    }

    @Override
    public UniqueKey<JournalEntriesRecord> getPrimaryKey() {
        return Keys.KEY_JOURNAL_ENTRIES_PRIMARY;
    }

    @Override
    public List<ForeignKey<JournalEntriesRecord, ?>> getReferences() {
        return Arrays.asList(Keys.JOURNAL_ENTRIES_IBFK_1);
    }

    private transient Users _users;

    /**
     * Get the implicit join path to the <code>accountbook.users</code> table.
     */
    public Users users() {
        if (_users == null)
            _users = new Users(this, Keys.JOURNAL_ENTRIES_IBFK_1);

        return _users;
    }

    @Override
    public JournalEntries as(String alias) {
        return new JournalEntries(DSL.name(alias), this);
    }

    @Override
    public JournalEntries as(Name alias) {
        return new JournalEntries(alias, this);
    }

    @Override
    public JournalEntries as(Table<?> alias) {
        return new JournalEntries(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    public JournalEntries rename(String name) {
        return new JournalEntries(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public JournalEntries rename(Name name) {
        return new JournalEntries(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    public JournalEntries rename(Table<?> name) {
        return new JournalEntries(name.getQualifiedName(), null);
    }

    // -------------------------------------------------------------------------
    // Row6 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row6<String, LocalDate, String, String, Integer, String> fieldsRow() {
        return (Row6) super.fieldsRow();
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Function)}.
     */
    public <U> SelectField<U> mapping(Function6<? super String, ? super LocalDate, ? super String, ? super String, ? super Integer, ? super String, ? extends U> from) {
        return convertFrom(Records.mapping(from));
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Class,
     * Function)}.
     */
    public <U> SelectField<U> mapping(Class<U> toType, Function6<? super String, ? super LocalDate, ? super String, ? super String, ? super Integer, ? super String, ? extends U> from) {
        return convertFrom(toType, Records.mapping(from));
    }
}
