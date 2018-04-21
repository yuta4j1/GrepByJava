package com.yutaka.grep.util;

import java.util.Optional;

public class AppUtil {
    public static Optional<String> string2optional(String val) {
        return Optional.ofNullable(val);
    }

}
