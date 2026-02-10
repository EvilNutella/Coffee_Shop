import model.ResourceType;
import service.CoffeeService;
import service.impl.CoffeeServiceImpl;

import java.util.Comparator;
import java.util.Map;
import java.util.Scanner;

public class Application {
    private static final int MAX_ID_OF_RESOURCES_PLUS_ONE = ResourceType.MAX_ID + 1;
    private static final Scanner SCANNER = new Scanner(System.in);
    private static final int RUN_AS_ADMIN_MENU_NUMBER_OF_OPTIONS = 3;
    private static final int ROLE_SELECTION_MENU_NUMBER_OF_OPTIONS = 3;

    private static CoffeeService coffeeService = new CoffeeServiceImpl();
    private static boolean isThatNotAll = true;


    public static void main(String[] args) {
        System.out.println("Welcome to our cafe!");

        do {
            showRoleSelectionMenu();
        } while (isThatNotAll);
    }

    private static void makeTheOrder() {
        boolean continueOrdering = true;

        addMissingRequiredResources();

        while (continueOrdering) {
            printCurrentOrderAndTotalAmount();
            System.out.println("Would you like to add something in order? Yes/No");
            String userInput = getUserInput();

            if (userInput.equalsIgnoreCase("Yes")) {
                suggestAnAction();
            } else if (userInput.equalsIgnoreCase("No")) {
                confirmOrder();
                continueOrdering = false;
            } else {
                printAMessageAboutIncorrectInput();
            }
        }
    }

    private static void addMissingRequiredResources() {
        coffeeService.getMissingRequiredResources()
                .forEach(resource -> {
                    if (coffeeService.hasResource(resource)) {
                        coffeeService.addResourceInOrder(resource);
                    } else {
                        System.out.println("Sorry, we're out of " + resource.getDisplayName()
                                .toLowerCase() + "!");
                    }
                });

    }

    private static String getUserInput() {
        String userInput = SCANNER.nextLine();
        System.out.println();
        return userInput;
    }

    private static void showRoleSelectionMenu() {
        while (isThatNotAll) {
            System.out.println("Do you want to: \n" +
                    "1. Make the order. \n" +
                    "2. Run as administrator. \n" +
                    "3. Thank you, I have to go.");

            int numberOfAction = getValidIntInput(ROLE_SELECTION_MENU_NUMBER_OF_OPTIONS);

            switch (numberOfAction) {
                case 1 -> makeTheOrder();
                case 2 -> processingAdminActions();
                case 3 -> {
                    System.out.println("Have a good day!");
                    isThatNotAll = false;
                }
            }
        }
    }

    private static void suggestExistResources() {
        for (int id = 1; id < MAX_ID_OF_RESOURCES_PLUS_ONE; id++) {
            ResourceType resource = ResourceType.getById(id);
            if (coffeeService.hasResource(resource)) {
                System.out.println(resource);
            }
        }
    }

    private static int runAsAdmin() {
        System.out.println("What would you like to do as administrator?");
        System.out.println("1. Show all available resources in stock; \n" +
                "2. Purchase resources. \n" +
                "3. Back.");

        return getValidIntInput(RUN_AS_ADMIN_MENU_NUMBER_OF_OPTIONS);
    }

    private static void processingAdminActions() {
        boolean keepRunning = true;

        while (keepRunning) {
            int action = runAsAdmin();

            switch (action) {
                case 1 -> printAllResource();
                case 2 -> purchaseResources();
                case 3 -> keepRunning = false;
            }
        }
    }

    private static void purchaseResources() {
        boolean needToRepeat;

        do {
            needToRepeat = false;
            int sumAtStorage = coffeeService.getSumProfit();

            printAllResource();

            if (sumAtStorage >= ResourceType.MIN_PURCHASE_PRICE) {
                System.out.println("What resource needs to be purchased?");

                for (int id = 1; id < MAX_ID_OF_RESOURCES_PLUS_ONE; id++) {
                    ResourceType resource = ResourceType.getById(id);
                    System.out.println(resource + ", purchase price: " + resource.getPurchasePrice() + "$");
                }
                System.out.println(MAX_ID_OF_RESOURCES_PLUS_ONE + ". That's all, thank you.");
                int numberOfResource = getValidIntInput(MAX_ID_OF_RESOURCES_PLUS_ONE);

                if (numberOfResource < MAX_ID_OF_RESOURCES_PLUS_ONE) {
                    ResourceType resource = ResourceType.getById(numberOfResource);
                    if (coffeeService.buyResource(resource)) {
                        System.out.println("The resource has been purchased!");
                    } else {
                        System.out.println("We have no money for this :(");
                    }
                    needToRepeat = true;
                }
            } else {
                System.out.println("We have no money for this :(");
            }
        } while (needToRepeat);
    }


    private static int getValidIntInput(int max) {
        int input = 0;
        boolean isValid = false;

        while (!isValid) {
            if (SCANNER.hasNextInt()) {
                input = SCANNER.nextInt();
                getUserInput();

                if (input >= 1 && input <= max) {
                    isValid = true;
                } else {
                    printAMessageAboutIncorrectInput();
                }
            } else {
                getUserInput();
                printAMessageAboutIncorrectInput();
            }
        }
        return input;
    }

    private static void suggestAnAction() {
        System.out.println("What would you like to add to your order?");
        suggestExistResources();
        System.out.println(MAX_ID_OF_RESOURCES_PLUS_ONE + ". That's all, thank you.");

        int numberOfAction = getValidIntInput(MAX_ID_OF_RESOURCES_PLUS_ONE);
        processSupplement(numberOfAction);
    }

    private static void processSupplement(int action) {
        if (action < MAX_ID_OF_RESOURCES_PLUS_ONE) {
            coffeeService.addResourceInOrder(ResourceType.getById(action));
            System.out.println("Added successfully!");
        }
    }

    private static void confirmOrder() {
        boolean needToRepeat;

        printCurrentOrderAndTotalAmount();
        do {
            needToRepeat = false;
            System.out.println("Confirm order? Yes/No");
            String answer = getUserInput();

            if (answer.equalsIgnoreCase("yes")) {
                coffeeService.calculateRevenue();
                System.out.println("The order is confirmed!");

            } else if (answer.equalsIgnoreCase("no")) {
                coffeeService.cancelTheOrder();
                System.out.println("The order has been cancelled.");

            } else {
                System.out.println("Only \"yes\" or \"no\", please.");
                needToRepeat = true;
            }
        } while (needToRepeat);
    }

    private static void printAMessageAboutIncorrectInput() {
        System.out.println("Please, only the answers given.");
    }

    private static void printCurrentOrderAndTotalAmount() {
        Map<ResourceType, Integer> currentOrderQuantityByType = coffeeService.getCurrentOrder();

        System.out.println("Currently on order: ");
        printListOfResource(currentOrderQuantityByType);

        System.out.println("Total: " + coffeeService.getTotalOrderAmount() + " $");
    }

    private static void printListOfResource(Map<ResourceType, Integer> resourcesQuantityByType) {
        resourcesQuantityByType.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey(Comparator.comparing(ResourceType::getId)))
                .forEach(entry -> System.out.println(entry.getKey() + " x " + entry.getValue()));
    }

    private static void printAllResource() {
        Map<ResourceType, Integer> ResourceAtStorageQuantityByType = coffeeService.getAllResources();

        System.out.println("Currently at storage: ");
        printListOfResource(ResourceAtStorageQuantityByType);

        System.out.println("Money in the cash register: " + coffeeService.getSumProfit() + " $");
    }
}