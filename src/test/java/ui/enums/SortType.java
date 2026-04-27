package ui.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SortType {

    NAME_ASC("Name A - Z"),
    NAME_DESC("Name Z - A"),
    PRICE_ASC("Price Low > High"),
    PRICE_DESC("Price High > Low");

    private final String displayText;
}
