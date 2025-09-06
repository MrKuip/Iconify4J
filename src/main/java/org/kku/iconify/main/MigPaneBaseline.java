package org.kku.iconify.main;

import java.io.IOException;
import org.kku.iconify.ui.FxIcon;
import org.kku.iconify.ui.FxIcon.IconAlignment;
import org.kku.iconify.ui.FxIcon.IconSize;
import org.tbee.javafx.scene.layout.MigPane;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class MigPaneBaseline
  extends Application
{
  private int counter;

  @Override
  public void start(Stage stage) throws IOException
  {
    Pane migPane;
    HBox hbox;
    VBox vbox;

    migPane = new MigPane("", "", "baseline");

    hbox = new HBox();
    hbox.setAlignment(Pos.BASELINE_CENTER);

    vbox = new VBox();
    //vbox.getChildren().addAll(showPane(hbox), showPane(migPane));
    vbox.getChildren().addAll(showPane(migPane));

    Scene scene = new Scene(vbox, 1200, 600);
    stage.setTitle("Baseline");
    stage.setScene(scene);
    stage.show();
  }

  private Pane showPane(Pane pane)
  {
    ComboBox<String> comboBox;

    comboBox = new ComboBox<>(FXCollections.observableArrayList("ABC", "DEF", "GHI"));
    comboBox.getSelectionModel().select(0);

    //add(pane, new Label("A"), "");
    //add(pane, bigText("A"), "");
    //add(pane, createSvgIcon(), "");
    //add(pane, createBigIcon(), "");
    //add(pane, createIcon(), "");
    //add(pane, createFxIcon(), "");
    //add(pane, createFxHFlipIcon(), "");
    //add(pane, createFxVFlipIcon(), "");
    add(pane, createFxHVFlipIcon(), "");
    //add(pane, createBigFxIcon(), "");
    //add(pane, createStackedPane(), "");
    //add(pane, createMultiFxIcon(), "");
    //add(pane, comboBox, "");
    //add(pane, new CheckBox("XYX"), "");
    //add(pane, new TextField("KLM"), "");
    //add(pane, new Label("KLM"), "wrap");

    return pane;
  }

  private void add(Pane pane, Node node, String constraint)
  {
    node.setId("" + counter++);

    if (pane instanceof MigPane)
    {
      ((MigPane) pane).add(node, constraint);
      return;
    }

    pane.getChildren().add(node);
  }

  private Node bigText(String t)
  {
    Label text;

    text = new Label(t);
    text.setFont(Font.font(48.0));

    return text;
  }

  private Node createFxIcon()
  {
    FxIcon icon;

    icon = new FxIcon("mdi-filter");

    return icon.getIconNode();
  }

  private Node createFxHFlipIcon()
  {
    FxIcon icon;

    icon = new FxIcon("mdi-filter-hflip");

    return icon.getIconNode();
  }

  private Node createFxVFlipIcon()
  {
    FxIcon icon;

    icon = new FxIcon("mdi-filter-vflip");

    return icon.getIconNode();
  }

  private Node createFxHVFlipIcon()
  {
    FxIcon icon;

    icon = new FxIcon("mdi-filter");
    System.out.println("icon:" + icon.getId() + " hflip=" + icon.getIconData().getHFlip());
    System.out.println("  iconSet:" + icon.getIconData().getIconSetData() + " hflip="
        + icon.getIconData().getIconSetData().getHFlip());

    icon = new FxIcon("mdi-filter-vhflip").size(IconSize.VERY_LARGE);
    System.out.println("icon:" + icon.getId() + " hflip=" + icon.getIconData().getHFlip());
    System.out.println("  iconSet:" + icon.getIconData().getIconSetData() + " hflip="
        + icon.getIconData().getIconSetData().getHFlip());

    return icon.getIconNode();
  }

  private Node createBigFxIcon()
  {
    return new FxIcon("mdi-filter").size(IconSize.VERY_LARGE).getIconNode();
  }

  private Node createMultiFxIcon()
  {
    FxIcon filter;
    FxIcon close;

    close = new FxIcon("mdi-close-thick").fillColor(Color.RED).strokeColor(Color.BLACK).strokeWidth(0.5);

    filter = new FxIcon("mdi-filter-outline").size(IconSize.VERY_LARGE);
    filter = filter.strokeColor(filter.getFillColor());
    filter = filter.addBadge(IconAlignment.UPPER_RIGHT, close);

    return filter.getIconNode();
  }

  public static void main(String args[])
  {
    launch(args);
  }
}
