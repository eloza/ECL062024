package com.ECL062024.ToolRental.service;

import com.ECL062024.ToolRental.model.RentalAgreement;
import com.ECL062024.ToolRental.model.Tool;
import com.ECL062024.ToolRental.repository.ToolRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mockStatic;

class ToolRentalServiceTest {
    @InjectMocks
    private ToolRentalService toolRentalService;
    private ToolRepository toolRepository;

    @BeforeEach
    void setUp() {
        toolRepository = Mockito.mock(ToolRepository.class);
        toolRentalService = new ToolRentalService(toolRepository);
    }

    /**
     * Tests the scenario where the discount percent is invalid (greater than 100).
     */
    @Test
    void testCheckoutTool_invalidDiscountPercent() {
        String toolCode = "JAKR";
        String checkoutDateStr = "09/03/15";
        int rentalDays = 5;
        int discountPercent = 101;

        IllegalArgumentException thrown =
                assertThrows(IllegalArgumentException.class, () -> toolRentalService.checkOutTool(
                        toolCode,
                        rentalDays,
                        discountPercent,
                        checkoutDateStr));

        assertEquals("Discount percent must be between 0 and 100 inclusive.", thrown.getMessage());
    }

    /**
     * Tests the checkout of a Ladder tool (LADW) with 3 rental days and 10% discount.
     *
     * @throws ParseException if the checkout date string cannot be parsed.
     */
    @Test
    void testCheckoutTool_LADW() throws ParseException {
        String toolCode = "LADW";
        String checkoutDateStr = "07/02/20";
        int rentalDays = 3;
        int discountPercent = 10;

        Tool mockTool = new Tool(toolCode,
                "Ladder",
                "Werner",
                1.99,
                true,
                true,
                false);

        try (MockedStatic<ToolRepository> mockedStatic = mockStatic(ToolRepository.class)) {
            mockedStatic.when(() -> ToolRepository.findByCode(toolCode)).thenReturn(mockTool);

            RentalAgreement rentalAgreement =
                    toolRentalService.checkOutTool(toolCode, rentalDays, discountPercent, checkoutDateStr);

            assertNotNull(rentalAgreement);
            assertEquals(toolCode, rentalAgreement.getToolCode());
            assertEquals("Ladder", rentalAgreement.getToolType());
            assertEquals("Werner", rentalAgreement.getToolBrand());
            assertEquals(rentalDays, rentalAgreement.getRentalDays());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yy");
            LocalDate expectedCheckoutDate = LocalDate.parse(checkoutDateStr, formatter);
            LocalDate actualCheckoutDate =
                    rentalAgreement.getCheckoutDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            assertEquals(expectedCheckoutDate, actualCheckoutDate);
            assertEquals(expectedCheckoutDate.plusDays(rentalDays), rentalAgreement.getDueDate().toInstant().
                    atZone(ZoneId.systemDefault()).toLocalDate());
            assertEquals(BigDecimal.valueOf(1.99), rentalAgreement.getDailyCharge());
            assertEquals(2, rentalAgreement.getChargeDays());
            assertEquals(BigDecimal.valueOf(3.98), rentalAgreement.getPreDiscountCharge());
            assertEquals(discountPercent, rentalAgreement.getDiscountPercent());
            assertEquals(BigDecimal.valueOf(3.58), rentalAgreement.getFinalCharge());
        }
    }

    /**
     * Tests the checkout of a Chainsaw tool (CHNS) with 5 rental days and 25% discount.
     *
     * @throws ParseException if the checkout date string cannot be parsed.
     */
    @Test
    void testCheckoutTool_CHNS() throws ParseException {
        String toolCode = "CHNS";
        String checkoutDateStr = "07/02/15";
        int rentalDays = 5;
        int discountPercent = 25;

        Tool mockTool = new Tool(toolCode,
                "Chainsaw",
                "Stihl",
                1.49,
                true,
                false,
                true);

        try (MockedStatic<ToolRepository> mockedStatic = mockStatic(ToolRepository.class)) {
            mockedStatic.when(() -> ToolRepository.findByCode(toolCode)).thenReturn(mockTool);
            RentalAgreement rentalAgreement =
                    toolRentalService.checkOutTool(toolCode, rentalDays, discountPercent, checkoutDateStr);
            assertNotNull(rentalAgreement);
            assertEquals(toolCode, rentalAgreement.getToolCode());
            assertEquals("Chainsaw", rentalAgreement.getToolType());
            assertEquals("Stihl", rentalAgreement.getToolBrand());
            assertEquals(rentalDays, rentalAgreement.getRentalDays());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yy");
            LocalDate checkoutDate = LocalDate.parse(checkoutDateStr, formatter);
            assertEquals(checkoutDate, rentalAgreement.getCheckoutDate().toInstant().atZone(ZoneId.systemDefault()).
                    toLocalDate());
            assertEquals(checkoutDate.plusDays(rentalDays), rentalAgreement.getDueDate().toInstant().
                    atZone(ZoneId.systemDefault()).toLocalDate());
            assertEquals(new BigDecimal("1.49"), rentalAgreement.getDailyCharge());
            assertEquals(3, rentalAgreement.getChargeDays());
            assertEquals(new BigDecimal("4.47"), rentalAgreement.getPreDiscountCharge());
            assertEquals(discountPercent, rentalAgreement.getDiscountPercent());
            BigDecimal expectedDiscountAmount = new BigDecimal("1.12");
            assertEquals(expectedDiscountAmount, rentalAgreement.getDiscountAmount());
            BigDecimal expectedFinalCharge = new BigDecimal("3.35");
            assertEquals(expectedFinalCharge, rentalAgreement.getFinalCharge());
        }
    }

    /**
     * Tests the checkout of a Jackhammer tool (JACKD) with 6 rental days and no discount.
     *
     * @throws ParseException if the checkout date string cannot be parsed.
     */
    @Test
    void testCheckoutTool_JACKD() throws ParseException {
        String toolCode = "JACKD";
        String checkoutDateStr = "09/13/15";
        int rentalDays = 6;
        int discountPercent = 0;

        Tool mockTool = new Tool(toolCode,
                "Jackhammer",
                "DeWalt",
                2.99,
                true,
                false,
                false);

        try (MockedStatic<ToolRepository> mockedStatic = mockStatic(ToolRepository.class)) {
            mockedStatic.when(() -> ToolRepository.findByCode(toolCode)).thenReturn(mockTool);

            RentalAgreement rentalAgreement =
                    toolRentalService.checkOutTool(toolCode, rentalDays, discountPercent, checkoutDateStr);

            assertNotNull(rentalAgreement);
            assertEquals(toolCode, rentalAgreement.getToolCode());
            assertEquals("Jackhammer", rentalAgreement.getToolType());
            assertEquals("DeWalt", rentalAgreement.getToolBrand());
            assertEquals(rentalDays, rentalAgreement.getRentalDays());

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yy");
            LocalDate checkoutDate = LocalDate.parse(checkoutDateStr, formatter);

            assertEquals(checkoutDate, rentalAgreement.getCheckoutDate().toInstant().atZone(ZoneId.
                    systemDefault()).toLocalDate());
            assertEquals(checkoutDate.plusDays(rentalDays), rentalAgreement.getDueDate().toInstant().
                    atZone(ZoneId.systemDefault()).toLocalDate());
            assertEquals(new BigDecimal("2.99"), rentalAgreement.getDailyCharge());
            // 5 chargeable days: 9/13, 9/14, 9/15, 9/16, 9/17 (9/18 is weekend)
            assertEquals(5, rentalAgreement.getChargeDays());
            // 2.99 * 5
            assertEquals(new BigDecimal("14.95"), rentalAgreement.getPreDiscountCharge());
            assertEquals(discountPercent, rentalAgreement.getDiscountPercent());
            assertEquals(BigDecimal.valueOf(5), BigDecimal.valueOf(rentalAgreement.getChargeDays()),
                    "Expected 0 chargeable days");
            BigDecimal expectedFinalCharge = new BigDecimal("14.95");
            // 14.95 - 0 (no discount)
            assertEquals(expectedFinalCharge, rentalAgreement.getFinalCharge());
        }
    }

    /**
     * Tests the checkout of a jackhammer tool (JACKR) with 9 rental days and 0% discount.
     *
     * @throws ParseException if the checkout date string cannot be parsed.
     */
    @Test
    void testCheckoutTool_JAKR_9days() throws ParseException {
        String toolCode = "JACKR";
        String checkoutDateStr = "07/02/15";
        int rentalDays = 9;
        int discountPercent = 0;

        // Mocking the tool
        Tool mockTool = new Tool(toolCode,
                "jackhammer",
                "Ridgid",
                1.99,
                true,
                false,
                false);

        try (MockedStatic<ToolRepository> mockedStatic = mockStatic(ToolRepository.class)) {
            mockedStatic.when(() -> ToolRepository.findByCode(toolCode)).thenReturn(mockTool);

            RentalAgreement rentalAgreement = toolRentalService.checkOutTool(toolCode, rentalDays, discountPercent,
                    checkoutDateStr);

            assertNotNull(rentalAgreement);
            assertEquals(toolCode, rentalAgreement.getToolCode());
            assertEquals("jackhammer", rentalAgreement.getToolType());
            assertEquals("Ridgid", rentalAgreement.getToolBrand());
            assertEquals(rentalDays, rentalAgreement.getRentalDays());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yy");
            LocalDate checkoutDate = LocalDate.parse(checkoutDateStr, formatter);
            assertEquals(checkoutDate, rentalAgreement.getCheckoutDate().toInstant().atZone(ZoneId.systemDefault()).
                    toLocalDate());
            assertEquals(checkoutDate.plusDays(rentalDays), rentalAgreement.getDueDate().toInstant().atZone(ZoneId.
                    systemDefault()).toLocalDate());
            assertEquals(new BigDecimal("1.99"), rentalAgreement.getDailyCharge());
            assertEquals(7, rentalAgreement.getChargeDays());
            assertEquals(new BigDecimal("13.93"), rentalAgreement.getPreDiscountCharge());
            assertEquals(discountPercent, rentalAgreement.getDiscountPercent());
            assertEquals(BigDecimal.ZERO.setScale(2, RoundingMode.UNNECESSARY), rentalAgreement.
                    getDiscountAmount().setScale(2, RoundingMode.UNNECESSARY));
            BigDecimal expectedFinalCharge = new BigDecimal("13.93");
            assertEquals(expectedFinalCharge, rentalAgreement.getFinalCharge());
        }
    }

    /**
     * Tests the checkout of a Jackhammer tool (JAKR) with 4 rental days and 50% discount.
     *
     * @throws ParseException if the checkout date string cannot be parsed.
     */
    @Test
    void testCheckoutTool_JAKR_4days() throws ParseException {
        String toolCode = "JACKR";
        int rentalDays = 4;
        int discountPercent = 50;
        String checkoutDateStr = "07/02/2020";

        // Mocking the tool
        Tool mockTool = new Tool(toolCode,
                "Jackhammer",
                "Rigid",
                2.99,
                true,
                false,
                false);

        try (MockedStatic<ToolRepository> mockedStatic = mockStatic(ToolRepository.class)) {
            mockedStatic.when(() -> ToolRepository.findByCode(toolCode)).thenReturn(mockTool);

            RentalAgreement rentalAgreement =
                    toolRentalService.checkOutTool(toolCode, rentalDays, discountPercent, checkoutDateStr);

            assertNotNull(rentalAgreement);
            assertEquals(toolCode, rentalAgreement.getToolCode());
            assertEquals("Jackhammer", rentalAgreement.getToolType());
            assertEquals("Rigid", rentalAgreement.getToolBrand());
            assertEquals(rentalDays, rentalAgreement.getRentalDays());
            assertEquals(new BigDecimal("2.99"), rentalAgreement.getDailyCharge());
            // Expected 2 chargeable days: 7/2, 7/3
            assertEquals(2, rentalAgreement.getChargeDays());
            // 2.99 * 2
            assertEquals(new BigDecimal("5.98"), rentalAgreement.getPreDiscountCharge());
            assertEquals(discountPercent, rentalAgreement.getDiscountPercent());
            // 50% of 5.98
            assertEquals(new BigDecimal("2.99"), rentalAgreement.getDiscountAmount());
            // 5.98 - 2.99 (50% discount)
            assertEquals(new BigDecimal("2.99"), rentalAgreement.getFinalCharge());
        }
    }

}
