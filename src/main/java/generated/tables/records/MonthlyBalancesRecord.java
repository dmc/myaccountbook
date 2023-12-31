/*
 * This file is generated by jOOQ.
 */
package generated.tables.records;


import generated.tables.MonthlyBalances;

import org.jooq.Field;
import org.jooq.Record4;
import org.jooq.Record5;
import org.jooq.Row5;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class MonthlyBalancesRecord extends UpdatableRecordImpl<MonthlyBalancesRecord> implements Record5<String, String, String, String, Integer> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>accountbook.monthly_balances.accounttitle_id</code>.
     */
    public void setAccounttitleId(String value) {
        set(0, value);
    }

    /**
     * Getter for <code>accountbook.monthly_balances.accounttitle_id</code>.
     */
    public String getAccounttitleId() {
        return (String) get(0);
    }

    /**
     * Setter for <code>accountbook.monthly_balances.sub_accounttitle_id</code>.
     */
    public void setSubAccounttitleId(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>accountbook.monthly_balances.sub_accounttitle_id</code>.
     */
    public String getSubAccounttitleId() {
        return (String) get(1);
    }

    /**
     * Setter for <code>accountbook.monthly_balances.fiscal_yearmonth</code>.
     */
    public void setFiscalYearmonth(String value) {
        set(2, value);
    }

    /**
     * Getter for <code>accountbook.monthly_balances.fiscal_yearmonth</code>.
     */
    public String getFiscalYearmonth() {
        return (String) get(2);
    }

    /**
     * Setter for <code>accountbook.monthly_balances.user_id</code>.
     */
    public void setUserId(String value) {
        set(3, value);
    }

    /**
     * Getter for <code>accountbook.monthly_balances.user_id</code>.
     */
    public String getUserId() {
        return (String) get(3);
    }

    /**
     * Setter for <code>accountbook.monthly_balances.balance</code>.
     */
    public void setBalance(Integer value) {
        set(4, value);
    }

    /**
     * Getter for <code>accountbook.monthly_balances.balance</code>.
     */
    public Integer getBalance() {
        return (Integer) get(4);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record4<String, String, String, String> key() {
        return (Record4) super.key();
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
        return MonthlyBalances.MONTHLY_BALANCES.ACCOUNTTITLE_ID;
    }

    @Override
    public Field<String> field2() {
        return MonthlyBalances.MONTHLY_BALANCES.SUB_ACCOUNTTITLE_ID;
    }

    @Override
    public Field<String> field3() {
        return MonthlyBalances.MONTHLY_BALANCES.FISCAL_YEARMONTH;
    }

    @Override
    public Field<String> field4() {
        return MonthlyBalances.MONTHLY_BALANCES.USER_ID;
    }

    @Override
    public Field<Integer> field5() {
        return MonthlyBalances.MONTHLY_BALANCES.BALANCE;
    }

    @Override
    public String component1() {
        return getAccounttitleId();
    }

    @Override
    public String component2() {
        return getSubAccounttitleId();
    }

    @Override
    public String component3() {
        return getFiscalYearmonth();
    }

    @Override
    public String component4() {
        return getUserId();
    }

    @Override
    public Integer component5() {
        return getBalance();
    }

    @Override
    public String value1() {
        return getAccounttitleId();
    }

    @Override
    public String value2() {
        return getSubAccounttitleId();
    }

    @Override
    public String value3() {
        return getFiscalYearmonth();
    }

    @Override
    public String value4() {
        return getUserId();
    }

    @Override
    public Integer value5() {
        return getBalance();
    }

    @Override
    public MonthlyBalancesRecord value1(String value) {
        setAccounttitleId(value);
        return this;
    }

    @Override
    public MonthlyBalancesRecord value2(String value) {
        setSubAccounttitleId(value);
        return this;
    }

    @Override
    public MonthlyBalancesRecord value3(String value) {
        setFiscalYearmonth(value);
        return this;
    }

    @Override
    public MonthlyBalancesRecord value4(String value) {
        setUserId(value);
        return this;
    }

    @Override
    public MonthlyBalancesRecord value5(Integer value) {
        setBalance(value);
        return this;
    }

    @Override
    public MonthlyBalancesRecord values(String value1, String value2, String value3, String value4, Integer value5) {
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
     * Create a detached MonthlyBalancesRecord
     */
    public MonthlyBalancesRecord() {
        super(MonthlyBalances.MONTHLY_BALANCES);
    }

    /**
     * Create a detached, initialised MonthlyBalancesRecord
     */
    public MonthlyBalancesRecord(String accounttitleId, String subAccounttitleId, String fiscalYearmonth, String userId, Integer balance) {
        super(MonthlyBalances.MONTHLY_BALANCES);

        setAccounttitleId(accounttitleId);
        setSubAccounttitleId(subAccounttitleId);
        setFiscalYearmonth(fiscalYearmonth);
        setUserId(userId);
        setBalance(balance);
    }
}
