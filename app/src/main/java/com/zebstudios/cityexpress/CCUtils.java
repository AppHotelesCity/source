package com.zebstudios.cityexpress;

/**
 * Created by rczuart on 05/11/2014.
 * http://www.rgagnon.com/javadetails/java-0034.html
 */
public class CCUtils
{
	public static final int INVALID = -1;
	public static final int VISA = 0;
	public static final int MASTERCARD = 1;
	public static final int AMERICAN_EXPRESS = 2;

	private static final String[] cardNames = { "VISA", "MASTERCARD", "AMERICANEXPRESS" };

	/**
	 * Valid a Credit Card number
	 */
	public static boolean validCC( String number )
	{
		return getCardID( number ) != -1 && validCCNumber( number );
	}

	/**
	 * Get the Card type
	 * returns the credit card type
	 * INVALID          = -1;
	 * VISA             = 0;
	 * MASTERCARD       = 1;
	 * AMERICAN_EXPRESS = 2;
	 */
	public static int getCardID( String number )
	{
		int valid = INVALID;

		String digit1 = number.substring( 0, 1 );
		String digit2 = number.substring( 0, 2 );

		if( isNumber( number ) )
		{
	  		/*
      		* VISA  prefix=4 length=13 or 16  (can be 15 too!?! maybe)
      		* MASTERCARD  prefix= 51 ... 55 length= 16
      		* AMEX  prefix=34 or 37 length=15
      		*/
			if( digit1.equals( "4" ) )
			{
				if( number.length() == 13 || number.length() == 16 )
				{
					valid = VISA;
				}
			}
			else if( digit2.compareTo( "51" ) >= 0 && digit2.compareTo( "55" ) <= 0 )
			{
				if( number.length() == 16 )
				{
					valid = MASTERCARD;
				}
			}
			else if( digit2.equals( "34" ) || digit2.equals( "37" ) )
			{
				if( number.length() == 15 )
				{
					valid = AMERICAN_EXPRESS;
				}
			}
		}

		return valid;
	}

	public static boolean isNumber( String n )
	{
		try
		{
			double d = Double.valueOf( n );
			return true;
		}
		catch( Exception e )
		{
			return false;
		}
	}

	public static String getCardName( String number )
	{
		return getCardName( getCardID( number ) );
	}

	public static String getCardName( int id )
	{
		return ( id > -1 && id < cardNames.length ? cardNames[id] : "" );
	}

	public static boolean validCCNumber( String n )
	{
		try
		{
			//known as the LUHN Formula (mod10)
			int j = n.length();

			String[] s1 = new String[j];
			for( int i = 0; i < n.length(); i++ )
			{
				s1[i] = "" + n.charAt( i );
			}

			int checksum = 0;

			int k = 0;
			for( int i = s1.length - 1; i >= 0; i -= 2 )
			{
				if( i > 0 )
				{
					k = Integer.valueOf( s1[i - 1] ) * 2;
					if( k > 9 )
					{
						String s = "" + k;
						k = Integer.valueOf( s.substring( 0, 1 ) ) + Integer.valueOf( s.substring( 1 ) );
					}
					checksum += Integer.valueOf( s1[i] ) + k;
				}
				else
				{
					checksum += Integer.valueOf( s1[0] );
				}
			}
			return ( ( checksum % 10 ) == 0 );
		}
		catch( Exception e )
		{
			e.printStackTrace();
			return false;
		}
	}
}
