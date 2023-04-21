/*
 * This file is generated by jOOQ.
 */
package generated.tables.records;


import generated.tables.SubAccounttitles;

import org.jooq.Field;
import org.jooq.Record3;
import org.jooq.Record4;
import org.jooq.Row4;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class SubAccounttitlesRecord extends UpdatableRecordImpl<SubAccounttitlesRecord> implements Record4<String, String, String, String> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for
     * <code>accountbook_test.sub_accounttitles.sub_accounttitle_id</code>.
     */
    public void setSubAccounttitleId(String value) {
        set(0, value);
    }

    /**
     * Getter for
     * <code>accountbook_test.sub_accounttitles.sub_accounttitle_id</code>.
     */
    public String getSubAccounttitleId() {
        return (String) get(0);
    }

    /**
     * Setter for
     * <code>accountbook_test.sub_accounttitles.accounttitle_id</code>.
     */
    public void setAccounttitleId(String value) {
        set(1, value);
    }

    /**
     * Getter for
     * <code>accountbook_test.sub_accounttitles.accounttitle_id</code>.
     */
    public String getAccounttitleId() {
        return (String) get(1);
    }

    /**
     * Setter for <code>accountbook_test.sub_accounttitles.user_id</code>.
     */
    public void setUserId(String value) {
        set(2, value);
    }

    /**
     * Getter for <code>accountbook_test.sub_accounttitles.user_id</code>.
     */
    public String getUserId() {
        return (String) get(2);
    }

    /**
     * Setter for
     * <code>accountbook_test.sub_accounttitles.sub_accounttitle_name</code>.
     */
    public void setSubAccounttitleName(String value) {
        set(3, value);
    }

    /**
     * Getter for
     * <code>accountbook_test.sub_accounttitles.sub_accounttitle_name</code>.
     */
    public String getSubAccounttitleName() {
        return (String) get(3);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record3<String, String, String> key() {
        return (Record3) super.key();
    }

    // -------------------------------------------------------------------------
    // Record4 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row4<String, String, String, String> fieldsRow() {
        return (Row4) super.fieldsRow();
    }

    @Override
    public Row4<String, String, String, String> valuesRow() {
        return (Row4) super.valuesRow();
    }

    @Override
    public Field<String> field1() {
        return SubAccounttitles.SUB_ACCOUNTTITLES.SUB_ACCOUNTTITLE_ID;
    }

    @Override
    public Field<String> field2() {
        return SubAccounttitles.SUB_ACCOUNTTITLES.ACCOUNTTITLE_ID;
    }

    @Override
    public Field<String> field3() {
        return SubAccounttitles.SUB_ACCOUNTTITLES.USER_ID;
    }

    @Override
    public Field<String> field4() {
        return SubAccounttitles.SUB_ACCOUNTTITLES.SUB_ACCOUNTTITLE_NAME;
    }

    @Override
    public String component1() {
        return getSubAccounttitleId();
    }

    @Override
    public String component2() {
        return getAccounttitleId();
    }

    @Override
    public String component3() {
        return getUserId();
    }

    @Override
    public String component4() {
        return getSubAccounttitleName();
    }

    @Override
    public String value1() {
        return getSubAccounttitleId();
    }

    @Override
    public String value2() {
        return getAccounttitleId();
    }

    @Override
    public String value3() {
        return getUserId();
    }

    @Override
    public String value4() {
        return getSubAccounttitleName();
    }

    @Override
    public SubAccounttitlesRecord value1(String value) {
        setSubAccounttitleId(value);
        return this;
    }

    @Override
    public SubAccounttitlesRecord value2(String value) {
        setAccounttitleId(value);
        return this;
    }

    @Override
    public SubAccounttitlesRecord value3(String value) {
        setUserId(value);
        return this;
    }

    @Override
    public SubAccounttitlesRecord value4(String value) {
        setSubAccounttitleName(value);
        return this;
    }

    @Override
    public SubAccounttitlesRecord values(String value1, String value2, String value3, String value4) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached SubAccounttitlesRecord
     */
    public SubAccounttitlesRecord() {
        super(SubAccounttitles.SUB_ACCOUNTTITLES);
    }

    /**
     * Create a detached, initialised SubAccounttitlesRecord
     */
    public SubAccounttitlesRecord(String subAccounttitleId, String accounttitleId, String userId, String subAccounttitleName) {
        super(SubAccounttitles.SUB_ACCOUNTTITLES);

        setSubAccounttitleId(subAccounttitleId);
        setAccounttitleId(accounttitleId);
        setUserId(userId);
        setSubAccounttitleName(subAccounttitleName);
    }
}
