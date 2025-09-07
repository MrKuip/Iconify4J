# Iconify4J
Iconify icons for java

JavaFX example:

Example:
<pre>

    button = new Button("", new FxIcon("mdi-filter").getIconNode());
    label = new Label("", new FxIcon("ant-design-filter-twotone").size(IconSize.VERY_LARGE).getIconNode());
    button3 = new Button("", new FxIcon("streamline-ultimate-color-filter-1").size(IconSize.SUPER_LARGE).fillColor(Color.PURPLE).getIconNode());
    
</pre>

<img width="254" height="199" alt="image" src="https://github.com/user-attachments/assets/427d4299-bbed-48f1-ad07-b74e273d2d38" />

Example adding badges:
<pre>

    closeIcon = new FxIcon("mdi-close-thick").color(Color.RED).size(48.0);
    filterIcon = new FxIcon("mdi-filter").size(96.0);
    filterIcon.addBadge(IconAlignment.LOWER_RIGHT, closeIcon);
    
</pre>

<img width="148" height="144" alt="image" src="https://github.com/user-attachments/assets/2eabb171-2931-4399-97af-d122ff9e618f" />

Execute 'gradlew run' for view of all available icon sets:

<img width="1274" height="874" alt="image" src="https://github.com/user-attachments/assets/2c219aa7-68fa-47c6-bcd2-06821a255cfa" />
