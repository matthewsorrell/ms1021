package com.msorrell.project.models;

import com.msorrell.project.exceptions.InvalidInputException;

import java.time.LocalDate;

import static com.msorrell.project.util.ProjectUtils.INVALID_CHECKOUT_DATE_MESSAGE;
import static com.msorrell.project.util.ProjectUtils.INVALID_DISCOUNT_PERCENT_MESSAGE;
import static com.msorrell.project.util.ProjectUtils.INVALID_RENTAL_DAY_MESSAGE;
import static com.msorrell.project.util.ProjectUtils.INVALID_TOOL_CODE;
import static com.msorrell.project.validators.InputValidator.validateCheckoutDate;
import static com.msorrell.project.validators.InputValidator.validateDiscountPercent;
import static com.msorrell.project.validators.InputValidator.validateRentalDays;
import static com.msorrell.project.validators.InputValidator.validateTool;

/**
 * Checkout class.
 * Contains the tool, the rental day count, discount percent, and checkout date.
 * Generated from user input.
 */
public class Checkout {

    /**
     * Tool.
     */
    private Tool tool;

    /**
     * Number of days the tool will be rented.
     */
    private int rentalDayCount;

    /**
     * The amount of discount applied.
     * Valid Values: 0 -100
     */
    private int discountPercent;

    /**
     * The date the tool will be checked out.
     */
    private LocalDate checkoutDate;

    public Tool getTool() {
        return tool;
    }

    public void setTool(final Tool tool) {
        if (!validateTool(tool)) {
            throw new InvalidInputException(INVALID_TOOL_CODE);
        }
        this.tool = tool;
    }

    public int getRentalDayCount() {
        return rentalDayCount;
    }

    public void setRentalDayCount(final int rentalDayCount) {
        if (!validateRentalDays(rentalDayCount)) {
            throw new InvalidInputException(INVALID_RENTAL_DAY_MESSAGE);
        }
        this.rentalDayCount = rentalDayCount;
    }

    public int getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(final int discountPercent) {
        if (!validateDiscountPercent(discountPercent)) {
            throw new InvalidInputException(INVALID_DISCOUNT_PERCENT_MESSAGE);
        }
        this.discountPercent = discountPercent;
    }

    public LocalDate getCheckoutDate() {
        return checkoutDate;
    }

    public void setCheckoutDate(final LocalDate checkoutDate) {
        if (!validateCheckoutDate(checkoutDate)) {
            throw new InvalidInputException(INVALID_CHECKOUT_DATE_MESSAGE);
        }
        this.checkoutDate = checkoutDate;
    }
}
