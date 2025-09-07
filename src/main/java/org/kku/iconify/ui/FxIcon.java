package org.kku.iconify.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.kku.iconify.data.IconSetData;
import org.kku.iconify.data.IconSetData.Flip;
import org.kku.iconify.data.IconSetData.IconData;
import org.kku.iconify.data.IconSetData.Rotation;
import org.kku.iconify.util.ColorUtil;
import org.kku.iconify.util.SVGUtil;
import com.github.weisj.jsvg.SVGDocument;
import com.github.weisj.jsvg.renderer.jfx.FXSVGRenderer;
import javafx.geometry.Dimension2D;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.BlendMode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.transform.Rotate;

public class FxIcon
{
  private IconData m_iconData;
  private String m_parsedSVG;
  private Dimension2D m_size;
  private Color m_color;
  private List<Badge> m_badgeList;

  public FxIcon(String iconId)
  {
    this(IconSetData.searchIconData(iconId));
  }

  public FxIcon(IconData iconData)
  {
    m_iconData = iconData;
    setSize(IconSize.REGULAR);
    setColor(IconColor.DEFAULT_OUTLINE);
  }

  public FxIcon(FxIcon other)
  {
    m_iconData = other.m_iconData;
    m_size = other.m_size;
    m_color = other.m_color;
  }

  public FxIcon size(IconSize size)
  {
    return size(size.getSize());
  }

  public FxIcon size(double size)
  {
    return size(new Dimension2D(size, size));
  }

  public FxIcon size(Dimension2D size)
  {
    FxIcon copy;

    copy = new FxIcon(this);
    copy.setSize(size);

    return copy;
  }

  public void setSize(IconSize size)
  {
    setSize(size.getSize());
  }

  public void setSize(double size)
  {
    setSize(new Dimension2D(size, size));
  }

  public void setSize(Dimension2D size)
  {
    m_size = size;
  }

  public Dimension2D getSize()
  {
    return m_size;
  }

  public FxIcon color(IconColor color)
  {
    return color(color.getColor());
  }

  public FxIcon color(Color color)
  {
    FxIcon copy;

    copy = new FxIcon(this);
    copy.setColor(color);

    return copy;
  }

  public void setColor(IconColor color)
  {
    setColor(color.getColor());
  }

  public void setColor(Color color)
  {
    m_color = color;
  }

  public Color getColor()
  {
    return m_color;
  }

  public void addBadge(IconAlignment alignment, FxIcon icon, double scale)
  {
    icon = new FxIcon(icon);
    if (scale > 0.0)
    {
      icon.size(new Dimension2D((getSize().getWidth() * scale), getSize().getHeight() * scale));
    }

    if (m_badgeList == null)
    {
      m_badgeList = new ArrayList<>();
    }
    m_badgeList.add(new Badge(alignment, icon));
  }

  public void addBadge(IconAlignment alignment, FxIcon fxIcon)
  {
    addBadge(alignment, fxIcon, 0.0);
  }

  public void setParsedSVG(String parsedSVG)
  {
    m_parsedSVG = parsedSVG;
  }

  public String getParsedSVGText()
  {
    if (m_parsedSVG == null)
    {
      String svgText;

      svgText = getIconData().getSVGDocumentText();
      svgText = svgText.replace("currentColor", ColorUtil.toRgb(getColor()));
      svgText = svgText.replace("${top}", Double.toString(getIconData().getTop()));
      svgText = svgText.replace("${left}", Double.toString(getIconData().getLeft()));
      svgText = svgText.replace("${width}", Double.toString(getIconData().getWidth()));
      svgText = svgText.replace("${height}", Double.toString(getIconData().getHeight()));

      m_parsedSVG = svgText;
    }

    return m_parsedSVG;
  }

  private IconData getIconData()
  {
    return m_iconData;
  }

  public Node getNode()
  {
    try
    {
      return new SVGNode();
    }
    catch (Exception e)
    {
      return new Circle(getSize().getWidth(), Color.RED);
    }
  }

  public static String normalizeIconName(String iconName)
  {
    return iconName.toLowerCase();
  }

  private class SVGNode
    extends Canvas
  {
    private final SVGDocument m_document;
    private final Flip m_hFlip;
    private final Flip m_vFlip;
    private final Rotation m_rotate;

    public SVGNode() throws IOException
    {
      m_document = SVGUtil.createDocument(getParsedSVGText());

      setHeight(getSize().getHeight());
      setWidth(getSize().getWidth());

      m_hFlip = getIconData().getHFlip();
      m_vFlip = getIconData().getVFlip();
      m_rotate = getIconData().getRotate();

      update();
    }

    public boolean hasFlip()
    {
      return m_hFlip.get() || m_vFlip.get();
    }

    public boolean hasRotate()
    {
      return m_rotate != Rotation.ROTATE_0;
    }

    public void update()
    {
      GraphicsContext graphics = getGraphicsContext2D();
      graphics.save();
      try
      {
        double scaleX;
        double scaleY;
        double scale;

        scaleX = getWidth() / m_document.size().getWidth();
        scaleY = getHeight() / m_document.size().getHeight();
        scale = Math.min(scaleX, scaleY);

        graphics.setTransform(1, 0, 0, 1, 0, 0);
        if (hasRotate())
        {
          Rotate rotate;

          rotate = new Rotate(m_rotate.get(), getWidth() / 2.0, getHeight() / 2.0);
          graphics.setTransform(rotate.getMxx(), rotate.getMyx(), rotate.getMxy(), rotate.getMyy(), rotate.getTx(),
              rotate.getTy());
        }
        if (hasFlip())
        {
          graphics.translate(m_hFlip.get() ? getWidth() : 0, m_vFlip.get() ? getHeight() : 0);
          graphics.scale(m_hFlip.get() ? -1 : 1, m_vFlip.get() ? -1 : 1);
        }
        graphics.scale(scale, scale);
        graphics.setGlobalAlpha(1D);
        graphics.setGlobalBlendMode(BlendMode.SRC_OVER);
        FXSVGRenderer.render(m_document, getGraphicsContext2D());

        if (m_badgeList != null)
        {
          for (Badge badge : m_badgeList)
          {
            SVGDocument document;

            graphics.save();
            try
            {
              double badgeScaleX;
              double badgeScaleY;
              double badgeScale;
              double translateX;
              double translateY;

              document = SVGUtil.createDocument(badge.getFxIcon().getParsedSVGText());
              badgeScaleX = badge.getFxIcon().getSize().getWidth() / getWidth();
              badgeScaleY = badge.getFxIcon().getSize().getHeight() / getHeight();
              badgeScale = Math.min(badgeScaleX, badgeScaleY);
              System.out.println("getWidth()=" + getWidth());
              System.out.println("badge.width=" + badge.getFxIcon().getSize().getHeight());
              System.out.println("badgeScale=" + badgeScale);

              switch (badge.mi_alignment)
              {
                case CENTER_CENTER:
                  translateX = (getWidth() * (1 - badgeScale)) / 2;
                  translateY = (getHeight() * (1 - badgeScale)) / 2;
                  break;
                case CENTER_LEFT:
                  translateX = 0;
                  translateY = (getHeight() * (1 - badgeScale)) / 2;
                  break;
                case CENTER_RIGHT:
                  translateX = getWidth() * (1 - badgeScale);
                  translateY = (getHeight() * (1 - badgeScale)) / 2;
                  break;
                case LOWER_CENTER:
                  translateX = (getWidth() * (1 - badgeScale)) / 2;
                  translateY = getHeight() * (1 - badgeScale);
                  break;
                case LOWER_LEFT:
                  translateX = 0;
                  translateY = getHeight() * (1 - badgeScale);
                  break;
                case LOWER_RIGHT:
                  translateX = getWidth() * (1 - badgeScale);
                  translateY = getHeight() * (1 - badgeScale);
                  break;
                case UPPER_CENTER:
                  translateX = (getWidth() * (1 - badgeScale)) / 2;
                  translateY = 0.0;
                  break;
                case UPPER_LEFT:
                  translateX = 0.0;
                  translateY = 0.0;
                  break;
                case UPPER_RIGHT:
                  translateX = getWidth() * (1 - badgeScale);
                  translateY = 0.0;
                  break;
                default:
                  translateX = 0.0;
                  translateY = 0.0;
                  break;
              }

              graphics.translate(translateX / scale, translateY / scale);
              graphics.scale(badgeScale, badgeScale);
              FXSVGRenderer.render(document, getGraphicsContext2D());
              graphics.restore();
            }
            catch (Exception e)
            {
            }
            finally
            {
              graphics.restore();
            }
          }
        }
      }
      finally
      {
        graphics.restore();
      }
    }
  }

  public enum IconSize
  {
    TINY(10, 10),
    SMALL(12, 12),
    MEDIUM(18, 18),
    REGULAR(24, 24),
    LARGE(32, 32),
    BIG(48, 48),
    HUGE(96, 96);

    private final int m_width;
    private final int m_height;
    private final Dimension2D m_dimension;

    IconSize(int width, int height)
    {
      m_width = width;
      m_height = height;
      m_dimension = new Dimension2D(width, height);
    }

    public int getWidth()
    {
      return m_width;
    }

    public int getHeight()
    {
      return m_height;
    }

    public Dimension2D getSize()
    {
      return m_dimension;
    }
  }

  public enum IconColor
  {
    DEFAULT_OUTLINE(Color.rgb(0, 74, 131)),
    DEFAULT_FILL(Color.WHITE),
    LIGHT_BLUE_FILL(Color.rgb(160, 200, 255)),
    WHITE(Color.WHITE),
    BLACK(Color.BLACK),
    RED(Color.RED),
    BLUE(Color.BLUE),
    YELLOW(Color.YELLOW);

    private Color m_color;

    IconColor(Color color)
    {
      m_color = color;
    }

    public Color getColor()
    {
      return m_color;
    }
  }

  public enum IconAlignment
  {
    UPPER_RIGHT(0.33),
    UPPER_CENTER(0.33),
    UPPER_LEFT(0.33),
    CENTER_RIGHT(0.33),
    CENTER_CENTER(1),
    CENTER_LEFT(0.33),
    LOWER_RIGHT(0.33),
    LOWER_CENTER(0.33),
    LOWER_LEFT(0.33);

    private final double mi_defaultSizeFactor;

    IconAlignment(double sizeFactor)
    {
      mi_defaultSizeFactor = sizeFactor;
    }

    public double getDefaultSizeFactor()
    {
      return mi_defaultSizeFactor;
    }
  }

  class Badge
  {
    private final IconAlignment mi_alignment;
    private final FxIcon mi_icon;

    Badge(IconAlignment alignment, FxIcon icon)
    {
      mi_alignment = alignment;
      mi_icon = icon;
    }

    IconAlignment getAlignment()
    {
      return mi_alignment;
    }

    FxIcon getFxIcon()
    {
      return mi_icon;
    }
  }
}
