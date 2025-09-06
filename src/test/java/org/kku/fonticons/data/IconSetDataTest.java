package org.kku.fonticons.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.kku.fonticons.data.IconSetData.Rotate;
import org.kku.fonticons.data.IconSetData.Flip;

class IconSetDataTest
{
  @Test
  public void testRotate()
  {
    assertEquals(Rotate.ROTATE_0.get(), 0);
    assertEquals(Rotate.ROTATE_90.get(), 90);
    assertEquals(Rotate.ROTATE_180.get(), 180);
    assertEquals(Rotate.ROTATE_270.get(), 270);
    
    assertEquals(Rotate.ROTATE_0, Rotate.get(0));
    assertEquals(Rotate.ROTATE_90, Rotate.get(1));
    assertEquals(Rotate.ROTATE_180, Rotate.get(2));
    assertEquals(Rotate.ROTATE_270, Rotate.get(3));
    assertEquals(Rotate.ROTATE_0, Rotate.get(4));
    assertEquals(Rotate.ROTATE_90, Rotate.get(5));
    assertEquals(Rotate.ROTATE_180, Rotate.get(6));
    assertEquals(Rotate.ROTATE_270, Rotate.get(7));
    assertEquals(Rotate.ROTATE_0, Rotate.get(8));
    
    assertEquals(Rotate.ROTATE_0.add(Rotate.ROTATE_0), Rotate.ROTATE_0);
    assertEquals(Rotate.ROTATE_0.add(Rotate.ROTATE_90), Rotate.ROTATE_90);
    assertEquals(Rotate.ROTATE_0.add(Rotate.ROTATE_180), Rotate.ROTATE_180);
    assertEquals(Rotate.ROTATE_0.add(Rotate.ROTATE_270), Rotate.ROTATE_270);
    
    assertEquals(Rotate.ROTATE_90.add(Rotate.ROTATE_0), Rotate.ROTATE_90);
    assertEquals(Rotate.ROTATE_90.add(Rotate.ROTATE_90), Rotate.ROTATE_180);
    assertEquals(Rotate.ROTATE_90.add(Rotate.ROTATE_180), Rotate.ROTATE_270);
    assertEquals(Rotate.ROTATE_90.add(Rotate.ROTATE_270), Rotate.ROTATE_0);
    
    assertEquals(Rotate.ROTATE_180.add(Rotate.ROTATE_0), Rotate.ROTATE_180);
    assertEquals(Rotate.ROTATE_180.add(Rotate.ROTATE_90), Rotate.ROTATE_270);
    assertEquals(Rotate.ROTATE_180.add(Rotate.ROTATE_180), Rotate.ROTATE_0);
    assertEquals(Rotate.ROTATE_180.add(Rotate.ROTATE_270), Rotate.ROTATE_90);
    
    assertEquals(Rotate.ROTATE_270.add(Rotate.ROTATE_0), Rotate.ROTATE_270);
    assertEquals(Rotate.ROTATE_270.add(Rotate.ROTATE_90), Rotate.ROTATE_0);
    assertEquals(Rotate.ROTATE_270.add(Rotate.ROTATE_180), Rotate.ROTATE_90);
    assertEquals(Rotate.ROTATE_270.add(Rotate.ROTATE_270), Rotate.ROTATE_180);
  }
  
  @Test
  public void testFlip()
  {
    assertEquals(Flip.FALSE.get(), false);
    assertEquals(Flip.TRUE.get(), true);
    
    assertEquals(Flip.FALSE, Flip.get(false));
    assertEquals(Flip.TRUE, Flip.get(true));
    
    assertEquals(Flip.FALSE.add(Flip.FALSE), Flip.FALSE);
    assertEquals(Flip.FALSE.add(Flip.TRUE), Flip.TRUE);
    assertEquals(Flip.TRUE.add(Flip.FALSE), Flip.TRUE);
    assertEquals(Flip.TRUE.add(Flip.TRUE), Flip.FALSE);
  }
}
