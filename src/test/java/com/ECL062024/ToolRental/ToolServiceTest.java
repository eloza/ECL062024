package com.ECL062024.ToolRental;

import com.ECL062024.ToolRental.model.RentalAgreement;
import com.ECL062024.ToolRental.service.ToolRentalService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class ToolRentalApplicationTests {

    @Autowired
    private ToolRentalService toolRentalService;

    /**
     * Integration test for the ToolRentalService to ensure the application context loads
     * and the checkOutTool method works as expected.
     *
     * @throws ParseException if the checkout date string cannot be parsed.
     */
    @Test
    void testCheckoutTool() throws ParseException {
        String toolCode = "JAKR";
        String checkoutDateStr = "09/03/15";
        int rentalDays = 5;
        int discountPercent = 0;

        RentalAgreement rentalAgreement = toolRentalService.checkOutTool(
                toolCode,
                rentalDays,
                discountPercent,
                checkoutDateStr);

        assertNotNull(rentalAgreement);
        assertEquals("JAKR", rentalAgreement.getToolCode());
        assertEquals("Jackhammer", rentalAgreement.getToolType());
        assertEquals("Ridgid", rentalAgreement.getToolBrand());

        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yy");
        Date checkoutDate = dateFormat.parse(checkoutDateStr);
        assertEquals(checkoutDate, rentalAgreement.getCheckoutDate());

        Date expectedDueDate = new Date(checkoutDate.getTime() + (1000 * 60 * 60 * 24 * rentalDays));
        assertEquals(expectedDueDate, rentalAgreement.getDueDate());
    }
}
