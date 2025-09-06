# Iconify4J
Iconify icons for java

JavaFX example:

<img width="254" height="199" alt="image" src="https://github.com/user-attachments/assets/427d4299-bbed-48f1-ad07-b74e273d2d38" />

The code:
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


Execute 'gradlew run' for view of all available icon sets:

<img width="1274" height="874" alt="image" src="https://github.com/user-attachments/assets/2c219aa7-68fa-47c6-bcd2-06821a255cfa" />
