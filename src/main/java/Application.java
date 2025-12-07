import model.ResourceType;
import service.CoffeeService;
import service.CoffeeServiceImpl;

import java.util.Map;
import java.util.Scanner;

public class Application {
    public static CoffeeService coffeeService = new CoffeeServiceImpl();
    private static boolean isThatNotAll = true;

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to our cafe!");

        do {
            printCurrentOrderAndTotalAmount();
            System.out.println("Would you like to add something in order? Yes/No");
            if (scanner.nextLine().equalsIgnoreCase("Yes")) {
                suggestAnAction(scanner);
            } else {
                System.out.println("Do you want to: \n1. Confirm order. \n2. Run as administrator. ");
                if (scanner.hasNextInt()) {
                    int numberOfAction = scanner.nextInt();
                    scanner.nextLine();

                    if (numberOfAction == 1) {
                        printCurrentOrderAndTotalAmount();
                        confirmOrder(scanner);
                        isThatNotAll = false;
                    }
                    if (numberOfAction == 2) {
                        int action = runAsAdmin(scanner);
                        processingAdminActions(action, scanner);
                    }

                    if (numberOfAction < 1 || numberOfAction > 2) {
                        System.out.println("Please, only number 1 or 2");
                    }
                } else {
                    System.out.println("Please, only number 1 or 2");
                }
            }
        } while (isThatNotAll);
    }

    public static void suggestExistResources() {
        if (coffeeService.hasResource(ResourceType.COFFEE)) {
            System.out.println("1. COFFEE;");
        }

        if (coffeeService.hasResource(ResourceType.SUGAR)) {
            System.out.println("2. SUGAR;");
        }

        if (coffeeService.hasResource(ResourceType.SYRUP)) {
            System.out.println("3. SYRUP;");
        }

        if (coffeeService.hasResource(ResourceType.DONUT)) {
            System.out.println("4. DONUT;");
        }
    }

    public static int runAsAdmin(Scanner scanner) {
        System.out.println("What would you like to do as administrator?");
        System.out.println("1. Show all available resources in stock; \n" +
                "2. Purchase resources. \n" +
                "3. Back.");

        int numberOfActionForAdmin = 0;

        if (scanner.hasNextInt()) {
            numberOfActionForAdmin = scanner.nextInt();
            scanner.nextLine();

            if (numberOfActionForAdmin < 1 || numberOfActionForAdmin > 3) {
                System.out.println("Enter only number from 1 to 3, please!");
            }
        } else {
            System.out.println("Enter only number from 1 to 3, please!");
            scanner.nextLine();
        }
        return numberOfActionForAdmin;
    }

    public static void processingAdminActions(int action, Scanner scanner) {
        switch (action) {
            case 1 -> {
                printAllResource();
                int newAction = runAsAdmin(scanner);
                processingAdminActions(newAction, scanner);
            }
            case 2 -> {
                purchaseResources(scanner);
                int newAction = runAsAdmin(scanner);
                processingAdminActions(newAction, scanner);
            }
            case 3 -> suggestAnAction(scanner);
        }
    }

    public static void purchaseResources(Scanner scanner) {
        int sumAtStorage = coffeeService.getSumProfit();
        int minPriceForResource = ResourceType.SUGAR.getPrice();

        if (sumAtStorage > minPriceForResource) {
            System.out.println("What resource needs to be purchased? \n" +
                    "1. Coffee\n" +
                    "2. Sugar\n" +
                    "3. Syrup\n" +
                    "4. Donut\n" +
                    "5. That's all, thank you.");
            int numberOfResource = inputValidationFrom1To5(scanner);

            switch (numberOfResource) {
                case 1 -> coffeeService.buyResource(ResourceType.COFFEE);
                case 2 -> coffeeService.buyResource(ResourceType.SUGAR);
                case 3 -> coffeeService.buyResource(ResourceType.SYRUP);
                case 4 -> coffeeService.buyResource(ResourceType.DONUT);
                case 5 -> suggestAnAction(scanner);
            }
        } else {
            System.out.println("We have no money :(");
        }
    }

    public static int inputValidationFrom1To5(Scanner scanner) {
        int numberOfAction = 0;

        if (scanner.hasNextInt()) {
            numberOfAction = scanner.nextInt();
            scanner.nextLine();

            if (numberOfAction < 1 || numberOfAction > 5) {
                System.out.println("Enter only number from 1 to 5, please!");
                return inputValidationFrom1To5(scanner);
            }
        } else {
            System.out.println("Enter only number from 1 to 5, please!");
            scanner.nextLine();
        }
        return numberOfAction;
    }

    public static void suggestAnAction(Scanner scanner) {
        System.out.println("What would you like to add to your order?");
        suggestExistResources();
        System.out.println("5. That's all, thank you.");
        int numberOfAction = inputValidationFrom1To5(scanner);
        processSupplement(numberOfAction, scanner);
    }


    private static void processSupplement(int action, Scanner scanner) {
        switch (action) {
            case 1 -> coffeeService.addResourceInOrder(ResourceType.COFFEE);
            case 2 -> coffeeService.addResourceInOrder(ResourceType.SUGAR);
            case 3 -> coffeeService.addResourceInOrder(ResourceType.SYRUP);
            case 4 -> coffeeService.addResourceInOrder(ResourceType.DONUT);
            case 5 -> {
                isThatNotAll = false;
                printCurrentOrderAndTotalAmount();
                confirmOrder(scanner);
            }
        }
    }

    private static void printCurrentOrderAndTotalAmount() {
        Map<ResourceType, Integer> currentOrderQuantityByType = coffeeService.getCurrentOrder();
        System.out.println("Currently on order: \n");

        currentOrderQuantityByType.forEach((resourceType, integer) -> {
            System.out.println(resourceType + " x " + integer);
            System.out.println("Total: " + coffeeService.getTotalOrderAmount() + " $");
        });
    }

    private static void printAllResource() {
        Map<ResourceType, Integer> ResourceAtStorageQuantityByType = coffeeService.getAllResources();

        System.out.println("Currently at storage: \n");
        ResourceAtStorageQuantityByType.forEach((resourceType, quantity) -> {
            System.out.println(resourceType + " x " + quantity);
        });

        System.out.println("Money in the cash register: " + coffeeService.getSumProfit() + "\n");
    }

    public static void confirmOrder(Scanner scanner) {
        System.out.println("Confirm order? Yes/No");

        String answer = scanner.nextLine();

        if (answer.equalsIgnoreCase("yes")) {
            printCurrentOrderAndTotalAmount();
            coffeeService.calculateRevenue();
            System.out.println("The order is confirmed!");
        } else if (answer.equalsIgnoreCase("no")) {
            coffeeService.cancelTheOrder();
            System.out.println("The order has been cancelled.");
        } else {
            System.out.println("Only \"yes\" or \"no\", please.");
            confirmOrder(scanner);
        }
    }
}
