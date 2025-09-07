package org.kku.iconify.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.kku.iconify.data.IconSetData.Rotation;
import org.kku.iconify.data.IconSetData.Flip;

class IconSetDataTest
{
  @Test
  public void testRotate()
  {
    assertEquals(Rotation.ROTATE_0.get(), 0);
    assertEquals(Rotation.ROTATE_90.get(), 90);
    assertEquals(Rotation.ROTATE_180.get(), 180);
    assertEquals(Rotation.ROTATE_270.get(), 270);
    
    assertEquals(Rotation.ROTATE_0, Rotation.get(0));
    assertEquals(Rotation.ROTATE_90, Rotation.get(1));
    assertEquals(Rotation.ROTATE_180, Rotation.get(2));
    assertEquals(Rotation.ROTATE_270, Rotation.get(3));
    assertEquals(Rotation.ROTATE_0, Rotation.get(4));
    assertEquals(Rotation.ROTATE_90, Rotation.get(5));
    assertEquals(Rotation.ROTATE_180, Rotation.get(6));
    assertEquals(Rotation.ROTATE_270, Rotation.get(7));
    assertEquals(Rotation.ROTATE_0, Rotation.get(8));
    
    assertEquals(Rotation.ROTATE_0.add(Rotation.ROTATE_0), Rotation.ROTATE_0);
    assertEquals(Rotation.ROTATE_0.add(Rotation.ROTATE_90), Rotation.ROTATE_90);
    assertEquals(Rotation.ROTATE_0.add(Rotation.ROTATE_180), Rotation.ROTATE_180);
    assertEquals(Rotation.ROTATE_0.add(Rotation.ROTATE_270), Rotation.ROTATE_270);
    
    assertEquals(Rotation.ROTATE_90.add(Rotation.ROTATE_0), Rotation.ROTATE_90);
    assertEquals(Rotation.ROTATE_90.add(Rotation.ROTATE_90), Rotation.ROTATE_180);
    assertEquals(Rotation.ROTATE_90.add(Rotation.ROTATE_180), Rotation.ROTATE_270);
    assertEquals(Rotation.ROTATE_90.add(Rotation.ROTATE_270), Rotation.ROTATE_0);
    
    assertEquals(Rotation.ROTATE_180.add(Rotation.ROTATE_0), Rotation.ROTATE_180);
    assertEquals(Rotation.ROTATE_180.add(Rotation.ROTATE_90), Rotation.ROTATE_270);
    assertEquals(Rotation.ROTATE_180.add(Rotation.ROTATE_180), Rotation.ROTATE_0);
    assertEquals(Rotation.ROTATE_180.add(Rotation.ROTATE_270), Rotation.ROTATE_90);
    
    assertEquals(Rotation.ROTATE_270.add(Rotation.ROTATE_0), Rotation.ROTATE_270);
    assertEquals(Rotation.ROTATE_270.add(Rotation.ROTATE_90), Rotation.ROTATE_0);
    assertEquals(Rotation.ROTATE_270.add(Rotation.ROTATE_180), Rotation.ROTATE_90);
    assertEquals(Rotation.ROTATE_270.add(Rotation.ROTATE_270), Rotation.ROTATE_180);
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
