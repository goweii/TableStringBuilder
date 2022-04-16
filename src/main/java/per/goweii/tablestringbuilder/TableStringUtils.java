package per.goweii.tablestringbuilder;

import java.util.List;

public class TableStringUtils {
    /**
     * 生成一行，包含左右边框
     *
     * @param texts       每个单元格的文字
     * @param widths      每个单元格的宽度
     * @param padding     每个单元格的边距
     * @param leading     行开始位置边框符号
     * @param separator   单元格分割符号
     * @param trailing    行结束位置边框符号
     * @param placeholder 单元格占位符
     * @param alignment   单元格对齐方式
     * @return 行字符串
     */
    public static String buildRowString(List<String> texts, int[] widths, int padding, char leading, char separator, char trailing, char placeholder, int alignment, boolean autoWrap) {
        StringBuilder stringBuilder = new StringBuilder();
        final int count = widths != null ? widths.length : 0;
        boolean hasMoreLine;
        int currLineNum = 0;
        do {
            hasMoreLine = false;
            if (currLineNum > 0) {
                stringBuilder.append("\n");
            }
            stringBuilder.append(leading);
            for (int i = 0; i < count; i++) {
                if (i > 0) {
                    stringBuilder.append(separator);
                }
                String text = texts != null && texts.size() > i ? texts.get(i) : null;
                if (autoWrap) {
                    int textLength = text != null ? text.length() : 0;
                    int usableWidth = Math.min(widths[i], widths[i] - padding * 2);
                    int startIndex = usableWidth * currLineNum;
                    if (textLength > 0 && textLength > startIndex) {
                        int endIndex = Math.min(textLength, startIndex + usableWidth);
                        stringBuilder.append(buildCellString(text.substring(startIndex, endIndex), widths[i], padding, placeholder, alignment));
                        if (endIndex != textLength) {
                            hasMoreLine = true;
                        }
                    } else {
                        stringBuilder.append(buildCellString(null, widths[i], padding, placeholder, alignment));
                    }
                } else {
                    stringBuilder.append(buildCellString(text, widths[i], padding, placeholder, alignment));
                }
            }
            stringBuilder.append(trailing);
            currLineNum++;
        } while (hasMoreLine);
        return stringBuilder.toString();
    }

    /**
     * 生成一个单元格，不包含边框
     *
     * @param text        单元格文字，可空
     * @param width       单元格宽度
     * @param padding     单元格左右边距
     * @param placeholder 占位符
     * @param alignment   对齐方式，小于0为左对齐，等于0为居中对齐，大于0为右对齐
     * @return 单元格字符串
     */
    public static String buildCellString(String text, int width, int padding, char placeholder, int alignment) {
        if (width <= 0) {
            return "";
        }
        int usableWidth = Math.min(width, width - padding * 2);
        int textLength = text != null ? text.length() : 0;
        StringBuilder stringBuilder = new StringBuilder();
        if (textLength > 0) {
            int diff = textLength - usableWidth;
            if (diff <= 0) {
                stringBuilder.append(text);
            } else {
                if (alignment < 0) {
                    stringBuilder.append(text, 0, usableWidth);
                } else if (alignment > 0) {
                    stringBuilder.append(text, diff, textLength);
                } else {
                    int half = diff / 2;
                    stringBuilder.append(text, half, textLength - (diff - half));
                }
            }
        }
        while (stringBuilder.length() < usableWidth) {
            if (alignment < 0) {
                stringBuilder.append(placeholder);
            } else if (alignment > 0) {
                stringBuilder.insert(0, placeholder);
            } else {
                if ((usableWidth - stringBuilder.length()) % 2 == 0) {
                    stringBuilder.insert(0, placeholder);
                } else {
                    stringBuilder.append(placeholder);
                }
            }
        }
        for (int i = 0; i < padding; i++) {
            stringBuilder.insert(0, placeholder);
            stringBuilder.append(placeholder);
        }
        return stringBuilder.toString();
    }
}
