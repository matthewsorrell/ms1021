package com.msorrell.project.services;

import com.msorrell.project.enums.ToolType;
import com.msorrell.project.models.Checkout;
import com.msorrell.project.models.RentalAgreement;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.util.Set;

import static com.msorrell.project.validators.InputValidator.findTool;
import static com.msorrell.project.validators.InputValidator.validateCheckoutAgain;
import static com.msorrell.project.validators.InputValidator.validateCheckoutDate;
import static com.msorrell.project.validators.InputValidator.validateDiscountPercent;
import static com.msorrell.project.validators.InputValidator.validateRentalDays;
import static com.msorrell.project.validators.InputValidator.validateToolCode;
import static com.msorrell.project.util.ProjectUtils.DATE_FORMATTER;

import static java.time.temporal.TemporalAdjusters.firstInMonth;
import static java.util.stream.Collectors.toSet;

/**
 * Service that generates the Checkout, the RentalAgreement.
 */
public class ToolRentalService {

    /**
     * Set of the DayOfWeek that are weekend days.
     */
    private static final Set<DayOfWeek> WEEKEND_DAYS = Set.of(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY);

    /**
     * Set of the DayOfWeek that are weekdays.
     */
    private static final Set<DayOfWeek> WEEKDAY_DAYS = Set.of(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY,
            DayOfWeek.THURSDAY, DayOfWeek.FRIDAY);

    /**
     * Calculates the number of chargeable days.
     * Count of chargeable days, from day after checkout through and including due date,
     * excluding “no charge” days as specified by the tool type.
     * @param checkoutDate Day the tool is checked out
     * @param dueDate Day the tool is due
     * @param toolType The type of tool. Used to determine which days count as chargeable
     * @return number of chargeable days
     */
    public int calculateChargeDays(final LocalDate checkoutDate, final LocalDate dueDate, final ToolType toolType) {
        final LocalDate dayAfterCheckout = checkoutDate.plusDays(1);

        //A set that contains every date between the day ofter checkout and the due date
        // (LocalDate.datesUntil is exclusive.)
        final Set<LocalDate> setOfDays = dayAfterCheckout.datesUntil(dueDate.plusDays(1)).collect(toSet());

        int numberOfChargeDays = 0;
        for (final LocalDate day : setOfDays) {
            //Determine if the day is a holiday
            if (isHoliday(day)) {
                if (toolType.isHolidayCharge()) {
                    numberOfChargeDays++;
                }
                continue;
            }
            //Determine if the day is a weekend
            if (isWeekend(day)) {
                if (toolType.isWeekendCharge()) {
                    numberOfChargeDays++;
                }
                continue;
            }
            //Determine if the day is a weekday
            if (isWeekday(day)) {
                if (toolType.isWeekdayCharge()) {
                    numberOfChargeDays++;
                }
            }
        }
        return numberOfChargeDays;
    }

    /**
     * Generates the Rental Agreement from the Checkout.
     * @param checkout Checkout object
     * @return Rental Agreement
     */
    public RentalAgreement generateRentalAgreement(final Checkout checkout) {
        final RentalAgreement rentalAgreement = new RentalAgreement();
        rentalAgreement.setTool(checkout.getTool());
        rentalAgreement.setRentalDays(checkout.getRentalDayCount());
        rentalAgreement.setCheckoutDate(checkout.getCheckoutDate());
        rentalAgreement.setDiscountPercent(checkout.getDiscountPercent());
        rentalAgreement.setDueDate(rentalAgreement.getCheckoutDate().plusDays(rentalAgreement.getRentalDays()));
        rentalAgreement.setDailyRentalCharge(rentalAgreement.getTool().getType().getDailyCharge());
        rentalAgreement.setChargeDays(calculateChargeDays(rentalAgreement.getCheckoutDate(),
                rentalAgreement.getDueDate(),
                rentalAgreement.getTool().getType()));
        //Set pre-discount charge. Formula: charge days X daily charge. Round half up to the nearest cent.
        rentalAgreement.setPreDiscountCharge(rentalAgreement.getDailyRentalCharge()
                .multiply(BigDecimal.valueOf(rentalAgreement.getChargeDays())).setScale(2, RoundingMode.HALF_UP));

        final double percent = rentalAgreement.getDiscountPercent() * .01;
        //Set discount amount. Formula: discount % and pre-discount charge. Round half up to the nearest cent.
        rentalAgreement.setDiscountAmount(rentalAgreement.getPreDiscountCharge()
                .multiply(BigDecimal.valueOf(percent)).setScale(2, RoundingMode.HALF_UP));

        rentalAgreement.setFinalCharge(rentalAgreement.getPreDiscountCharge()
                .subtract(rentalAgreement.getDiscountAmount()));

        return rentalAgreement;
    }

    /**
     * Reads the user's input to generate a Checkout object.
     * @param reader BufferedReader
     * @return Checkout
     * @throws IOException exception while reading input
     */
    public Checkout readInput(final BufferedReader reader) throws IOException {
        final Checkout checkout = new Checkout();
        System.out.println("Please provide the tool code");
        String toolCode = reader.readLine();
        while (!validateToolCode(toolCode)) {
            System.out.println("Please provide a valid tool code");
            toolCode = reader.readLine();
        }
        checkout.setTool(findTool(toolCode));

        System.out.println("Please provide the number of rental day");
        String rentalDays = reader.readLine();
        while (!validateRentalDays(rentalDays)) {
            System.out.println("Please provide a number greater than 0");
            rentalDays = reader.readLine();
        }
        checkout.setRentalDayCount(Integer.parseInt(rentalDays));

        System.out.println("Please provide the discount");
        String discount = reader.readLine();
        while (!validateDiscountPercent(discount)) {
            System.out.println("Please provide a number between 0-100");
            discount = reader.readLine();
        }
        checkout.setDiscountPercent(Integer.parseInt(discount));

        System.out.println("Please provide the checkout date (Format: MM/DD/YY)");
        String checkoutDate = reader.readLine();
        while (!validateCheckoutDate(checkoutDate)) {
            System.out.println("Please provide a valid date (Format: MM/DD/YY)");
            checkoutDate = reader.readLine();
        }
        checkout.setCheckoutDate(LocalDate.parse(checkoutDate, DATE_FORMATTER));

        return checkout;
    }

    /**
     * Determines if the user wants to generate another checkout.
     * @param reader BufferedReader
     * @return true - checkout again, false - terminate
     * @throws IOException exception while reading input
     */
    public boolean checkoutAgain(final BufferedReader reader) throws IOException {
        System.out.println("Checkout again? (y/n)");
        String again = reader.readLine();
        while (!validateCheckoutAgain(again)) {
            System.out.println("Please provide a valid answer");
            again = reader.readLine();
        }
        return again.equalsIgnoreCase("Y");
    }

    /**
     * Determines if the day is a weekend date.
     * @param date LocalDate to test
     * @return boolean
     */
    private static boolean isWeekend(final LocalDate date) {
        return WEEKEND_DAYS.contains(date.getDayOfWeek());
    }

    /**
     * Determines if the day is a weekday.
     * @param date LocalDate to test
     * @return boolean
     */
    private static boolean isWeekday(final LocalDate date) {
        return WEEKDAY_DAYS.contains(date.getDayOfWeek());
    }

    /**
     * Determines if the date is a holiday.
     * @param date LocalDate to test
     * @return boolean
     */
    private static boolean isHoliday(final LocalDate date) {
        return isLaborDay(date) || isIndependenceDay(date);
    }

    /**
     * Determines if the date is labor day.
     * First Monday in September
     * @param date LocalDate to test
     * @return boolean
     */
    private static boolean isLaborDay(final LocalDate date) {
        final LocalDate laborDay = LocalDate.of(date.getYear(), Month.SEPTEMBER, 1).
                with(firstInMonth(DayOfWeek.MONDAY));
        return date.isEqual(laborDay);
    }

    /**
     * Determines if the date is independence day.
     * July 4th - If falls on weekend, it is observed on the closest weekday (if Sat, then
     * Friday before, if Sunday, then Monday after)
     * @param date LocalDate to test
     * @return boolean
     */
    private static boolean isIndependenceDay(final LocalDate date) {
       LocalDate independenceDay = LocalDate.of(date.getYear(), Month.JULY, 4);
       if (DayOfWeek.SUNDAY.equals(independenceDay.getDayOfWeek())) {
           independenceDay = independenceDay.plusDays(1);
       }
       if (DayOfWeek.SATURDAY.equals(independenceDay.getDayOfWeek())) {
           independenceDay = independenceDay.minusDays(1);
       }
       return date.isEqual(independenceDay);
    }
}
