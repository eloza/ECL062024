package com.ECL062024.ToolRental.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RentalAgreement {
    private static final Logger logger = LoggerFactory.getLogger(RentalAgreement.class);
    private final String toolCode;
    private final String toolType;
    private final String toolBrand;
    private final int rentalDays;
    private final Date checkoutDate;
    private final Date dueDate;
    private final BigDecimal dailyCharge;
    private final int chargeDays;
    private final BigDecimal preDiscountCharge;
    private final int discountPercent;
    private final BigDecimal discountAmount;
    private final BigDecimal finalCharge;

    // Private constructor to be used by the builder
    private RentalAgreement(String toolCode,
                            String toolType,
                            String toolBrand,
                            int rentalDays,
                            Date checkoutDate,
                            Date dueDate,
                            BigDecimal dailyCharge,
                            int chargeDays,
                            BigDecimal preDiscountCharge,
                            int discountPercent,
                            BigDecimal discountAmount,
                            BigDecimal finalCharge) {
        this.toolCode = toolCode;
        this.toolType = toolType;
        this.toolBrand = toolBrand;
        this.rentalDays = rentalDays;
        this.checkoutDate = checkoutDate;
        this.dueDate = dueDate;
        this.dailyCharge = dailyCharge;
        this.chargeDays = chargeDays;
        this.preDiscountCharge = preDiscountCharge;
        this.discountPercent = discountPercent;
        this.discountAmount = discountAmount;
        this.finalCharge = finalCharge;

        logger.info("RentalAgreement created: {}", this);
    }

    // Getters for all fields
    public String getToolCode() {
        return toolCode;
    }

    public String getToolType() {
        return toolType;
    }

    public String getToolBrand() {
        return toolBrand;
    }

    public int getRentalDays() {
        return rentalDays;
    }

    public Date getCheckoutDate() {
        return checkoutDate;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public BigDecimal getDailyCharge() {
        return dailyCharge;
    }

    public int getChargeDays() {
        return chargeDays;
    }

    public BigDecimal getPreDiscountCharge() {
        return preDiscountCharge;
    }

    public int getDiscountPercent() {
        return discountPercent;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public BigDecimal getFinalCharge() {
        return finalCharge;
    }

    // Method to print the rental agreement details
    public void printRentalAgreement() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yy");
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
        DecimalFormat percentFormat = new DecimalFormat("#");
        logger.debug("Printing rental agreement for tool code: {}", toolCode);

        System.out.println("Tool code: " + toolCode);
        System.out.println("Tool type: " + toolType);
        System.out.println("Tool brand: " + toolBrand);
        System.out.println("Rental days: " + rentalDays);
        System.out.println("Checkout date: " + dateFormat.format(checkoutDate));
        System.out.println("Due date: " + dateFormat.format(dueDate));
        System.out.println("Daily rental charge: " + currencyFormat.format(dailyCharge));
        System.out.println("Charge days: " + chargeDays);
        System.out.println("Pre-discount charge: " + currencyFormat.format(preDiscountCharge));
        System.out.println("Discount percent: " + percentFormat.format(discountPercent) + "%");
        System.out.println("Discount amount: " + currencyFormat.format(discountAmount));
        System.out.println("Final charge: " + currencyFormat.format(finalCharge));
    }

    // Builder class to construct RentalAgreement instances
    public static class Builder {
        private final String toolCode;
        private final String toolType;
        private final String toolBrand;
        private int rentalDays;
        private Date checkoutDate;
        private Date dueDate;
        private BigDecimal dailyCharge;
        private int chargeDays;
        private BigDecimal preDiscountCharge;
        private int discountPercent;
        private BigDecimal discountAmount;
        private BigDecimal finalCharge;

        public Builder(String toolCode, String toolType, String toolBrand) {
            this.toolCode = toolCode;
            this.toolType = toolType;
            this.toolBrand = toolBrand;
        }

        public Builder rentalDays(int rentalDays) {
            this.rentalDays = rentalDays;
            return this;
        }

        public Builder checkoutDate(Date checkoutDate) {
            this.checkoutDate = checkoutDate;
            return this;
        }

        public Builder dueDate(Date dueDate) {
            this.dueDate = dueDate;
            return this;
        }

        public Builder dailyCharge(BigDecimal dailyCharge) {
            this.dailyCharge = dailyCharge;
            return this;
        }

        public Builder chargeDays(int chargeDays) {
            this.chargeDays = chargeDays;
            return this;
        }

        public Builder preDiscountCharge(BigDecimal preDiscountCharge) {
            this.preDiscountCharge = preDiscountCharge;
            return this;
        }

        public Builder discountPercent(int discountPercent) {
            this.discountPercent = discountPercent;
            return this;
        }

        public Builder discountAmount(BigDecimal discountAmount) {
            this.discountAmount = discountAmount;
            return this;
        }

        public Builder finalCharge(BigDecimal finalCharge) {
            this.finalCharge = finalCharge;
            return this;
        }

        public RentalAgreement build() {
            logger.debug("Building RentalAgreement for tool code: {}", toolCode);
            return new RentalAgreement(
                    toolCode,
                    toolType,
                    toolBrand,
                    rentalDays,
                    checkoutDate,
                    dueDate,
                    dailyCharge,
                    chargeDays,
                    preDiscountCharge,
                    discountPercent,
                    discountAmount,
                    finalCharge);
        }
    }

}
