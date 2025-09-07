package org.kku.iconify.data;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;

public class IconSetData
    implements Comparable<IconSetData>
{
  private static int ICONIFY_DEFAULT_WIDTH = 16;
  private static int ICONIFY_DEFAULT_HEIGHT = 16;
  private static int ICONIFY_DEFAULT_LEFT = 0;
  private static int ICONIFY_DEFAULT_TOP = 0;
  private static Rotation ICONIFY_DEFAULT_ROTATE = Rotation.ROTATE_0;
  private static Flip ICONIFY_DEFAULT_HFLIP = Flip.FALSE;
  private static Flip ICONIFY_DEFAULT_VFLIP = Flip.FALSE;

  private static Map<String, IconSetDataHolder> m_iconSetDataByIdMap;
  private static List<IconSetData> m_iconSetDataList;
  private static Set<String> m_allCategoryList = new HashSet<>();
  public static final String ALL = "ALL";

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

  private IconSetData()
  {
  }

  public boolean isAll()
  {
    return ALL.equals(getId());
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

  private void setLeft(int left)
  {
    m_left = left;
  }

  public int getLeft()
  {
    return m_left;
  }

  private void setTop(int top)
  {
    m_top = top;
  }

  public int getTop()
  {
    return m_top;
  }

  public void setWidth(int width)
  {
    m_width = width;
  }

  public int getWidth()
  {
    return m_width;
  }

  public void setHeight(int height)
  {
    m_height = height;
  }

  public int getHeight()
  {
    return m_height;
  }

  private void setRotate(Rotation rotate)
  {
    m_rotate = rotate;
  }

  public Rotation getRotate()
  {
    return m_rotate;
  }

  private void setHFlip(Flip hFlip)
  {
    m_hFlip = hFlip;
  }

  public Flip getHFlip()
  {
    return m_hFlip;
  }

  private void setVFlip(Flip vFlip)
  {
    m_vFlip = vFlip;
  }

  public Flip getVFlip()
  {
    return m_vFlip;
  }

  private void setNumberOfIcons(int numberOfIcons)
  {
    m_numberOfIcons = numberOfIcons;
  }

  public int getNumberOfIcons()
  {
    return m_numberOfIcons;
  }

  private void setVersion(String version)
  {
    m_version = version;
  }

  public String getVersion()
  {
    return m_version;
  }

  private void setName(String name)
  {
    m_name = name;
  }

  public String getName()
  {
    return m_name;
  }

  private void setLicenseURL(String licenseURL)
  {
    m_licenseURL = licenseURL;
  }

  public String getLicenseURL()
  {
    return m_licenseURL;
  }

  private void setLicenseName(String licenseName)
  {
    m_licenseName = licenseName;
  }

  public String getLicenseName()
  {
    return m_licenseName;
  }

  private void setCategory(String category)
  {
    m_category = category;
  }

  public String getCategory()
  {
    return m_category;
  }

  private void setAuthor(String author)
  {
    m_author = author;
  }

  public String getAuthor()
  {
    return m_author;
  }

  private void setProjectURL(String projectURL)
  {
    m_projectURL = projectURL;
  }

  public String getProjectURL()
  {
    return m_projectURL;
  }

  private IconData addIcon()
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
      if(mi_iconSetData == null )
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

    private void setName(String name)
    {
      mi_name = name;
      mi_id = null;
    }

    private void setLeft(int left)
    {
      mi_left = left;
    }

    public int getLeft()
    {
      return mi_left;
    }

    private void setTop(int top)
    {
      mi_top = top;
    }

    public int getTop()
    {
      return mi_top;
    }

    private void setWidth(int width)
    {
      mi_width = width;
    }

    public int getWidth()
    {
      return mi_width;
    }

    private void setHeight(int height)
    {
      mi_height = height;
    }

    public int getHeight()
    {
      return mi_height;
    }

    private void setRotate(Rotation rotateDegrees)
    {
      mi_rotate = rotateDegrees;
    }

    public Rotation getRotate()
    {
      return mi_rotate;
    }

    private void setHFlip(Flip hFlip)
    {
      mi_hFlip = hFlip;
    }

    public Flip getHFlip()
    {
      return mi_hFlip;
    }

    private void setVFlip(Flip vFlip)
    {
      mi_vFlip = vFlip;
    }

    public Flip getVFlip()
    {
      return mi_vFlip;
    }

    private Alias addAlias(String aliasName)
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

    private Category addCategory(String text)
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

    private void setSVGText(String svg)
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

  public static synchronized Collection<IconSetData> getIconSetDataCollection()
  {
    if (m_iconSetDataList == null)
    {
      m_iconSetDataList = getIconSetDataByIdMap().values().stream().map(IconSetDataHolder::getIconSetData).collect(Collectors.toList());
    }

    return m_iconSetDataList;
  }

  public static synchronized Map<String, IconSetDataHolder> getIconSetDataByIdMap()
  {
    if (m_iconSetDataByIdMap == null)
    {
      m_iconSetDataByIdMap = new LinkedHashMap<>();
      m_iconSetDataByIdMap.put(ALL, new IconSetDataHolder(ALL, "all",  () -> generateAll()));

      for (String id : parseIconifyIconSets())
      {
        m_iconSetDataByIdMap.put(id, new IconSetDataHolder(id, id, () -> parseIconify(id)));
      }
    }
    return m_iconSetDataByIdMap;
  }
  
  private static List<String> parseIconifyIconSets()
  {
    ObjectReader reader;
    String url;
    List<String> result;
    
    url = "/module-resources/iconify/IconifyIconSets.json";
    reader = new ObjectMapper().reader();
    result = new ArrayList<>();
    
    System.out.println("parseIconify: " + url);
    try (InputStream is = IconSetData.class.getResourceAsStream(url))
    {
      JsonNode rootNode;

      rootNode = reader.readTree(IconSetData.class.getResourceAsStream(url));
      JsonNode a = rootNode.path("icon-sets");
      a.forEach(System.out::println);
      
      rootNode.path("icon-sets").forEach(idNode -> {
        System.out.print(idNode);
        System.out.print(idNode.asText());
        result.add(idNode.asText());
      });
    }
    catch (Exception e)
    {
      System.err.println("Error parsing JSON: " + e.getMessage());
      e.printStackTrace();
    }

    return result;
  }

  private static IconSetData generateAll()
  {
    IconSetData all;
    
      all= new IconSetData();
      all.setId(ALL);
      all.setName("All");
      
      return all;
  }
  
  private static IconSetData parseIconify(String name)
  {
    ObjectReader reader;
    String url;

    url = "/module-resources/iconify/" + name + ".json";
    reader = new ObjectMapper().reader();
    
    System.out.println("parseIconify: " + url);
    try (InputStream is = IconSetData.class.getResourceAsStream(url))
    {
      JsonNode rootNode;
      JsonNode infoNode;
      IconSetData iconSetData;
      Map<String, IconData> iconMap;

      iconMap = new HashMap<>();

      rootNode = reader.readTree(IconSetData.class.getResourceAsStream(url));
      iconSetData = new IconSetData();

      iconSetData.setId(rootNode.path("prefix").asText());
      iconSetData.setPrefix(rootNode.path("prefix").asText());

      iconSetData.setLeft(rootNode.path("left").asInt(ICONIFY_DEFAULT_LEFT));
      iconSetData.setTop(rootNode.path("top").asInt(ICONIFY_DEFAULT_TOP));
      iconSetData.setWidth(rootNode.path("width").asInt(ICONIFY_DEFAULT_WIDTH));
      iconSetData.setHeight(rootNode.path("height").asInt(ICONIFY_DEFAULT_HEIGHT));
      iconSetData.setRotate(Rotation.get(rootNode.path("rotate").asInt(ICONIFY_DEFAULT_ROTATE.get())));
      iconSetData.setHFlip(Flip.get(rootNode.path("hFlip").asBoolean(ICONIFY_DEFAULT_HFLIP.get())));
      iconSetData.setVFlip(Flip.get(rootNode.path("vFlip").asBoolean(ICONIFY_DEFAULT_VFLIP.get())));

      infoNode = rootNode.path("info");
      iconSetData.setName(infoNode.path("id").asText());
      iconSetData.setName(infoNode.path("name").asText());
      iconSetData.setNumberOfIcons(infoNode.path("total").asInt());
      iconSetData.setVersion(infoNode.path("version").asText());
      iconSetData.setAuthor(infoNode.path("author").path("name").asText());
      iconSetData.setProjectURL(infoNode.path("author").path("url").asText());
      iconSetData.setCategory(infoNode.path("category").asText());
      iconSetData.setLicenseName(infoNode.path("license").path("spdx").asText());
      iconSetData.setLicenseURL(infoNode.path("license").path("url").asText());

      rootNode.path("icons").properties().stream().forEach(e -> {
        String iconName;
        JsonNode iconNode;
        IconData iconData;

        iconName = e.getKey();
        iconNode = e.getValue();

        iconData = iconSetData.addIcon();
        iconData.setName(iconName);
        iconData.setSVGText(iconNode.path("body").asText());
        iconData.setLeft(iconNode.path("left").asInt(iconSetData.getLeft()));
        iconData.setTop(iconNode.path("top").asInt(iconSetData.getTop()));
        iconData.setWidth(iconNode.path("width").asInt(iconSetData.getWidth()));
        iconData.setHeight(iconNode.path("height").asInt(iconSetData.getHeight()));
        iconData.setRotate(
            iconSetData.getRotate().add(Rotation.get(iconNode.path("rotate").asInt(ICONIFY_DEFAULT_ROTATE.get()))));
        iconData.setHFlip(
            iconSetData.getHFlip().add(Flip.get(iconNode.path("hFlip").asBoolean(ICONIFY_DEFAULT_HFLIP.get()))));
        iconData.setVFlip(
            iconSetData.getVFlip().add(Flip.get(iconNode.path("vFlip").asBoolean(ICONIFY_DEFAULT_VFLIP.get()))));

        iconMap.put(iconData.getName(), iconData);
      });

      rootNode.path("aliases").properties().stream().forEach(e -> {
        String aliasName;
        JsonNode aliasNode;
        String parentName;
        IconData parentIconData;

        aliasName = e.getKey();
        aliasNode = e.getValue();
        parentName = aliasNode.path("parent").asText();
        parentIconData = iconMap.get(parentName);
        if (parentIconData != null)
        {
          IconData aliasIconData;

          aliasIconData = iconSetData.addIcon();
          parentIconData.addAlias(aliasName);

          aliasIconData.setName(aliasName);
          aliasIconData.setLeft(aliasNode.path("left").asInt(parentIconData.getLeft()));
          aliasIconData.setTop(aliasNode.path("top").asInt(parentIconData.getTop()));
          aliasIconData.setWidth(aliasNode.path("width").asInt(parentIconData.getWidth()));
          aliasIconData.setHeight(aliasNode.path("height").asInt(parentIconData.getHeight()));
          aliasIconData.setSVGText(parentIconData.getSVGText());
          aliasIconData.setRotate(Rotation.get(aliasNode.path("rotate").asInt(ICONIFY_DEFAULT_ROTATE.get()))
              .add(parentIconData.getRotate()));
          aliasIconData.setHFlip(
              Flip.get(aliasNode.path("hFlip").asBoolean(ICONIFY_DEFAULT_HFLIP.get())).add(parentIconData.getHFlip()));
          aliasIconData.setVFlip(
              Flip.get(aliasNode.path("vFlip").asBoolean(ICONIFY_DEFAULT_VFLIP.get())).add(parentIconData.getVFlip()));
        }
      });

      rootNode.path("categories").properties().stream().forEach(e -> {
        String categorieName;
        JsonNode categorieNode;

        categorieName = e.getKey();
        categorieNode = e.getValue();

        categorieNode.elements().forEachRemaining(iconName -> {
          IconData icon;

          icon = iconMap.get(iconName.asText());
          if (icon != null)
          {
            icon.addCategory(categorieName);
            m_allCategoryList.add(categorieName);
          }
        });
      });

      return iconSetData;
    }
    catch (Exception e)
    {
      System.err.println("Error parsing JSON: " + e.getMessage());
      e.printStackTrace();
    }
    
    return null;
  }

  public static IconData searchIconData(String iconId)
  {
    Optional<IconSetDataHolder> iconSetDataHolder;
    
    iconSetDataHolder = getIconSetDataByIdMap().values().stream().filter(holder -> iconId.startsWith(holder.getPrefix())).findFirst();
    if(iconSetDataHolder.isPresent())
    {
      return iconSetDataHolder.get().getIconSetData().getIconDataByIdMap().get(iconId);
    }

    return null;
  }

  @Override
  public String toString()
  {
    return getName();
  }
}
