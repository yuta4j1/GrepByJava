package com.yutaka.grep.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileUtil {

    public static List<Path> getUnderParentPaths(String dir, String extension){

        Path parent = Paths.get(dir);
        final List<Path> strFilePaths;
        try(Stream<Path> children = Files.list(parent)){
            strFilePaths = children.filter(node -> node.toFile().isFile()).filter(f -> f.toString().endsWith(extension))
                    .map(Path::toAbsolutePath).collect(Collectors.toList());
        }catch(FileNotFoundException e){
            e.printStackTrace();
            throw new RuntimeException();
        }catch(IOException e){
            e.printStackTrace();
            throw new RuntimeException();
        }
        return strFilePaths;

    }

}
