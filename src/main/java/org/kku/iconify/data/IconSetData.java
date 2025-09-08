package org.kku.iconify.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class IconSetData
    implements Comparable<IconSetData>
{
  private String m_id;
  private String m_prefix;
  private int m_numberOfIcons;
  private int m_left;
  private int m_top;
  private int m_height;
  private int m_width;
  private Rotation m_rotate;
  private Flip m_hFlip;
  private Flip m_vFlip;
  private String m_version = "N/A";
  private String m_name = "N/A";
  private String m_author = "N/A";
  private String m_projectURL = "N/A";
  private String m_licenseURL = "N/A";
  private String m_licenseName = "N/A";
  private String m_category = "N/A";
  private List<String> m_iconList;
  private Map<String, IconData> m_iconDataByIdMap;
  private List<IconData> m_iconDataList = new ArrayList<>();

  public enum Flip
  {
    FALSE(false),
    TRUE(true);

    private final boolean mi_value;

    Flip(boolean value)
    {
      mi_value = value;
    }

    public boolean get()
    {
      return mi_value;
    }

    public Flip add(Flip flip)
    {
      return Flip.values()[(ordinal() + flip.ordinal()) % Flip.values().length];
    }

    public static Flip get(boolean flip)
    {
      return Flip.values()[flip ? 1 : 0];
    }
  }

  public enum Rotation
  {
    ROTATE_0(0),
    ROTATE_90(90),
    ROTATE_180(180),
    ROTATE_270(270);

    private final int mi_degrees;

    private Rotation(int degrees)
    {
      mi_degrees = degrees;
    }

    public int get()
    {
      return mi_degrees;
    }

    public Rotation add(Rotation rotate)
    {
      return Rotation.values()[(ordinal() + rotate.ordinal()) % Rotation.values().length];
    }

    public static Rotation get(int number)
    {
      return Rotation.values()[number % 4];
    }
  }

  IconSetData()
  {
  }

  public boolean isAll()
  {
    return IconSets.ALL.equals(getId());
  }

  public void setId(String id)
  {
    m_id = id;
  }

  public String getId()
  {
    return m_id;
  }

  public void setPrefix(String prefix)
  {
    m_prefix = prefix;
  }

  public String getPrefix()
  {
    return m_prefix;
  }

  void setLeft(int left)
  {
    m_left = left;
  }

  public int getLeft()
  {
    return m_left;
  }

  void setTop(int top)
  {
    m_top = top;
  }

  public int getTop()
  {
    return m_top;
  }

  void setWidth(int width)
  {
    m_width = width;
  }

  public int getWidth()
  {
    return m_width;
  }

  void setHeight(int height)
  {
    m_height = height;
  }

  public int getHeight()
  {
    return m_height;
  }

  void setRotate(Rotation rotate)
  {
    m_rotate = rotate;
  }

  public Rotation getRotate()
  {
    return m_rotate;
  }

  void setHFlip(Flip hFlip)
  {
    m_hFlip = hFlip;
  }

  public Flip getHFlip()
  {
    return m_hFlip;
  }

  void setVFlip(Flip vFlip)
  {
    m_vFlip = vFlip;
  }

  public Flip getVFlip()
  {
    return m_vFlip;
  }

  void setNumberOfIcons(int numberOfIcons)
  {
    m_numberOfIcons = numberOfIcons;
  }

  public int getNumberOfIcons()
  {
    return m_numberOfIcons;
  }

  void setVersion(String version)
  {
    m_version = version;
  }

  public String getVersion()
  {
    return m_version;
  }

  void setName(String name)
  {
    m_name = name;
  }

  public String getName()
  {
    return m_name;
  }

  void setLicenseURL(String licenseURL)
  {
    m_licenseURL = licenseURL;
  }

  public String getLicenseURL()
  {
    return m_licenseURL;
  }

  void setLicenseName(String licenseName)
  {
    m_licenseName = licenseName;
  }

  public String getLicenseName()
  {
    return m_licenseName;
  }

  void setCategory(String category)
  {
    m_category = category;
  }

  public String getCategory()
  {
    return m_category;
  }

  void setAuthor(String author)
  {
    m_author = author;
  }

  public String getAuthor()
  {
    return m_author;
  }

  void setProjectURL(String projectURL)
  {
    m_projectURL = projectURL;
  }

  public String getProjectURL()
  {
    return m_projectURL;
  }

  IconData addIcon()
  {
    IconData iconData;

    iconData = new IconData();
    m_iconDataList.add(iconData);

    return iconData;
  }

  public List<String> getIconNameList()
  {
    if (m_iconList == null)
    {
      m_iconList = getIconDataList().stream().map(IconData::getId).collect(Collectors.toList());
    }
    return m_iconList;
  }

  public Map<String, IconData> getIconDataByIdMap()
  {
    if (m_iconDataByIdMap == null)
    {
      m_iconDataByIdMap = getIconDataList().stream()
          .collect(Collectors.toMap(IconData::getId, Function.identity(), (m1, m2) -> m1));
    }
    return m_iconDataByIdMap;
  }

  public List<IconData> getIconDataList()
  {
    return m_iconDataList;
  }

  @Override
  public int compareTo(IconSetData ifd)
  {
    return getName().compareTo(ifd.getName());
  }

  public static class IconSetDataHolder
  {
    private final String mi_id;
    private final String mi_prefix;
    private Supplier<IconSetData> mi_iconSetDataSupplier;
    private IconSetData mi_iconSetData;

    public IconSetDataHolder(String id, String prefix, Supplier<IconSetData> iconSetDataSupplier)
    {
      mi_id = id;
      mi_prefix = prefix;
      mi_iconSetDataSupplier = iconSetDataSupplier;
    }

    public String getId()
    {
      return mi_id;
    }

    public String getPrefix()
    {
      return mi_prefix;
    }

    public IconSetData getIconSetData()
    {
      if (mi_iconSetData == null)
      {
        mi_iconSetData = mi_iconSetDataSupplier.get();
      }

      return mi_iconSetData;
    }
  }

  public class IconData
      implements Comparable<IconData>
  {
    private String mi_id;
    private int mi_left = 0;
    private int mi_top = 0;
    private int mi_width = -1;
    private int mi_height = -1;
    private String mi_name;
    private String mi_svgText;
    private Rotation mi_rotate;
    private Flip mi_hFlip;
    private Flip mi_vFlip;
    private List<Alias> mi_aliasList;
    private List<Category> mi_categoryList;

    IconData()
    {
    }

    public IconSetData getIconSetData()
    {
      return IconSetData.this;
    }

    public String getId()
    {
      if (mi_id == null)
      {
        mi_id = getIconSetData().getPrefix() + "-" + mi_name;
      }

      return mi_id;
    }

    public String getPrefix()
    {
      return getIconSetData().getPrefix();
    }

    public String getName()
    {
      return mi_name;
    }

    void setName(String name)
    {
      mi_name = name;
      mi_id = null;
    }

    void setLeft(int left)
    {
      mi_left = left;
    }

    public int getLeft()
    {
      return mi_left;
    }

    void setTop(int top)
    {
      mi_top = top;
    }

    public int getTop()
    {
      return mi_top;
    }

    void setWidth(int width)
    {
      mi_width = width;
    }

    public int getWidth()
    {
      return mi_width;
    }

    void setHeight(int height)
    {
      mi_height = height;
    }

    public int getHeight()
    {
      return mi_height;
    }

    void setRotate(Rotation rotateDegrees)
    {
      mi_rotate = rotateDegrees;
    }

    public Rotation getRotate()
    {
      return mi_rotate;
    }

    void setHFlip(Flip hFlip)
    {
      mi_hFlip = hFlip;
    }

    public Flip getHFlip()
    {
      return mi_hFlip;
    }

    void setVFlip(Flip vFlip)
    {
      mi_vFlip = vFlip;
    }

    public Flip getVFlip()
    {
      return mi_vFlip;
    }

    Alias addAlias(String aliasName)
    {
      Alias alias;

      if (mi_aliasList == null)
      {
        mi_aliasList = new ArrayList<>();
      }
      alias = new Alias(aliasName);
      mi_aliasList.add(alias);

      return alias;
    }

    public List<Alias> getAliasList()
    {
      return mi_aliasList == null ? Collections.emptyList() : mi_aliasList;
    }

    Category addCategory(String text)
    {
      Category category;

      if (mi_categoryList == null)
      {
        mi_categoryList = new ArrayList<>();
      }
      category = new Category(text);
      mi_categoryList.add(category);

      return category;
    }

    public List<Category> getCategoryList()
    {
      return mi_categoryList == null ? Collections.emptyList() : mi_categoryList;
    }

    public class Alias
    {
      private final String mi_alias;

      Alias(String alias)
      {
        mi_alias = alias;
      }

      public String getAlias()
      {
        return mi_alias;
      }

    }

    public class Category
    {
      private final String mi_category;
      private final String mi_categoryLowerCase;

      Category(String category)
      {
        mi_category = category;
        mi_categoryLowerCase = category.toLowerCase();
      }

      public String getCategory()
      {
        return mi_category;
      }

      public String getCategoryLowerCase()
      {
        return mi_categoryLowerCase;
      }
    }

    void setSVGText(String svg)
    {
      mi_svgText = svg;
    }

    public String getSVGText()
    {
      return mi_svgText;
    }

    public String getSVGDocumentText()
    {
      StringBuilder builder;
      String header;

      header = "<svg xmlns=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" width=\"${width}\" height=\"${height}\" viewBox=\"${left} ${top} ${width} ${height}\">";
      builder = new StringBuilder(header);
      builder.append("\n");
      builder.append(mi_svgText);
      builder.append("\n");
      builder.append("</svg>\n");

      return builder.toString();
    }

    @Override
    public int compareTo(IconData ifd)
    {
      return getId().compareTo(ifd.getId());
    }

    @Override
    public String toString()
    {
      return "Icon[" + getId() + "]";
    }
  }

  @Override
  public String toString()
  {
    return getName();
  }
}
