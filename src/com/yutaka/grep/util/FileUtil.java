package com.yutaka.grep.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * ファイル操作Utilクラス。
 */
public class FileUtil {

    /**
     * 指定パス配下のファイル（指定した拡張子であり、ディレクトリを除く）の絶対パスを取得する。
     *
     * @param dir
     *            親パス
     * @param extension
     *            gerp対象のファイル拡張子
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static List<Path> getPaths(final String dir, final String extension)
            throws FileNotFoundException, IOException {

        Path parent = Paths.get(dir);
        List<Path> strFilePaths;
        try (Stream<Path> children = Files.list(parent)) {
            strFilePaths = children.filter(node -> node.toFile().isFile()).filter(f -> f.toString().endsWith(extension))
                    .map(Path::toAbsolutePath).collect(Collectors.toList());
        }
        return strFilePaths;

    }

    /**
     * 指定パス配下のファイル（指定した拡張子であり、ディレクトリを含む）の絶対パスを取得する。
     *
     * @param dir
     *            親パス
     * @param extension
     *            拡張子
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static List<Path> getPathsContainsDirectories(final String dir, final String extension)
            throws FileNotFoundException, IOException {

        Path parentDir = Paths.get(dir);
        List<Path> strFilePaths = null;
        try (Stream<Path> children = Files.list(parentDir)) {
            strFilePaths = children.filter(p -> {
                if (p.toFile().isDirectory()) {
                    return true;
                } else {
                    return p.toString().endsWith(extension);
                }
            }).map(Path::toAbsolutePath).collect(Collectors.toList());
        }

        return strFilePaths;
    }

}
