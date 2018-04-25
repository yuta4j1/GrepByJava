package com.yutaka.grep.util;

import java.util.Optional;

public class AppUtil {
    /**
     * StringをOptionalに変換する。
     *
     * @param val
     * @return
     */
    public static Optional<String> string2optional(String val) {
        return Optional.ofNullable(val);
    }

}
