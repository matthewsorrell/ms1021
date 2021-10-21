package com.msorrell.project.validators;

import com.msorrell.project.enums.ToolBrand;
import com.msorrell.project.enums.ToolType;
import com.msorrell.project.models.Tool;

import java.time.LocalDate;

import static com.msorrell.project.util.ProjectUtils.CHECKOUT_AGAIN_PATTERN;
import static com.msorrell.project.util.ProjectUtils.DATE_PATTERN;
import static com.msorrell.project.util.ProjectUtils.TOOL_CODE_PATTERN;
import static com.msorrell.project.util.ProjectUtils.initializeTools;

/**
 * Validates user input.
 */
public final class InputValidator {

    /**
     * Retrieves the Tool that is associated with the toolCodes passed.
     * Returns null if no tool is associated with the code
     * @param toolCode Tool's code
     * @return Tool object
     */
    public static Tool findTool(final String toolCode) {
        if (toolCode != null && toolCode.length() == 4) {
            return initializeTools().stream()
                    .filter(tool -> toolCode.trim().toUpperCase().equals(tool.getCode())).findFirst()
                    .orElse(null);
        }
        return null;
    }

    /**
     * Determines if the tool code is valid.
     * The code needs to be nonnull, not blank, 4 characters long, and one of the provided tool codes.
     * @param toolCode String representing the tool code
     * @return true - valid, false - not valid
     */
    public static boolean validateToolCode(final String toolCode) {
        return toolCode != null && !toolCode.isBlank() && TOOL_CODE_PATTERN.matcher(toolCode).matches()
                && findTool(toolCode) != null;
    }

    /**
     * Determines if the Tool is valid.
     * The tool needs to have a valid tool code, tool brand, and tool type.
     * @param tool Tool object
     * @return true - valid, false - not valid
     */
    public static boolean validateTool(final Tool tool) {
        return tool != null && validateToolCode(tool.getCode()) && validateToolBrand(tool.getBrand())
                && validateToolType(tool.getType());
    }

    /**
     * Determines if the number of rental days is valid.
     * The rental days needs to be a number > 1
     * @param rentalDays number of rental days
     * @return true - valid, false - not valid
     */
    public static boolean validateRentalDays(final String rentalDays) {
        final int days;
        try {
            days = Integer.parseInt(rentalDays);
        } catch (NumberFormatException e) {
            return false;
        }
        return days > 0;
    }

    /**
     * Determines if the number of rental days is valid.
     * The rental days needs to be a number > 1
     * @param rentalDays number of rental days
     * @return true - valid, false - not valid
     */
    public static boolean validateRentalDays(final int rentalDays) {
        return validateRentalDays(String.valueOf(rentalDays));
    }

    /**
     * Determines if the discount percent is valid.
     * The percent needs to be a number between 0 and 100.
     * @param discountPercent percent of discount
     * @return true - valid, false - not valid
     */
    public static boolean validateDiscountPercent(final String discountPercent) {
        final int discount;
        try {
            discount = Integer.parseInt(discountPercent);
        } catch (NumberFormatException e) {
            return false;
        }
        return discount >= 0 && discount <= 100;
    }

    /**
     * Determines if the discount percent is valid.
     * The percent needs to be a number between 0 and 100.
     * @param discountPercent percent of discount
     * @return true - valid, false - not valid
     */
    public static boolean validateDiscountPercent(final int discountPercent) {
        return validateDiscountPercent(String.valueOf(discountPercent));
    }

    /**
     * Determines if the checkout date is valid.
     * The date needs to be formatted MM/DD/YY.
     * @param checkoutDate date the tool is to be checked out
     * @return true - valid, false - not valid
     */
    public static boolean validateCheckoutDate(final String checkoutDate) {
        return DATE_PATTERN.matcher(checkoutDate).matches();
    }

    /**
     * Determines if the checkout date is valid.
     * The date needs to be nonnull
     * @param checkoutDate date the tool is to be checked out
     * @return true - valid, false - not valid
     */
    public static boolean validateCheckoutDate(final LocalDate checkoutDate) {
        return checkoutDate != null;
    }

    /**
     * Determines if the checkout again is valid.
     * The String needs to be either Y, y, N, or n.
     * @param again String representing the checkout again input
     * @return true - valid, false - not valid
     */
    public static boolean validateCheckoutAgain(final String again) {
        return CHECKOUT_AGAIN_PATTERN.matcher(again).matches();
    }

    /**
     * Determines if the tool type is valid.
     * The tool type needs to be nonnull
     * @param toolType ToolType object
     * @return true - valid, false - not valid
     */
    public static boolean validateToolType(final ToolType toolType) {
        return toolType != null;
    }

    public static boolean validateToolBrand(final ToolBrand toolBrand) {
        return toolBrand != null;
    }

}
