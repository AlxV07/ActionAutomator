package Gui;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class GuiResources {
    public static final Insets defaultMargin = new Insets(2, 2, 2, 2);
    public static final Font defaultFont = new Font("Source Code Pro", Font.PLAIN, 12);
    public static Font smallerFont = new Font("Source Code Pro", Font.PLAIN, 10);
    public static final Border lightThemeBorder = BorderFactory.createLineBorder(Color.BLACK);
    public static final Border darkThemeBorder = BorderFactory.createLineBorder(Color.WHITE);
    public static final Color lightThemeColor = Color.WHITE;
    public static final Color darkThemeColor = Color.BLACK;
    public static Border roundedBorder = new RoundedBorder(20);

    public static class RoundedBorder implements Border {
        private final int radius;

        RoundedBorder(int radius) {
            this.radius = radius;
        }

        public Insets getBorderInsets(Component c) {
            return new Insets(this.radius+1, this.radius+1, this.radius+2, this.radius);
        }

        public boolean isBorderOpaque() {
            return true;
        }

        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            g.drawRoundRect(x, y, width-1, height-1, radius, radius);
        }
    }
}
