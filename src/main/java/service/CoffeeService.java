package service;

import model.ResourceType;

import java.util.Map;

public interface CoffeeService {
    void addResourceInOrder(ResourceType resource);
    Map<ResourceType, Integer> getCurrentOrder();
    Map<ResourceType, Integer> getAllResources();
    void calculateRevenue();
    int getTotalOrderAmount();
    void cancelTheOrder();
    boolean hasResource(ResourceType resource);
    boolean canBuyResource(int id);

    int getSumProfit();
}
