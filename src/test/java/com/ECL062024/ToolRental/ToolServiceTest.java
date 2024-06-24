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

    @Test
    void testCheckoutTool() throws ParseException {
        // Test parameters for Test 1
        String toolCode = "JAKR";
        String checkoutDateStr = "09/03/15"; // Using string format as expected by the service
        int rentalDays = 5;
        int discountPercent = 0;

        // Execute checkout
        RentalAgreement rentalAgreement = toolRentalService.checkOutTool(
                toolCode,
                rentalDays,
                discountPercent,
                checkoutDateStr);

        // Assertions
        assertNotNull(rentalAgreement);
        assertEquals("JAKR", rentalAgreement.getToolCode());
        assertEquals("Jackhammer", rentalAgreement.getToolType());
        assertEquals("Ridgid", rentalAgreement.getToolBrand());

        // Format date for comparison
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yy");
        Date checkoutDate = dateFormat.parse(checkoutDateStr);

        assertEquals(checkoutDate, rentalAgreement.getCheckoutDate());
        assertEquals(new Date(checkoutDate.getTime() + (1000 * 60 * 60 * 24 * rentalDays)), rentalAgreement.getDueDate());
    }
}
