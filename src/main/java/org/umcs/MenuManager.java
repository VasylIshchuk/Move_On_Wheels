package org.umcs;

import lombok.Getter;

import java.util.List;
import java.util.Scanner;

public class MenuManager {
    @Getter
    private final int adminMenuOptionCount = 5;
    @Getter
    private final int startMenuOptionCount = 2;
    @Getter
    private final int timeOptionsRentVehicle = 3;
    @Getter
    private final int indexRegisterOption = 2;
    private final Scanner scanner = new Scanner(System.in);

    public void showStartOptions() {
        String menuPrompt = "Select role (write number):" +
                "\n\t1) Admin" +
                "\n\t2) Client";
        System.out.println(menuPrompt);
    }

    public void showAdminOptions() {
        String menuPrompt = "Select an option (write number):" +
                "\n\t1) Show the list of vehicles;" +
                "\n\t2) Show the list of rental vehicles;" +
                "\n\t3) Add a new vehicle;" +
                "\n\t4) Remove a vehicle;" +
                "\n\t5) Show the list of clients";
        System.out.println(menuPrompt);
    }

    public void showClientOptions() {
        String menuPrompt = "Select an option (write number):" +
                "\n\t1) Rent vehicle" +
                "\n\t2) Return vehicle";
        System.out.println(menuPrompt);
    }

    public void showTimeOptionsRentVehicle() {
        String menuPrompt = "Select an option (write number):" +
                "\n\t1) 1 day" +
                "\n\t2) 1 week" +
                "\n\t3) 1 month";
        System.out.println(menuPrompt);
    }

    public void showAuthenticationMenu() {
        String authenticationMenu = "Select an option (write number):" +
                "\n\t1) Sign in" +
                "\n\t2) Register";
        System.out.println(authenticationMenu);
    }

    public void showLoginHeader() {
        System.out.println("Login to your account: ");
    }

    public void showLoginPrompt() {
        System.out.print("\tLogin: ");
    }

    public void showPasswordPrompt() {
        System.out.print("\tPassword: ");
    }

    public void showRegisterHeader() {
        System.out.println("Register a new account:");
    }

    public void showNewLoginPrompt() {
        System.out.print("\tNew login: ");
    }

    public void showCreateNewPasswordPrompt() {
        System.out.print("\tNew password: ");
    }

    public void showLoginExistsPrompt() {
        System.out.println("\tThis login already exists ;(");
    }

    public void showInvalidLoginPrompt() {
        System.out.println("\tInvalid login :( ");
    }

    public void showInvalidPasswordPrompt() {
        System.out.println("\tInvalid password :( ");
    }

    public void showIdSelectionPrompt() {
        System.out.print("\tSelect ID number: ");
    }

    public void showInvalidIdPrompt() {
        System.out.println("\tInvalid ID number :( ");
    }

    public void showNoRentalVehiclePrompt() {
        System.out.print("This user has no rented vehicle");
    }

    public void showGetIdPrompt() {
        System.out.print("Enter id: ");
    }

    public void showGetCategoryPrompt() {
        System.out.print("Enter category: ");
    }

    public void showGetRegistrationNumberPrompt() {
        System.out.print("Enter registration number: ");
    }

    public void showGetBrandPrompt() {
        System.out.print("Enter brand: ");
    }

    public void showGetModelPrompt() {
        System.out.print("Enter model: ");
    }

    public void showGetYearPrompt() {
        System.out.print("Enter year: ");
    }

    public void showGetPricePrompt() {
        System.out.print("Enter price: ");
    }

    public void showGetAttributeNamePrompt() {
        System.out.print("\tEnter attribute name: ");
    }

    public void showGetAttributeValuePrompt() {
        System.out.print("\tEnter attribute value: ");
    }

    public void showAddAttributeConfirmationPrompt() {
        System.out.print("Do you want to add an additional attribute? (Y/N): ");
    }


    public int getUserNumberFromSelection(int maxOptions) {
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

    private boolean isValidIndex(int idx, int maxOptions) {
        return idx >= 1 && idx <= maxOptions;
    }


    public String getNumberFromConsole() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            if (scanner.hasNextInt() || scanner.hasNextDouble()) {
                return scanner.next();
            } else {
                System.out.println("This is not a number! Try again.");
                scanner.next();
            }
        }
    }

    public String getIdFromConsole() {
        showIdSelectionPrompt();
        return scanner.next();
    }

    public <T> void showList(List<T> list) {
        StringBuilder stringBuffer = new StringBuilder();
        int idx = 1;
        for (T element : list) {
            stringBuffer.append(idx).append(") ").append(element).append("\n");
            idx++;
        }
        System.out.println(stringBuffer);
    }
}




