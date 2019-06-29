import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.py.translate.markdown.renderer.MarkdownContentRenderer;

public class MarkdownTest {
    public static void main(String[] args) {
        Parser parser = Parser.builder().build();
//        String file = "/workspace4Idea/ui/tt/intellij-sdk-docs-master/basics/run_configurations.md";
//        Reader tem = new FileReader(file);
//        Node node = parser.parseReader(tem);
//        System.err.println(node);


        String selectText = ""+"";
        selectText += "Classes used to ![Architecture](img/classes.png)" + "\n";
        selectText += "> **Note** This means that the state of the file system and the file contents displayed" + "\n";
        selectText += ">" + "\n";
        selectText += "> For example, in some cases deleted files can still be visible in the UI for some time before the deletion is picked up by the IntelliJ Platform." + "\n";
        selectText += "---" + "\n";
        selectText += "        title: Action System" + "\n";
        selectText += "---" + "\n";
        selectText += "To ensure that your plugin is initialized on IDEA start-up, make the following changes to the `<application-components>` section of the `plugin.xml` file:"+"\n";
        selectText += "**To register an action on IDEA startup**"+"\n";
        selectText += "| OS      | Storage |"+"\n";
        selectText += "|---------|---------|"+"\n";
        selectText += "| Windows | File in [KeePass](https://keepass.info) format |"+"\n";;
        selectText += "| macOS   | Keychain using [Security Framework](https://developer.apple.com/documentation/security/keychain_services) |"+"\n";;
        selectText += "| Linux   | [Secret Service API](https://standards.freedesktop.org/secret-service) using [libsecret](https://wiki.gnome.org/Projects/Libsecret) |"+"\n";;
        selectText += "Use [`PasswordSafe`](upsource:///platform/platform-api/src/com/intellij/ide/passwordSafe/PasswordSafe.kt) to work with credentials."+"\n";
        selectText += "`JavaPsiFacade.findClass()`"+"\n\n";
        selectText += "# 我是井号"+"\n\n";
        selectText += "## 我是井号123123"+"\n\n";
        selectText += "* [Execution](/basics/run_configurations/run_configuration_execution.md)."+"\n\n";
        selectText += "* [Execution](/basics/run_configurations/run_configuration_execution.md)."+"\n\n";
        selectText += "```"+"\n\n";
        selectText += "xxx112"+"\n\n";
        selectText += "222"+"\n\n";
        selectText += "333"+"\n\n";
        selectText += "```"+"\n\n";
//        MarkdownUtil.parse(selectText, new MyCallback());
        Node node = parser.parse(selectText);
       /* if (node instanceof Document) {
            Node firstChild = node.getFirstChild();
            while (firstChild.getNext() != null) {
                if(firstChild instanceof Heading){
                    Node firstChild1 = firstChild.getFirstChild();
                    Text text = (Text)firstChild1;
                    System.out.println(text.getLiteral());
                    text.setLiteral("123");
                }
                firstChild = firstChild.getNext();
            }
        }*/

        MarkdownContentRenderer renderer = MarkdownContentRenderer.builder().build();

//        HtmlRenderer renderer = HtmlRenderer.builder().build();
        String render = renderer.render(node);
        System.out.println(render);

    }
}
