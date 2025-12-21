package model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public enum ResourceType {
    COFFEE(1, 10, "Coffee"),
    SUGAR(2, 2, "Sugar"),
    SYRUP(3, 3, "Syrup"),
    DONUT(4, 4, "Donut");

    private static final Map<Integer, ResourceType> RESOURCE_BY_ID =
            Arrays.stream(ResourceType.values())
                    .collect(Collectors.collectingAndThen(
                            Collectors.toMap(resource -> resource.id, resource -> resource),
                            Collections::unmodifiableMap));

    private static final int MAX_ID =
            Arrays.stream(ResourceType.values())
                    .mapToInt(ResourceType::getId)
                    .max()
                    .orElse(0);

    private static final int MIN_PRICE =
            Arrays.stream(ResourceType.values())
                    .mapToInt(ResourceType::getPrice)
                    .min()
                    .orElse(0);

    private final int id;
    private final int price;
    private final String displayName;

    public static int getMAX_ID() {
        return MAX_ID;
    }

    public static int getMinPrice() {
        return MIN_PRICE;
    }

    public String toString() {
        return id + ". " + displayName + " " + price + "$";
    }

    public static ResourceType getById(int id) {
        return RESOURCE_BY_ID.get(id);
    }

}


