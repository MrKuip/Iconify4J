package org.kku.fonticons.util;

import javafx.scene.paint.Color;

public class ColorUtil
{
  public static String toRgb(Color color)
  {
    return "rgba(" + ((int) (color.getRed() * 255.0)) + "," + ((int) (color.getGreen() * 255.0)) + ","
        + ((int) (color.getBlue() * 255.0)) + ", " + color.getOpacity() + ")";
  }
}
