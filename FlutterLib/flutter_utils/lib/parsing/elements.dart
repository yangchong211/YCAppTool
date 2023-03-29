import 'dart:convert';

import 'package:yc_flutter_utils/parsing/markup_utils.dart';
import 'package:yc_flutter_utils/parsing/node_type.dart';




/// 这是一个HTML/XML元素，由下面这些组成
/// [RootNode],[ElementNode],[TextNode],[CommentNode]
abstract class Node {

  String type;
  String tag;
  String text;
  List<Attribute> attributes;
  List<Node> children;

  Node(this.type, {this.tag, this.text, this.attributes, this.children});

  Map<String, dynamic> toMap() {
    Map<String, dynamic> map = {"type": type};

    if (tag != null) map["tag"] = tag;
    if (text != null) map["text"] = text;

    if (attributes != null) {
      map["attributes"] =
          attributes.map((attribute) => attribute.toMap()).toList();
    }

    if (children != null) {
      map["children"] = children.map((child) => child.toMap()).toList();
    }
    return map;
  }

  String toMarKup() {
    return MarkupUtils.mapToMarkup(toMap());
  }

  String toJson() {
    return toString();
  }

  String toString() {
    return jsonEncode(toMap());
  }
}

/// 它是JSON的起点，它包含实际的HTML/XML元素的子元素。
/// 开始标记。它拥有孩子。
class RootNode extends Node {

  String type = NodeType.ROOT;

  List<Node> children;

  RootNode(this.children) : super(NodeType.ROOT, children: children);
}

/// 它是包含属性和子元素的HTML/XML标记。
/// 应该指定标签(例如:div)。支持属性和子元素。
class ElementNode extends Node {
  String type = NodeType.ELEMENT;
  String tag;
  List<Attribute> attributes;
  List<Node> children;

  ElementNode({this.tag, this.attributes, this.children})
      : super(NodeType.ELEMENT,
            tag: tag, attributes: attributes, children: children);
}

/// 它是HTML/XML元素中的文本内容。
/// 比如. <div>Hello world!</div>
/// 那么： `Hello world!` is the TextNode
/// 它是出现在HTML/Markup标签内的文本。
class TextNode extends Node {
  String type = NodeType.TEXT;
  String text;

  TextNode(this.text) : super(NodeType.TEXT, text: text);
}

/// 它是节点之间的注释
/// 它是一个注释，可以放在元素之间，
class CommentNode extends Node {
  String type = NodeType.COMMENT;
  String text;

  CommentNode(this.text) : super(NodeType.COMMENT, text: text);
}

/// 有HTML/XML元素的属性。所以只有[ElementNode]支持属性。
class Attribute {

  String name;
  String value;
  String valueOriginal;

  Attribute(this.name, this.value, [this.valueOriginal]) {
    if (valueOriginal == null) {
      valueOriginal = '"$value"';
    }
  }

  Map<String, dynamic> toMap() {
    return {"name": name, "value": value, "valueOriginal": valueOriginal};
  }

  String toJson() {
    return toString();
  }

  @override
  String toString() {
    return jsonEncode(toMap());
  }
}
