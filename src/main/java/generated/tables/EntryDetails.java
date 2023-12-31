/*
 * This file is generated by jOOQ.
 */
package generated.tables;


import generated.Accountbook;
import generated.Indexes;
import generated.Keys;
import generated.tables.records.EntryDetailsRecord;

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
public class EntryDetails extends TableImpl<EntryDetailsRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>accountbook.entry_details</code>
     */
    public static final EntryDetails ENTRY_DETAILS = new EntryDetails();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<EntryDetailsRecord> getRecordType() {
        return EntryDetailsRecord.class;
    }

    /**
     * The column <code>accountbook.entry_details.entry_id</code>.
     */
    public final TableField<EntryDetailsRecord, String> ENTRY_ID = createField(DSL.name("entry_id"), SQLDataType.VARCHAR(36), this, "");

    /**
     * The column <code>accountbook.entry_details.accounttitle_id</code>.
     */
    public final TableField<EntryDetailsRecord, String> ACCOUNTTITLE_ID = createField(DSL.name("accounttitle_id"), SQLDataType.VARCHAR(3).nullable(false), this, "");

    /**
     * The column <code>accountbook.entry_details.sub_accounttitle_id</code>.
     */
    public final TableField<EntryDetailsRecord, String> SUB_ACCOUNTTITLE_ID = createField(DSL.name("sub_accounttitle_id"), SQLDataType.VARCHAR(1).nullable(false), this, "");

    /**
     * The column <code>accountbook.entry_details.loan_type</code>.
     */
    public final TableField<EntryDetailsRecord, String> LOAN_TYPE = createField(DSL.name("loan_type"), SQLDataType.VARCHAR(8).nullable(false), this, "");

    /**
     * The column <code>accountbook.entry_details.amount</code>.
     */
    public final TableField<EntryDetailsRecord, Integer> AMOUNT = createField(DSL.name("amount"), SQLDataType.INTEGER.nullable(false), this, "");

    private EntryDetails(Name alias, Table<EntryDetailsRecord> aliased) {
        this(alias, aliased, null);
    }

    private EntryDetails(Name alias, Table<EntryDetailsRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>accountbook.entry_details</code> table reference
     */
    public EntryDetails(String alias) {
        this(DSL.name(alias), ENTRY_DETAILS);
    }

    /**
     * Create an aliased <code>accountbook.entry_details</code> table reference
     */
    public EntryDetails(Name alias) {
        this(alias, ENTRY_DETAILS);
    }

    /**
     * Create a <code>accountbook.entry_details</code> table reference
     */
    public EntryDetails() {
        this(DSL.name("entry_details"), null);
    }

    public <O extends Record> EntryDetails(Table<O> child, ForeignKey<O, EntryDetailsRecord> key) {
        super(child, key, ENTRY_DETAILS);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : Accountbook.ACCOUNTBOOK;
    }

    @Override
    public List<Index> getIndexes() {
        return Arrays.asList(Indexes.ENTRY_DETAILS_ACCOUNTTITLE_ID, Indexes.ENTRY_DETAILS_ENTRY_ID);
    }

    @Override
    public List<ForeignKey<EntryDetailsRecord, ?>> getReferences() {
        return Arrays.asList(Keys.ENTRY_DETAILS_IBFK_1, Keys.ENTRY_DETAILS_IBFK_2);
    }

    private transient JournalEntries _journalEntries;
    private transient Accounttitles _accounttitles;

    /**
     * Get the implicit join path to the
     * <code>accountbook.journal_entries</code> table.
     */
    public JournalEntries journalEntries() {
        if (_journalEntries == null)
            _journalEntries = new JournalEntries(this, Keys.ENTRY_DETAILS_IBFK_1);

        return _journalEntries;
    }

    /**
     * Get the implicit join path to the <code>accountbook.accounttitles</code>
     * table.
     */
    public Accounttitles accounttitles() {
        if (_accounttitles == null)
            _accounttitles = new Accounttitles(this, Keys.ENTRY_DETAILS_IBFK_2);

        return _accounttitles;
    }

    @Override
    public EntryDetails as(String alias) {
        return new EntryDetails(DSL.name(alias), this);
    }

    @Override
    public EntryDetails as(Name alias) {
        return new EntryDetails(alias, this);
    }

    @Override
    public EntryDetails as(Table<?> alias) {
        return new EntryDetails(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    public EntryDetails rename(String name) {
        return new EntryDetails(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public EntryDetails rename(Name name) {
        return new EntryDetails(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    public EntryDetails rename(Table<?> name) {
        return new EntryDetails(name.getQualifiedName(), null);
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
