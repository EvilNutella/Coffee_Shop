package model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Getter
@RequiredArgsConstructor
public enum ResourceType {
    COFFEE(1, 10, "Coffee"),
    SUGAR(2, 2, "Sugar"),
    SYRUP(3, 3, "Syrup"),
    DONUT(4, 4, "Donut");

    private final int id;
    private final int price;
    private final String displayName;

    private static final Map<Integer, ResourceType> RESOURCE_BY_ID = new HashMap<>();

    public String toString() {
        return id + ". " + displayName + " " + price + "$";
    }

    static {
        for (ResourceType resource : values()) {
            RESOURCE_BY_ID.put(resource.id, resource);
        }
    }

    public static ResourceType getById(int id) {
        return RESOURCE_BY_ID.get(id);
    }

    public static int getMaxId() {
        int maxId =
                Arrays.stream(ResourceType.values())
                        .mapToInt(ResourceType::getId)
                        .max()
                        .orElse(0);

        return maxId;
    }

    public static int getMinPrice() {
        int minPrice =
                Arrays.stream(ResourceType.values())
                        .mapToInt(ResourceType::getPrice)
                        .min()
                        .orElse(0);

        return minPrice;
    }
}


