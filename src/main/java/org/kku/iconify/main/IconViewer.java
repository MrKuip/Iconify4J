package org.kku.iconify.main;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.kku.iconify.data.IconSetData;
import org.kku.iconify.data.IconSetData.IconData;
import org.kku.iconify.data.IconSetData.IconData.Category;
import org.kku.iconify.main.view.SVGEditor;
import org.kku.iconify.scene.control.TabPaneNode;
import org.kku.iconify.ui.FxIcon;
import org.kku.iconify.ui.FxIcon.IconSize;
import org.tbee.javafx.scene.layout.MigPane;
import javafx.application.Application;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.IndexedCell;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.skin.TableViewSkin;
import javafx.scene.control.skin.VirtualFlow;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

public class IconViewer
  extends Application
{
  private final static int NUMBER_OF_COLUMNS = 10;
  private TabPaneNode m_tabbedPaneNode;
  private TableView<IconRow> m_iconTableView;
  private TableView<Icon> m_iconView;
  private Map<String, List<Icon>> m_iconListByIdMap;
  private final ObservableList<Icon> m_iconList = FXCollections.observableArrayList();
  private FilteredList<Icon> m_filteredIconList = new FilteredList<Icon>(m_iconList);
  private IconSetData m_selectedIconSetData;

  @Override
  public void start(Stage stage) throws IOException
  {
    MigPane pane;

    m_iconListByIdMap = getIconMap();

    m_iconTableView = getIconTableView();
    m_iconView = getIconView();

    pane = new MigPane("", "[grow][]", "[][][][grow]");
    pane.add(getFilterPane(), "cell 0 0 2 1, grow");
    pane.add(m_iconTableView, "cell 0 1 1 3, grow");
    pane.add(getButtonPane(), "cell 1 1, grow");
    pane.add(getIconSetInfoNode(), "cell 1 2, width 30%, grow");
    pane.add(m_iconView, "cell 1 3, grow, width pref");

    m_iconView.getSelectionModel().select(0);

    m_tabbedPaneNode = new TabPaneNode(pane);

    Scene scene = new Scene(m_tabbedPaneNode, 1200, 600, false, SceneAntialiasing.DISABLED);
    stage.setTitle("Iconify icons");
    stage.setScene(scene);
    stage.show();
  }

  private Node getFilterPane()
  {
    MigPane pane;
    Node filterLabel;
    Node menuButton;
    TextField filterField;
    ComboBox<IconSetData> comboBox;

    filterLabel = new FxIcon("mdi-filter").getNode();

    filterField = new TextField();
    filterField.setPromptText("Filter by name...");
    filterField.textProperty().addListener((obs, oldValue, newValue) -> {
      Icon selectedIcon;
      String[] filterValues;

      filterValues = newValue.toLowerCase().split(" ");

      selectedIcon = m_iconView.getSelectionModel().getSelectedItem();
      m_filteredIconList.setPredicate(data -> {
        if (filterValues != null && filterValues.length > 0)
        {
          for (String filter : filterValues)
          {
            if (!data.getIdLowerCase().contains(filter))
            {
              boolean found = false;
              for (Category categorie : data.getIconData().getCategoryList())
              {
                if (categorie.getCategoryLowerCase().contains(filter))
                {
                  found = true;
                  break;
                }
              }
              return found;
            }
          }
        }
        return true;
      });
      m_iconView.getSelectionModel().select(selectedIcon);
    });

    comboBox = new ComboBox<>();
    comboBox.setConverter(null);
    comboBox.getItems().addAll(IconSetData.getIconSetDataCollection().stream().collect(Collectors.toList()));
    comboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
      m_selectedIconSetData = newValue;
      m_iconList.setAll(m_iconListByIdMap.getOrDefault(m_selectedIconSetData.getId(), Collections.emptyList()));
    });
    comboBox.getSelectionModel().select(IconSetData.getIconSetDataByIdMap().get(IconSetData.ALL));

    menuButton = new Button(null, new FxIcon("mdi-menu").size(IconSize.SMALL).getNode());

    // HBox.setHgrow(label, Priority.NEVER);
    HBox.setHgrow(filterField, Priority.ALWAYS);
    HBox.setHgrow(comboBox, Priority.NEVER);

    pane = new MigPane("", "[][grow, fill][][][]", "[baseline]");
    pane.add(filterLabel, "aligny center");
    pane.add(filterField, "");
    pane.add(new Label("Icon set:"));
    pane.add(comboBox);
    pane.add(menuButton);

    return pane;
  }

  private Node getButtonPane()
  {
    MigPane pane;

    pane = new MigPane("", "[]push[]push[]push[]push[]", "[]");

    for (IconSize iconSize : FxIcon.IconSize.values())
    {
      Button button;

      button = new Button();
      m_iconView.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, selectedIcon) -> {
        if (selectedIcon != null)
        {
          Node iconLabel;

          iconLabel = selectedIcon.getFxIcon().size(iconSize).getNode();
          button.graphicProperty().set(iconLabel);
        }
      });

      pane.add(button, "");
    }

    return pane;
  }

  private Node getIconSetInfoNode()
  {
    MigPane pane;
    Label glyphNameText;
    Button editSVGButton;
    Label versionText;
    Label authorText;
    Hyperlink licenseURLHyperlink;
    Hyperlink projectURLHyperlink;
    Label numberOfIconsText;
    Label prefixText;

    pane = new MigPane("fill, wrap 2", "[shrink 0][grow, push]");

    glyphNameText = new Label();
    pane.add(new Label("Glyph name"));
    pane.add(glyphNameText);

    editSVGButton = new Button("Edit svg");
    pane.add(new Label("SVG icon"));
    pane.add(editSVGButton);

    prefixText = new Label();
    pane.add(new Label("Prefix"));
    pane.add(prefixText);

    versionText = new Label();
    pane.add(new Label("Version"));
    pane.add(versionText);

    authorText = new Label();
    pane.add(new Label("Author"));
    pane.add(authorText);

    projectURLHyperlink = new Hyperlink();
    projectURLHyperlink.setFocusTraversable(false);
    pane.add(new Label("Website"));
    pane.add(projectURLHyperlink);

    licenseURLHyperlink = new Hyperlink();
    licenseURLHyperlink.setFocusTraversable(false);
    pane.add(new Label("License url"));
    pane.add(licenseURLHyperlink);

    numberOfIconsText = new Label();
    pane.add(new Label("number of icons"));
    pane.add(numberOfIconsText);

    m_iconView.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
      FxIcon fxIcon;
      IconData iconData;
      IconSetData iconSetData;

      fxIcon = newValue == null ? null : newValue.getFxIcon();
      iconData = newValue == null ? null : newValue.getIconData();
      iconSetData = iconData == null ? null : iconData.getIconSetData();

      glyphNameText.setText(iconSetData != null ? newValue.getName() : "");
      prefixText.setText(iconSetData != null ? newValue.getPrefix() : "");
      editSVGButton.onActionProperty().set((a) -> {
        SVGEditor svgEditor;
        svgEditor = new SVGEditor(fxIcon);
        m_tabbedPaneNode.addTab(iconData.getId(), svgEditor.getEditor(), true);
      });
      versionText.setText(iconSetData != null ? iconSetData.getVersion() : "");
      authorText.setText(iconSetData != null ? iconSetData.getAuthor() : "");
      projectURLHyperlink.setText(iconSetData != null ? iconSetData.getProjectURL() : "");
      projectURLHyperlink.setOnAction((ae) -> { browse(projectURLHyperlink.getText()); });
      licenseURLHyperlink.setText(iconSetData != null ? iconSetData.getLicenseName() : "");
      licenseURLHyperlink.setOnAction((ae) -> { browse(iconSetData.getLicenseURL()); });
      numberOfIconsText.setText(iconSetData != null ? "" + iconSetData.getNumberOfIcons() : "");
    });

    return pane;
  }

  /**
   * Launches default browser to display a uri.
   * 
   * @param url
   */
  private void browse(String url)
  {
    // This MUST happen in a separate Thread otherwise the JavaFX Thread will be blocked.
    new Thread(() -> {
      try
      {
        Desktop.getDesktop().browse(new URI(url));
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
    }).start();
  }

  @SuppressWarnings("rawtypes")
  private TableView<IconRow> getIconTableView()
  {
    TableView<IconRow> tableView;
    TableColumn<IconRow, Node> column;

    tableView = new TableView<>();
    tableView.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
    tableView.getSelectionModel().setCellSelectionEnabled(true);
    tableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    tableView.getSelectionModel().getSelectedCells().addListener(((ListChangeListener<TablePosition>) change -> {
      tableView.getSelectionModel().getSelectedCells().forEach(selectedCell -> {
        int rowIndex;

        if (selectedCell.getColumn() > 0)
        {
          rowIndex = selectedCell.getRow() * NUMBER_OF_COLUMNS + selectedCell.getColumn() - 1;
          m_iconView.getSelectionModel().select(rowIndex);
          if (!isRowVisible(m_iconView, rowIndex))
          {
            m_iconView.scrollTo(rowIndex);
          }
        }
      });
    }));

    m_filteredIconList.addListener((ListChangeListener<Icon>) change -> {
      m_iconTableView.setItems(getIconRowList());
    });

    tableView.setMaxWidth(Double.MAX_VALUE);

    addRankColumn(tableView, "Row", NUMBER_OF_COLUMNS);

    for (int columnIndex = 0; columnIndex < NUMBER_OF_COLUMNS; columnIndex++)
    {
      final int colIndex;

      colIndex = columnIndex;
      column = new TableColumn<>(String.valueOf(columnIndex + 1));
      column.setCellValueFactory(cb -> {
        List<Icon> list = cb.getValue().getIconList();
        if (colIndex < 0 || colIndex >= list.size())
        {
          return null;
        }

        return new SimpleObjectProperty<>(list.get(colIndex).getIconLabel());
      });
      tableView.getColumns().add(column);
    }

    tableView.setItems(getIconRowList());

    return tableView;
  }

  private TableView<Icon> getIconView()
  {
    TableView<Icon> tableView;
    TableColumn<Icon, String> nameColumn;
    TableColumn<Icon, Node> iconLabelColumn;

    tableView = new TableView<>();
    tableView.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
    tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
      int index;
      int rowIndex;
      int columnIndex;
      TableColumn<IconRow, ?> column;

      index = m_iconView.getSelectionModel().getSelectedIndex();
      if (index > -1)
      {
        rowIndex = index / NUMBER_OF_COLUMNS;
        columnIndex = index % NUMBER_OF_COLUMNS + 1;

        column = m_iconTableView.getColumns().get(columnIndex);

        m_iconTableView.getSelectionModel().select(rowIndex, column);
        if (!isRowVisible(m_iconTableView, rowIndex))
        {
          m_iconTableView.scrollTo(rowIndex);
        }
      }
    });
    tableView.setMaxWidth(Double.MAX_VALUE);
    nameColumn = new TableColumn<>("Name");
    nameColumn.setCellValueFactory(cb -> new SimpleStringProperty(cb.getValue().getId()));
    tableView.getColumns().add(nameColumn);
    iconLabelColumn = new TableColumn<>("Icon");
    iconLabelColumn.setCellValueFactory(cb -> new SimpleObjectProperty<>(cb.getValue().getIconLabel()));
    tableView.getColumns().add(iconLabelColumn);

    tableView.setItems(m_filteredIconList);

    return tableView;
  }

  private boolean isRowVisible(TableView<?> tableView, int rowIndex)
  {
    Object o;
    TableViewSkin<?> ts;
    VirtualFlow<?> vf;
    int firstIndex;
    int lastIndex;
    IndexedCell<?> firstCell;
    IndexedCell<?> lastCell;

    o = tableView.getSkin();
    if (!(o instanceof TableViewSkin<?>))
    {
      return false;
    }
    ts = (TableViewSkin<?>) o;

    o = ts.getChildren().stream().filter(node -> node instanceof VirtualFlow).findFirst().orElse(null);
    if (o == null || !(o instanceof VirtualFlow<?>))
    {
      return false;
    }
    vf = (VirtualFlow<?>) o;

    firstCell = vf.getFirstVisibleCell();
    lastCell = vf.getLastVisibleCell();

    if (firstCell != null && lastCell != null)
    {
      firstIndex = firstCell.getIndex();
      lastIndex = lastCell.getIndex();

      return rowIndex >= firstIndex && rowIndex <= lastIndex;
    }

    return false;
  }

  private ObservableList<IconRow> getIconRowList()
  {
    IconRow iconRow;
    List<IconRow> iconRowList;

    iconRowList = new ArrayList<>();
    iconRow = null;

    for (Icon icon : m_filteredIconList)
    {
      if (iconRow == null)
      {
        iconRow = new IconRow();
        iconRowList.add(iconRow);
      }

      iconRow.add(icon);
      if (iconRow.getIconList().size() >= NUMBER_OF_COLUMNS)
      {
        iconRow = null;
      }
    }

    return FXCollections.observableArrayList(iconRowList);
  }

  private Map<String, List<Icon>> getIconMap()
  {
    return IconSetData.getIconSetDataCollection().stream()
        .collect(Collectors.toMap(iconSet -> iconSet.getId(), this::getIconList));
  }

  private List<Icon> getIconList(IconSetData iconSetData)
  {
    List<Icon> list;

    if (iconSetData.isAll())
    {
      list = IconSetData.getIconSetDataCollection().stream().filter(f -> !f.isAll()).map(this::getIconList)
          .flatMap(List::stream).sorted().collect(Collectors.toList());
    }
    else
    {
      list = iconSetData.getIconDataList().stream().sorted().map(id -> new Icon(id)).collect(Collectors.toList());
    }

    return list;
  }

  class IconRow
  {
    private List<Icon> m_iconList = new ArrayList<>();

    public void add(Icon icon)
    {
      m_iconList.add(icon);
    }

    public List<Icon> getIconList()
    {
      return m_iconList;
    }
  }

  class Icon
      implements Comparable<Icon>
  {
    private final IconData mi_iconData;
    private final String mi_idLowerCase;
    private final FxIcon mi_fxIcon;

    Icon(IconData iconData)
    {
      mi_iconData = iconData;
      mi_idLowerCase = getId().toLowerCase();
      mi_fxIcon = new FxIcon(iconData).size(IconSize.HUGE);
    }

    public IconData getIconData()
    {
      return mi_iconData;
    }

    public String getId()
    {
      return mi_iconData.getId();
    }

    public String getName()
    {
      return mi_iconData.getName();
    }

    public FxIcon getFxIcon()
    {
      return mi_fxIcon;
    }

    public String getIconSetName()
    {
      return mi_iconData.getIconSetData().getName();
    }

    public String getIdLowerCase()
    {
      return mi_idLowerCase;
    }

    public Node getIconLabel()
    {
      return mi_fxIcon.getNode();
    }

    public String getPrefix()
    {
      return getIconData().getPrefix();
    }

    @Override
    public String toString()
    {
      return getIconData().getId();
    }

    @Override
    public int compareTo(Icon icon)
    {
      int result;

      result = getIconSetName().compareTo(icon.getIconSetName());
      if (result != 0)
      {
        return result;
      }

      return getName().compareTo(icon.getName());
    }
  }

  public <S> TableColumn<S, Number> addRankColumn(TableView<S> tableView, String columnName, int multiplier)
  {
    TableColumn<S, Number> rankColumn;
    RankColumnData rankColumnData;

    rankColumnData = new RankColumnData(tableView);

    rankColumn = new TableColumn<S, Number>(columnName);
    tableView.getColumns().add(rankColumn);
    rankColumn
        .setCellValueFactory(cb -> new SimpleIntegerProperty((rankColumnData.getRank(cb.getValue()) - 1) * multiplier));
    rankColumn.setCellFactory(cb -> new TableCell<S, Number>()
    {
      @Override
      protected void updateItem(Number item, boolean empty)
      {
        super.updateItem(item, empty);
        if (empty || item == null)
        {
          setText(null);
        }
        else
        {
          setText(item.toString());
          setStyle("-fx-font-weight: bold;");
        }
      }
    });
    return rankColumn;
  }

  private class RankColumnData
  {
    private final Map<Integer, Integer> m_lineNumberMap = new HashMap<>();

    private RankColumnData(TableView<?> tableView)
    {
      tableView.itemsProperty().addListener((obs, oldValue, newValue) -> {
        m_lineNumberMap.clear();
        for (int index = 0; index < newValue.size(); index++)
        {
          m_lineNumberMap.put(System.identityHashCode(newValue.get(index)), index + 1);
        }
      });
    }

    public Integer getRank(Object item)
    {
      return m_lineNumberMap.getOrDefault(System.identityHashCode(item), Integer.valueOf(0));
    }
  }

  public static void main(String args[])
  {
    launch(args);
  }
}
