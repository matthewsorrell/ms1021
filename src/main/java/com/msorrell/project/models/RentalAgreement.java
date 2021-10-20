package com.msorrell.project.models;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.Locale;

import static com.msorrell.project.util.ProjectUtils.DATE_FORMATTER;

/**
 * Rental Agreement.
 * Generated once checkout has completed. Contains checkout data plus charges.
 */
public class RentalAgreement {

    /**
     * The Tool that is checked out.
     */
    private Tool tool;

    /**
     * The number of rental days.
     */
    private int rentalDays;

    /**
     * The checkout date.
     */
    private LocalDate checkoutDate;

    /**
     * The day the tool is due back.
     */
    private LocalDate dueDate;

    /**
     * The daily rental charge. Determined by the tool type.
     */
    private BigDecimal dailyRentalCharge;

    /**
     * The number of days to be charged. Determined by the tool type.
     */
    private int chargeDays;

    /**
     * The amount charged before the discount is applied.
     * Calculated by dailyRentalCharge X chargeDays
     */
    private BigDecimal preDiscountCharge;

    /**
     * The percent to discount.
     * Represented by a whole number 0-100
     */
    private int discountPercent;

    /**
     * The amount to be discounted.
     * Calculated by preDiscountCharge X (discountPercent X .01)
     */
    private BigDecimal discountAmount;

    /**
     * The final amount after the discount has been applied.
     * Calculated by preDiscountCharge - discountAmount.
     */
    private BigDecimal finalCharge;

    /**
     * Prints the rental agreement to the console.
     */
    public void printToConsole() {
        final NumberFormat dayFormatter = NumberFormat.getInstance();
        dayFormatter.setGroupingUsed(true);
        System.out.println("Tool code: " + getTool().getCode());
        System.out.println("Tool type: " + getTool().getType());
        System.out.println("Tool brand: " + getTool().getBrand());
        System.out.println("Rental days: " + dayFormatter.format(getRentalDays()));
        System.out.println("Checkout date: " +  getCheckoutDate().format(DATE_FORMATTER));
        System.out.println("Due date: " + getDueDate().format(DATE_FORMATTER));
        System.out.println("Daily rental charge: " + NumberFormat.getCurrencyInstance(Locale.US)
                .format(getDailyRentalCharge()));
        System.out.println("Charge days: " + dayFormatter.format(getChargeDays()));
        System.out.println("Pre-discount charge: " + NumberFormat.getCurrencyInstance(Locale.US)
                .format(getPreDiscountCharge()));
        System.out.printf("Discount percent: %d%%%n", getDiscountPercent());
        System.out.println("Discount amount: " + NumberFormat.getCurrencyInstance(Locale.US)
                .format(getDiscountAmount()));
        System.out.println("Final charge: " + NumberFormat.getCurrencyInstance(Locale.US).format(getFinalCharge()));
    }

    public Tool getTool() {
        return tool;
    }

    public void setTool(final Tool tool) {
        this.tool = tool;
    }

    public int getRentalDays() {
        return rentalDays;
    }

    public void setRentalDays(final int rentalDays) {
        this.rentalDays = rentalDays;
    }

    public LocalDate getCheckoutDate() {
        return checkoutDate;
    }

    public void setCheckoutDate(final LocalDate checkoutDate) {
        this.checkoutDate = checkoutDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(final LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public BigDecimal getDailyRentalCharge() {
        return dailyRentalCharge;
    }

    public void setDailyRentalCharge(final BigDecimal dailyRentalCharge) {
        this.dailyRentalCharge = dailyRentalCharge;
    }

    public int getChargeDays() {
        return chargeDays;
    }

    public void setChargeDays(final int chargeDays) {
        this.chargeDays = chargeDays;
    }

    public BigDecimal getPreDiscountCharge() {
        return preDiscountCharge;
    }

    public void setPreDiscountCharge(final BigDecimal preDiscountCharge) {
        this.preDiscountCharge = preDiscountCharge;
    }

    public int getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(final int discountPercent) {
        this.discountPercent = discountPercent;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(final BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public BigDecimal getFinalCharge() {
        return finalCharge;
    }

    public void setFinalCharge(final BigDecimal finalCharge) {
        this.finalCharge = finalCharge;
    }
}
