package net.leoninja.javagarbledverification.gradlesample;

import com.google.common.io.BaseEncoding;

import java.io.FileInputStream;
import java.nio.charset.Charset;

public class MavenSampleMain {
    public static void main(String[] args) {
        // 以下のコードは https://blog1.mammb.com/entry/2024/03/07/090000 からお借りしました
        System.out.println("Java Runtime version      :" + System.getProperty("java.runtime.version"));
        System.out.println("Charset.defaultCharset()  :" + Charset.defaultCharset());
        System.out.println("----------------------------------------------");
        System.out.println("\"file.encoding\"          = " + System.getProperty("file.encoding"));
        System.out.println("\"native.encoding\"        = " + System.getProperty("native.encoding"));
        System.out.println("\"sun.jnu.encoding\"       = " + System.getProperty("sun.jnu.encoding"));
        System.out.println("\"sun.stdout.encoding\"    = " + System.getProperty("sun.stdout.encoding"));
        System.out.println("\"sun.stderr.encoding\"    = " + System.getProperty("sun.stderr.encoding"));
        // ここまで
        System.out.println("\"stdout.encoding\"    = " + System.getProperty("stdout.encoding"));
        System.out.println("\"stderr.encoding\"    = " + System.getProperty("stderr.encoding"));
        System.out.println("----------------------------------------------");

        System.out.println("こんにちは世界。");

        String str = "あいうえお";
        System.out.println("\"" + str + "\".getByte()   = " + BaseEncoding.base16().encode(str.getBytes()));

        try {
            new FileInputStream("NOT_FOUND");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        System.out.println("----------------------------------------------");
    }
}
