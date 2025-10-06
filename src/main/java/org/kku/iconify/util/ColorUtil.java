package org.kku.iconify.util;

import java.awt.Color;

public class ColorUtil
{
  public static String toRgb(Color color)
  {
    return "rgba(" + color.getRed() + "," + color.getGreen() + "," + color.getBlue() + ", " + (color.getAlpha() / 255.0)
        + ")";
  }
}
