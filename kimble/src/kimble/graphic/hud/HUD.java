/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kimble.graphic.hud;

import de.matthiasmann.twl.GUI;
import de.matthiasmann.twl.ScrollPane;
import de.matthiasmann.twl.TextArea;
import de.matthiasmann.twl.ToggleButton;
import de.matthiasmann.twl.Widget;
import de.matthiasmann.twl.model.ToggleButtonModel;
import de.matthiasmann.twl.renderer.lwjgl.LWJGLRenderer;
import de.matthiasmann.twl.textarea.SimpleTextAreaModel;
import de.matthiasmann.twl.theme.ThemeManager;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;

/**
 *
 * @author Christoffer
 */
public class HUD extends Widget {

    private LWJGLRenderer renderer;
    private ThemeManager themeManager;
    private GUI gui;

    private SimpleTextAreaModel model;
    private TextArea textArea;
    private ScrollPane scrollPane;
    private StringBuilder sb;

    private ToggleButton toggleInfoButton;
    private boolean showInfo = false;

    public HUD() {
        try {
            renderer = new LWJGLRenderer();
            gui = new GUI(this, renderer);

            themeManager = ThemeManager.createThemeManager(HUD.class.getResource("/res/hud/KimbleHUD.xml"), renderer);
            gui.applyTheme(themeManager);

        } catch (IOException ex) {
            Logger.getLogger(HUD.class.getName()).log(Level.SEVERE, null, ex);
        } catch (LWJGLException ex) {
            Logger.getLogger(HUD.class.getName()).log(Level.SEVERE, null, ex);
        }

        setup();
    }

    private void setup() {

        sb = new StringBuilder();
        model = new SimpleTextAreaModel();
        textArea = new TextArea(model);
        scrollPane = new ScrollPane(textArea);
        scrollPane.setTheme("scrollpane");
        scrollPane.setFixed(ScrollPane.Fixed.HORIZONTAL);
        scrollPane.setExpandContentSize(true);

        this.add(scrollPane);

        toggleInfoButton = new ToggleButton("Hide Turn Info");
        toggleInfoButton.addCallback(new Runnable() {

            @Override
            public void run() {
                showInfo = ((ToggleButtonModel) toggleInfoButton.getModel()).isSelected();
                if (showInfo) {
                    toggleInfoButton.setText("Hide Turn Info");
                } else {
                    toggleInfoButton.setText("Show Turn Info");
                }
                scrollPane.setVisible(showInfo);
            }
        });
        ((ToggleButtonModel) toggleInfoButton.getModel()).setSelected(true);
        this.add(toggleInfoButton);
    }

    @Override
    protected void layout() {

        scrollPane.adjustSize();
        scrollPane.setSize(scrollPane.getWidth(), renderer.getHeight() - 100);
        scrollPane.setPosition(20, 20);

        toggleInfoButton.adjustSize();
        toggleInfoButton.setPosition(20, Display.getHeight() - toggleInfoButton.getHeight() - 20);
    }

    public void setViewport(int x, int y, int width, int height) {
        renderer.setViewport(x, y, width, height);
    }

    public void update(float dt) {
    }

    /**
     * Remember the 'newline' feed character!
     *
     * @param sb
     */
    public void appendLine(StringBuilder sb) {
        this.sb.append(sb);
        updateInfoTextArea();
    }

    public void appendLine(String line) {
        sb.append(line).append("\n");
        updateInfoTextArea();
    }

    private void updateInfoTextArea() {
        model.setText(sb.toString());
        boolean isAtEnd = scrollPane.getMaxScrollPosY() == scrollPane.getScrollPositionY();
        if (isAtEnd) {
            scrollPane.validateLayout();
            scrollPane.setScrollPositionY(scrollPane.getMaxScrollPosY());
        }
    }

    public void render() {
        gui.update();
    }

}
