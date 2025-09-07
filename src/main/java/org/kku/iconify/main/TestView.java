package org.kku.iconify.main;

import java.io.IOException;
import org.kku.iconify.ui.FxIcon;
import org.kku.iconify.ui.FxIcon.IconAlignment;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class TestView
  extends Application
{
  @Override
  public void start(Stage stage) throws IOException
  {
    HBox hbox;
    FxIcon closeIcon;
    FxIcon filterIcon;

    hbox = new HBox();

    closeIcon = new FxIcon("mdi-close-thick").color(Color.RED);
    filterIcon = new FxIcon("mdi-filter").size(96.0);
    filterIcon.addBadge(IconAlignment.LOWER_RIGHT, closeIcon, 0.5);
    hbox.getChildren().add(new Button("", filterIcon.getNode()));

    closeIcon = new FxIcon("mdi-close-thick").color(Color.RED).size(48.0);
    filterIcon = new FxIcon("mdi-filter").size(96.0);
    filterIcon.addBadge(IconAlignment.LOWER_RIGHT, closeIcon);
    hbox.getChildren().add(new Button("", filterIcon.getNode()));

    Scene scene = new Scene(hbox, 1200, 600);
    stage.setTitle("Iconify");
    stage.setScene(scene);
    stage.show();
  }

  public static void main(String args[])
  {
    launch(args);
  }
}