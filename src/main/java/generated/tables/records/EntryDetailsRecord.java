/*
 * This file is generated by jOOQ.
 */
package generated.tables.records;


import generated.tables.EntryDetails;

import org.jooq.Field;
import org.jooq.Record5;
import org.jooq.Row5;
import org.jooq.impl.TableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class EntryDetailsRecord extends TableRecordImpl<EntryDetailsRecord> implements Record5<String, String, String, String, Integer> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>accountbook_test.entry_details.entry_id</code>.
     */
    public void setEntryId(String value) {
        set(0, value);
    }

    /**
     * Getter for <code>accountbook_test.entry_details.entry_id</code>.
     */
    public String getEntryId() {
        return (String) get(0);
    }

    /**
     * Setter for <code>accountbook_test.entry_details.accounttitle_id</code>.
     */
    public void setAccounttitleId(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>accountbook_test.entry_details.accounttitle_id</code>.
     */
    public String getAccounttitleId() {
        return (String) get(1);
    }

    /**
     * Setter for
     * <code>accountbook_test.entry_details.sub_accounttitle_id</code>.
     */
    public void setSubAccounttitleId(String value) {
        set(2, value);
    }

    /**
     * Getter for
     * <code>accountbook_test.entry_details.sub_accounttitle_id</code>.
     */
    public String getSubAccounttitleId() {
        return (String) get(2);
    }

    /**
     * Setter for <code>accountbook_test.entry_details.loan_type</code>.
     */
    public void setLoanType(String value) {
        set(3, value);
    }

    /**
     * Getter for <code>accountbook_test.entry_details.loan_type</code>.
     */
    public String getLoanType() {
        return (String) get(3);
    }

    /**
     * Setter for <code>accountbook_test.entry_details.amount</code>.
     */
    public void setAmount(Integer value) {
        set(4, value);
    }

    /**
     * Getter for <code>accountbook_test.entry_details.amount</code>.
     */
    public Integer getAmount() {
        return (Integer) get(4);
    }

    // -------------------------------------------------------------------------
    // Record5 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row5<String, String, String, String, Integer> fieldsRow() {
        return (Row5) super.fieldsRow();
    }

    @Override
    public Row5<String, String, String, String, Integer> valuesRow() {
        return (Row5) super.valuesRow();
    }

    @Override
    public Field<String> field1() {
        return EntryDetails.ENTRY_DETAILS.ENTRY_ID;
    }

    @Override
    public Field<String> field2() {
        return EntryDetails.ENTRY_DETAILS.ACCOUNTTITLE_ID;
    }

    @Override
    public Field<String> field3() {
        return EntryDetails.ENTRY_DETAILS.SUB_ACCOUNTTITLE_ID;
    }

    @Override
    public Field<String> field4() {
        return EntryDetails.ENTRY_DETAILS.LOAN_TYPE;
    }

    @Override
    public Field<Integer> field5() {
        return EntryDetails.ENTRY_DETAILS.AMOUNT;
    }

    @Override
    public String component1() {
        return getEntryId();
    }

    @Override
    public String component2() {
        return getAccounttitleId();
    }

    @Override
    public String component3() {
        return getSubAccounttitleId();
    }

    @Override
    public String component4() {
        return getLoanType();
    }

    @Override
    public Integer component5() {
        return getAmount();
    }

    @Override
    public String value1() {
        return getEntryId();
    }

    @Override
    public String value2() {
        return getAccounttitleId();
    }

    @Override
    public String value3() {
        return getSubAccounttitleId();
    }

    @Override
    public String value4() {
        return getLoanType();
    }

    @Override
    public Integer value5() {
        return getAmount();
    }

    @Override
    public EntryDetailsRecord value1(String value) {
        setEntryId(value);
        return this;
    }

    @Override
    public EntryDetailsRecord value2(String value) {
        setAccounttitleId(value);
        return this;
    }

    @Override
    public EntryDetailsRecord value3(String value) {
        setSubAccounttitleId(value);
        return this;
    }

    @Override
    public EntryDetailsRecord value4(String value) {
        setLoanType(value);
        return this;
    }

    @Override
    public EntryDetailsRecord value5(Integer value) {
        setAmount(value);
        return this;
    }

    @Override
    public EntryDetailsRecord values(String value1, String value2, String value3, String value4, Integer value5) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached EntryDetailsRecord
     */
    public EntryDetailsRecord() {
        super(EntryDetails.ENTRY_DETAILS);
    }

    /**
     * Create a detached, initialised EntryDetailsRecord
     */
    public EntryDetailsRecord(String entryId, String accounttitleId, String subAccounttitleId, String loanType, Integer amount) {
        super(EntryDetails.ENTRY_DETAILS);

        setEntryId(entryId);
        setAccounttitleId(accounttitleId);
        setSubAccounttitleId(subAccounttitleId);
        setLoanType(loanType);
        setAmount(amount);
    }
}
