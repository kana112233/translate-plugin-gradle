package entity;

public enum MARK {
    H1("# ", "","<h1>%1$s</h1>"),
    H2("## ", "","<h2>%1$s</h2>"),
    H3("### ", "","<h3>%1$s</h3>"),
    H4("#### ", "","<h4>%1$s</h4>"),
    H5("##### ", "","<h5>%1$s</h5>"),
    H6("###### ", "","<h6>%1$s</h6>"),
    HORIZONTAL_LINE("","---"+"","<br/>%1$s<br/><hr/>"),
    QUOTE(">", "","<p class=\"quote\">%1$s</p>"),
    TAB("    ", null,"<p class=\"tab\">%1$s</p>"),
    CHECK_BOX("- [ ] ", "","<input type=\"checkbox\" disabled=\"\"><label>%1$s</label>"),
    DISABLE_CHECK_BOX("- [x] ", "","<input type=\"checkbox\" disabled=\"\" checked=\"checked\"><label>%1$s</label>"),
    CODE("```"+"", "```"+"","<pre class=\"code\">%1$s</pre>"),
    HIGHLIGHT("==", "==","<span class=\"highlight\">%1$s</span>"),
    UNDERLINE("++", "++","<span class=\"underline\">%1$s</span>"),
    ERASURE("~~", "~~","<span class=\"erasure\">%1$s</span>"),
    ITALIC("*", "*","<span class=\"italic\">%1$s</span>"),
    LITERARY("", null,"%1$s"),
    BOLD("**", "**","<span class=\"bold\">%1$s</span>"),
    IMAGE("![",")","<image src=\"%1$s\"/><label>%2$s</label>"),
    HYPER_LINK("[",")","<a href=\"%1$s\">%2$s</a>"),
    ORDERED_LIST("1. ",null,null),
    UNORDERED_LIST("- ",null,null),
    TABLE("",null,null);

    private String start;
    private String end;
    private String format;

    public String getStart() {
        return start;
    }

    public String getEnd() {
        return end;
    }

    public String getFormat() {
        return format;
    }

    MARK(String start, String end, String format) {
        this.start = start;
        this.end = end;
        this.format=format;
    }
}
