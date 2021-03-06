package org.py.translate.markdown.renderer;

import org.commonmark.internal.renderer.text.BulletListHolder;
import org.commonmark.internal.renderer.text.ListHolder;
import org.commonmark.internal.renderer.text.OrderedListHolder;
import org.commonmark.node.*;
import org.commonmark.renderer.NodeRenderer;
import org.py.translate.action.TranslateJob;
import org.py.translate.util.MarkdownUtil;
import org.py.translate.util.MyCallback;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * The node renderer that renders all the core nodes (comes last in the order of node renderers).
 */
public class CoreMarkdownContentNodeRenderer extends AbstractVisitor implements NodeRenderer {

    private final MarkdownContentNodeRendererContext context;
    private final MarkdownContentWriter textContent;

    private ListHolder listHolder;

    public CoreMarkdownContentNodeRenderer(MarkdownContentNodeRendererContext context) {
        this.context = context;
        this.textContent = context.getWriter();
    }

    @Override
    public Set<Class<? extends Node>> getNodeTypes() {
        return new HashSet<>(Arrays.asList(
                Document.class,
                Heading.class,
                Paragraph.class,
                BlockQuote.class,
                BulletList.class,
                FencedCodeBlock.class,
                HtmlBlock.class,
                ThematicBreak.class,
                IndentedCodeBlock.class,
                Link.class,
                ListItem.class,
                OrderedList.class,
                Image.class,
                Emphasis.class,
                StrongEmphasis.class,
                Text.class,
                Code.class,
                HtmlInline.class,
                SoftLineBreak.class,
                HardLineBreak.class
        ));
    }

    @Override
    public void render(Node node) {
        node.accept(this);
    }

    @Override
    public void visit(Document document) {
        // No rendering itself
        visitChildren(document);
        textContent.write("\n");
    }

    @Override
    public void visit(BlockQuote blockQuote) {
        textContent.write('>');

//        while(blockQuote.getLastChild()!= null){
//
//        }
        visitChildren(blockQuote);
//        textContent.write('»');

        writeEndOfLineIfNeeded(blockQuote, null);
        textContent.write('\n');
    }

    @Override
    public void visit(BulletList bulletList) {
        if (listHolder != null) {
            writeEndOfLine();
        }
        listHolder = new BulletListHolder(listHolder, bulletList);
        visitChildren(bulletList);
        writeEndOfLineIfNeeded(bulletList, null);
        if (listHolder.getParent() != null) {
            listHolder = listHolder.getParent();
        } else {
            listHolder = null;
        }
        textContent.write('\n');
    }

    @Override
    public void visit(Code code) {
        textContent.write('`');
        textContent.write(code.getLiteral() );
        textContent.write('`');
    }

    private String translate(String str) {
//        return str;
        if(str.trim().startsWith("title:")){
            return str;
        }else {
            String translateText = TranslateJob.parseString(null, str);
            return translateText;
        }
    }

    @Override
    public void visit(FencedCodeBlock fencedCodeBlock) {
        if (context.stripNewlines()) {
            textContent.writeStripped(translate(fencedCodeBlock.getLiteral()) );
            writeEndOfLineIfNeeded(fencedCodeBlock, null);
        } else {
            textContent.write("```\n");
            textContent.write(fencedCodeBlock.getLiteral() );
            textContent.write("\n");
            textContent.write("```\n");
        }
        textContent.write('\n');
    }

    @Override
    public void visit(HardLineBreak hardLineBreak) {
        writeEndOfLineIfNeeded(hardLineBreak, null);
    }

    @Override
    public void visit(Heading heading) {
        visitChildren(heading);
        writeEndOfLineIfNeeded(heading, ':');
        textContent.write('\n');
    }

    @Override
    public void visit(ThematicBreak thematicBreak) {
        if (!context.stripNewlines()) {
            textContent.write("---");
        }
        writeEndOfLineIfNeeded(thematicBreak, null);
        textContent.write('\n');
    }

    @Override
    public void visit(HtmlInline htmlInline) {
        writeText(htmlInline.getLiteral());
    }

    @Override
    public void visit(HtmlBlock htmlBlock) {
        writeText(htmlBlock.getLiteral());
    }

    @Override
    public void visit(Image image) {
        textContent.write('!');
        writeLink(image, image.getTitle(), image.getDestination());
    }

    @Override
    public void visit(IndentedCodeBlock indentedCodeBlock) {
        if (context.stripNewlines()) {
            textContent.writeStripped(translate(indentedCodeBlock.getLiteral()) );
            writeEndOfLineIfNeeded(indentedCodeBlock, null);
        } else {
            textContent.write(translate(indentedCodeBlock.getLiteral()) );
        }
    }

    @Override
    public void visit(Link link) {
        writeLink(link, link.getTitle(), link.getDestination());
    }

    @Override
    public void visit(ListItem listItem) {
        if (listHolder != null && listHolder instanceof OrderedListHolder) {
            OrderedListHolder orderedListHolder = (OrderedListHolder) listHolder;
            String indent = context.stripNewlines() ? "" : orderedListHolder.getIndent();
            textContent.write(indent + orderedListHolder.getCounter() + orderedListHolder.getDelimiter() + " ");
            visitChildren(listItem);
            writeEndOfLineIfNeeded(listItem, null);
            orderedListHolder.increaseCounter();
        } else if (listHolder != null && listHolder instanceof BulletListHolder) {
            BulletListHolder bulletListHolder = (BulletListHolder) listHolder;
            if (!context.stripNewlines()) {
                textContent.write(bulletListHolder.getIndent() + bulletListHolder.getMarker() + " ");
            }
            visitChildren(listItem);
            writeEndOfLineIfNeeded(listItem, null);
        }
//        textContent.write('\n');
    }

    @Override
    public void visit(OrderedList orderedList) {
        if (listHolder != null) {
            writeEndOfLine();
        }
        listHolder = new OrderedListHolder(listHolder, orderedList);
        visitChildren(orderedList);
        writeEndOfLineIfNeeded(orderedList, null);
        if (listHolder.getParent() != null) {
            listHolder = listHolder.getParent();
        } else {
            listHolder = null;
        }
        textContent.write('\n');
    }

    @Override
    public void visit(Paragraph paragraph) {
        visitChildren(paragraph);
        // Add "end of line" only if its "root paragraph.
        if (paragraph.getParent() == null || paragraph.getParent() instanceof Document) {
            writeEndOfLineIfNeeded(paragraph, null);
        }
        textContent.write('\n');
    }

    @Override
    public void visit(SoftLineBreak softLineBreak) {
        writeEndOfLineIfNeeded(softLineBreak, null);
        textContent.write('\n');
    }

    @Override
    public void visit(Text text) {
        writeText(text);
    }

    @Override
    protected void visitChildren(Node parent) {
        if (parent instanceof StrongEmphasis) {
            StrongEmphasis se = (StrongEmphasis)(parent);
            textContent.write(""+se.getOpeningDelimiter());
        }
        Node node = parent.getFirstChild();
        while (node != null) {
            Node next = node.getNext();
            context.render(node);
            node = next;
        }
        if (parent instanceof StrongEmphasis) {
            StrongEmphasis se = (StrongEmphasis)(parent);
            textContent.write(""+se.getClosingDelimiter());
        }
    }

    private void writeText(String text) {
        if (context.stripNewlines()) {
            textContent.writeStripped(text);
        } else {
            textContent.write(text);
        }
    }
    private void writeText(Text text) {
        if (context.stripNewlines()) {
            textContent.writeStripped(translate(text.getLiteral()) );
        } else {
            if (text.getParent() instanceof Heading) {
                Heading heading = (Heading)text.getParent();
                StringBuilder str = new StringBuilder();
                for (int i = 0; i < heading.getLevel(); i++) {
                     str.append("#");
                }
                str.append(" ");
                textContent.write(str + translate(text.getLiteral()) );
            }else {
                textContent.write(translate(text.getLiteral()) );
            }
        }
    }

    private void writeLink(Node node, String title, String destination) {
        boolean hasChild = node.getFirstChild() != null;
        boolean hasTitle = title != null && !title.equals(destination);
        boolean hasDestination = destination != null && !destination.equals("");

        if (hasChild) {
            if (node instanceof Text) {
                textContent.write('[');

                Text text = (Text)node.getFirstChild();
                textContent.write(translate(text.getLiteral()) );
                textContent.write(']');
                if (hasTitle || hasDestination) {
//                textContent.whitespace();
                    textContent.write('(');
                }
            } else {
                textContent.write("[");
                visitChildren(node);
                textContent.write("]");
                if (hasTitle || hasDestination) {
//                    textContent.whitespace();
                    textContent.write('(');
                }
            }

        }

        if (hasTitle) {
            textContent.write(title);
            if (hasDestination) {
                textContent.colon();
                textContent.whitespace();
            }
        }

        if (hasDestination) {
            textContent.write(destination);
        }

        if (hasChild && (hasTitle || hasDestination)) {
            textContent.write(')');
        }
    }

    private void writeEndOfLineIfNeeded(Node node, Character c) {
        if (context.stripNewlines()) {
            if (c != null) {
                textContent.write(c);
            }
            if (node.getNext() != null) {
                textContent.whitespace();
            }
        } else {
            if (node.getNext() != null) {
                textContent.line();
            }
        }
    }

    private void writeEndOfLine() {
        if (context.stripNewlines()) {
            textContent.whitespace();
        } else {
            textContent.line();
        }
    }
}
