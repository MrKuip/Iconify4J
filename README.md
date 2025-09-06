# Iconify4J
Iconify icons for java

JavaFX example:
<pre>
package org.kku.iconify.main;

import java.io.IOException;
import org.kku.iconify.ui.FxIcon;
import org.kku.iconify.ui.FxIcon.IconSize;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
    Button button;
    Label label;
    Button button3;

    hbox = new HBox();
    button = new Button("", new FxIcon("mdi-filter").getIconNode());
    label = new Label("", new FxIcon("ant-design-filter-twotone").size(IconSize.VERY_LARGE).getIconNode());
    button3 = new Button("", new FxIcon("streamline-ultimate-color-filter-1").size(IconSize.SUPER_LARGE).fillColor(Color.PURPLE).getIconNode());
    
    hbox.getChildren().addAll(button, label, button3);

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
</pre>
Execute 'gradlew run' for view of all available icon sets
<img width="1570" height="910" alt="image" src="https://github.com/user-attachments/assets/c2eb35d9-49e6-4628-8b79-6bf3625bbc05" />
