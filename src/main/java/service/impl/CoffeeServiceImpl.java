package service.impl;

import model.ResourceType;
import service.CoffeeService;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;


public class CoffeeServiceImpl implements CoffeeService {
    private final int QUANTITY_OF_RESOURCES_AT_START = 4;

    private Map<ResourceType, Integer> orderQuantityByType = new HashMap<>();
    private Map<ResourceType, Integer> storageQuantityByType;

    private int sumProfit = 15;
    private int totalOrderAmount = 0;

    public CoffeeServiceImpl() {
        storageQuantityByType = Arrays.stream(ResourceType.values())
                .collect(Collectors.toMap(
                        Function.identity(),
                        type -> QUANTITY_OF_RESOURCES_AT_START
                ));
    }

    public int getSumProfit() {
        return sumProfit;
    }

    @Override
    public boolean hasResource(ResourceType resource) {
        int currentQuantityAtStorage = storageQuantityByType.get(resource);
        return currentQuantityAtStorage > 0;
    }

    @Override
    public boolean canBuyResource(int id) {
        ResourceType resource = ResourceType.getById(id);
        int purchasePrice = resource.getPurchasePrice();
        boolean wasResourcePurchased = sumProfit >= purchasePrice;

        if (wasResourcePurchased) {
            int currentQuantityAtStorage = storageQuantityByType.getOrDefault(resource, 0);
            storageQuantityByType.put(resource, currentQuantityAtStorage + 1);
            sumProfit -= purchasePrice;
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
    public List<ResourceType> getMissingRequiredResources() {
        return ResourceType.REQUIRED_RESOURCES
                .stream()
                .filter(resourceType -> orderQuantityByType.getOrDefault(resourceType, 0) <= 0)
                .toList();
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
        clearTheOrder();
    }

    @Override
    public int getTotalOrderAmount() {
        return totalOrderAmount;
    }

    private void clearTheOrder() {
        orderQuantityByType.clear();
        totalOrderAmount = 0;
    }

    @Override
    public void cancelTheOrder() {
        orderQuantityByType.forEach((resourceType, quantity) -> {
            int quantityAtStorageAfterReturns = storageQuantityByType.get(resourceType) + quantity;
            storageQuantityByType.put(resourceType, quantityAtStorageAfterReturns);
        });
        clearTheOrder();
    }
}
