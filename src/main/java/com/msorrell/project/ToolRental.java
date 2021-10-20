package com.msorrell.project;

import com.msorrell.project.models.Checkout;
import com.msorrell.project.models.RentalAgreement;
import com.msorrell.project.services.ToolRentalService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Class to rent tools to customers.
 * Generates a Checkout from user input and generate a Rental Agreement
 */
public class ToolRental {

    public static void main(final String[] args) {
        try (final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            final ToolRentalService toolRentalService = new ToolRentalService();
            do {
                final Checkout checkout = toolRentalService.readInput(reader);
                final RentalAgreement rentalAgreement = toolRentalService.generateRentalAgreement(checkout);
                rentalAgreement.printToConsole();
            } while (toolRentalService.checkoutAgain(reader));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
