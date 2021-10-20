package com.msorrell.project.enums;

import java.math.BigDecimal;

/**
 * The type of tool.
 * Contains the Tool's Daily Charge, Weekend Charge, Weekday Charge, and Holiday Charge
 */
public enum ToolType {

    /**
     * Ladder tool type.
     */
    LADDER(BigDecimal.valueOf(1.99), true, true, false),

    /**
     * Chainsaw tool type.
     */
    CHAINSAW(BigDecimal.valueOf(1.49), true, false, true),

    /**
     * Jack Hammer tool type.
     */
    JACKHAMMER(BigDecimal.valueOf(2.99), true, false, false);

    /**
     * Constructor.
     * @param dailyCharge How much is the daily charge
     * @param weekdayCharge Does the tool charge a weekday fee
     * @param weekendCharge Does the tool charge a weekend fee
     * @param holidayCharge Does the tool charge a holiday fee
     */
    ToolType(final BigDecimal dailyCharge, final boolean weekdayCharge, final boolean weekendCharge,
             final boolean holidayCharge) {
        this.dailyCharge = dailyCharge;
        this.weekdayCharge = weekdayCharge;
        this.weekendCharge = weekendCharge;
        this.holidayCharge = holidayCharge;
    }

    /**
     * Converts the enum to proper capitalization for output.
     * Ex. JACKHAMMER -> Jackhammer
     * @return Tool Type with proper capitalization
     */
    @Override
    public String toString() {
        return this.name().charAt(0) + this.name().substring(1).toLowerCase();
    }

    /**
     * How much is the daily charge to rent the tool.
     */
    private final BigDecimal dailyCharge;

    /**
     * Does the tool charge a weekday fee.
     */
    private final boolean weekdayCharge;

    /**
     * Does the tool charge a weekend fee.
     */
    private final boolean weekendCharge;

    /**
     * Does the tool charge a holiday fee.
     */
    private final boolean holidayCharge;

    public BigDecimal getDailyCharge() {
        return dailyCharge;
    }

    public boolean isWeekdayCharge() {
        return weekdayCharge;
    }

    public boolean isWeekendCharge() {
        return weekendCharge;
    }

    public boolean isHolidayCharge() {
        return holidayCharge;
    }
}
