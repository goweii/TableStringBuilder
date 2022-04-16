package tablestringbuilder;

import org.junit.jupiter.api.Test;
import per.goweii.tablestringbuilder.TableStringBuilder;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class TableStringBuilderTest {
    @Test
    public void testDemo() {
        TableStringBuilder tableStringBuilder = new TableStringBuilder();
        tableStringBuilder.getStyle().align.caption = 0;
        tableStringBuilder.getStyle().align.head = 0;
        tableStringBuilder.getStyle().align.body = 0;
        tableStringBuilder.setCaption("2022 January");
        List<String> head = new LinkedList<>();
        head.add("Sunday");
        head.add("Monday");
        head.add("Tuesday");
        head.add("Wednesday");
        head.add("Thursday");
        head.add("Friday");
        head.add("Saturday");
        tableStringBuilder.setHead(head);
        int offset = 6;
        int total = 31;
        int current = 0;
        while (current < total + offset - 1) {
            List<String> body = new LinkedList<>();
            while (body.size() < head.size()) {
                if (current >= offset && current <= total + offset - 1) {
                    body.add("" + (current - offset + 1));
                } else {
                    body.add("");
                }
                current++;
            }
            tableStringBuilder.addBody(body);
        }
        System.out.println(tableStringBuilder);
    }

    @Test
    public void testRandom() {
        TableStringBuilder tableStringBuilder = new TableStringBuilder();
        tableStringBuilder.setCaption("title-" + getRandomString(50));
        List<String> head = new LinkedList<>();
        for (int i = 0; i < 3; i++) {
            head.add("head-" + getRandomString(10));
        }
        tableStringBuilder.setHead(head);
        for (int i = 0; i < 10; i++) {
            List<String> body = new LinkedList<>();
            for (int j = 0; j < 3; j++) {
                body.add(getRandomString(30));
            }
            tableStringBuilder.addBody(body);
        }
        List<String> lines = tableStringBuilder.build();
        for (String line : lines) {
            System.out.println(line);
        }
    }

    private static final Random random = new Random();

    private static String getRandomString(int maxLength) {
        int size = random.nextInt(maxLength) + 1;
        StringBuilder stringBuilder = new StringBuilder();
        while (size-- > 0) {
            stringBuilder.append(random.nextInt(10));
        }
        return stringBuilder.toString();
    }
}
