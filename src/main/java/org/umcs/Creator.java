package org.umcs;

import org.umcs.models.*;
import java.util.Scanner;

public class Creator {
    private static final Scanner scanner = new Scanner(System.in);

    public static Vehicle createVehicleFromConsole() {
        MenuManager.showGetCategoryPrompt();
        String category = scanner.nextLine();

        MenuManager.showGetRegistrationNumberPrompt();
        String reg = scanner.nextLine();

        MenuManager.showGetBrandPrompt();
        String brand = scanner.nextLine();

        MenuManager.showGetModelPrompt();
        String model = scanner.nextLine();

        MenuManager.showGetYearPrompt();
        int year = Integer.parseInt(MenuManager.getNumberFromConsole());

        MenuManager.showGetPricePrompt();
        double price = Double.parseDouble(MenuManager.getNumberFromConsole());

        Vehicle vehicle = new Vehicle(category, reg, brand, model, year, price);
        addAttributesTo(vehicle);

        return vehicle;
    }

    private static void addAttributesTo(Vehicle vehicle) {
        do {
            if (!userWantsToAddAnotherAttribute()) return;

            MenuManager.showGetAttributeNamePrompt();
            String key = scanner.nextLine();

            MenuManager.showGetAttributeValuePrompt();
            Object value = scanner.nextLine();

            vehicle.addAttribute(key, value);
        } while (true);
    }

    private static boolean userWantsToAddAnotherAttribute() {
        do {
            MenuManager.showAddAttributeConfirmationPrompt();
            String response = scanner.nextLine().toUpperCase();
            if (response.equals("Y")) return true;
            else if (response.equals("N")) return false;
        } while (true);
    }
}
