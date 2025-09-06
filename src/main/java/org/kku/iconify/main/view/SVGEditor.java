package org.kku.iconify.main.view;

import java.io.StringReader;
import java.io.StringWriter;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.kku.iconify.javax.scene.SVGNode;
import org.kku.iconify.ui.FxIcon;
import org.tbee.javafx.scene.layout.MigPane;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;

public class SVGEditor
{
  static final private int MAX_PIXEL_SIZE = 640;
  private Node mi_editor;

  public SVGEditor(FxIcon fxIcon)
  {
    mi_editor = createEditor(fxIcon);
  }

  public Node getEditor()
  {
    return mi_editor;
  }

  private Node createEditor(FxIcon fxIcon)
  {
    MigPane pane;
    Button runButton;
    String svgText;
    TextArea svgTextArea;
    VBox svgImageBox;

    svgImageBox = new VBox();

    svgText = getPrettyPrint(fxIcon.getParsedSVGText());

    svgTextArea = new TextArea();
    svgTextArea.setStyle("-fx-font-family: monospace;");
    svgTextArea.setText(svgText);

    runButton = new Button("Run");
    runButton.setOnAction((a) -> {
      svgImageBox.getChildren().setAll(getSVGImage(svgTextArea.getText()));
    });

    svgImageBox.getChildren().add(getSVGImage(svgText));

    pane = new MigPane("", "[grow][pref]", "[][grow]");
    pane.add(runButton, "span, wrap");
    pane.add(svgTextArea, "grow");
    pane.add(svgImageBox, "");

    return pane;
  }

  private Node getSVGImage(String svgText)
  {
    return new SVGNode(svgText, MAX_PIXEL_SIZE, MAX_PIXEL_SIZE);
  }

  private String getPrettyPrint(String xmlText)
  {
    try
    {
      TransformerFactory transformerFactory;
      Transformer transformer;
      StringReader reader;
      StringWriter writer;

      transformerFactory = TransformerFactory.newInstance();
      transformer = transformerFactory.newTransformer();

      // Set output properties for pretty-printing
      transformer.setOutputProperty(OutputKeys.INDENT, "yes");
      transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
      transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");

      reader = new StringReader(xmlText);
      writer = new StringWriter(xmlText.length() + 1000);
      transformer.transform(new StreamSource(reader), new StreamResult(writer));

      return writer.toString();
    }
    catch (Exception ex)
    {
      return xmlText;
    }
  }
}
