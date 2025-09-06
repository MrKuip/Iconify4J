package org.kku.iconify.javax.scene;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import org.kku.iconify.data.IconSetData.Flip;
import org.kku.iconify.data.IconSetData.Rotate;
import org.kku.iconify.ui.FxIcon;
import com.github.weisj.jsvg.SVGDocument;
import com.github.weisj.jsvg.parser.LoaderContext;
import com.github.weisj.jsvg.parser.SVGLoader;
import com.github.weisj.jsvg.renderer.jfx.FXSVGRenderer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.BlendMode;

public class SVGNode
  extends Canvas
{
  private final SVGDocument m_document;
  private final Flip m_hFlip;
  private final Flip m_vFlip;
  private final Rotate m_rotate;

  public SVGNode(FxIcon fxIcon)
  {
    m_document = createDocument(fxIcon.getParsedSVGText());
    m_hFlip = fxIcon.getIconData().getHFlip();
    m_vFlip = fxIcon.getIconData().getVFlip();
    m_rotate = fxIcon.getIconData().getRotate();

    setWidth(fxIcon.getWidth());
    setHeight(fxIcon.getHeight());
    update();
  }

  public SVGNode(String svgText, double width, double height)
  {
    m_document = createDocument(svgText);
    m_hFlip = Flip.FALSE;
    m_vFlip = Flip.FALSE;
    m_rotate = Rotate.ROTATE_0;

    setWidth(width);
    setHeight(height);
    update();
  }

  public boolean hasFlip()
  {
    return m_hFlip.get() || m_vFlip.get();
  }

  public boolean hasRotate()
  {
    return m_rotate != Rotate.ROTATE_0;
  }

  public static final SVGDocument createDocument(String svg)
  {
    try (InputStream is = new ByteArrayInputStream(svg.getBytes()))
    {
      SVGDocument document;

      document = new SVGLoader().load(is, null, LoaderContext.createDefault());
      if (document == null)
      {
        System.out.println("svg=");
        System.out.println(svg);
      }

      return document;
    }
    catch (Exception e)
    {
      e.printStackTrace();
      return null;
    }
  }

  private void update()
  {
    double scaleX;
    double scaleY;
    double scale;
    GraphicsContext graphics;

    if (m_document == null)
    {
      return;
    }

    scaleX = getWidth() / m_document.size().getWidth();
    scaleY = getHeight() / m_document.size().getHeight();
    scale = Math.min(scaleX, scaleY);

    graphics = getGraphicsContext2D();
    graphics.save();
    try
    {
      graphics.setTransform(1, 0, 0, 1, 0, 0);
      if (hasFlip())
      {
        graphics.translate(m_hFlip.get() ? getWidth() : 0, m_vFlip.get() ? getHeight() : 0);
        graphics.scale(m_hFlip.get() ? -1 : 1, m_vFlip.get() ? -1 : 1);
      }
      if (hasRotate())
      {
        graphics.rotate(m_rotate.get());
      }
      graphics.scale(scale, scale);
      graphics.setGlobalAlpha(1D);
      graphics.setGlobalBlendMode(BlendMode.SRC_OVER);
      graphics.clearRect(0, 0, getWidth(), getHeight());
      if (m_document != null)
      {
        FXSVGRenderer.render(m_document, getGraphicsContext2D());
      }
    }
    finally
    {
      graphics.restore();
    }
  }
}
