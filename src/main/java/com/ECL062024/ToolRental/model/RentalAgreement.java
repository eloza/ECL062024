package com.ECL062024.ToolRental.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Represents a rental agreement for a tool.
 * Contains details about the tool, rental period, charges, and discounts.
 */
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

    /**
     * Represents a rental agreement for a tool.
     * Contains details about the tool, rental period, charges, and discounts.
     */
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

    /**
     * @return the tool code.
     */
    public String getToolCode() {
        return toolCode;
    }

    /**
     * @return the tool type.
     */
    public String getToolType() {
        return toolType;
    }

    /**
     * @return the tool brand.
     */
    public String getToolBrand() {
        return toolBrand;
    }

    /**
     * @return the number of rental days.
     */
    public int getRentalDays() {
        return rentalDays;
    }

    /**
     * @return the checkout date.
     */
    public Date getCheckoutDate() {
        return checkoutDate;
    }

    /**
     * @return the due date.
     */
    public Date getDueDate() {
        return dueDate;
    }

    /**
     * @return the daily charge.
     */
    public BigDecimal getDailyCharge() {
        return dailyCharge;
    }

    /**
     * @return the number of chargeable days.
     */
    public int getChargeDays() {
        return chargeDays;
    }

    /**
     * @return the pre-discount charge.
     */
    public BigDecimal getPreDiscountCharge() {
        return preDiscountCharge;
    }

    /**
     * @return the discount percent.
     */
    public int getDiscountPercent() {
        return discountPercent;
    }

    /**
     * @return the discount amount.
     */
    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    /**
     * @return the final charge.
     */
    public BigDecimal getFinalCharge() {
        return finalCharge;
    }

    /**
     * Prints the rental agreement details to the console.
     */
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

    /**
     * Builder class to construct RentalAgreement instances.
     */
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

        /**
         * Constructor for the Builder.
         *
         * @param toolCode  the tool code.
         * @param toolType  the tool type.
         * @param toolBrand the tool brand.
         */
        public Builder(String toolCode, String toolType, String toolBrand) {
            this.toolCode = toolCode;
            this.toolType = toolType;
            this.toolBrand = toolBrand;
        }

        /**
         * Sets the rental days.
         *
         * @param rentalDays the number of rental days.
         * @return the Builder instance.
         */
        public Builder rentalDays(int rentalDays) {
            this.rentalDays = rentalDays;
            return this;
        }

        /**
         * Sets the checkout date.
         *
         * @param checkoutDate the checkout date.
         * @return the Builder instance.
         */
        public Builder checkoutDate(Date checkoutDate) {
            this.checkoutDate = checkoutDate;
            return this;
        }

        /**
         * Sets the due date.
         *
         * @param dueDate the due date.
         * @return the Builder instance.
         */
        public Builder dueDate(Date dueDate) {
            this.dueDate = dueDate;
            return this;
        }

        /**
         * Sets the daily charge.
         *
         * @param dailyCharge the daily charge.
         * @return the Builder instance.
         */
        public Builder dailyCharge(BigDecimal dailyCharge) {
            this.dailyCharge = dailyCharge;
            return this;
        }

        /**
         * Sets the number of chargeable days.
         *
         * @param chargeDays the number of chargeable days.
         * @return the Builder instance.
         */
        public Builder chargeDays(int chargeDays) {
            this.chargeDays = chargeDays;
            return this;
        }

        /**
         * Sets the pre-discount charge.
         *
         * @param preDiscountCharge the pre-discount charge.
         * @return the Builder instance.
         */
        public Builder preDiscountCharge(BigDecimal preDiscountCharge) {
            this.preDiscountCharge = preDiscountCharge;
            return this;
        }

        /**
         * Sets the discount percent.
         *
         * @param discountPercent the discount percent.
         * @return the Builder instance.
         */
        public Builder discountPercent(int discountPercent) {
            this.discountPercent = discountPercent;
            return this;
        }

        /**
         * Sets the discount amount.
         *
         * @param discountAmount the discount amount.
         * @return the Builder instance.
         */
        public Builder discountAmount(BigDecimal discountAmount) {
            this.discountAmount = discountAmount;
            return this;
        }

        /**
         * Sets the final charge.
         *
         * @param finalCharge the final charge.
         * @return the Builder instance.
         */
        public Builder finalCharge(BigDecimal finalCharge) {
            this.finalCharge = finalCharge;
            return this;
        }

        /**
         * Builds and returns a RentalAgreement instance.
         *
         * @return the RentalAgreement instance.
         */
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
