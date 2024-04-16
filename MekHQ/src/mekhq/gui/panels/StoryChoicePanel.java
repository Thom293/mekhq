/*
 * Copyright (c) 2024 - The MegaMek Team. All Rights Reserved.
 *
 * This file is part of MekHQ.
 *
 * MekHQ is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * MekHQ is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with MekHQ. If not, see <http://www.gnu.org/licenses/>.
 */
package mekhq.gui.panels;

import mekhq.campaign.Campaign;
import mekhq.campaign.storyarc.StoryArc;
import mekhq.campaign.storyarc.StoryArcStub;
import mekhq.gui.baseComponents.AbstractMHQPanel;
import mekhq.gui.utilities.MarkdownRenderer;

import javax.swing.*;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;

public class StoryChoicePanel extends AbstractMHQPanel {

    JLabel lblChoice;
    JTextPane txtChoice;

    public StoryChoicePanel(final JFrame frame) {
        super(frame, "StoryChoicePanel");
        initialize();
    }

    @Override
    protected void initialize() {
        setLayout(new GridLayout(0,1));
        txtChoice = new JTextPane();
        txtChoice.setEditable(false);
        txtChoice.setContentType("text/html");
        txtChoice.setText("");
        add(txtChoice);
    }

    protected void updateChoice(String choice, boolean isSelected, Campaign c, Color fg, Color bg) {
        // this gets a little complicated because we have to dynamically set the height of the panel based on
        // how long the text is, but that text may or not be bolded. So we calculate the height as if it was
        // bolded and then switch for unselected cases.
        txtChoice.setText(MarkdownRenderer.getRenderedHtml(StoryArc.replaceTokens("**" + choice + "**", c)));
        txtChoice.setBackground(bg);
        txtChoice.setForeground(fg);
        // to set the height dynamically, we set the size with the width of the JTextPane we want and an arbitrary
        // height. This will set the preferred height correctly, which we can grab and use to set the height of the
        // overall panel. To make sure it fits I have found that I need to set it below 400 to adjust for some insets
        // and the scrollbar.
        txtChoice.setSize(375,5);
        int height = txtChoice.getPreferredSize().height;
        if(!isSelected) {
            txtChoice.setText(MarkdownRenderer.getRenderedHtml(StoryArc.replaceTokens(choice, c)));
        }
        setMinimumSize(new Dimension(400, height));
        setPreferredSize(new Dimension(400, height));
    }
}
