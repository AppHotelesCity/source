package com.zebstudios.cityexpress;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Denumeris Interactive on 24/10/2014.
 * https://github.com/nerdability/AndroidRssReader
 */
public class RSSHandler extends DefaultHandler
{
	// Feed and Article objects to use for temporary storage
	private RSSArticle currentArticle = new RSSArticle();
	private List<RSSArticle> articleList = new ArrayList<RSSArticle>();
    boolean bandera=false;

	//Current characters being accumulated
	StringBuffer chars = new StringBuffer();

	public List<RSSArticle> getArticleList()
	{
		return articleList;
	}

	/*
	 * This method is called everytime a start element is found (an opening XML marker)
	 * here we always reset the characters StringBuffer as we are only currently interested
	 * in the the text values stored at leaf nodes
	 *
	 * (non-Javadoc)
	 * @see org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	public void startElement( String uri, String localName, String qName, Attributes atts )
	{
		chars = new StringBuffer();
		if( qName.equalsIgnoreCase( "media:content" ) )
		{
			if( atts.getValue( "type" ).equalsIgnoreCase( "image/jpeg" ) )
			{
				currentArticle.setMedia( atts.getValue( "url" ) );
			}
		}
		/*if( localName.equalsIgnoreCase( "enclosure" ) )
		{
			if( atts.getValue( "type" ).equalsIgnoreCase( "image/jpeg" ) )
			{
				currentArticle.setMedia( atts.getValue( "url" ) );
			}
		}*/
	}


	/*
	 * This method is called everytime an end element is found (a closing XML marker)
	 * here we check what element is being closed, if it is a relevant leaf node that we are
	 * checking, such as Title, then we get the characters we have accumulated in the StringBuffer
	 * and set the current Article's title to the value
	 *
	 * If this is closing the "entry", it means it is the end of the article, so we add that to the list
	 * and then reset our Article object for the next one on the stream
	 *
	 *
	 * (non-Javadoc)
	 * @see org.xml.sax.helpers.DefaultHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
	 */
	public void endElement( String uri, String localName, String qName ) throws SAXException
	{
		if( qName.equalsIgnoreCase( "title" ) )
		{
			currentArticle.setTitle( chars.toString() );
		}
		else if( localName.equalsIgnoreCase( "description" ) )
		{
            if(!bandera){
                bandera=true;
            }else{
                bandera=false;
                currentArticle.setDescription(chars.toString());
            }
        } else if (localName.equalsIgnoreCase( "published" ) )
		{
			currentArticle.setPubDate( chars.toString() );
		}
		else if( localName.equalsIgnoreCase( "id" ) )
		{
			currentArticle.setGuid( chars.toString() );
		}
		else if( localName.equalsIgnoreCase( "author" ) )
		{
			currentArticle.setAuthor( chars.toString() );
		}
		else if( localName.equalsIgnoreCase( "link" ) )
		{
			currentArticle.setUrl( chars.toString() );
		}
		else if( localName.equalsIgnoreCase( "content" ) )
		{
			currentArticle.setEncodedContent( chars.toString() );
		}
		else if( localName.equalsIgnoreCase( "item" ) )
		{
			articleList.add( currentArticle );
			currentArticle = new RSSArticle();
		}
	}


	/*
	 * This method is called when characters are found in between XML markers, however, there is no
	 * guarante that this will be called at the end of the node, or that it will be called only once
	 * , so we just accumulate these and then deal with them in endElement() to be sure we have all the
	 * text
	 *
	 * (non-Javadoc)
	 * @see org.xml.sax.helpers.DefaultHandler#characters(char[], int, int)
	 */
	public void characters( char ch[], int start, int length )
	{
		chars.append( new String( ch, start, length ) );
	}
}
