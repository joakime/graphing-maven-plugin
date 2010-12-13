package net.erdfelt.maven.graphing.graph.decorator;

/*
 * Copyright (c) Joakim Erdfelt.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.awt.Color;

/**
 * EdgeDecorator
 * 
 * <p>
 * Original code by <a href="michal.maczka@dimatics.com">Michal Maczka</a>
 * <p>
 * Updated to JDK 1.6 and Generics by <a href="joakim@erdfelt.net">Joakim Erdfelt</a>
 * 
 * @since 1.0
 */
public class EdgeDecorator
{
    /**
     * The possible Line Style
     */
    public static enum LineStyle
    {
        NORMAL, BOLD, DASHED
    };

    /**
     * The possible Line Ending Styles
     */
    public static enum EndingStyles
    {
        NONE, ARROW, DOT, HOLLOW_DOT, INVERT_ARROW, INVERT_ARROW_DOT, INVERT_ARROW_HOLLOW_DOT
    };

    private Color lineColor;

    private EndingStyles lineHead = EndingStyles.ARROW;

    private EndingStyles lineTail = EndingStyles.NONE;

    private String lineLabel;

    private LineStyle style;

    private int fontSize = 8;

    public Color getLineColor()
    {
        return lineColor;
    }

    public void setLineColor(Color lineColor)
    {
        this.lineColor = lineColor;
    }

    public EndingStyles getLineHead()
    {
        return lineHead;
    }

    public void setLineHead(EndingStyles lineHead)
    {
        this.lineHead = lineHead;
    }

    public String getLineLabel()
    {
        return lineLabel;
    }

    public void setLineLabel(String lineLabel)
    {
        this.lineLabel = lineLabel;
    }

    public EndingStyles getLineTail()
    {
        return lineTail;
    }

    public void setLineTail(EndingStyles lineTail)
    {
        this.lineTail = lineTail;
    }

    public LineStyle getStyle()
    {
        return style;
    }

    public void setStyle(LineStyle style)
    {
        this.style = style;
    }

    public int getFontSize()
    {
        return fontSize;
    }

    public void setFontSize(int fontSize)
    {
        this.fontSize = fontSize;
    }
}
