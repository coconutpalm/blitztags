package blitztags

sealed trait Node

trait InnerNode extends Node {
  val childNodes: Vector[Node]

  def appendChild(n: Node): InnerNode
}

trait LeafNode extends Node

case class ElementNode(
  tag: Symbol,
  attributes: Vector[AttrNode] = Vector(),
  childNodes: Vector[Node] = Vector()) extends InnerNode {

  override def toString() = {
    val attrs = attributes.mkString("")
    val childs = childNodes.mkString

    s"<${tag.name}$attrs>$childs</${tag.name}>"
  }

  override def appendChild(n: Node) = {
    copy(childNodes = childNodes :+ n)
  }
}

case class CommentNode(text: String) extends LeafNode{
  override def toString() = {
    s"<!-- $text -->"
  }
}

case class VoidElementNode(tag: Symbol, attributes: Vector[AttrNode]) extends LeafNode {
  override def toString() = {
    val attrs = attributes.mkString("")
    s"<${tag.name}$attrs />"
  }
}

case class AttrNode(name: Symbol, value: String) extends LeafNode {
  override def toString() = {
    s""" ${name.name}="${value}""""
  }
}

case class BooleanAttrNode(name: Symbol, value: Boolean) extends LeafNode {
  override def toString() =
    if (value) s" ${name.name}"
    else ""
}

case class DocumentNode(
  doctype: DocumentTypeNode,
  childNodes: Vector[Node] = Vector()) extends InnerNode {

  override def toString() = {
    doctype.toString + childNodes.mkString
  }

  override def appendChild(n: Node) = {
    copy(childNodes = childNodes :+ n)
  }
}

case class DocumentTypeNode(doctype: String) extends LeafNode{
  override def toString() = {
    doctype
  }
}

case class TextNode(text: String) extends LeafNode {
  override def toString() = {
    text
  }
}