package com.ECL062024.ToolRental.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Tool {
    private static final Logger logger = LoggerFactory.getLogger(Tool.class);
    private final String toolCode;
    private final String toolType;
    private final String toolBrand;
    private final double dailyCharge;
    private final boolean weekdayCharge;
    private final boolean weekendCharge;
    private final boolean holidayCharge;

    // Constructor, getters, and setters
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

    // Getters
    public String getToolCode() {
        logger.debug("Getting tool code: {}", toolCode);
        return toolCode;
    }

    public String getToolType() {
        logger.debug("Getting tool type: {}", toolType);
        return toolType;
    }

    public String getToolBrand() {
        logger.debug("Getting tool brand: {}", toolBrand);
        return toolBrand;
    }

    public double getDailyCharge() {
        logger.debug("Getting daily charge: {}", dailyCharge);
        return dailyCharge;
    }

    public boolean isWeekdayCharge() {
        logger.debug("Getting weekday charge: {}", weekdayCharge);
        return weekdayCharge;
    }

    public boolean isWeekendCharge() {
        logger.debug("Getting weekend charge: {}", weekendCharge);
        return weekendCharge;
    }

    public boolean isHolidayCharge() {
        logger.debug("Getting holiday charge: {}", holidayCharge);
        return holidayCharge;
    }

}
