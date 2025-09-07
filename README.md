# Iconify4J
Iconify icons for java.  
see [iconify website](https://icon-sets.iconify.design/) for all available icon sets.  
The animated icons in the sets do not work (yet)  

## JavaFX example:


Example adding graphics to Button and Label.  
Learn about size(), color() properties.
<pre>

    button = new Button("", new FxIcon("mdi-filter").getIconNode());
    label = new Label("", new FxIcon("ant-design-filter-twotone").size(IconSize.VERY_LARGE).getIconNode());
    button3 = new Button("", new FxIcon("streamline-ultimate-color-filter-1").size(IconSize.SUPER_LARGE).color(Color.PURPLE).getIconNode());
    
</pre>

<img width="254" height="199" alt="image" src="https://github.com/user-attachments/assets/427d4299-bbed-48f1-ad07-b74e273d2d38" />

Example adding badges to an Icon. Learn about addBadge() method. 
<pre>

    closeIcon = new FxIcon("mdi-close-thick").color(Color.RED).size(48.0);
    filterIcon = new FxIcon("mdi-filter").size(96.0);
    filterIcon.addBadge(IconAlignment.LOWER_RIGHT, closeIcon);
    
</pre>

<img width="148" height="144" alt="image" src="https://github.com/user-attachments/assets/2eabb171-2931-4399-97af-d122ff9e618f" />

## IconViewer
Execute 'gradlew run' to inspect all available icon sets:

<img width="1274" height="874" alt="image" src="https://github.com/user-attachments/assets/2c219aa7-68fa-47c6-bcd2-06821a255cfa" />
