package org.kku.iconify.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import javax.swing.Icon;
import org.kku.iconify.data.IconSetData.Flip;
import org.kku.iconify.data.IconSetData.IconData;
import org.kku.iconify.data.IconSetData.Rotation;
import org.kku.iconify.data.IconSets;
import org.kku.iconify.util.ColorUtil;
import org.kku.iconify.util.SVGUtil;
import com.github.weisj.jsvg.SVGDocument;
import com.github.weisj.jsvg.view.ViewBox;

public abstract class AbstractIcon
{
  protected IconData m_iconData;
  private String m_parsedSVG;
  protected Dimension m_size;
  protected Color m_color;
  private List<Badge> m_badgeList;

  AbstractIcon(String iconId)
  {
    this(IconSets.get().searchIconData(iconId));
  }

  AbstractIcon(IconData iconData)
  {
    m_iconData = iconData;
    setSize(IconSize.REGULAR);
    setColor(IconColor.DEFAULT_OUTLINE);
  }

  protected void setSize(IconSize size)
  {
    setSize(size.getSize());
  }

  public void setSize(double size)
  {
    Dimension dim;

    dim = new Dimension();
    dim.setSize(size, size);

    setSize(dim);
  }

  protected void setSize(Dimension size)
  {
    m_size = size;
  }

  public Dimension getSize()
  {
    return m_size;
  }

  protected void setColor(IconColor color)
  {
    setColor(color.getColor());
  }

  protected void setColor(Color color)
  {
    m_color = color;
  }

  public Color getColor()
  {
    return m_color;
  }

  protected void _addBadge(IconAlignment alignment, AbstractIcon icon, double scale)
  {
    if (m_badgeList == null)
    {
      m_badgeList = new ArrayList<>();
    }
    m_badgeList.add(new Badge(alignment, icon, scale));
  }

  public void setParsedSVG(String parsedSVG)
  {
    m_parsedSVG = parsedSVG;
  }

  public String getParsedSVGText()
  {
    if (getIconData() == null)
    {
      return null;
    }

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

  protected IconData getIconData()
  {
    return m_iconData;
  }

  public Icon getIcon()
  {
    return new JSVGIcon();
  }

  public static String normalizeIconName(String iconName)
  {
    return iconName.toLowerCase();
  }

  private class JSVGIcon
      implements Icon
  {
    private SVGDocument m_document;
    private Flip m_hFlip = Flip.FALSE;
    private Flip m_vFlip = Flip.FALSE;
    private Rotation m_rotate = Rotation.ROTATE_0;

    public JSVGIcon()
    {
      try
      {
        m_document = SVGUtil.createDocument(getParsedSVGText());
      }
      catch (Exception ex)
      {
        ex.printStackTrace();
      }

      if (getIconData() != null)
      {
        m_hFlip = getIconData().getHFlip();
        m_vFlip = getIconData().getVFlip();
        m_rotate = getIconData().getRotate();
      }
    }

    @Override
    public int getIconWidth()
    {
      return (int) getWidth();
    }

    @Override
    public int getIconHeight()
    {
      return (int) getHeight();
    }

    double getWidth()
    {
      return getSize().getWidth();
    }

    double getHeight()
    {
      return getSize().getHeight();
    }

    public boolean hasFlip()
    {
      return m_hFlip.get() || m_vFlip.get();
    }

    public boolean hasRotate()
    {
      return m_rotate != Rotation.ROTATE_0;
    }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y)
    {
      Graphics2D g2d;
      Stack<AffineTransform> stack;

      stack = new Stack<>();

      g2d = (Graphics2D) g.create();
      stack.push(g2d.getTransform());

      g2d.translate(x, y);
      if (m_document == null)
      {
        g2d.setColor(java.awt.Color.RED);
        g2d.fillRect(0, 0, getIconWidth(), getIconHeight());
        return;
      }

      try
      {
        double svgWidth;
        double svgHeight;
        double documentWidth;
        double documentHeight;
        double scaleX;
        double scaleY;
        double scale;

        svgWidth = getIconWidth();
        svgHeight = getIconHeight();
        documentWidth = m_document.size().getWidth();
        documentHeight = m_document.size().getHeight();

        // Calculate the scale that the SVG document fits this canvas
        scaleX = svgWidth / documentWidth;
        scaleY = svgHeight / documentHeight;
        scale = Math.min(scaleX, scaleY);

        // Make sure the SVG document is centered in this canvas
        if (scaleY != scaleX)
        {
          double centerX;
          double centerY;

          if (scaleY > scaleX)
          {
            centerX = 0;
            centerY = (svgHeight - documentHeight * scale) / 2.0;
          }
          else
          {
            centerX = (svgWidth - documentWidth * scale) / 2.0;
            centerY = 0;
          }
          g2d.translate(centerX, centerY);
        }

        // A Iconify icon can have a rotate defined.
        if (hasRotate())
        {
          g2d.rotate(m_rotate.get(), getWidth() / 2.0, getHeight() / 2.0);
        }
        // A Iconify icon can have a flip defined.
        if (hasFlip())
        {
          g2d.translate(m_hFlip.get() ? getWidth() : 0, m_vFlip.get() ? getHeight() : 0);
          g2d.scale(m_hFlip.get() ? -1 : 1, m_vFlip.get() ? -1 : 1);
        }

        // Perform the calculated scale to fit the SVG document in this canvas.
        g2d.scale(scale, scale);

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);

        m_document.render(null, g2d, new ViewBox(0, 0, (int) documentWidth, (int) documentHeight));

        if (m_badgeList != null)
        {
          for (Badge badge : m_badgeList)
          {
            SVGDocument document;

            stack.push(g2d.getTransform());
            try
            {
              double badgeScaleX;
              double badgeScaleY;
              double badgeScale;
              double translateX;
              double translateY;

              document = SVGUtil.createDocument(badge.getIcon().getParsedSVGText());
              badgeScaleX = badge.getScale() * badge.getSize().getWidth() / svgWidth;
              badgeScaleY = badge.getScale() * badge.getSize().getHeight() / svgHeight;
              badgeScale = Math.min(badgeScaleX, badgeScaleY);

              switch (badge.mi_alignment)
              {
                case CENTER_CENTER:
                  translateX = (svgWidth * (1 - badgeScale)) / 2;
                  translateY = (svgHeight * (1 - badgeScale)) / 2;
                  break;
                case CENTER_LEFT:
                  translateX = 0;
                  translateY = (svgHeight * (1 - badgeScale)) / 2;
                  break;
                case CENTER_RIGHT:
                  translateX = svgWidth * (1 - badgeScale);
                  translateY = (svgHeight * (1 - badgeScale)) / 2;
                  break;
                case LOWER_CENTER:
                  translateX = (svgWidth * (1 - badgeScale)) / 2;
                  translateY = svgHeight * (1 - badgeScale);
                  break;
                case LOWER_LEFT:
                  translateX = 0;
                  translateY = svgHeight * (1 - badgeScale);
                  break;
                case LOWER_RIGHT:
                  translateX = svgWidth * (1 - badgeScale);
                  translateY = svgHeight * (1 - badgeScale);
                  break;
                case UPPER_CENTER:
                  translateX = (svgWidth * (1 - badgeScale)) / 2;
                  translateY = 0.0;
                  break;
                case UPPER_LEFT:
                  translateX = 0.0;
                  translateY = 0.0;
                  break;
                case UPPER_RIGHT:
                  translateX = svgWidth * (1 - badgeScale);
                  translateY = 0.0;
                  break;
                default:
                  translateX = 0.0;
                  translateY = 0.0;
                  break;
              }

              g2d.translate(translateX / scale, translateY / scale);
              g2d.scale(badgeScale, badgeScale);
              document.render(null, g2d,
                  new ViewBox(0, 0, (int) document.size().getWidth(), (int) document.size().getHeight()));
            }
            catch (Exception e)
            {
            }
            finally
            {
              g2d.setTransform(stack.pop());
            }
          }
        }
      }
      finally
      {
        g2d.setTransform(stack.pop());
      }

      g2d.dispose();
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
    private final Dimension m_dimension;

    IconSize(int width, int height)
    {
      m_width = width;
      m_height = height;
      m_dimension = new Dimension(width, height);
    }

    public int getWidth()
    {
      return m_width;
    }

    public int getHeight()
    {
      return m_height;
    }

    public Dimension getSize()
    {
      return m_dimension;
    }
  }

  public enum IconColor
  {
    DEFAULT_OUTLINE(new Color(0, 74, 131)),
    DEFAULT_FILL(Color.WHITE),
    LIGHT_BLUE_FILL(new Color(160, 200, 255)),
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
    private final AbstractIcon mi_icon;
    private final double mi_scale;

    Badge(IconAlignment alignment, AbstractIcon icon, double scale)
    {
      mi_alignment = alignment;
      mi_icon = icon;
      mi_scale = scale;
    }

    IconAlignment getAlignment()
    {
      return mi_alignment;
    }

    AbstractIcon getIcon()
    {
      return mi_icon;
    }

    double getScale()
    {
      return mi_scale;
    }

    public Dimension getSize()
    {
      return AbstractIcon.this.getSize();
    }
  }
}
