package tablestringbuilder;

import per.goweii.tablestringbuilder.TableStringBuilder;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Test {
    public static void main(String[] args) {
        TableStringBuilder tableStringBuilder = new TableStringBuilder();

        tableStringBuilder.setContentAlignment(-1);
        tableStringBuilder.setContentDivide(false);

        tableStringBuilder.setTitle("title");
        List<String> headers = new LinkedList<>();
        for (int i = 0; i < 3; i++) {
            headers.add("header-" + getRandomString());
        }
        tableStringBuilder.setHeader(headers);
        for (int i = 0; i < 10; i++) {
            List<String> content = new LinkedList<>();
            for (int j = 0; j < 3; j++) {
                content.add("content-" + getRandomString());
            }
            tableStringBuilder.addContent(content);
        }
        List<String> lines = tableStringBuilder.build();
        for (String line : lines) {
            System.out.println(line);
        }
    }

    private static final Random random = new Random();

    private static String getRandomString() {
        int size = random.nextInt(10) + 1;
        StringBuilder stringBuilder = new StringBuilder();
        while (size-- > 0) {
            stringBuilder.append(random.nextInt(10));
        }
        return stringBuilder.toString();
    }
}
