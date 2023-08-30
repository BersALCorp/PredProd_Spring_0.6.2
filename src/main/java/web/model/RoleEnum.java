package web.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.ToString;

@ToString
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum RoleEnum {

    UNDEFINED("UNDEFINED"),
    ADMIN("ADMIN"),
    USER("USER"),
    MANAGER("MANAGER"),
    MODERATOR("MODERATOR"),
    GUEST("GUEST");


    private final String displayName;

    RoleEnum(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @JsonCreator
    public static RoleEnum fromValue(String value) {
        for (RoleEnum roleEnum : values()) {
            if (roleEnum.name().equalsIgnoreCase(value)) {
                return roleEnum;
            }
        }
        throw new IllegalArgumentException("Invalid value for SexEnum: " + value);
    }
}