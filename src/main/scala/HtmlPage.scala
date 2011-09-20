package cn.orz.pascal.scala.mechanize

import com.gargoylesoftware.htmlunit.html.{HtmlPage => HtmlUnitPage}
import com.gargoylesoftware.htmlunit.html.{HtmlForm => HtmlUnitForm}
import com.gargoylesoftware.htmlunit.html.{HtmlInput => HtmlUnitInput}
import java.net.URL
import scala.collection.JavaConversions._

// vim: set ts=4 sw=4 et:
abstract class HttpMethod
case object Get extends HttpMethod
case object Post extends HttpMethod
case object Put extends HttpMethod
case object Delete extends HttpMethod
case object Head extends HttpMethod

abstract class FieldAttribute() { def value:String }
case class Name(val value:String) extends FieldAttribute
case class Class(val value:String) extends FieldAttribute
case class Type(val value:String) extends FieldAttribute
case class XPath(val value:String) extends FieldAttribute

class HtmlPage(val page:HtmlUnitPage) {
    def title:String =  page.getTitleText
    def url:URL = page.getUrl
    def forms:List[HtmlForm] = {
        page.getForms.map(new HtmlForm(_)).toList
    }
}

class HtmlForm(val form:HtmlUnitForm) {
    def name:String = form.getNameAttribute
    def method:HttpMethod = {
        form.getMethodAttribute.toUpperCase match {
          case "GET" => Get
          case "POST" => Post
          case _ => Get
        }
    }    
    def submit():HtmlPage = new HtmlPage(form.click.asInstanceOf[HtmlUnitPage])

    def fields_with(attribute:FieldAttribute):List[HtmlField] = {
        (attribute match {
          case Name(value) => findByXpath(".//input[@name='" + value + "']", form)
          case Class(value) => findByXpath(".//input[@class='" + value + "']", form)
          case Type(value) => findByXpath(".//input[@type='" + value + "']", form)
          case XPath(xpath) => findByXpath(xpath, form)
        }).map( node => new HtmlField(node.asInstanceOf[HtmlUnitInput]))
    }

    def findByXpath(xpathValue:String, node:org.w3c.dom.Node):List[org.w3c.dom.Node] = {
        import javax.xml.xpath._
        import org.w3c.dom._
        
        val xpathParser = XPathFactory.newInstance().newXPath().compile(xpathValue)
        val nodelist = xpathParser.evaluate(node, XPathConstants.NODESET).asInstanceOf[NodeList]
        (0 to nodelist.getLength).map( i => nodelist.item(i)).toList
    }
}
class HtmlField(val field:HtmlUnitInput) {
    def name():String = field.getNameAttribute
}
