package org.kku.iconify.data;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.kku.iconify.data.IconSetData.Flip;
import org.kku.iconify.data.IconSetData.IconData;
import org.kku.iconify.data.IconSetData.IconSetDataHolder;
import org.kku.iconify.data.IconSetData.Rotation;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;

public class IconSets
{
  private static final IconSets INSTANCE = new IconSets();

  private static int ICONIFY_DEFAULT_WIDTH = 16;
  private static int ICONIFY_DEFAULT_HEIGHT = 16;
  private static int ICONIFY_DEFAULT_LEFT = 0;
  private static int ICONIFY_DEFAULT_TOP = 0;
  private static Rotation ICONIFY_DEFAULT_ROTATE = Rotation.ROTATE_0;
  private static Flip ICONIFY_DEFAULT_HFLIP = Flip.FALSE;
  private static Flip ICONIFY_DEFAULT_VFLIP = Flip.FALSE;

  public static final String ALL = "ALL";

  private Map<String, IconSetDataHolder> m_iconSetDataByIdMap;
  private List<IconSetData> m_iconSetDataList;
  private Set<String> m_allCategoryList = new HashSet<>();

  private IconSets()
  {
  }

  public static IconSets get()
  {
    return INSTANCE;
  }

  public synchronized Collection<IconSetData> getIconSetDataCollection()
  {
    if (m_iconSetDataList == null)
    {
      m_iconSetDataList = getIconSetDataByIdMap().values().stream().map(IconSetDataHolder::getIconSetData)
          .collect(Collectors.toList());
    }

    return m_iconSetDataList;
  }

  public synchronized Map<String, IconSetDataHolder> getIconSetDataByIdMap()
  {
    if (m_iconSetDataByIdMap == null)
    {
      m_iconSetDataByIdMap = new LinkedHashMap<>();
      m_iconSetDataByIdMap.put(ALL, new IconSetDataHolder(ALL, "all", () -> generateAll()));

      for (String id : parseIconifyIconSets())
      {
        m_iconSetDataByIdMap.put(id, new IconSetDataHolder(id, id, () -> parseIconify(id)));
      }
    }
    return m_iconSetDataByIdMap;
  }

  private List<String> parseIconifyIconSets()
  {
    ObjectReader reader;
    String url;
    List<String> result;

    url = "/module-resources/iconify/IconifyIconSets.json";
    reader = new ObjectMapper().reader().at("/icon-sets");
    result = new ArrayList<>();

    System.out.println("parseIconify: " + url);
    try (InputStream is = IconSetData.class.getResourceAsStream(url))
    {
      JsonNode iconSetsNode;

      iconSetsNode = reader.readTree(IconSetData.class.getResourceAsStream(url));
      iconSetsNode.forEach(idNode -> {
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

    all = new IconSetData();
    all.setId(ALL);
    all.setName("All");

    return all;
  }

  private IconSetData parseIconify(String name)
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

  public IconData searchIconData(String iconId)
  {
    Optional<IconSetDataHolder> iconSetDataHolder;

    iconSetDataHolder = getIconSetDataByIdMap().values().stream()
        .filter(holder -> iconId.startsWith(holder.getPrefix())).findFirst();
    if (iconSetDataHolder.isPresent())
    {
      return iconSetDataHolder.get().getIconSetData().getIconDataByIdMap().get(iconId);
    }

    return null;
  }
}
