package org.kku.iconify.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import com.github.weisj.jsvg.SVGDocument;
import com.github.weisj.jsvg.parser.LoaderContext;
import com.github.weisj.jsvg.parser.SVGLoader;

public class SVGUtil
{
  private SVGUtil()
  {
  }

  public static final SVGDocument createDocument(String svg) throws IOException
  {
    try (InputStream is = new ByteArrayInputStream(svg.getBytes()))
    {
      return new SVGLoader().load(is, null, LoaderContext.createDefault());
    }
  }
}
