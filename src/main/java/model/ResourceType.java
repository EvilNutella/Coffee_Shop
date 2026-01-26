package model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public enum ResourceType {
    COFFEE(1, 10, "Coffee", true, 6),
    SUGAR(2, 2, "Sugar", false),
    SYRUP(3, 3, "Syrup", false),
    DONUT(4, 4, "Donut", false);

    public static final Map<Integer, ResourceType> RESOURCE_BY_ID =
            Arrays.stream(ResourceType.values())
                    .collect(Collectors.collectingAndThen(
                            Collectors.toMap(resource -> resource.id, resource -> resource),
                            Collections::unmodifiableMap));

    public static final List<ResourceType> REQUIRED_RESOURCES =
            Arrays.stream(ResourceType.values())
                    .filter(ResourceType::isRequired)
                    .toList();

    public static final int MAX_ID =
            Arrays.stream(ResourceType.values())
                    .mapToInt(ResourceType::getId)
                    .max()
                    .orElse(0);

    public static final int MIN_PRICE =
            Arrays.stream(ResourceType.values())
                    .mapToInt(ResourceType::getPrice)
                    .min()
                    .orElse(0);

    public static final int MIN_PURCHASE_PRICE =
            Arrays.stream(ResourceType.values())
                    .mapToInt(ResourceType::getPurchasePrice)
                    .min()
                    .orElse(0);

    private final int id;
    private final int price;
    private final String displayName;
    private final boolean isRequired;
    private final int purchasePrice;

    public String toString() {
        return id + ". " + displayName + " " + price + "$";
    }

    public static ResourceType getById(int id) {
        return RESOURCE_BY_ID.get(id);
    }

    ResourceType(int id, int price, String displayName, boolean isRequired) {
        this(id, price, displayName, isRequired, (price - 1));
    }
}




