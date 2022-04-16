package per.goweii.tablestringbuilder;

public final class TableStyle {
    public final Border border = new Border();
    public final Align align = new Align();
    public int cellPadding = 1;
    public int cellMaxWidth = -1;
    public boolean cellAutoWrap = true;
    public boolean bodyDivide = true;

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
