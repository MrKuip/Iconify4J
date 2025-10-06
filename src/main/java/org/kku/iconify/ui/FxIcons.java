package org.kku.iconify.ui;

import org.kku.iconify.data.IconSetData.IconData;

public class FxIcons
{
  private FxIcons()
  {
  }

  public static FxIcon create(String iconId)
  {
    return new FxIcon(iconId);
  }

  public static FxIcon create(IconData iconData)
  {
    return new FxIcon(iconData);
  }

  public static FxIcon create(FxIcon fxIcon)
  {
    return new FxIcon(fxIcon);
  }
}
