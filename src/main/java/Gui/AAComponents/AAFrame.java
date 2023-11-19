package Gui.AAComponents;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class AAFrame extends JFrame implements AAComponent {
    protected final List<AAComponent> aaComponents;

    public AAFrame() {
        super();
        aaComponents = new ArrayList<>();
    }

    @Override
    public void updateColorTheme(Color primaryColor, Color secondaryColor, Color alternateColor) {
        super.setForeground(primaryColor);
        super.setBackground(secondaryColor);
        for (AAComponent aaComponent : aaComponents) {
            aaComponent.updateColorTheme(primaryColor, secondaryColor, alternateColor);
        }
    }

    @Override
    public Component add(Component component) {
        try {
            aaComponents.add((AAComponent) component);
        } catch (ClassCastException ignored) {
        }
        return super.add(component);
    }

    @Override
    public void remove(Component component) {
        try {
            aaComponents.add((AAComponent) component);
        } catch (ClassCastException ignored) {
        }
        super.remove(component);
    }
}
