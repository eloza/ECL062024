package com.ECL062024.ToolRental.service;

import com.ECL062024.ToolRental.model.RentalAgreement;
import com.ECL062024.ToolRental.model.Tool;
import com.ECL062024.ToolRental.repository.ToolRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * Service for managing tool rentals.
 * Provides methods to check out tools and calculate rental charges.
 */
@Service
public class ToolRentalService {
    private static final Logger logger = LoggerFactory.getLogger(ToolRentalService.class);
    private final ToolRepository toolRepository;

    /**
     * Constructor to initialize ToolRentalService with the given ToolRepository.
     *
     * @param toolRepository the tool repository
     */
    @Autowired
    public ToolRentalService(ToolRepository toolRepository) {
        this.toolRepository = toolRepository;
    }

    /**
     * Checks out a tool and creates a rental agreement.
     *
     * @param toolCode        the tool code
     * @param rentalDays      the number of rental days
     * @param discountPercent the discount percent
     * @param checkoutDateStr the checkout date as a string
     * @return the rental agreement
     * @throws ParseException if the checkout date string is invalid
     */
    public RentalAgreement checkOutTool(String toolCode, int rentalDays, int discountPercent, String checkoutDateStr)
            throws ParseException {
        logger.info("Checking out tool with code: {}, rental days: {}, discount percent: {}, checkout date: {}",
                toolCode, rentalDays, discountPercent, checkoutDateStr);
        // Validate rental days and discount percent
        validateRentalDays(rentalDays);
        validateDiscountPercent(discountPercent);

        // Get tool information from repository
        Tool tool = toolRepository.findByCode(toolCode);
        logger.debug("Tool retrieved: {}", tool);

        // Check if tool exists
        if (tool == null) {
            logger.error("Tool with code {} does not exist.", toolCode);
            throw new IllegalArgumentException("Tool with code " + toolCode + " does not exist.");
        }

        // Parse checkout date
        Date checkoutDate = parseDate(checkoutDateStr);
        logger.debug("Parsed checkout date: {}", checkoutDate);

        // Calculate chargeable days
        int chargeableDays = calculateChargeableDays(checkoutDate, rentalDays);
        logger.debug("Chargeable days calculated: {}", chargeableDays);

        // Calculate due date
        Date dueDate = calculateDueDate(checkoutDate, rentalDays);
        logger.debug("Due date calculated: {}", dueDate);

        // Calculate pre-discount charge
        BigDecimal dailyCharge = BigDecimal.valueOf(tool.getDailyCharge());
        BigDecimal preDiscountCharge = calculatePreDiscountCharge(dailyCharge, chargeableDays);
        logger.debug("Pre-discount charge calculated: {}", preDiscountCharge);

        // Calculate discount amount
        BigDecimal discountAmount = calculateDiscountAmount(preDiscountCharge, discountPercent);
        logger.debug("Discount amount calculated: {}", discountAmount);

        // Calculate final charge
        BigDecimal finalCharge = calculateFinalCharge(preDiscountCharge, discountAmount);
        logger.debug("Final charge calculated: {}", finalCharge);

        // Build rental agreement using builder pattern
        return new RentalAgreement.Builder(toolCode, tool.getToolType(), tool.getToolBrand())
                .rentalDays(rentalDays)
                .checkoutDate(checkoutDate)
                .dueDate(dueDate)
                .dailyCharge(dailyCharge)
                .preDiscountCharge(preDiscountCharge)
                .discountPercent(discountPercent)
                .discountAmount(discountAmount)
                .finalCharge(finalCharge)
                .chargeDays(chargeableDays)
                .build();
    }


    /**
     * Calculates the number of chargeable days for the rental period.
     *
     * @param checkoutDate the checkout date
     * @param rentalDays   the number of rental days
     * @return the number of chargeable days
     */
    private int calculateChargeableDays(Date checkoutDate, int rentalDays) {
        int chargeableDays = 0;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(checkoutDate);

        for (int i = 0; i < rentalDays; i++) {
            Date currentDate = calendar.getTime();

            // Check if the current date is chargeable (weekday and not a holiday)
            if (isWeekday(currentDate) && !isHoliday(currentDate)) {
                chargeableDays++;
            }

            // Move to the next day
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        logger.debug("Chargeable days for checkout date {}: {}", checkoutDate, chargeableDays);
        return chargeableDays;
    }

    /**
     * Checks if a given date is a chargeable day.
     *
     * @param date the date to check
     * @return true if the date is chargeable, false otherwise
     */
    private boolean isChargeableDay(Date date) {
        // Check if it is a holiday first
        if (isHoliday(date)) {
            return false;
        }

        // Check if it is a weekday
        return isWeekday(date);
    }

    /**
     * Checks if a given date is a weekday.
     *
     * @param date the date to check
     * @return true if the date is a weekday, false otherwise
     */
    private boolean isWeekday(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        return dayOfWeek != Calendar.SATURDAY && dayOfWeek != Calendar.SUNDAY;
    }

    /**
     * Checks if a given date is a holiday.
     *
     * @param date the date to check
     * @return true if the date is a holiday, false otherwise
     */
    private boolean isHoliday(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        int month = cal.get(Calendar.MONTH);
        int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);

        // Check for Independence Day July 4th
        if (month == Calendar.JULY && dayOfMonth == 4) {
            // If July 4th falls on a Saturday, observe on Friday July 3rd
            if (dayOfWeek == Calendar.SATURDAY) {
                return true;
            }
            // If July 4th falls on a Sunday, observe on Monday July 5th
            if (dayOfWeek == Calendar.SUNDAY) {
                return true;
            }
        }

        // Check for Labor Day, First Monday in September
        if (month == Calendar.SEPTEMBER && dayOfWeek == Calendar.MONDAY && dayOfMonth <= 7) {
            // First Monday in September is a holiday - Labor Day
            return true;
        }

        return false;
    }

    /**
     * Calculates the pre-discount charge.
     *
     * @param dailyCharge    the daily charge
     * @param chargeableDays the number of chargeable days
     * @return the pre-discount charge
     */
    private BigDecimal calculatePreDiscountCharge(BigDecimal dailyCharge, int chargeableDays) {
        return dailyCharge.multiply(BigDecimal.valueOf(chargeableDays)).setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Calculates the discount amount.
     *
     * @param preDiscountCharge the pre-discount charge
     * @param discountPercent   the discount percent
     * @return the discount amount
     */
    private BigDecimal calculateDiscountAmount(BigDecimal preDiscountCharge, int discountPercent) {
        return preDiscountCharge.multiply(BigDecimal.valueOf(discountPercent)).divide(BigDecimal.valueOf(100),
                        RoundingMode.HALF_UP).setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Calculates the final charge after discount.
     *
     * @param preDiscountCharge the pre-discount charge
     * @param discountAmount    the discount amount
     * @return the final charge
     */
    private BigDecimal calculateFinalCharge(BigDecimal preDiscountCharge, BigDecimal discountAmount) {
        return preDiscountCharge.subtract(discountAmount).setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Validates the number of rental days.
     *
     * @param rentalDays the number of rental days
     */
    private void validateRentalDays(int rentalDays) {
        if (rentalDays < 1) {
            throw new IllegalArgumentException("Rental days must be 1 or greater.");
        }
    }

    /**
     * Validates the discount percent.
     *
     * @param discountPercent the discount percent
     */
    private void validateDiscountPercent(int discountPercent) {
        if (discountPercent < 0 || discountPercent > 100) {
            logger.error("Invalid discount percent: {}", discountPercent);
            throw new IllegalArgumentException("Discount percent must be between 0 and 100 inclusive.");
        }
    }

    /**
     * Parses a date from a string.
     *
     * @param dateStr the date string
     * @return the parsed date
     * @throws ParseException if the date string is invalid
     */
    private Date parseDate(String dateStr) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yy");
        return dateFormat.parse(dateStr);
    }

    /**
     * Calculates the due date.
     *
     * @param checkoutDate the checkout date
     * @param rentalDays   the number of rental days
     * @return the due date
     */
    private Date calculateDueDate(Date checkoutDate, int rentalDays) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(checkoutDate);
        calendar.add(Calendar.DAY_OF_MONTH, rentalDays);
        return calendar.getTime();
    }

}
