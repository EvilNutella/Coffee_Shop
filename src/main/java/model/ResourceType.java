package model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

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

    private static final Map<Integer, ResourceType> IDBYRESOURCE = new HashMap<>();
    private static final Map<ResourceType, Integer> RESOURCEBYPRICE = new HashMap<>();

    static {
        for (ResourceType resource : values()) {
            IDBYRESOURCE.put(resource.id, resource);
            RESOURCEBYPRICE.put(resource, resource.price);
        }
    }

    public static ResourceType getById(int id) {
        return IDBYRESOURCE.get(id);
    }

    public static int getMinPrice() {
        Integer minPrice = RESOURCEBYPRICE.values().stream()
                .min(Integer::compareTo)
                .orElse(null);

        return minPrice;
    }
}


