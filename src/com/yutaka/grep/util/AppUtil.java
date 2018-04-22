package com.yutaka.grep.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.Properties;

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

    /**
     * プロパティファイルの読み込み。
     *
     * @return
     */
    public static Properties readProperty() {
        Path path = Paths.get("/resources/app.properties");
        try(BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)){
            Properties property = new Properties();
            property.load(reader);
            return property;

        }catch(FileNotFoundException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }catch(IOException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }catch(Exception e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

}
