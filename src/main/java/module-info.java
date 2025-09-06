module org.kku.fonticons
{
  requires java.logging;
  requires transitive javafx.graphics;
  requires transitive javafx.controls;
  requires com.fasterxml.jackson.core;
  requires com.miglayout.javafx;
  requires java.xml;
  requires com.github.weisj.jsvg.javafx;
  requires com.github.weisj.jsvg;
  requires com.fasterxml.jackson.databind;
  
  exports org.kku.fonticons.ui;
  exports org.kku.fonticons.main;
}
