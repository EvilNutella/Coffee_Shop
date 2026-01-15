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
    COFFEE(1, 10, "Coffee", true),
    SUGAR(2, 2, "Sugar", false),
    SYRUP(3, 3, "Syrup", false),
    DONUT(4, 4, "Donut", false);

    public static final Map<Integer, ResourceType> RESOURCE_BY_ID =
            Arrays.stream(ResourceType.values())
                    .collect(Collectors.collectingAndThen(
                            Collectors.toMap(resource -> resource.ID, resource -> resource),
                            Collections::unmodifiableMap));

    public static final List<ResourceType> REQUIRED_RESOURCES =
            Arrays.stream(ResourceType.values())
                    .filter(ResourceType::isIS_REQUIRED)
                    .toList();

    public static final int MAX_ID =
            Arrays.stream(ResourceType.values())
                    .mapToInt(ResourceType::getID)
                    .max()
                    .orElse(0);

    public static final int MIN_PRICE =
            Arrays.stream(ResourceType.values())
                    .mapToInt(ResourceType::getPRICE)
                    .min()
                    .orElse(0);

    private final int ID;
    private final int PRICE;
    public final String DISPLAY_NAME;
    private final boolean IS_REQUIRED;

    public String toString() {
        return ID + ". " + DISPLAY_NAME + " " + PRICE + "$";
    }

    public static ResourceType getById(int id) {
        return RESOURCE_BY_ID.get(id);
    }

}


