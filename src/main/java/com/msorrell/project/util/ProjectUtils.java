package com.msorrell.project.util;

import com.msorrell.project.enums.ToolBrand;
import com.msorrell.project.enums.ToolType;
import com.msorrell.project.models.Tool;

import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Utility class for common fields and methods.
 */
public final class ProjectUtils {

    /**
     * Regex pattern for validating dates.
     */
    public static final Pattern DATE_PATTERN =
            Pattern.compile("(0[1-9]|1[012])/(0[1-9]|[12][0-9]|3[01])/([0-9]{2})");

    /**
     * Regex for validating the Tool's code.
     */
    public static final Pattern TOOL_CODE_PATTERN = Pattern.compile("[a-zA-Z]{4}");

    /**
     * DateTimeFormatter for LocalDate.
     */
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MM/dd/yy");

    /**
     * Regex for validate the Checkout again question.
     */
    public static final Pattern CHECKOUT_AGAIN_PATTERN = Pattern.compile("[yYnN]");

    /**
     * Validation message for invalid tool code.
     */
    public static final String INVALID_TOOL_CODE = "Invalid tool code";

    /**
     * Validation message for invalid rental days.
     */
    public static final String INVALID_RENTAL_DAY_MESSAGE = "Rental Day Count must be greater than 0";

    /**
     * Validation message for invalid discount percent.
     */
    public static final String INVALID_DISCOUNT_PERCENT_MESSAGE = "Discount Percent must be between 0 and 100";

    /**
     * Validation message for invalid checkout date.
     */
    public static final String INVALID_CHECKOUT_DATE_MESSAGE = "Checkout Date must not be null";

    public static Set<Tool> initializeTools() {
        final Set<Tool> toolSet = new HashSet<>();
        toolSet.add(new Tool(ToolType.LADDER, ToolBrand.WERNER, "LADW"));
        toolSet.add(new Tool(ToolType.CHAINSAW, ToolBrand.STIHL, "CHNS"));
        toolSet.add(new Tool(ToolType.JACKHAMMER, ToolBrand.RIDGID, "JAKR"));
        toolSet.add(new Tool(ToolType.JACKHAMMER, ToolBrand.DEWALT, "JAKD"));
        return toolSet;
    }
}
