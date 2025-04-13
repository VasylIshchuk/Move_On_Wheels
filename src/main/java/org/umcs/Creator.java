package org.umcs;

import org.umcs.models.*;

import java.time.LocalDate;
import java.util.Scanner;

public class Creator {
    private static final MenuManager menuManager = new MenuManager();
    private static final Scanner scanner = new Scanner(System.in);

    public static Vehicle createVehicleFromConsole() {
        menuManager.showGetCategoryPrompt();
        String category = scanner.nextLine();

        menuManager.showGetRegistrationNumberPrompt();
        String reg = scanner.nextLine();

        menuManager.showGetBrandPrompt();
        String brand = scanner.nextLine();

        menuManager.showGetModelPrompt();
        String model = scanner.nextLine();

        menuManager.showGetYearPrompt();
        int year = Integer.parseInt(menuManager.getNumberFromConsole());

        menuManager.showGetPricePrompt();
        double price = Double.parseDouble(menuManager.getNumberFromConsole());

        Vehicle vehicle = new Vehicle(category, reg, brand, model, year, price);
        addAttributesTo(vehicle);

        return vehicle;
    }

    private static void addAttributesTo(Vehicle vehicle) {
        do {
            if (!userWantsToAddAnotherAttribute()) return;

            menuManager.showGetAttributeNamePrompt();
            String key = scanner.nextLine();

            menuManager.showGetAttributeValuePrompt();
            Object value = scanner.nextLine();

            vehicle.addAttribute(key, value);
        } while (true);
    }

    private static boolean userWantsToAddAnotherAttribute() {
        do {
            menuManager.showAddAttributeConfirmationPrompt();
            String response = scanner.nextLine().toUpperCase();
            if (response.equals("Y")) return true;
            else if (response.equals("N")) return false;
        } while (true);
    }

    public static Rental createRentalFromConsole(User user, String vehicleId) {
        String returnDate = getReturnDate().toString();
        String currentDate = LocalDate.now().toString();
        String userId = user.getId();
        return new Rental(vehicleId, userId, currentDate, returnDate);
    }

    private static LocalDate getReturnDate() {
        LocalDate currentDate = LocalDate.now();
        menuManager.showTimeOptionsRentVehicle();
        int index = menuManager.getUserNumberFromSelection(menuManager.getTimeOptionsRentVehicle());
        if (index == 1) {
            return currentDate.plusDays(1);
        } else if (index == 2) {
            return currentDate.plusWeeks(1);
        } else if (index == 3) {
            return currentDate.plusMonths(1);
        }
        return currentDate;
    }
}
