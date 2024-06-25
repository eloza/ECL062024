package com.ECL062024.ToolRental.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents a tool available for rental.
 */
public class Tool {
    private static final Logger logger = LoggerFactory.getLogger(Tool.class);
    private final String toolCode;
    private final String toolType;
    private final String toolBrand;
    private final double dailyCharge;
    private final boolean weekdayCharge;
    private final boolean weekendCharge;
    private final boolean holidayCharge;

    /**
     * Constructs a new Tool with the specified attributes.
     *
     * @param toolCode       the code identifying the tool
     * @param toolType       the type of the tool
     * @param toolBrand      the brand of the tool
     * @param dailyCharge    the daily rental charge for the tool
     * @param weekdayCharge  indicates if the tool has a weekday charge
     * @param weekendCharge  indicates if the tool has a weekend charge
     * @param holidayCharge  indicates if the tool has a holiday charge
     */
    public Tool(String toolCode, String toolType, String toolBrand, double dailyCharge,
                boolean weekdayCharge, boolean weekendCharge, boolean holidayCharge) {
        this.toolCode = toolCode;
        this.toolType = toolType;
        this.toolBrand = toolBrand;
        this.dailyCharge = dailyCharge;
        this.weekdayCharge = weekdayCharge;
        this.weekendCharge = weekendCharge;
        this.holidayCharge = holidayCharge;

        logger.info("Tool created: {} - {} (Brand: {}, Daily Charge: {}, Weekday Charge: {}, Weekend Charge: {}, " +
                        "Holiday Charge: {})", toolCode, toolType, toolBrand, dailyCharge, weekdayCharge,
                weekendCharge, holidayCharge);
    }

    /**
     * Gets the tool code.
     *
     * @return the tool code
     */
    public String getToolCode() {
        logger.debug("Getting tool code: {}", toolCode);
        return toolCode;
    }

    /**
     * Gets the tool type.
     *
     * @return the tool type
     */
    public String getToolType() {
        logger.debug("Getting tool type: {}", toolType);
        return toolType;
    }

    /**
     * Gets the tool brand.
     *
     * @return the tool brand
     */
    public String getToolBrand() {
        logger.debug("Getting tool brand: {}", toolBrand);
        return toolBrand;
    }

    /**
     * Gets the daily charge for the tool.
     *
     * @return the daily charge
     */
    public double getDailyCharge() {
        logger.debug("Getting daily charge: {}", dailyCharge);
        return dailyCharge;
    }

    /**
     * Checks if the tool has a weekday charge.
     *
     * @return true if the tool has a weekday charge, false otherwise
     */
    public boolean isWeekdayCharge() {
        logger.debug("Getting weekday charge: {}", weekdayCharge);
        return weekdayCharge;
    }

    /**
     * Checks if the tool has a weekend charge.
     *
     * @return true if the tool has a weekend charge, false otherwise
     */
    public boolean isWeekendCharge() {
        logger.debug("Getting weekend charge: {}", weekendCharge);
        return weekendCharge;
    }

    /**
     * Checks if the tool has a holiday charge.
     *
     * @return true if the tool has a holiday charge, false otherwise
     */
    public boolean isHolidayCharge() {
        logger.debug("Getting holiday charge: {}", holidayCharge);
        return holidayCharge;
    }

}
