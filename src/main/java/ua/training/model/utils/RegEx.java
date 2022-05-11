package ua.training.model.utils;

public class RegEx {
    // \u0400-\u04ff
    public static final String NAME_REGEX = "[A-Z][a-z]+|[А-ЯЯЇЄЁ][а-яюєїё']+";
    public static final String SURNAME_REGEX = "[A-Z][a-z]+|[А-ЯЯЇЄЁ][а-яюєїё']+";
    public static final String USERNAME_REGEX = "^[A-Za-z][A-Za-z0-9_]{1,20}$";
    public static final String PASSWORD_REGEX = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?!.*\\s).*$";

    public static final String NOT_EMPTY = "^(?!\\s*$).+";
    public static final String HAS_NO_SPACE_ELEMENT = "^[^\\s]+$";
    public static final String AUTHOR_NAME_AND_SURNAME = "^[A-ZА-ЯЇЮЄ][a-zа-яїює]+\\s[A-ZА-ЯЇЮЄ][a-zа-яїює]+$";
}
