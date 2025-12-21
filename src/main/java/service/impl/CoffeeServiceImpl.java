package service.impl;

import model.ResourceType;
import service.CoffeeService;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


public class CoffeeServiceImpl implements CoffeeService {

    public int getSumProfit() {
        return sumProfit;
    }

    private int sumProfit = 15;
    private int totalOrderAmount = 0;
    private final int QUANTITY_OF_RESOURCES_AT_START = 4;

    Map<ResourceType, Integer> orderQuantityByType = new HashMap<>();
    Map<ResourceType, Integer> storageQuantityByType = new HashMap<>();

    public CoffeeServiceImpl() {
        Arrays.stream(ResourceType.values())
                .forEach(type -> storageQuantityByType.put(type, QUANTITY_OF_RESOURCES_AT_START));

        addResourceInOrder(ResourceType.COFFEE);
    }

    @Override
    public boolean hasResource(ResourceType resource) {
        int currentQuantityInOrder = orderQuantityByType.getOrDefault(resource, 0);
        int currentQuantityAtStorage = storageQuantityByType.get(resource);
        return currentQuantityInOrder <= currentQuantityAtStorage;
    }

    @Override
    public boolean canBuyResource(int id) {
        boolean wasResourcePurchased = false;

        ResourceType resource = ResourceType.getById(id);

        if (sumProfit > resource.getPrice()) {
            int currentQuantityAtStorage = storageQuantityByType.getOrDefault(resource, 0);
            storageQuantityByType.put(resource, currentQuantityAtStorage + 1);
            sumProfit -= resource.getPrice();
            wasResourcePurchased = true;
        }
        return wasResourcePurchased;
    }

    @Override
    public void addResourceInOrder(ResourceType resource) {
        if (hasResource(resource)) {
            int currentQuantityInOrder = orderQuantityByType.getOrDefault(resource, 0);
            orderQuantityByType.put(resource, currentQuantityInOrder + 1);

            int currentQuantityAtStorage = storageQuantityByType.getOrDefault(resource, 0);
            storageQuantityByType.put(resource, currentQuantityAtStorage - 1);

            totalOrderAmount += resource.getPrice();
        }
    }

    @Override
    public Map<ResourceType, Integer> getCurrentOrder() {
        return Map.copyOf(orderQuantityByType);
    }

    @Override
    public Map<ResourceType, Integer> getAllResources() {
        return Map.copyOf(storageQuantityByType);
    }

    @Override
    public void calculateRevenue() {
        sumProfit += totalOrderAmount;
    }

    @Override
    public int getTotalOrderAmount() {
        return totalOrderAmount;
    }

    @Override
    public void cancelTheOrder() {
        orderQuantityByType.forEach((resourceType, quantity) -> {

            int quantityAtStorageAfterReturns = storageQuantityByType.get(resourceType) + quantity;
            storageQuantityByType.put(resourceType, quantityAtStorageAfterReturns);
        });
    }
}
