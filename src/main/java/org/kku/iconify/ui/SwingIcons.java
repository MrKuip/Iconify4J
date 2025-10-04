package org.kku.iconify.ui;

import org.kku.iconify.data.IconSetData.IconData;

public class SwingIcons
{
  private SwingIcons()
  {
  }

  public static SwingIcon create(String iconId)
  {
    return new SwingIcon(iconId);
  }

  public static SwingIcon create(IconData iconData)
  {
    return new SwingIcon(iconData);
  }

  public static SwingIcon create(SwingIcon swingIcon)
  {
    return new SwingIcon(swingIcon);
  }
}
