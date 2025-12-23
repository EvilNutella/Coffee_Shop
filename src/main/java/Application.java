import model.ResourceType;
import service.CoffeeService;
import service.impl.CoffeeServiceImpl;

import java.util.Map;
import java.util.Scanner;

public class Application {
    private static final int MAX_ID_OF_RESOURCES_PLUS_ONE = ResourceType.getMAX_ID() + 1;

    public static CoffeeService coffeeService = new CoffeeServiceImpl();
    private static boolean isThatNotAll = true;


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to our cafe!");

        do {
            printCurrentOrderAndTotalAmount();
            System.out.println("Would you like to add something in order? Yes/No");

            String userInput = scanner.nextLine();
            if (userInput.equalsIgnoreCase("Yes")) {
                suggestAnAction(scanner);
            } else if (userInput.equalsIgnoreCase("No")) {
                showAfterStartMenu(scanner);
            } else {
                printAMessageAboutIncorrectInput();
            }
        } while (isThatNotAll);
    }

    public static void showAfterStartMenu(Scanner scanner) {
        boolean needToRepeat;
        do {
            needToRepeat = false;
            System.out.println("Do you want to: \n" +
                    "1. Confirm order. \n" +
                    "2. Run as administrator. \n" +
                    "3. Thank you, I have to go.");

            if (scanner.hasNextInt()) {
                int numberOfAction = scanner.nextInt();
                scanner.nextLine();

                switch (numberOfAction) {
                    case 1 -> {
                        printCurrentOrderAndTotalAmount();
                        confirmOrder(scanner);
                    }
                    case 2 -> {
                        int action = runAsAdmin(scanner);
                        processingAdminActions(action, scanner);
                    }
                    case 3 -> {
                        System.out.println("Have a good day!");
                        isThatNotAll = false;
                    }
                    default -> {
                        printAMessageAboutIncorrectInput();
                        needToRepeat = true;
                    }
                }
            } else {
                printAMessageAboutIncorrectInput();
                scanner.nextLine();
                needToRepeat = true;
            }
        } while (needToRepeat);
    }

    public static void suggestExistResources() {
        for (int id = 1; id < MAX_ID_OF_RESOURCES_PLUS_ONE; id++) {
            ResourceType resource = ResourceType.getById(id);
            if (coffeeService.hasResource(resource)) {
                System.out.println(resource);
            }
        }
    }

    public static int runAsAdmin(Scanner scanner) {
        boolean needToRepeat;
        int numberOfActionForAdmin = 0;

        do {
            needToRepeat = false;
            System.out.println("What would you like to do as administrator?");
            System.out.println("1. Show all available resources in stock; \n" +
                    "2. Purchase resources. \n" +
                    "3. Back.");

            if (scanner.hasNextInt()) {
                numberOfActionForAdmin = scanner.nextInt();
                scanner.nextLine();
            } else {
                printAMessageAboutIncorrectInput();
                scanner.nextLine();
                needToRepeat = true;
            }
        } while (needToRepeat);
        return numberOfActionForAdmin;
    }

    public static void printAMessageAboutIncorrectInput() {
        System.out.println("Please, only the answers given." + "\n");
    }

    public static void processingAdminActions(int action, Scanner scanner) {
        boolean needToRepeat = true;

        do {
            switch (action) {
                case 1 -> {
                    printAllResource();
                    action = runAsAdmin(scanner);
                }
                case 2 -> {
                    purchaseResources(scanner);
                    action = runAsAdmin(scanner);
                }
                case 3 -> {
                    suggestAnAction(scanner);
                    needToRepeat = false;
                }
                default -> {
                    printAMessageAboutIncorrectInput();
                    action = runAsAdmin(scanner);
                }
            }
        } while (needToRepeat);
    }

    public static void purchaseResources(Scanner scanner) {
        boolean needToRepeat;

        do {
            needToRepeat = false;
            int sumAtStorage = coffeeService.getSumProfit();
            int minPriceForResource = ResourceType.getMinPrice();

            printAllResource();

            if (sumAtStorage > minPriceForResource) {
                System.out.println("What resource needs to be purchased?");

                for (int id = 1; id < MAX_ID_OF_RESOURCES_PLUS_ONE; id++) {
                    ResourceType resource = ResourceType.getById(id);
                    if (coffeeService.hasResource(resource)) {
                        System.out.println(resource + ", purchase price: " + (resource.getPrice() - 1) + "$");
                    }
                }
                System.out.println(MAX_ID_OF_RESOURCES_PLUS_ONE + ". That's all, thank you.");
                int numberOfResource = inputValidationFrom1ToMaxId(scanner);

                if (numberOfResource < MAX_ID_OF_RESOURCES_PLUS_ONE) {
                    if (coffeeService.canBuyResource(numberOfResource)) {
                        System.out.println("The resource has been purchased! \n");
                    } else {
                        System.out.println("We have no money for this :( \n");
                    }
                    needToRepeat = true;
                } else {
                    suggestAnAction(scanner);
                }
            } else {
                System.out.println("We have no money for this :( \n");
            }
        } while (needToRepeat);
    }


    public static int inputValidationFrom1ToMaxId(Scanner scanner) {
        boolean isValid = false;
        int number = 0;

        while (!isValid) {
            if (scanner.hasNextInt()) {
                number = scanner.nextInt();
                scanner.nextLine();

                if (number >= 1 && number <= MAX_ID_OF_RESOURCES_PLUS_ONE) {
                    isValid = true;
                } else {
                    printAMessageAboutIncorrectInput();
                }
            } else {
                printAMessageAboutIncorrectInput();
                scanner.nextLine();
            }
        }

        return number;
    }

    public static void suggestAnAction(Scanner scanner) {
        System.out.println("What would you like to add to your order?");
        suggestExistResources();
        System.out.println(MAX_ID_OF_RESOURCES_PLUS_ONE + ". That's all, thank you.");
        int numberOfAction = inputValidationFrom1ToMaxId(scanner);
        processSupplement(numberOfAction, scanner);
    }

    private static void processSupplement(int action, Scanner scanner) {
        if (action < MAX_ID_OF_RESOURCES_PLUS_ONE) {
            coffeeService.addResourceInOrder(ResourceType.getById(action));
        } else {
            isThatNotAll = false;
            printCurrentOrderAndTotalAmount();
            confirmOrder(scanner);
        }
    }

    private static void printCurrentOrderAndTotalAmount() {
        Map<ResourceType, Integer> currentOrderQuantityByType = coffeeService.getCurrentOrder();
        System.out.println("Currently on order: \n");

        currentOrderQuantityByType.forEach((resourceType, integer) -> {
            System.out.println(resourceType + " x " + integer);
        });

        System.out.println("Total: " + coffeeService.getTotalOrderAmount() + " $");
    }

    private static void printAllResource() {
        Map<ResourceType, Integer> ResourceAtStorageQuantityByType = coffeeService.getAllResources();

        System.out.println("Currently at storage: \n");
        ResourceAtStorageQuantityByType.forEach((resourceType, quantity) -> {
            System.out.println(resourceType + " x " + quantity);
        });

        System.out.println("Money in the cash register: " + coffeeService.getSumProfit() + " $" + "\n");
    }

    public static void confirmOrder(Scanner scanner) {
        boolean needToRepeat;

        do {
            needToRepeat = false;
            System.out.println("Confirm order? Yes/No");
            String answer = scanner.nextLine();

            if (answer.equalsIgnoreCase("yes")) {
                printCurrentOrderAndTotalAmount();
                coffeeService.calculateRevenue();
                System.out.println("The order is confirmed! \n");

            } else if (answer.equalsIgnoreCase("no")) {
                coffeeService.cancelTheOrder();
                System.out.println("The order has been cancelled.");
            } else {
                System.out.println("Only \"yes\" or \"no\", please.");
                needToRepeat = true;
            }
        } while (needToRepeat);
    }
}
