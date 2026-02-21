package resource.modid.gui;

import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WButton;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.data.Insets;
import net.minecraft.network.chat.Component;

public class ExampleGui extends LightweightGuiDescription {
    public ExampleGui() {
        WGridPanel root = new WGridPanel();
        setRootPanel(root);
        root.setSize(256,240);
        root.setInsets(Insets.ROOT_PANEL);



        WButton button = new WButton(Component.translatable("gui.examplegui.buttonTest"));
        root.add(button, 0, 3, 4, 1);

        WLabel label = new WLabel(Component.translatable("gui.examplegui.labelTest"), 0xFFFFFF);
        root.add(label, 0, 6, 2, 1);

        root.validate(this);
    }
}
