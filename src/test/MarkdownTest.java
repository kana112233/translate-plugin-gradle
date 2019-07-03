/*
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.junit.Test;
import org.py.translate.markdown.renderer.MarkdownContentRenderer;

import java.io.*;

public class MarkdownTest {

//    @Test
    public void testHtml() {
        String str = "D:\\workspace4Idea\\ui\\intellij-sdk-docs\\tutorials\\action_system.md";
        StringBuffer buffer = getFileStr(str);
//        String selectText = getSelectText();
        Parser parser = Parser.builder().build();
        Node node = parser.parse(buffer.toString());
        HtmlRenderer renderer = HtmlRenderer.builder().build();

        String render = renderer.render(node);
        System.out.println(render);
    }

    private static StringBuffer getFileStr(String str) {
        StringBuffer buffer = new StringBuffer();
        try {
            InputStream is = new FileInputStream(str);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line = reader.readLine();
            while (line != null) {
                buffer.append(line);
                buffer.append("\n");
                line = reader.readLine();
            }
            reader.close();
            is.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer;
    }

    @Test
    public void testMarkdown() {
        Parser parser = Parser.builder().build();

        String str = "D:\\workspace4Idea\\ui\\docs-travis-ci-com\\user\\apps.md";
        StringBuffer buffer = getFileStr(str);

        Node node = parser.parse(buffer.toString());

        MarkdownContentRenderer renderer = MarkdownContentRenderer.builder().build();

        String render = renderer.render(node);
        System.out.println(render);

    }

    private static StringBuilder getSelectText() {
        StringBuilder selectText = new StringBuilder();
        selectText.append("---" + "\n");
        selectText.append("        title: Action System" + "\n");
        selectText.append("---" + "\n");
        selectText.append("Classes used to ![Architecture](img/classes.png)" + "\n");
        selectText.append("> **Note** This means that the state of the file system and the file contents displayed" + "\n");
        selectText.append(">" + "\n");
        selectText.append("> For example, in some cases deleted files can still be visible in the UI for some time before the deletion is picked up by the IntelliJ Platform." + "\n");
        selectText.append("To ensure that your plugin is initialized on IDEA start-up, make the following changes to the `<application-components>` section of the `plugin.xml` file:"+"\n");
        selectText.append("**To register an action on IDEA startup**"+"\n");
        selectText.append("| OS      | Storage |"+"\n");
        selectText.append("|---------|---------|"+"\n");
        selectText.append("| Windows | File in [KeePass](https://keepass.info) format |"+"\n");
        selectText.append("| macOS   | Keychain using [Security Framework](https://developer.apple.com/documentation/security/keychain_services) |"+"\n");
        selectText.append("| Linux   | [Secret Service API](https://standards.freedesktop.org/secret-service) using [libsecret](https://wiki.gnome.org/Projects/Libsecret) |"+"\n");
        selectText.append("Use [`PasswordSafe`](upsource:///platform/platform-api/src/com/intellij/ide/passwordSafe/PasswordSafe.kt) to work with credentials."+"\n");
        selectText.append("`JavaPsiFacade.findClass()`"+"\n");
        selectText.append("# 我是井号"+"\n");
        selectText.append("## 我是井号123123"+"\n");
        selectText.append("* [Execution](/basics/run_configurations/run_configuration_execution.md)."+"\n");
        selectText.append("* [Execution](/basics/run_configurations/run_configuration_execution.md)."+"\n");
        selectText.append("```"+"\n");
        selectText.append("xxx112"+"\n");
        selectText.append("222"+"\n");
        selectText.append("333"+"\n");
        selectText.append("```"+"\n");
        return selectText;
    }
}
*/
