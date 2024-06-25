package com.ECL062024.ToolRental.service;

import com.ECL062024.ToolRental.model.RentalAgreement;
import com.ECL062024.ToolRental.model.Tool;
import com.ECL062024.ToolRental.repository.ToolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Service
public class ToolRentalService {

    private final ToolRepository toolRepository;

    @Autowired
    public ToolRentalService(ToolRepository toolRepository) {
        this.toolRepository = toolRepository;
    }

    public RentalAgreement checkOutTool(String toolCode, int rentalDays, int discountPercent, String checkoutDateStr)
            throws ParseException {
        // Validate rental days and discount percent
        validateRentalDays(rentalDays);
        validateDiscountPercent(discountPercent);

        // Retrieve tool information from repository
        Tool tool = toolRepository.findByCode(toolCode);

        // Check if tool exists
        if (tool == null) {
            throw new IllegalArgumentException("Tool with code " + toolCode + " does not exist.");
        }

        // Parse checkout date
        Date checkoutDate = parseDate(checkoutDateStr);

        // Calculate chargeable days
        int chargeableDays = calculateChargeableDays(checkoutDate, rentalDays);

        // Calculate due date
        Date dueDate = calculateDueDate(checkoutDate, rentalDays);

        // Calculate pre-discount charge
        BigDecimal dailyCharge = BigDecimal.valueOf(tool.getDailyCharge());
        BigDecimal preDiscountCharge = calculatePreDiscountCharge(dailyCharge, chargeableDays);

        // Calculate discount amount
        BigDecimal discountAmount = calculateDiscountAmount(preDiscountCharge, discountPercent);

        // Calculate final charge
        BigDecimal finalCharge = calculateFinalCharge(preDiscountCharge, discountAmount);

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

        return chargeableDays;
    }


    private boolean isChargeableDay(Date date) {
        // Check if it's a holiday first
        if (isHoliday(date)) {
            return false;
        }

        // Check if it's a weekday
        return isWeekday(date);
    }

    private boolean isWeekday(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        return dayOfWeek != Calendar.SATURDAY && dayOfWeek != Calendar.SUNDAY;
    }

    private boolean isHoliday(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        int month = cal.get(Calendar.MONTH);
        int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);

        // Check for Independence Day (July 4th)
        if (month == Calendar.JULY && dayOfMonth == 4) {
            // If July 4th falls on a Saturday, observe on Friday (July 3rd)
            if (dayOfWeek == Calendar.SATURDAY) {
                return true; // July 3rd is a holiday
            }
            // If July 4th falls on a Sunday, observe on Monday (July 5th)
            if (dayOfWeek == Calendar.SUNDAY) {
                return true; // July 5th is a holiday
            }
        }

        // Check for Labor Day (First Monday in September)
        if (month == Calendar.SEPTEMBER && dayOfWeek == Calendar.MONDAY && dayOfMonth <= 7) {
            return true; // First Monday in September is a holiday (Labor Day)
        }

        return false;
    }


    private BigDecimal calculatePreDiscountCharge(BigDecimal dailyCharge, int chargeableDays) {
        return dailyCharge.multiply(BigDecimal.valueOf(chargeableDays)).setScale(2, RoundingMode.HALF_UP);
    }



    private BigDecimal calculateDiscountAmount(BigDecimal preDiscountCharge, int discountPercent) {
        return preDiscountCharge.multiply(BigDecimal.valueOf(discountPercent)).divide(BigDecimal.valueOf(100),
                        RoundingMode.HALF_UP).setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal calculateFinalCharge(BigDecimal preDiscountCharge, BigDecimal discountAmount) {
        return preDiscountCharge.subtract(discountAmount).setScale(2, RoundingMode.HALF_UP);
    }

    private void validateRentalDays(int rentalDays) {
        if (rentalDays < 1) {
            throw new IllegalArgumentException("Rental days must be 1 or greater.");
        }
    }

    private void validateDiscountPercent(int discountPercent) {
        if (discountPercent < 0 || discountPercent > 100) {
            throw new IllegalArgumentException("Discount percent must be between 0 and 100 inclusive.");
        }
    }

    private Date parseDate(String dateStr) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yy");
        return dateFormat.parse(dateStr);
    }

    private Date calculateDueDate(Date checkoutDate, int rentalDays) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(checkoutDate);
        calendar.add(Calendar.DAY_OF_MONTH, rentalDays);
        return calendar.getTime();
    }

}
