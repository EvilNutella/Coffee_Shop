package model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ResourceType {
    COFFEE(10),
    SUGAR(2),
    SYRUP(3),
    DONUT(4);

    private final int price;
}


