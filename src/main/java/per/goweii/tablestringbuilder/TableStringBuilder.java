package per.goweii.tablestringbuilder;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public final class TableStringBuilder {
    private String mTitle = null;
    private List<String> mHeader = null;
    private List<List<String>> mContents = null;

    private TableStyle mStyle = new TableStyle();

    private boolean mContentDivide = true;

    private int mTitleAlignment = 0;
    private int mHeaderAlignment = -1;
    private int mContentAlignment = -1;

    public TableStringBuilder setTableStyle(TableStyle style) {
        if (style != null) {
            mStyle = style;
        }
        return this;
    }

    public TableStringBuilder setTitleAlignment(int alignment) {
        mTitleAlignment = alignment;
        return this;
    }

    public TableStringBuilder setHeaderAlignment(int alignment) {
        mHeaderAlignment = alignment;
        return this;
    }

    public TableStringBuilder setContentAlignment(int alignment) {
        mContentAlignment = alignment;
        return this;
    }

    public TableStringBuilder setContentDivide(boolean contentDivide) {
        mContentDivide = contentDivide;
        return this;
    }

    public TableStringBuilder setTitle(String title) {
        mTitle = title;
        return this;
    }

    public TableStringBuilder setHeader(List<String> header) {
        mHeader = header;
        return this;
    }

    public TableStringBuilder addContent(List<String> content) {
        if (mContents == null) {
            mContents = new LinkedList<>();
        }
        if (content != null) {
            mContents.add(content);
        }
        return this;
    }

    public List<String> build() {
        int rowCount = mHeader != null ? mHeader.size() : 0;
        if (mContents != null) {
            for (List<String> content : mContents) {
                rowCount = Math.max(rowCount, content.size());
            }
        }
        int[] rowLengths = new int[rowCount];
        Arrays.fill(rowLengths, 0);
        if (mHeader != null) {
            for (int i = 0; i < mHeader.size(); i++) {
                rowLengths[i] = mHeader.get(i) != null ? mHeader.get(i).length() : 0;
            }
        }
        if (mContents != null) {
            for (List<String> content : mContents) {
                for (int i = 0; i < content.size(); i++) {
                    rowLengths[i] = Math.max(rowLengths[i], content.get(i) != null ? content.get(i).length() : 0);
                }
            }
        }
        int tableLength = rowCount + 1;
        for (int rowLength : rowLengths) {
            tableLength += rowLength;
        }
        boolean hasTitle = mTitle != null && mTitle.length() > 0;
        boolean hasHeader = mHeader != null && mHeader.size() > 0;
        boolean hasContent = mContents != null && mContents.size() > 0;
        List<String> lines = new LinkedList<>();
        if (hasTitle) {
            lines.add(getLineString(null, rowLengths, mStyle.lt, mStyle.h, mStyle.rt, mStyle.h, -1));
            lines.add(mStyle.v + getCellString(mTitle, tableLength - 2, mStyle.p, mTitleAlignment) + mStyle.v);
        }
        if (hasHeader) {
            if (hasTitle) {
                lines.add(getLineString(null, rowLengths, mStyle.cl, mStyle.ct, mStyle.cr, mStyle.h, -1));
            } else {
                lines.add(getLineString(null, rowLengths, mStyle.lt, mStyle.ct, mStyle.rt, mStyle.h, -1));
            }
            lines.add(getLineString(mHeader, rowLengths, mStyle.v, mStyle.v, mStyle.v, mStyle.p, mHeaderAlignment));
        }
        if (hasContent) {
            if (hasHeader) {
                lines.add(getLineString(null, rowLengths, mStyle.cl, mStyle.c, mStyle.cr, mStyle.h, -1));
            } else if (hasTitle) {
                lines.add(getLineString(null, rowLengths, mStyle.cl, mStyle.ct, mStyle.cr, mStyle.h, -1));
            } else {
                lines.add(getLineString(null, rowLengths, mStyle.lt, mStyle.ct, mStyle.rt, mStyle.h, -1));
            }
            for (int i = 0; i < mContents.size(); i++) {
                lines.add(getLineString(mContents.get(i), rowLengths, mStyle.v, mStyle.v, mStyle.v, mStyle.p, mContentAlignment));
                if (mContentDivide && i < mContents.size() - 1) {
                    lines.add(getLineString(null, rowLengths, mStyle.cl, mStyle.c, mStyle.cr, mStyle.h, -1));
                }
            }
        }
        if (hasContent || hasHeader) {
            lines.add(getLineString(null, rowLengths, mStyle.lb, mStyle.cb, mStyle.rb, mStyle.h, -1));
        } else if (hasTitle) {
            lines.add(getLineString(null, rowLengths, mStyle.lb, mStyle.h, mStyle.rb, mStyle.h, -1));
        }
        return lines;
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

    private String getLineString(List<String> texts, int[] lengths, char leading, char separator, char trailing, char placeholder, int alignment) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(leading);
        int count = Math.max(texts != null ? texts.size() : 0, lengths != null ? lengths.length : 0);
        for (int i = 0; i < count; i++) {
            String text = texts != null && texts.size() > i ? texts.get(i) : null;
            int length = lengths != null && lengths.length > i ? lengths[i] : 0;
            if (i > 0) {
                stringBuilder.append(separator);
            }
            stringBuilder.append(getCellString(text, length, placeholder, alignment));
        }
        stringBuilder.append(trailing);
        return stringBuilder.toString();
    }

    private String getCellString(String text, int length, char placeholder, int alignment) {
        if (length <= 0) {
            return "";
        }
        int textLength = text != null ? text.length() : 0;
        if (textLength > length) {
            return text.substring(0, length);
        } else if (textLength == length) {
            return text;
        }
        StringBuilder stringBuilder = new StringBuilder();
        if (textLength > 0) {
            stringBuilder.append(text);
        }
        while (stringBuilder.length() < length) {
            if (alignment < 0) {
                stringBuilder.append(placeholder);
            } else if (alignment > 0) {
                stringBuilder.insert(0, placeholder);
            } else {
                if ((length - stringBuilder.length()) % 2 == 0) {
                    stringBuilder.insert(0, placeholder);
                } else {
                    stringBuilder.append(placeholder);
                }
            }
        }
        return stringBuilder.toString();
    }
}
