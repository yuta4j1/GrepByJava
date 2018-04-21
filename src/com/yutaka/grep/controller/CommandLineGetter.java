package com.yutaka.grep.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

import com.yutaka.grep.model.Arguments;

/**
 * コマンドラインから引数を取得し、パラメタに設定する。
 */
public class CommandLineGetter {

    /**
     * 引数を取得し、オブジェクトに格納するメソッド。
     *
     * @param args
     * @return
     */
    public static Arguments getArguments(String[] args) {
        /* 第一引数と第二引数が入力されない場合の処理。 */
        String[] reAcquisitionArgs = null;
        /* 必須である第一引数と第二引数が取得されない場合、処理を繰り返す。 */
        while(args.length <= 1){
            reAcquisitionArgs = reGetArguments();
            args = reAcquisitionArgs;
        }
        /* オプションが設定されていなかった場合 */
        if (args.length == 2) {
            return new Arguments(args[0], args[1], null);
        }

        List<String> option = new ArrayList<>(Arrays.asList(args[2].split("")));
        /* 接頭辞の「-」を除去する */
        option.remove(0);
        return new Arguments(args[0], args[1], option);

    }

    public static String[] reGetArguments() {
        System.out.println("Please enter first argumentand second argument.");
        String args[] = null;
        try(Scanner sc = new Scanner(System.in)){
            args = sc.nextLine().split(" ");
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (IllegalStateException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        return args;
    }

}
