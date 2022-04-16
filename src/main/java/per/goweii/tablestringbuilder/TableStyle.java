package per.goweii.tablestringbuilder;

import java.util.Objects;

public final class TableStyle {
    private Border mBorder = new Border();
    private Align mAlign = new Align();
    private int mCellPadding = 1;
    private int mCellMaxWidth = -1;
    private boolean mCellAutoWrap = true;
    private boolean mBodyDivide = true;

    public Border getBorder() {
        Objects.requireNonNull(mBorder);
        return mBorder;
    }

    public void setBorder(Border border) {
        Objects.requireNonNull(border);
        this.mBorder = border;
    }

    public Align getAlign() {
        Objects.requireNonNull(mAlign);
        return mAlign;
    }

    public void setAlign(Align align) {
        Objects.requireNonNull(align);
        mAlign = align;
    }

    public int getCellPadding() {
        return Math.max(0, mCellPadding);
    }

    public void setCellPadding(int cellPadding) {
        mCellPadding = Math.max(0, cellPadding);
    }

    public int getCellMaxWidth() {
        return mCellMaxWidth;
    }

    public void setCellMaxWidth(int cellMaxWidth) {
        mCellMaxWidth = cellMaxWidth;
    }

    public boolean isCellAutoWrap() {
        return mCellAutoWrap;
    }

    public void setCellAutoWrap(boolean cellAutoWrap) {
        mCellAutoWrap = cellAutoWrap;
    }

    public boolean isBodyDivide() {
        return mBodyDivide;
    }

    public void setBodyDivide(boolean bodyDivide) {
        mBodyDivide = bodyDivide;
    }

    public static final class Align {
        public static final int ALIGN_LEFT = -1;
        public static final int ALIGN_CENTER = 0;
        public static final int ALIGN_RIGHT = 1;

        public int caption = ALIGN_CENTER;
        public int head = ALIGN_LEFT;
        public int body = ALIGN_RIGHT;
    }

    public static final class Border {
        public char topLeft = '┌';
        public char topRight = '┐';
        public char bottomLeft = '└';
        public char bottomRight = '┘';
        public char topCenter = '┬';
        public char bottomCenter = '┴';
        public char leftCenter = '├';
        public char rightCenter = '┤';
        public char horizontal = '─';
        public char vertical = '│';
        public char center = '┼';
        public char placeholder = ' ';
    }
}
