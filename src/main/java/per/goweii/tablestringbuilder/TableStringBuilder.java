package per.goweii.tablestringbuilder;

import java.util.*;

import static per.goweii.tablestringbuilder.TableStringUtils.*;

/**
 * 文本表格生成工具
 * <p>
 * ┌──────────────────────────────────────────────────────────────────────┐
 * │                             2022 January                             │
 * ├────────┬────────┬─────────┬───────────┬──────────┬────────┬──────────┤
 * │ Sunday │ Monday │ Tuesday │ Wednesday │ Thursday │ Friday │ Saturday │
 * ├────────┼────────┼─────────┼───────────┼──────────┼────────┼──────────┤
 * │        │        │         │           │          │        │        1 │
 * ├────────┼────────┼─────────┼───────────┼──────────┼────────┼──────────┤
 * │      2 │      3 │       4 │         5 │        6 │      7 │        8 │
 * ├────────┼────────┼─────────┼───────────┼──────────┼────────┼──────────┤
 * │      9 │     10 │      11 │        12 │       13 │     14 │       15 │
 * ├────────┼────────┼─────────┼───────────┼──────────┼────────┼──────────┤
 * │     16 │     17 │      18 │        19 │       20 │     21 │       22 │
 * ├────────┼────────┼─────────┼───────────┼──────────┼────────┼──────────┤
 * │     23 │     24 │      25 │        26 │       27 │     28 │       29 │
 * ├────────┼────────┼─────────┼───────────┼──────────┼────────┼──────────┤
 * │     30 │     31 │         │           │          │        │          │
 * └────────┴────────┴─────────┴───────────┴──────────┴────────┴──────────┘
 */
public final class TableStringBuilder {
    private TableStyle mStyle = new TableStyle();

    private String mCaption = null;
    private List<String> mHead = null;
    private List<List<String>> mBody = null;

    public TableStyle getStyle() {
        Objects.requireNonNull(mStyle);
        return mStyle;
    }

    public TableStringBuilder setStyle(TableStyle style) {
        Objects.requireNonNull(style);
        mStyle = style;
        return this;
    }

    public TableStringBuilder setCaption(String caption) {
        mCaption = caption;
        return this;
    }

    public TableStringBuilder setHead(List<String> head) {
        mHead = head;
        return this;
    }

    public TableStringBuilder addBody(List<String> body) {
        if (mBody == null) {
            mBody = new LinkedList<>();
        }
        if (body != null) {
            mBody.add(body);
        }
        return this;
    }

    public TableStringBuilder clearBody() {
        if (mBody != null) {
            mBody.clear();
        }
        return this;
    }

    public TableStringBuilder clear() {
        mCaption = null;
        mHead = null;
        mBody = null;
        return this;
    }

    public boolean isEmpty() {
        return !hasCaption() && !hasHead() && !hasBody();
    }

    public boolean hasCaption() {
        return mCaption != null && mCaption.length() > 0;
    }

    public boolean hasHead() {
        return mHead != null && mHead.size() > 0;
    }

    public boolean hasBody() {
        return mBody != null && mBody.size() > 0;
    }

    public List<String> build() {
        if (isEmpty()) {
            return Collections.emptyList();
        }

        final int cellPadding = mStyle.getCellPadding();
        final int cellMaxWidth = mStyle.getCellMaxWidth();

        // 表格的列数
        int columnCount = hasCaption() ? 1 : 0;
        if (hasHead()) {
            columnCount = Math.max(columnCount, mHead.size());
        }
        if (hasBody()) {
            for (List<String> body : mBody) {
                columnCount = Math.max(columnCount, body.size());
            }
        }
        // 表格每列的宽度
        int[] columnWidths = new int[columnCount];
        Arrays.fill(columnWidths, 0);
        if (hasBody()) {
            for (List<String> body : mBody) {
                for (int i = 0; i < body.size(); i++) {
                    int bodyWidth = body.get(i) != null ? body.get(i).length() : 0;
                    columnWidths[i] = Math.max(columnWidths[i], bodyWidth + cellPadding * 2);
                }
            }
        }
        if (hasHead()) {
            for (int i = 0; i < mHead.size(); i++) {
                int headWidth = mHead.get(i) != null ? mHead.get(i).length() : 0;
                columnWidths[i] = headWidth + cellPadding * 2;
            }
        }
        if (hasCaption()) {
            if (columnWidths.length == 1 && columnWidths[0] == 0) {
                columnWidths[0] = mCaption.length();
            }
        }
        if (cellMaxWidth > 0) {
            for (int i = 0; i < columnWidths.length; i++) {
                columnWidths[i] = Math.min(columnWidths[i], cellMaxWidth);
            }
        }

        // 生成表格每一行的字符串
        List<String> lines = new LinkedList<>();
        buildCaption(lines, columnWidths);
        buildHead(lines, columnWidths);
        buildBody(lines, columnWidths);
        return lines;
    }

    /**
     * 生成表名栏每一行的字符串，包含顶部和底部边框
     *
     * @param lines        表格
     * @param columnWidths 每列宽度
     */
    private void buildCaption(List<String> lines, int[] columnWidths) {
        if (!hasCaption()) {
            return;
        }

        final TableStyle.Border border = mStyle.getBorder();
        final TableStyle.Align align = mStyle.getAlign();
        final int cellPadding = mStyle.getCellPadding();

        // 表格的宽度
        int tableWidth = columnWidths.length + 1;
        for (int rowLength : columnWidths) {
            tableWidth += rowLength;
        }

        // 表头的宽度
        int captionWidth = tableWidth - 2;
        // 表头的可使用宽度
        int unableWidth = captionWidth - cellPadding * 2;

        lines.add(buildRowString(null, columnWidths, cellPadding, border.topLeft, border.horizontal, border.topRight, border.horizontal, TableStyle.Align.ALIGN_LEFT, false));

        int startIndex = 0;
        do {
            int endIndex = Math.min(mCaption.length(), startIndex + unableWidth);
            lines.add(border.vertical + buildCellString(mCaption.substring(startIndex, endIndex), captionWidth, cellPadding, border.placeholder, align.caption) + border.vertical);
            startIndex = endIndex;
        } while (startIndex < mCaption.length());

        if (hasHead()) {
            lines.add(buildRowString(null, columnWidths, cellPadding, border.leftCenter, border.topCenter, border.rightCenter, border.horizontal, TableStyle.Align.ALIGN_LEFT, false));
        } else if (hasBody()) {
            lines.add(buildRowString(null, columnWidths, cellPadding, border.leftCenter, border.topCenter, border.rightCenter, border.horizontal, TableStyle.Align.ALIGN_LEFT, false));
        } else {
            lines.add(buildRowString(null, columnWidths, cellPadding, border.bottomLeft, border.horizontal, border.bottomRight, border.horizontal, TableStyle.Align.ALIGN_LEFT, false));
        }
    }

    /**
     * 生成标题栏每一行的字符串，包含顶部和底部边框
     *
     * @param lines        表格
     * @param columnWidths 每列宽度
     */
    private void buildHead(List<String> lines, int[] columnWidths) {
        if (!hasHead()) {
            return;
        }

        final TableStyle.Border border = mStyle.getBorder();
        final TableStyle.Align align = mStyle.getAlign();
        final int cellPadding = mStyle.getCellPadding();
        final boolean cellAutoWrap = mStyle.isCellAutoWrap();

        if (!hasCaption()) {
            lines.add(buildRowString(null, columnWidths, cellPadding, border.topLeft, border.topCenter, border.topRight, border.horizontal, TableStyle.Align.ALIGN_LEFT, false));
        }

        lines.add(buildRowString(mHead, columnWidths, cellPadding, border.vertical, border.vertical, border.vertical, border.placeholder, align.head, cellAutoWrap));

        if (hasBody()) {
            lines.add(buildRowString(null, columnWidths, cellPadding, border.leftCenter, border.center, border.rightCenter, border.horizontal, TableStyle.Align.ALIGN_LEFT, false));
        } else {
            lines.add(buildRowString(null, columnWidths, cellPadding, border.bottomLeft, border.horizontal, border.bottomRight, border.horizontal, TableStyle.Align.ALIGN_LEFT, false));
        }
    }

    /**
     * 生成内容栏每一行的字符串，包含顶部和底部边框
     *
     * @param lines        表格
     * @param columnWidths 每列宽度
     */
    private void buildBody(List<String> lines, int[] columnWidths) {
        if (!hasBody()) {
            return;
        }

        final TableStyle.Border border = mStyle.getBorder();
        final TableStyle.Align align = mStyle.getAlign();
        final int cellPadding = mStyle.getCellPadding();
        final boolean cellAutoWrap = mStyle.isCellAutoWrap();

        if (!hasHead() && !hasCaption()) {
            lines.add(buildRowString(null, columnWidths, cellPadding, border.topLeft, border.topCenter, border.topRight, border.horizontal, TableStyle.Align.ALIGN_LEFT, false));
        }

        for (int i = 0; i < mBody.size(); i++) {
            lines.add(buildRowString(mBody.get(i), columnWidths, cellPadding, border.vertical, border.vertical, border.vertical, border.placeholder, align.body, cellAutoWrap));
            if (mStyle.isBodyDivide() && i < mBody.size() - 1) {
                lines.add(buildRowString(null, columnWidths, cellPadding, border.leftCenter, border.center, border.rightCenter, border.horizontal, TableStyle.Align.ALIGN_LEFT, false));
            }
        }

        lines.add(buildRowString(null, columnWidths, cellPadding, border.bottomLeft, border.bottomCenter, border.bottomRight, border.horizontal, TableStyle.Align.ALIGN_LEFT, false));
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        List<String> lines = build();
        for (int i = 0; i < lines.size(); i++) {
            if (i > 0) {
                stringBuilder.append('\n');
            }
            stringBuilder.append(lines.get(i));
        }
        return stringBuilder.toString();
    }
}
