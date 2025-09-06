package org.kku.fonticons.iconify;

import java.io.BufferedWriter;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.stream.Collectors;
import org.gradle.api.logging.Logger;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;

/**
 * Retrieves all icon sets from Iconify.
 * 
 * Downloads an icon set only if it is missing or has been updated.
 */
class IconifyDownload
{
  private final Path m_destinationDir;
  private final String m_baseURL;
  private List<IconSet> m_iconSetList;
  private Logger mi_logger;

  IconifyDownload(Path destinationDir, String baseURL)
  {
    m_destinationDir = destinationDir;
    m_baseURL = baseURL;
  }

  void execute()
  {
    long time = System.currentTimeMillis();
    try
    {
      Files.createDirectories(m_destinationDir);

      // Retrieve all supported icon-sets of Iconify
      createIconSetList(retrieveLastModifiedFile());

      // Download all missing/changed icon-set files
      retrieveIconSetFiles();

      // Generate a json file with all available icon-sets
      generateIconifyIconsFile();
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }

    info("execute took " + (System.currentTimeMillis() - time) + " msec");
  }

  private void retrieveIconSetFiles() throws Exception
  {
    ObjectReader reader;

    reader = new ObjectMapper().reader().at("/lastModified");

    for (IconSet iconSet : m_iconSetList)
    {
      Path iconSetPath;
      boolean download;

      download = false;
      iconSetPath = m_destinationDir.resolve(iconSet.id() + ".json");
      if (!Files.exists(iconSetPath))
      {
        // If file is missing it will be retrieved
        download = true;
        info("not exist: " + iconSetPath);
      }
      else
      {
        JsonNode lastModifiedNode;

        // If file has changed it will be retrieved
        // The last modification date of an icon-set is contained in the icon-set file
        // itself
        lastModifiedNode = reader.readTree(Files.readString(iconSetPath));
        if (lastModifiedNode.isNumber())
        {
          // Compare the last modification date of the icon-set to the current last
          // modificaton date on the website of Iconify
          if (lastModifiedNode.asInt() != iconSet.lastModification())
          {
            download = true;
            info("changed  : " + iconSetPath);
          }
          else
          {
            info("uptodate : " + iconSetPath);
          }
        }
        else
        {
          info("Cannot find lastModified node in " + iconSetPath);
        }
      }

      if (download)
      {
        info("download : " + iconSetPath);
        downloadIconSetFile(iconSet.id());
      }
    }
  }

  class IconSet
  {
    private final String mi_id;
    private final int mi_lastModification;

    private IconSet(String id, int lastModification)
    {
      mi_id = id;
      mi_lastModification = lastModification;
    }

    public String id()
    {
      return mi_id;
    }

    public int lastModification()
    {
      return mi_lastModification;
    }
  }

  private void createIconSetList(String lastModifiedFileJson) throws Exception
  {
    ObjectReader reader;
    JsonNode lastModifiedNode;

    reader = new ObjectMapper().reader().at("/lastModified");
    lastModifiedNode = reader.readTree(lastModifiedFileJson);

    m_iconSetList = lastModifiedNode.properties().stream().map(e -> new IconSet(e.getKey(), e.getValue().intValue()))
        .collect(Collectors.toList());
  }

  private String retrieveLastModifiedFile() throws Exception
  {
    String url;
    HttpClient client;
    HttpRequest request;
    HttpResponse<String> response;

    url = "https://api.iconify.design/last-modified";
    client = HttpClient.newHttpClient();
    request = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();

    response = client.send(request, HttpResponse.BodyHandlers.ofString());
    if (response.statusCode() == 200)
    {
      return response.body();
    }
    else
    {
      info("Failed to fetch data. Status code: " + response.statusCode());
    }

    return "";
  }

  private void downloadIconSetFile(String collection) throws Exception
  {
    URL url;
    String fileName;

    fileName = collection + ".json";
    info("download " + fileName);
    url = new URI(m_baseURL).resolve("json/" + fileName).toURL();
    try (InputStream inputStream = url.openStream())
    {
      Files.copy(inputStream, m_destinationDir.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
    }
  }

  void setLogger(Logger logger)
  {
    mi_logger = logger;
  }

  private void info(String log)
  {
    if (mi_logger != null)
    {
      mi_logger.info(log);
    }
    else
    {
      System.out.println(log);
    }
  }

  private void generateIconifyIconsFile() throws Exception
  {
    Path jsonPath;

    jsonPath = m_destinationDir.resolve("IconifyIconSets.json");
    try (BufferedWriter jsonWriter = Files.newBufferedWriter(jsonPath);
        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(jsonWriter))
    {
      String[] iconSetArray;

      jsonGenerator.setPrettyPrinter(new DefaultPrettyPrinter());

      iconSetArray = m_iconSetList.stream().map(IconSet::id).sorted().toArray(String[]::new);
      jsonGenerator.writeStartObject();
      jsonGenerator.writeFieldName("icon-sets");
      jsonGenerator.writeArray(iconSetArray, 0, iconSetArray.length);
      jsonGenerator.writeEndObject();

    }
  }

  public static void main(String[] args)
  {
    Path destinationDir;
    String collectionURL;

    if (args.length < 2)
    {
      System.out.println("Usage: ");
      System.out.println("java IconifyDownload <destination dir> <url for collections.json>");
      System.exit(-1);
    }

    destinationDir = Paths.get(args[0]);
    collectionURL = args[1];

    try
    {
      new IconifyDownload(destinationDir, collectionURL).execute();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }
}
