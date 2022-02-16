package ua.training.model.utils;

public class RegEx {
    public static final String NAME_REGEX = "[A-Za-z0-9]+|[А-Яа-яЯЇЄЁяїєё']+";
    public static final String SURNAME_REGEX = "[A-Za-z0-9]+|[А-Яа-яЯЇЄЁяїєё']+";
    public static final String USERNAME_REGEX = "[A-Za-z0-9_]+";
    public static final String PASSWORD_REGEX = "[A-Za-z0-9]+|[А-Яа-яЯЇЄЁяїєё']+";

    public static final String NOT_EMPTY = "^(?!\\s*$).+";
    public static final String HAS_NO_SPACE_ELEMENT = "^[^\\s]+$";
    public static final String AUTHOR_NAME_AND_SURNAME = "^[A-ZА-ЯЇЮЄ][a-zа-яїює]+\\s[A-ZА-ЯЇЮЄ][a-zа-яїює]+$";
}
