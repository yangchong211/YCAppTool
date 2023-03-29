import 'dart:convert';

import 'package:yc_flutter_utils/parsing/html_parser.dart';
import 'package:yc_flutter_utils/parsing/node_type.dart';

import 'Elements.dart';

class MarkupUtils {

  ///Converts [Map] to [Node]
  static Node mapToNode(Map<String, dynamic> json) {
    dynamic node;
    switch (json["type"]) {
      case NodeType.ROOT:
        node = RootNode(null);
        break;
      case NodeType.ELEMENT:
        node = ElementNode(tag: json["tag"]);
        break;
      case NodeType.TEXT:
        node = TextNode(json["text"]);
        break;
      case NodeType.COMMENT:
        node = CommentNode(json["text"]);
        break;
    }

    if (json["children"] != null) {
      node.children =
          (json["children"] as List<dynamic>).map((c) => mapToNode(c)).toList();
    }

    if (json["attributes"] != null) {
      node.attributes = (json["attributes"] as List<dynamic>)
          .map((attr) =>
              Attribute(attr["name"], attr["value"], attr["valueOriginal"]))
          .toList();
    }

    return node;
  }

  ///Converts json to [Node]
  static Node jsonToNode(String jsonString) {
    return mapToNode(jsonDecode(jsonString));
  }

  /// Removes DOCTYPE
  /// 删除文档类型
  static _removeDOCTYPE(String html) {
    String xmlType;
    if ((xmlType = RegExp(r"<\?xml.*\?>").stringMatch(html)) != null) {
      html = html.replaceFirst(xmlType, '');
    }

    String hxmlType1;
    if ((hxmlType1 = RegExp(r"<!doctype.*\>").stringMatch(html)) != null) {
      html = html.replaceFirst(hxmlType1, '');
    }

    String hxmlType2;
    if ((hxmlType2 = RegExp(r"<!DOCTYPE.*\>").stringMatch(html)) != null) {
      html = html.replaceFirst(hxmlType2, '');
    }
    return html;
  }

  /// 将HTML/XML字符串转换为[Node]
  static Node markup2json(String markupString) {
    markupString = _removeDOCTYPE(markupString);
    List<Node> bufArray = [];
    RootNode results = RootNode([]);
    HtmlParser(markupString, {
      "start": (String tag, List<Attribute> attrs, bool unary) {
        ElementNode elementNode = ElementNode(tag: tag.trim());
        if (attrs.isNotEmpty) {
          elementNode.attributes = attrs;
        }
        if (unary) {
          var parent =
              (bufArray.isNotEmpty ? bufArray[0] : results) as ElementNode;
          if (parent.children == null) {
            parent.children = [];
          }
          parent.children.add(elementNode);
        } else {
          bufArray.insert(0, elementNode);
        }
      },
      "end": (String tag) {
        if (tag.trim().isEmpty) return;

        var node = bufArray.removeAt(0) as ElementNode;
        if (node.tag != tag) print('invalid state: mismatch end tag: $node');

        if (bufArray.isEmpty) {
          results.children.add(node);
        } else {
          var parent = bufArray[0] as ElementNode;
          if (parent.children == null) {
            parent.children = [];
          }
          parent.children.add(node);
        }
      },
      "chars": (String text) {
        if (text.trim().isEmpty) {
          return;
        }

        TextNode textNode = TextNode(text);
        if (bufArray.isEmpty) {
          results.children.add(textNode);
        } else {
          var parent = bufArray[0] as ElementNode;
          if (parent.children == null) {
            parent.children = [];
          }
          parent.children.add(textNode);
        }
      },
      "comment": (String text) {
        CommentNode commentNode = CommentNode(text);
        var parent = bufArray[0] as ElementNode;
        if (parent.children == null) {
          parent.children = [];
        }
        parent.children.add(commentNode);
      }
    });
    return results;
  }

  /// Converts jsonString to markupString
  /// 将json字符串转换为标记字符串
  static String jsonToMarkup(String jsonString) =>
      _parse(jsonDecode(jsonString));

  ///Converts [Map] to markupString
  static String mapToMarkup(Map<String, dynamic> jsonMap) => _parse(jsonMap);

  ///Converts [Map] to markupString
  static String _parse(Map<String, dynamic> json) {
    var child = '';
    if (json["children"] != null) {
      child = (json["children"] as List<dynamic>).map((c) {
        return _parse(c);
      }).join('');
    }

    var attr = '';

    if (json["attributes"] != null) {
      attr = (json["attributes"] as List<dynamic>).map((attrObj) {
        if (attrObj['valueOriginal'].toString().isEmpty) return attrObj['name'];
        return '${attrObj['name']}=${attrObj['valueOriginal']}';
      }).join(' ');
      if (attr.isNotEmpty) attr = ' ' + attr;
    }

    if (json["type"] == NodeType.ELEMENT) {
      var tag = json["tag"].toString();
      if (json["children"] == null) {
        return '<' + tag + attr + '/>';
      }

      var open = '<' + tag + attr + '>';
      var close = '</' + tag + '>';
      return open + child + close;
    }

    if (json["type"] == NodeType.TEXT) {
      return json["text"];
    }

    if (json["type"] == NodeType.COMMENT) {
      return '<!--' + json["text"] + '-->';
    }

    if (json["type"] == NodeType.ROOT) {
      return child;
    }
    return child;
  }
}


