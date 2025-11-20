module org.kku.iconify4j
{
  requires transitive javafx.graphics;
  requires com.github.weisj.jsvg.javafx;
  requires com.github.weisj.jsvg;
  requires com.fasterxml.jackson.databind;
  requires javafx.swing;
  requires java.desktop;

  exports org.kku.iconify.ui;
  exports org.kku.iconify.data;
}
