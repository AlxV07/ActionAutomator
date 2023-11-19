package Gui.AAComponents;

import Gui.GuiResources;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class AAMenuBar extends JMenuBar implements AAComponent {
    protected final List<AAComponent> aaComponents;

    public AAMenuBar() {
        super();
        super.setFont(Font.getFont("Source Code Pro"));
        super.setMargin(GuiResources.noMargin);
        super.setBorder(GuiResources.defaultBorder);
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
