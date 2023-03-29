
import 'Elements.dart';

var empty = makeMap(
    "area,base,basefont,br,col,frame,hr,img,input,link,meta,param,embed,command,keygen,source,track,wbr, ?xml");

var special = makeMap("script,style");

/// 解析HTML/XML元素并返回' start '， ' end '， ' comment '， ' chars '。
/// 基于这些回调函数构造JSON。
class HtmlParser {

  var index, chars;
  List<String> stack = [];
  String match, last;
  Map<String, Function> handler;

  String stackLast() {
    if (stack.isEmpty) return "";
    return stack[stack.length - 1];
  }

  HtmlParser(String html, Map<String, Function> handler) {
    last = html;
    this.handler = handler;

    ;
    while (html.isNotEmpty) {
      chars = true;
      if (html.indexOf("<!--") == 0) {
        index = html.indexOf("-->");

        if (index >= 0) {
          handler["comment"](html.substring(4, index));
          html = html.substring(index + 3);
          chars = false;
        }
      } else if (html.indexOf("</") == 0) {
        match = RegExp(r"^<([\s\S]*?)>").stringMatch(html);
        var matchLength = match.length;

        if (match != null && matchLength > 0) {
          html = html.substring(matchLength);
          match = match.substring(2, matchLength - 1);

          parseEndTag(match);
          chars = false;
        }
      } else if (html.indexOf("<") == 0) {
        if ((match = RegExp(r"^<([\s\S]*?)>").stringMatch(html)) != null &&
            match.isNotEmpty) {
          var matchLength = match.length;
          html = html.substring(matchLength);
          if (match.endsWith(r"/>")) {
            match = match.substring(1, matchLength - 2);
            parseStartTag(match, match.split(" ")[0], true);
          } else {
            match = match.substring(1, matchLength - 1);
            parseStartTag(match, match.split(" ")[0], false);
          }
        }
        chars = false;
      }
      if (chars) {
        index = html.indexOf("<");

        var text = index < 0 ? html : html.substring(0, index);
        html = index < 0 ? "" : html.substring(index);

        if (text.trim().isNotEmpty) handler["chars"](text);
      }

      if (html == last) throw "Parse Error: " + html;
      last = html;
    }
    parseEndTag();
  }

  parseStartTag(String tag, String tagName, bool unary) {
    unary = empty[tagName] == true || unary;

    if (!unary) stack.add(tagName);

    var rest = tag.replaceFirst(tagName, '').trim();

    var attrRegExp = RegExp(
        r"""([a-zA-Z_:@#][-a-zA-Z0-9_:.]*)(?:\s*=(\s*(?:(?:"((?:\\.|[^"])*)")|(?:'((?:\\.|[^'])*)')|([^>\s]+))))?""");

    List<Attribute> attrs = attrRegExp.allMatches(rest).map((match) {
      String value = match.group(2) != null
          ? match.group(2)
          : match.group(3) != null
              ? match.group(3)
              : match.group(4) != null ? match.group(4) : "";
      return Attribute(match.group(1),
          value.isEmpty ? "" : value.substring(1, value.length - 1), value);
    }).toList();

    handler["start"](tagName, attrs, unary);
  }

  parseEndTag([String tagName = ""]) {
    if (tagName.trim().isNotEmpty) handler["end"](tagName);
  }
}

Map<String, bool> makeMap(str) {
  var obj = Map<String, bool>(), items = str.split(",");
  for (var i = 0; i < items.length; i++) {
    obj[items[i]] = true;
  }
  return obj;
}
