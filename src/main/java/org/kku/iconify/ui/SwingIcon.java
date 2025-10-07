package org.kku.iconify.ui;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.Icon;
import org.kku.iconify.data.IconSetData.IconData;

public class SwingIcon
  extends AbstractIcon
{
  public SwingIcon(String iconId)
  {
    super(iconId);
  }

  public SwingIcon(IconData iconData)
  {
    super(iconData);
  }

  public SwingIcon(SwingIcon other)
  {
    this(other.getIconData());
    setSize(other.getSize());
    setColor(other.getColor());
  }

  public Icon get()
  {
    return getIcon();
  }

  public SwingIcon size(IconSize size)
  {
    setSize(size);
    return this;
  }

  public SwingIcon size(double size)
  {
    setSize(size);
    return this;
  }

  public SwingIcon size(Dimension size)
  {
    setSize(size);
    return this;
  }

  public SwingIcon color(IconColor color)
  {
    setColor(color);
    return this;
  }

  public SwingIcon color(Color color)
  {
    setColor(color);
    return this;
  }

  public SwingIcon addBadge(IconAlignment alignment, AbstractIcon icon, double scale)
  {
    _addBadge(alignment, icon, scale);
    return this;
  }

  public SwingIcon addBadge(IconAlignment alignment, AbstractIcon icon)
  {
    return addBadge(alignment, icon, 0.3333);
  }
}
