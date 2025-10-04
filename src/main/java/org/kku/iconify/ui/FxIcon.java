package org.kku.iconify.ui;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import javax.swing.Icon;
import org.kku.iconify.data.IconSetData.IconData;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

public class FxIcon
  extends AbstractIcon
{
  FxIcon(String iconId)
  {
    super(iconId);
  }

  FxIcon(IconData iconData)
  {
    super(iconData);
  }

  FxIcon(FxIcon other)
  {
    this(other.getIconData());
    setSize(other.getSize());
    setColor(other.getColor());
  }

  public Image get()
  {
    Icon icon;
    BufferedImage bufferedImage;

    icon = getIcon();
    if (icon == null)
    {
      return null;
    }

    bufferedImage = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
    icon.paintIcon(null, bufferedImage.getGraphics(), 0, 0);
    return SwingFXUtils.toFXImage(bufferedImage, null);
  }

  public FxIcon create()
  {
    return FxIcons.create(getIconData());
  }

  public Node getNode()
  {
    return new ImageView(get());
  }

  public FxIcon size(IconSize size)
  {
    setSize(size);
    return this;
  }

  public FxIcon size(double size)
  {
    setSize(size);
    return this;
  }

  public FxIcon size(Dimension size)
  {
    setSize(size);
    return this;
  }

  public FxIcon color(IconColor color)
  {
    setColor(color);
    return this;
  }

  public FxIcon color(Color color)
  {
    setColor(new java.awt.Color((int) (color.getRed() * 255), (int) (color.getGreen() * 255),
        (int) (color.getBlue() * 255), (int) (color.getOpacity() * 255)));
    return this;
  }

  public FxIcon addBadge(IconAlignment alignment, AbstractIcon icon, double scale)
  {
    _addBadge(alignment, icon, scale);
    return this;
  }

  public FxIcon addBadge(IconAlignment alignment, AbstractIcon icon)
  {
    return addBadge(alignment, icon, 0.3333);
  }
}
