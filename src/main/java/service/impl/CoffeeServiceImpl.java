package service.impl;

import model.ResourceType;
import service.CoffeeService;

import java.util.HashMap;
import java.util.Map;


public class CoffeeServiceImpl implements CoffeeService {

    public int getSumProfit() {
        return sumProfit;
    }

    private int sumProfit = 15;
    private int totalOrderAmount = 10;

    Map<ResourceType, Integer> orderQuantityByType = new HashMap<>();
    Map<ResourceType, Integer> storageQuantityByType = new HashMap<>();

    public CoffeeServiceImpl() {
        orderQuantityByType.put(ResourceType.COFFEE, 1);
        storageQuantityByType.put(ResourceType.COFFEE, 3);
        storageQuantityByType.put(ResourceType.SYRUP, 4);
        storageQuantityByType.put(ResourceType.SUGAR, 4);
        storageQuantityByType.put(ResourceType.DONUT, 4);
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
