# Intellij plugin for OFBiz
This plugin was made from scratch to use for APACHE free ERP : OFBiz
This is still work in progress.
At this point, we've worked mostly on references, to ease navigation toward services, labels, and entity definition.
There is also functioning references towards screens, forms, and menus definitions
In the future, we hope we'll ad many more cool features.

## USAGE
Go to IDEA parameters (default `CTRL`+`ALT`+`S`) then plugin, click the gear, and manage plugin repositories.
Add a new line, and paste :
https://lib.nereide.fr/idea-plugins/apache-ofbiz/updatePlugins.xml
Then go to the plugin market place, and search OFBiz. Plugin should appear.
All you have to do now is install, enjoy, and give us some feedback !

## FEATURES
#### REFERENCES 
Navigate by `CTRL`+`CLICK` (default)
- References in java file:
  - You can navigate towards entity, services, and UiLabels in most cases
- References in groovy file:
  - You can navigate towards entity, services, and UiLabels in most cases
- References in Screen.xml file
  - You can navigate towards entity, services, UiLabels in most cases
  - You can also navigate by `CTRL`+`CLICK` towards included screens, menus and forms, and scripts locations (such as html-templates)
- References in controller.xml file
  -You van navigate towards a view map definition, a service or java method in an event

#### COMPLETION
- Service name completion when calling service in most cases in java, groovy, and xml
- Entity and View-entity name completion when calling entities and views in most cases in java, groovy, and xml
- Entity and view entity fields completion in Java, Groovy, and Xml : 
  - Most common cases should be covered. But for the field completion to work, the relevant entity must be written somewhere in the method.

#### QUICK DOCUMENTATION
The plugins adds hover documentation for the following ofbiz elements, in javan groovy and xml :
- Services
- View and entities
For OFBiz properties (AKA UiLabels), the english message in the property will be display instead.

#### LANGUAGE INJECTION
Plugin now supports Language injection :
- Injects groovy in relevant locations in xml files

#### CODE INSPECTION
Plugin now has some code inspection capabilities (cache call and xml location for now)

**Feel free to contribute !**
## CONTRIBUTE
Clone this repo, and you can get started here : [DOCS](https://plugins.jetbrains.com/docs/intellij/basics.html)

*Made by Nereide*
