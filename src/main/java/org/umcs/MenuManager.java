package org.umcs;

import org.umcs.models.Vehicle;
import org.umcs.repositories.IVehicleRepository;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class MenuManager {

    public static final int ADMIN_MENU_OPTION_COUNT = 5;
    public static final int START_MENU_OPTION_COUNT = 2;
    public static final int CLIENT_MENU_OPTION_COUNT = 2;
    public static final int ROLE_MENU_OPTION_COUNT = 2;


    private static final Scanner scanner = new Scanner(System.in);

    public static void showStorageOptions() {
        System.out.println("Select role (write number):" +
                "\n\t1) Hibernate" +
                "\n\t2) JDBC" +
                "\n\t3) JSON");
    }

    public static void showStartOptions() {
        System.out.println("Select role (write number):" +
                "\n\t1) Admin" +
                "\n\t2) Client");
    }

    public static void showAdminOptions() {
        System.out.println("Select an option (write number):" +
                "\n\t1) Show the list of vehicles;" +
                "\n\t2) Show the list of rental vehicles;" +
                "\n\t3) Add a new vehicle;" +
                "\n\t4) Remove a vehicle;" +
                "\n\t5) Show the list of clients");
    }

    public static void showClientOptions() {
        System.out.println("Select an option (write number):" +
                "\n\t1) Rent vehicle" +
                "\n\t2) Return vehicle");
    }

    public static void showAuthenticationMenu() {
        System.out.println("Select an option (write number):" +
                "\n\t1) Sign in" +
                "\n\t2) Register");
    }

    public static void showLoginHeader() {
        System.out.println("Login to your account: ");
    }

    public static void showLoginPrompt() {
        System.out.print("\tLogin: ");
    }

    public static void showPasswordPrompt() {
        System.out.print("\tPassword: ");
    }

    public static void showRegisterHeader() {
        System.out.println("Register a new account:");
    }

    public static void showNewLoginPrompt() {
        System.out.print("\tNew login: ");
    }

    public static void showCreateNewPasswordPrompt() {
        System.out.print("\tNew password: ");
    }

    public static void showLoginExistsPrompt() {
        System.out.println("\tThis login already exists ;(");
    }

    public static void showInvalidLoginPrompt() {
        System.out.println("\tInvalid login :( ");
    }

    public static void showInvalidPasswordPrompt() {
        System.out.println("\tInvalid password :( ");
    }

    public static void showIdSelectionPrompt() {
        System.out.print("\tSelect ID number: ");
    }

    public static void showInvalidIdPrompt() {
        System.out.println("\tInvalid ID number :( ");
    }

    public static void showNoRentalVehiclePrompt() {
        System.out.print("This user has no rented vehicle");
    }

    public static void showGetIdPrompt() {
        System.out.print("Enter id: ");
    }

    public static void showGetCategoryPrompt() {
        System.out.print("Enter category: ");
    }

    public static void showGetRegistrationNumberPrompt() {
        System.out.print("Enter registration number: ");
    }

    public static void showGetBrandPrompt() {
        System.out.print("Enter brand: ");
    }

    public static void showGetModelPrompt() {
        System.out.print("Enter model: ");
    }

    public static void showGetYearPrompt() {
        System.out.print("Enter year: ");
    }

    public static void showGetPricePrompt() {
        System.out.print("Enter price: ");
    }

    public static void showGetAttributeNamePrompt() {
        System.out.print("\tEnter attribute name: ");
    }

    public static void showGetAttributeValuePrompt() {
        System.out.print("\tEnter attribute value: ");
    }

    public static void showAddAttributeConfirmationPrompt() {
        System.out.print("Do you want to add an additional attribute? (Y/N): ");
    }
    public static void UserAlreadyHasRentedVehicle() {
        System.out.println("You have already rented a vehicle.");
    }


    public static int getUserNumberFromSelection(int maxOptions) {
        while (true) {
            if (scanner.hasNextInt()) {
                int index = scanner.nextInt();
                if (isValidIndex(index, maxOptions)) return index;
                System.out.println("This number is out of bounds! Try again.");
            } else {
                System.out.println("This is not a number! Try again.");
                scanner.next();
            }
        }
    }

    private static boolean isValidIndex(int idx, int maxOptions) {
        return idx >= 1 && idx <= maxOptions;
    }

    public static String getNumberFromConsole() {
        while (true) {
            if (scanner.hasNextInt() || scanner.hasNextDouble()) {
                return scanner.next();
            } else {
                System.out.println("This is not a number! Try again.");
                scanner.next();
            }
        }
    }

    public static String getIdFromConsole() {
        showIdSelectionPrompt();
        return scanner.next();
    }

    public static <T> void showList(List<T> list) {
        StringBuilder stringBuffer = new StringBuilder();
        int idx = 1;
        for (T element : list) {
            stringBuffer.append(idx).append(") ").append(element).append("\n");
            idx++;
        }
        System.out.println(stringBuffer);
    }

    public static Vehicle getAvailableVehicleIdFromConsole(IVehicleRepository vehicleRepository) {
        do {
            List<Vehicle> availableVehicles = vehicleRepository.getNoRentedVehicles();
            showList(availableVehicles);

            String vehicleId = getIdFromConsole();

            Optional<Vehicle> optionalVehicle = vehicleRepository.findById(vehicleId);
            if (optionalVehicle.isPresent()) return optionalVehicle.get();
            showInvalidIdPrompt();
        } while (true);
    }
}
