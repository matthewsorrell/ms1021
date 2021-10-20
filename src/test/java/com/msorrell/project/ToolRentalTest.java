package com.msorrell.project;

import com.msorrell.project.enums.ToolBrand;
import com.msorrell.project.enums.ToolType;
import com.msorrell.project.exceptions.InvalidInputException;
import com.msorrell.project.models.Checkout;
import com.msorrell.project.models.RentalAgreement;
import com.msorrell.project.models.Tool;
import com.msorrell.project.services.ToolRentalService;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

import static com.msorrell.project.util.ProjectUtils.INVALID_CHECKOUT_DATE_MESSAGE;
import static com.msorrell.project.util.ProjectUtils.INVALID_DISCOUNT_PERCENT_MESSAGE;
import static com.msorrell.project.util.ProjectUtils.INVALID_RENTAL_DAY_MESSAGE;
import static com.msorrell.project.util.ProjectUtils.INVALID_TOOL_CODE;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ToolRentalTest {

    private ToolRentalService toolRentalService;

    private Checkout checkout;

    @BeforeAll
    void setUp() {
        toolRentalService = new ToolRentalService();
    }

    @BeforeEach
    void setUpEach() {
        checkout = new Checkout();
    }

    /**
     * Converts the Double to a BigDecimal and sets the scale.
     * @param number Double to convert
     * @return BigDecimal with 2 decimal places.
     */
    private BigDecimal convertDoubleToBigDecimal(final Double number) {
        return BigDecimal.valueOf(number).setScale(2, RoundingMode.HALF_UP);
    }

    @Test
    void test1() {
        final Tool tool = new Tool(ToolType.JACKHAMMER, ToolBrand.RIDGID, "JAKR");
        checkout.setTool(tool);
        checkout.setCheckoutDate(LocalDate.of(2015, 9, 3));
        checkout.setRentalDayCount(5);
        final Exception exception = assertThrows(InvalidInputException.class, () -> checkout.setDiscountPercent(101));
        final String expectedMessage = "Discount Percent must be between 0 and 100";
        final String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void test2() {
        final Tool tool = new Tool(ToolType.LADDER, ToolBrand.WERNER, "LADW");
        checkout.setTool(tool);
        checkout.setCheckoutDate(LocalDate.of(2020, 7, 2));
        checkout.setRentalDayCount(3);
        checkout.setDiscountPercent(10);
        final RentalAgreement rentalAgreement = toolRentalService.generateRentalAgreement(checkout);
        assertEquals(rentalAgreement.getTool(), tool);
        assertEquals(rentalAgreement.getRentalDays(), checkout.getRentalDayCount());
        assertEquals(rentalAgreement.getDiscountPercent(), checkout.getDiscountPercent());
        assertEquals(rentalAgreement.getCheckoutDate(), checkout.getCheckoutDate());
        assertEquals(rentalAgreement.getDueDate(), checkout.getCheckoutDate().plusDays(checkout.getRentalDayCount()));
        assertEquals(rentalAgreement.getDailyRentalCharge(), tool.getType().getDailyCharge());
        assertEquals(rentalAgreement.getChargeDays(), 2);
        assertEquals(rentalAgreement.getPreDiscountCharge(), convertDoubleToBigDecimal(3.98));
        assertEquals(rentalAgreement.getDiscountAmount(), convertDoubleToBigDecimal(0.40));
        assertEquals(rentalAgreement.getFinalCharge(), convertDoubleToBigDecimal(3.58));
    }

    @Test
    void test3() {
        final Tool tool = new Tool(ToolType.CHAINSAW, ToolBrand.STIHL, "CHNS");
        checkout.setTool(tool);
        checkout.setCheckoutDate(LocalDate.of(2015, 7, 2));
        checkout.setRentalDayCount(5);
        checkout.setDiscountPercent(25);
        final RentalAgreement rentalAgreement = toolRentalService.generateRentalAgreement(checkout);
        assertEquals(rentalAgreement.getTool(), tool);
        assertEquals(rentalAgreement.getRentalDays(), checkout.getRentalDayCount());
        assertEquals(rentalAgreement.getDiscountPercent(), checkout.getDiscountPercent());
        assertEquals(rentalAgreement.getCheckoutDate(), checkout.getCheckoutDate());
        assertEquals(rentalAgreement.getDueDate(), checkout.getCheckoutDate().plusDays(checkout.getRentalDayCount()));
        assertEquals(rentalAgreement.getDailyRentalCharge(), tool.getType().getDailyCharge());
        assertEquals(rentalAgreement.getChargeDays(), 3);
        assertEquals(rentalAgreement.getPreDiscountCharge(), convertDoubleToBigDecimal(4.47));
        assertEquals(rentalAgreement.getDiscountAmount(), convertDoubleToBigDecimal(1.12));
        assertEquals(rentalAgreement.getFinalCharge(), convertDoubleToBigDecimal(3.35));
    }

    @Test
    void test4() {
        final Tool tool = new Tool(ToolType.JACKHAMMER, ToolBrand.DEWALT, "JAKD");
        checkout.setTool(tool);
        checkout.setCheckoutDate(LocalDate.of(2015, 9, 3));
        checkout.setRentalDayCount(6);
        checkout.setDiscountPercent(0);
        final RentalAgreement rentalAgreement = toolRentalService.generateRentalAgreement(checkout);
        assertEquals(rentalAgreement.getTool(), tool);
        assertEquals(rentalAgreement.getRentalDays(), checkout.getRentalDayCount());
        assertEquals(rentalAgreement.getDiscountPercent(), checkout.getDiscountPercent());
        assertEquals(rentalAgreement.getCheckoutDate(), checkout.getCheckoutDate());
        assertEquals(rentalAgreement.getDueDate(), checkout.getCheckoutDate().plusDays(checkout.getRentalDayCount()));
        assertEquals(rentalAgreement.getDailyRentalCharge(), tool.getType().getDailyCharge());
        assertEquals(rentalAgreement.getChargeDays(), 3);
        assertEquals(rentalAgreement.getPreDiscountCharge(), convertDoubleToBigDecimal(8.97));
        assertEquals(rentalAgreement.getDiscountAmount(), convertDoubleToBigDecimal(0.00));
        assertEquals(rentalAgreement.getFinalCharge(), convertDoubleToBigDecimal(8.97));
    }

    @Test
    void test5() {
        final Tool tool = new Tool(ToolType.JACKHAMMER, ToolBrand.RIDGID, "JAKR");
        checkout.setTool(tool);
        checkout.setCheckoutDate(LocalDate.of(2015, 7, 2));
        checkout.setRentalDayCount(9);
        checkout.setDiscountPercent(0);
        final RentalAgreement rentalAgreement = toolRentalService.generateRentalAgreement(checkout);
        assertEquals(rentalAgreement.getTool(), tool);
        assertEquals(rentalAgreement.getRentalDays(), checkout.getRentalDayCount());
        assertEquals(rentalAgreement.getDiscountPercent(), checkout.getDiscountPercent());
        assertEquals(rentalAgreement.getCheckoutDate(), checkout.getCheckoutDate());
        assertEquals(rentalAgreement.getDueDate(), checkout.getCheckoutDate().plusDays(checkout.getRentalDayCount()));
        assertEquals(rentalAgreement.getDailyRentalCharge(), tool.getType().getDailyCharge());
        assertEquals(rentalAgreement.getChargeDays(), 5);
        assertEquals(rentalAgreement.getPreDiscountCharge(), convertDoubleToBigDecimal(14.95));
        assertEquals(rentalAgreement.getDiscountAmount(), convertDoubleToBigDecimal(0.00));
        assertEquals(rentalAgreement.getFinalCharge(), convertDoubleToBigDecimal(14.95));
    }

    @Test
    void test6() {
        final Tool tool = new Tool(ToolType.JACKHAMMER, ToolBrand.RIDGID, "JAKR");
        checkout.setTool(tool);
        checkout.setCheckoutDate(LocalDate.of(2020, 7, 2));
        checkout.setRentalDayCount(4);
        checkout.setDiscountPercent(50);
        final RentalAgreement rentalAgreement = toolRentalService.generateRentalAgreement(checkout);
        assertEquals(rentalAgreement.getTool(), tool);
        assertEquals(rentalAgreement.getRentalDays(), checkout.getRentalDayCount());
        assertEquals(rentalAgreement.getDiscountPercent(), checkout.getDiscountPercent());
        assertEquals(rentalAgreement.getCheckoutDate(), checkout.getCheckoutDate());
        assertEquals(rentalAgreement.getDueDate(), checkout.getCheckoutDate().plusDays(checkout.getRentalDayCount()));
        assertEquals(rentalAgreement.getDailyRentalCharge(), tool.getType().getDailyCharge());
        assertEquals(rentalAgreement.getChargeDays(), 1);
        assertEquals(rentalAgreement.getPreDiscountCharge(), convertDoubleToBigDecimal(2.99));
        assertEquals(rentalAgreement.getDiscountAmount(), convertDoubleToBigDecimal(1.50));
        assertEquals(rentalAgreement.getFinalCharge(), convertDoubleToBigDecimal(1.49));
    }

    @Test
    void testOneHundredPercentDiscount() {
        final Tool tool = new Tool(ToolType.LADDER, ToolBrand.WERNER, "LADW");
        checkout.setTool(tool);
        checkout.setCheckoutDate(LocalDate.of(1999, 1, 1));
        checkout.setRentalDayCount(5);
        checkout.setDiscountPercent(100);
        final RentalAgreement rentalAgreement = toolRentalService.generateRentalAgreement(checkout);
        assertEquals(rentalAgreement.getTool(), tool);
        assertEquals(rentalAgreement.getRentalDays(), checkout.getRentalDayCount());
        assertEquals(rentalAgreement.getDiscountPercent(), checkout.getDiscountPercent());
        assertEquals(rentalAgreement.getCheckoutDate(), checkout.getCheckoutDate());
        assertEquals(rentalAgreement.getDueDate(), checkout.getCheckoutDate().plusDays(checkout.getRentalDayCount()));
        assertEquals(rentalAgreement.getDailyRentalCharge(), tool.getType().getDailyCharge());
        assertEquals(rentalAgreement.getChargeDays(), 5);
        assertEquals(rentalAgreement.getPreDiscountCharge(), convertDoubleToBigDecimal(9.95));
        assertEquals(rentalAgreement.getDiscountAmount(), convertDoubleToBigDecimal(9.95));
        assertEquals(rentalAgreement.getFinalCharge(), convertDoubleToBigDecimal(0.00));
    }

    @Test
    void testNegativeDiscount() {
        final Tool tool = new Tool(ToolType.LADDER, ToolBrand.WERNER, "LADW");
        checkout.setTool(tool);
        checkout.setCheckoutDate(LocalDate.of(1999, 1, 1));
        checkout.setRentalDayCount(5);
        final Exception exception = assertThrows(InvalidInputException.class, () -> checkout.setDiscountPercent(-1));
        final String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(INVALID_DISCOUNT_PERCENT_MESSAGE));
    }

    @Test
    void testInvalidRentalDayCount() {
        final Tool tool = new Tool(ToolType.JACKHAMMER, ToolBrand.RIDGID, "JAKR");
        checkout.setTool(tool);
        checkout.setCheckoutDate(LocalDate.of(2015, 9, 3));
        final Exception exception = assertThrows(InvalidInputException.class, () -> checkout.setRentalDayCount(0));
        final String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(INVALID_RENTAL_DAY_MESSAGE));
    }

    @Test
    void testInvalidCheckoutDate() {
        final Exception exception = assertThrows(InvalidInputException.class, () -> checkout.setCheckoutDate(null));
        final String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(INVALID_CHECKOUT_DATE_MESSAGE));
    }

    @Test
    void testHighRentalDayCountChainsaw() {
        final Tool tool = new Tool(ToolType.CHAINSAW, ToolBrand.STIHL, "CHNS");
        checkout.setTool(tool);
        checkout.setCheckoutDate(LocalDate.of(2005, 5, 25));
        checkout.setRentalDayCount(100);
        checkout.setDiscountPercent(10);
        final RentalAgreement rentalAgreement = toolRentalService.generateRentalAgreement(checkout);
        assertEquals(rentalAgreement.getTool(), tool);
        assertEquals(rentalAgreement.getRentalDays(), checkout.getRentalDayCount());
        assertEquals(rentalAgreement.getDiscountPercent(), checkout.getDiscountPercent());
        assertEquals(rentalAgreement.getCheckoutDate(), checkout.getCheckoutDate());
        assertEquals(rentalAgreement.getDueDate(), checkout.getCheckoutDate().plusDays(checkout.getRentalDayCount()));
        assertEquals(rentalAgreement.getDailyRentalCharge(), tool.getType().getDailyCharge());
        assertEquals(rentalAgreement.getChargeDays(), 72);
        assertEquals(rentalAgreement.getPreDiscountCharge(), convertDoubleToBigDecimal(107.28));
        assertEquals(rentalAgreement.getDiscountAmount(), convertDoubleToBigDecimal(10.73));
        assertEquals(rentalAgreement.getFinalCharge(), convertDoubleToBigDecimal(96.55));
    }

    @Test
    void testHighRentalDayCountLadder() {
        final Tool tool = new Tool(ToolType.LADDER, ToolBrand.WERNER, "LADW");
        checkout.setTool(tool);
        checkout.setCheckoutDate(LocalDate.of(2005, 5, 25));
        checkout.setRentalDayCount(50);
        checkout.setDiscountPercent(50);
        final RentalAgreement rentalAgreement = toolRentalService.generateRentalAgreement(checkout);
        assertEquals(rentalAgreement.getTool(), tool);
        assertEquals(rentalAgreement.getRentalDays(), checkout.getRentalDayCount());
        assertEquals(rentalAgreement.getDiscountPercent(), checkout.getDiscountPercent());
        assertEquals(rentalAgreement.getCheckoutDate(), checkout.getCheckoutDate());
        assertEquals(rentalAgreement.getDueDate(), checkout.getCheckoutDate().plusDays(checkout.getRentalDayCount()));
        assertEquals(rentalAgreement.getDailyRentalCharge(), tool.getType().getDailyCharge());
        assertEquals(rentalAgreement.getChargeDays(), 49);
        assertEquals(rentalAgreement.getPreDiscountCharge(), convertDoubleToBigDecimal(97.51));
        assertEquals(rentalAgreement.getDiscountAmount(), convertDoubleToBigDecimal(48.76));
        assertEquals(rentalAgreement.getFinalCharge(), convertDoubleToBigDecimal(48.75));
    }

    @Test
    void testHighRentalDayCountJackhammer() {
        final Tool tool = new Tool(ToolType.JACKHAMMER, ToolBrand.RIDGID, "JAKR");
        checkout.setTool(tool);
        checkout.setCheckoutDate(LocalDate.of(2005, 5, 25));
        checkout.setRentalDayCount(75);
        checkout.setDiscountPercent(25);
        final RentalAgreement rentalAgreement = toolRentalService.generateRentalAgreement(checkout);
        assertEquals(rentalAgreement.getTool(), tool);
        assertEquals(rentalAgreement.getRentalDays(), checkout.getRentalDayCount());
        assertEquals(rentalAgreement.getDiscountPercent(), checkout.getDiscountPercent());
        assertEquals(rentalAgreement.getCheckoutDate(), checkout.getCheckoutDate());
        assertEquals(rentalAgreement.getDueDate(), checkout.getCheckoutDate().plusDays(checkout.getRentalDayCount()));
        assertEquals(rentalAgreement.getDailyRentalCharge(), tool.getType().getDailyCharge());
        assertEquals(rentalAgreement.getChargeDays(), 52);
        assertEquals(rentalAgreement.getPreDiscountCharge(), convertDoubleToBigDecimal(155.48));
        assertEquals(rentalAgreement.getDiscountAmount(), convertDoubleToBigDecimal(38.87));
        assertEquals(rentalAgreement.getFinalCharge(), convertDoubleToBigDecimal(116.61));
    }

    @Test
    void test10() {
        final Tool tool = new Tool(ToolType.JACKHAMMER, ToolBrand.RIDGID, "JAKR");
        checkout.setTool(tool);
        checkout.setCheckoutDate(LocalDate.of(2015, 7, 1));
        checkout.setRentalDayCount(10);
        checkout.setDiscountPercent(60);
        final RentalAgreement rentalAgreement = toolRentalService.generateRentalAgreement(checkout);
        assertEquals(rentalAgreement.getTool(), tool);
        assertEquals(rentalAgreement.getRentalDays(), checkout.getRentalDayCount());
        assertEquals(rentalAgreement.getDiscountPercent(), checkout.getDiscountPercent());
        assertEquals(rentalAgreement.getCheckoutDate(), checkout.getCheckoutDate());
        assertEquals(rentalAgreement.getDueDate(), checkout.getCheckoutDate().plusDays(checkout.getRentalDayCount()));
        assertEquals(rentalAgreement.getDailyRentalCharge(), tool.getType().getDailyCharge());
        assertEquals(rentalAgreement.getChargeDays(), 6);
        assertEquals(rentalAgreement.getPreDiscountCharge(), convertDoubleToBigDecimal(17.94));
        assertEquals(rentalAgreement.getDiscountAmount(), convertDoubleToBigDecimal(10.76));
        assertEquals(rentalAgreement.getFinalCharge(), convertDoubleToBigDecimal(7.18));
    }

    @Test
    void testInvalidJackhammerCode() {
        final Tool tool = new Tool(ToolType.JACKHAMMER, ToolBrand.WERNER, "JAKW");
        final Exception exception = assertThrows(InvalidInputException.class, () -> checkout.setTool(tool));
        final String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(INVALID_TOOL_CODE));
    }

    @Test
    void testInvalidLadderCode() {
        final Tool tool = new Tool(ToolType.LADDER, ToolBrand.RIDGID, "LADR");
        final Exception exception = assertThrows(InvalidInputException.class, () -> checkout.setTool(tool));
        final String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(INVALID_TOOL_CODE));
    }

    @Test
    void testInvalidChainsawCode() {
        final Tool tool = new Tool(ToolType.CHAINSAW, ToolBrand.RIDGID, "CHNR");
        final Exception exception = assertThrows(InvalidInputException.class, () -> checkout.setTool(tool));
        final String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(INVALID_TOOL_CODE));
    }
}
