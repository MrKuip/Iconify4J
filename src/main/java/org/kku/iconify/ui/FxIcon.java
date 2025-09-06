package org.kku.iconify.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import org.kku.iconify.data.IconSetData;
import org.kku.iconify.data.IconSetData.IconData;
import org.kku.iconify.javax.scene.SVGNode;
import org.kku.iconify.util.ColorUtil;
import javafx.geometry.BoundingBox;
import javafx.geometry.Dimension2D;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

public class FxIcon
{
  public enum IconSize
  {
    VERY_SMALL(12, 12),
    SMALLER(18, 18),
    SMALL(24, 24),
    LARGE(32, 32),
    VERY_LARGE(48, 48),
    SUPER_LARGE(96, 96);

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

  public enum IconColorModifier
  {
    DARKER(color -> color.darker()),
    BRIGHTER(color -> color.brighter());

    private final Function<Color, Color> m_modifier;

    private IconColorModifier(Function<Color, Color> modifier)
    {
      m_modifier = modifier;
    }

    public Color modify(Color color)
    {
      return m_modifier.apply(color);
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

  private final IconData m_iconData;
  private final Dimension2D m_size;
  private final Color m_fillColor;
  private final Color m_strokeColor;
  private final Double m_strokeWidth;
  private final List<Badge> m_badgeList;

  class Badge
  {
    private final IconAlignment mi_alignment;
    private final FxIcon mi_icon;

    Badge(IconAlignment alignment, FxIcon icon)
    {
      mi_alignment = alignment;
      mi_icon = icon;
    }

    IconAlignment alignment()
    {
      return mi_alignment;
    }

    FxIcon icon()
    {
      return mi_icon;
    }
  }

  public FxIcon(FxIcon icon)
  {
    this(icon.m_iconData, icon.m_size, icon.m_fillColor, icon.m_strokeColor, icon.m_strokeWidth,
        new ArrayList<>(icon.m_badgeList));
  }

  public FxIcon(IconData iconData)
  {
    this(iconData, IconSize.SMALL.getSize(), IconColor.DEFAULT_OUTLINE.getColor(), null, 1.0, new ArrayList<Badge>());
  }

  public FxIcon(String iconId)
  {
    this(iconId, IconSize.SMALL.getSize(), IconColor.DEFAULT_OUTLINE.getColor(), null, 1.0, new ArrayList<Badge>());
  }

  private FxIcon(String iconId, Dimension2D size, Color fillColor, Color strokeColor, Double strokeWidth,
      List<Badge> iconList)
  {
    this(IconSetData.searchIconData(iconId), size, fillColor, strokeColor, strokeWidth, iconList);
  }

  private FxIcon(IconData iconData, Dimension2D size, Color fillColor, Color strokeColor, Double strokeWidth,
      List<Badge> badgeList)
  {
    assert iconData != null;
    m_iconData = iconData;
    m_size = size;
    m_fillColor = fillColor;
    m_badgeList = badgeList;
    m_strokeColor = strokeColor;
    m_strokeWidth = strokeWidth;
  }

  public IconData getIconData()
  {
    return m_iconData;
  }

  public String getPrefix()
  {
    return m_iconData.getPrefix();
  }

  public String getId()
  {
    return m_iconData.getId();
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
    return new FxIcon(m_iconData, size, m_fillColor, m_strokeColor, m_strokeWidth, m_badgeList);
  }

  public Dimension2D getSize()
  {
    return m_size;
  }

  public double getWidth()
  {
    return getSize().getWidth();
  }

  public double getHeight()
  {
    return getSize().getHeight();
  }

  public Color getFillColor()
  {
    return m_fillColor;
  }

  public Color getStrokeColor()
  {
    return m_strokeColor;
  }

  public Double getStrokeWidth()
  {
    return m_strokeWidth;
  }

  public FxIcon fillColor(IconColor fillColor)
  {
    return fillColor(fillColor.getColor());
  }

  public FxIcon fillColor(IconColorModifier iconColorModifier)
  {
    return fillColor(iconColorModifier.modify(getFillColor()));
  }

  public FxIcon fillColor(Color fillColor)
  {
    return new FxIcon(m_iconData, m_size, fillColor, m_strokeColor, m_strokeWidth, m_badgeList);
  }

  public FxIcon strokeColor(IconColor strokeColor)
  {
    return strokeColor(strokeColor.getColor());
  }

  public FxIcon strokeColor(Color strokeColor)
  {
    return new FxIcon(m_iconData, m_size, m_fillColor, strokeColor, m_strokeWidth, m_badgeList);
  }

  public FxIcon strokeWidth(Double strokeWidth)
  {
    return new FxIcon(m_iconData, m_size, m_fillColor, m_strokeColor, strokeWidth, m_badgeList);
  }

  public String getParsedSVGText()
  {
    String svgText;

    svgText = getIconData().getSVGDocumentText();
    svgText = svgText.replace("currentColor", ColorUtil.toRgb(getFillColor()));
    svgText = svgText.replace("${top}", Double.toString(getIconData().getTop()));
    svgText = svgText.replace("${left}", Double.toString(getIconData().getLeft()));
    svgText = svgText.replace("${width}", Double.toString(getIconData().getWidth()));
    svgText = svgText.replace("${height}", Double.toString(getIconData().getHeight()));

    return svgText;
  }

  public FxIcon addBadge(IconAlignment alignment, FxIcon fxIcon)
  {
    return addBadge(alignment, fxIcon, alignment.getDefaultSizeFactor());
  }

  public FxIcon addBadge(IconAlignment alignment, FxIcon fxIcon, double sizeFactor)
  {
    List<Badge> badgeList;
    Dimension2D dimension;

    dimension = getSize();
    dimension = new Dimension2D(dimension.getWidth() * sizeFactor, dimension.getHeight() * sizeFactor);

    badgeList = new ArrayList<>(m_badgeList);
    badgeList.add(new Badge(alignment, new FxIcon(fxIcon).size(dimension)));

    return new FxIcon(m_iconData, m_size, m_fillColor, m_strokeColor, m_strokeWidth, badgeList);
  }

  public Node getIconNode()
  {
    return new IconLabel(this).getNode();
  }

  public static class IconLabel
    extends StackPane
  {
    private static final String BOUNDING_BOX = "BOUNDING_BOX";

    private final FxIcon m_icon;
    private Node m_mainNode;
    private Node m_node;

    private IconLabel(FxIcon icon)
    {
      m_icon = icon;
      init();
    }

    @Override
    public double getBaselineOffset()
    {
      // A stackpane returns the first baselineoffset of the first child.
      // In this case this is unwanted
      return m_mainNode.getBaselineOffset();
    }

    public Node getNode()
    {
      if (m_node == null)
      {
        m_node = m_icon.m_badgeList.isEmpty() ? m_mainNode : this;
      }

      return m_node;
    }

    private void init()
    {
      // Add all the icons in the CENTER_CENTER before the m_mainNode because the
      // mainNode should be on top.
      m_icon.m_badgeList.stream().filter(badge -> badge.alignment() == IconAlignment.CENTER_CENTER).forEach(badge -> {
        double x = 0 + (m_icon.getWidth() - badge.icon().getWidth());
        double y = 0 + (m_icon.getHeight() - badge.icon().getHeight()) / 2;
        IconLabel.this.addIcon(badge.icon(), x, y);
      });

      m_mainNode = addIcon(m_icon, 0.0, 0.0);

      m_icon.m_badgeList.stream().filter(badge -> badge.alignment() != IconAlignment.CENTER_CENTER).forEach(badge -> {
        double x;
        double y;
        FxIcon icon;
        IconAlignment alignment;

        icon = badge.icon();
        alignment = badge.alignment();

        switch (alignment)
        {
          case CENTER_CENTER:
            x = 0 + (m_icon.getWidth() - icon.getWidth()) / 2;
            y = 0 + (m_icon.getHeight() - icon.getHeight()) / 2;
            break;
          case CENTER_LEFT:
            x = 0;
            y = 0 + (m_icon.getHeight() - icon.getHeight()) / 2;
            break;
          case CENTER_RIGHT:
            x = 0 + (m_icon.getWidth() - icon.getWidth());
            y = 0 + (m_icon.getHeight() - icon.getHeight()) / 2;
            break;
          case LOWER_CENTER:
            x = 0 + (m_icon.getWidth() - icon.getWidth()) / 2;
            y = 0 + (m_icon.getHeight() - icon.getHeight());
            break;
          case LOWER_LEFT:
            x = 0;
            y = 0 + (m_icon.getHeight() - icon.getHeight());
            break;
          case LOWER_RIGHT:
            x = 0 + (m_icon.getWidth() - icon.getWidth());
            y = 0 + (m_icon.getHeight() - icon.getHeight());
            break;
          case UPPER_CENTER:
            x = 0 + (m_icon.getWidth() - icon.getWidth()) / 2;
            y = 0;
            break;
          case UPPER_LEFT:
            x = 0;
            y = 0;
            break;
          case UPPER_RIGHT:
            x = 0 + (m_icon.getWidth() - icon.getWidth());
            y = 0;
            break;
          default:
            x = 0;
            y = 0;
            break;
        }

        IconLabel.this.addIcon(badge.icon(), x, y);
      });
    }

    private Node addIcon(FxIcon icon, double x, double y)
    {
      try
      {
        return new SVGNode(icon);
      }
      catch (Exception ex)
      {
        return new Label("Parse error");
      }
    }

    @Override
    protected void layoutChildren()
    {
      // Position the text at (0,0) — baseline handled by getBaselineOffset()
      m_mainNode.relocate(0, 0);

      for (Node child : getChildren())
      {
        BoundingBox bb;

        bb = (BoundingBox) child.getProperties().get(BOUNDING_BOX);
        if (bb != null)
        {
          child.resizeRelocate(bb.getMinX(), bb.getMinY(), bb.getWidth(), bb.getHeight());
        }
      }
    }

  }

  public static String normalizeIconName(String iconName)
  {
    return iconName.toLowerCase();
  }

}
