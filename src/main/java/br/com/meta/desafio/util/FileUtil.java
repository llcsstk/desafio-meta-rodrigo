package br.com.meta.desafio.util;

import java.math.BigDecimal;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;

public class FileUtil {
    public static Integer extractFileLines(final String linesInfo) {
        String[] infos = linesInfo.split(" ");

        for (int i = 0; i < infos.length; i++) {
            if (infos[i].equals("lines")) {
                return Integer.parseInt(infos[i - 1]);
            }
        }

        return 0;
    }

    public static BigDecimal extractFileSizeInBytes(final String linesInfo) {
        String[] infos = linesInfo.split(" ");

        BigDecimal fileSize = new BigDecimal(infos[infos.length - 2]);
        String fileSizeNom = infos[infos.length - 1].toLowerCase();

        switch (fileSizeNom) {
            case "kb":
                fileSize = fileSize.multiply(new BigDecimal(1000));
                break;
            case "mb":
                fileSize = fileSize.multiply(new BigDecimal(1000000));
                break;
            case "gb":
                fileSize = fileSize.multiply(new BigDecimal(1000000000));
                break;
        }

        return fileSize;
    }

    public static String convertFileSizeToString(long bytes) {
        if (-1000 < bytes && bytes < 1000) {
            return bytes + " B";
        }
        CharacterIterator ci = new StringCharacterIterator("kMGTPE");
        while (bytes <= -999_950 || bytes >= 999_950) {
            bytes /= 1000;
            ci.next();
        }
        return String.format("%.1f %cB", bytes / 1000.0, ci.current());
    }
}
