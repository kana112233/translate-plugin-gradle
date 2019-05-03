package org.py.translate.constant;

/**
 * 常量值类
 * @author tangyouzhi
 */
public class Contstants {

    /**
     * 中译英
     */
    public static final String CN_TO_EN="cnToEn";

    /**
     * 英译中
     */
    public static final String EN_TO_CN="enToCn";

    /**
     * 最多一次选择5K字节查询
     */
    public static final Integer MAX_FANYI_SIZE=5000;

    /**
     * JS模拟生成TK参数值
     */
    public static final String TK_FUNC="var TKK = ((function() {  \n"+
            "  var a = 561666268;  \n"+
            "  var b = 1526272306;  \n"+
            "  return 406398 + '.' + (a + b);  \n"+
            "})());  \n"+
            "function b(a, b) {  \n"+
            "  for (var d = 0; d < b.length - 2; d += 3) {  \n"+
            "      var c = b.charAt(d + 2),  \n"+
            "          c = \"a\" <= c ? c.charCodeAt(0) - 87 : Number(c),  \n"+
            "          c = \"+\" == b.charAt(d + 1) ? a >>> c : a << c;  \n"+
            "      a = \"+\" == b.charAt(d) ? a + c & 4294967295 : a ^ c  \n"+
            "  }  \n"+
            "  return a  \n"+
            "}  \n"+
            "\n"+
            "function tk(a) {  \n"+
            "    for (var e = TKK.split(\".\"), h = Number(e[0]) || 0, g = [], d = 0, f = 0; f < a.length; f++) {  \n"+
            "        var c = a.charCodeAt(f);  \n"+
            "        128 > c ? g[d++] = c : (2048 > c ? g[d++] = c >> 6 | 192 : (55296 == (c & 64512) && f + 1 < a.length && 56320 == (a.charCodeAt(f + 1) & 64512) ? (c = 65536 + ((c & 1023) << 10) + (a.charCodeAt(++f) & 1023), g[d++] = c >> 18 | 240, g[d++] = c >> 12 & 63 | 128) : g[d++] = c >> 12 | 224, g[d++] = c >> 6 & 63 | 128), g[d++] = c & 63 | 128)  \n"+
            "    }  \n"+
            "    a = h;  \n"+
            "    for (d = 0; d < g.length; d++) a += g[d], a = b(a, \"+-a^+6\");  \n"+
            "    a = b(a, \"+-3^+b+-f\");  \n"+
            "    a ^= Number(e[1]) || 0;  \n"+
            "    0 > a && (a = (a & 2147483647) + 2147483648);  \n"+
            "    a %= 1E6;  \n"+
            "    return a.toString() + \".\" + (a ^ h)  \n"+
            "}";
}
