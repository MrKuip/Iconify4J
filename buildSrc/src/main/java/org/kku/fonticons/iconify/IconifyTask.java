package org.kku.fonticons.iconify;

import org.gradle.api.DefaultTask;
import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.OutputDirectory;
import org.gradle.api.tasks.TaskAction;

public abstract class IconifyTask
  extends DefaultTask
{
  @OutputDirectory
  public abstract DirectoryProperty getDestinationDir();

  @Input
  public abstract Property<String> getCollectionURL();

  @TaskAction
  public void executeTask()
  {
    IconifyDownload download;

    download = new IconifyDownload(getDestinationDir().get().getAsFile().toPath(), getCollectionURL().get());
    download.setLogger(getLogger());
    download.execute();
  }
}
