package com.zebstudios.cityexpress;

import java.io.Serializable;
import java.util.Comparator;

/**
 * Created by Denumeris Interactive on 4/24/2015.
 */
public class Country implements Serializable
{
    private static final long serialVersionUID = 0L;

    private String _code;
    private String _ISOCode;
    private String _name;

    public Country()
    {
    }

    public String getCode()
    {
        return _code;
    }

    public void setCode( String code )
    {
        _code = code;
    }

    public String getISOCode()
    {
        return _ISOCode;
    }

    public void setISOCode( String ISOCode )
    {
        _ISOCode = ISOCode;
    }

    public String getName()
    {
        return _name;
    }

    public void setName( String name )
    {
        _name = name;
    }

    public static class CountryComparator implements Comparator<Country>
    {
        public int compare( Country c1, Country c2 )
        {
            return c1.getName().compareToIgnoreCase( c2.getName() );
        }
    }
}
