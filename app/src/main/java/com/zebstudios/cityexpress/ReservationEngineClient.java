package com.zebstudios.cityexpress;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.Marshal;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;

/**
 * Created by rczuart on 14/09/2014.
 */
public class ReservationEngineClient
{
	String NAMESPACE = "http://tempuri.org/";

	public ResponseBooking InsertBookingv3_01( ReservationModelv3_01 reserva )
	{
		ResponseBooking responseBooking = new ResponseBooking();
		SoapObject request = new SoapObject( NAMESPACE, "InsertBookingv3_01" );

		PropertyInfo info = new PropertyInfo();
		info.setName( "mdlReservation" );
		info.setValue( reserva );
		info.setType( reserva.getClass() );

		request.addProperty( info );

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope( SoapEnvelope.VER11 );
		envelope.dotNet = true;
		envelope.setOutputSoapObject( request );
		envelope.setAddAdornments( false );

		envelope.addMapping( "http://schemas.datacontract.org/2004/07/CityHub", "ReservationModelv3_01", ReservationModelv3_01.class );
		envelope.addMapping( "http://schemas.datacontract.org/2004/07/CityHub", "Deposito_PayPal", Deposito_PayPal.class );
		envelope.addMapping( "http://schemas.datacontract.org/2004/07/CityHub", "Empresa", Empresa.class );
		envelope.addMapping( "http://schemas.datacontract.org/2004/07/CityHub", "Estancia", Estancia.class );
		envelope.addMapping( "http://schemas.datacontract.org/2004/07/CityHub", "Persona", Persona.class );
		envelope.addMapping( "http://schemas.datacontract.org/2004/07/CityHub", "ArrayOfPersona", ArrayOfPersona.class );
		envelope.addMapping( "http://schemas.datacontract.org/2004/07/CityHub", "Huesped", Huesped.class );
		envelope.addMapping( "http://schemas.datacontract.org/2004/07/CityHub", "Habitacion", Habitacion.class );
		envelope.addMapping( "http://schemas.datacontract.org/2004/07/CityHub", "Reservante", Reservante.class );
		envelope.addMapping( "http://schemas.datacontract.org/2004/07/CityHub", "TarjetaCredito", TarjetaCredito.class );
		envelope.addMapping( "http://schemas.datacontract.org/2004/07/CityHub", "TarjetaVirtual", TarjetaVirtual.class );

		MarshalFloat mf = new MarshalFloat();
		mf.register( envelope );

		HttpTransportSE transport = new HttpTransportSE( APIAddress.RESERVATION_ENGINE_URL );
		transport.debug = true;

		try
		{
			transport.call( "http://tempuri.org/IReservationEngine/InsertBookingv3_01", envelope );
			android.util.Log.d( "TEST", transport.requestDump );

			Object obj = envelope.bodyIn;
			if( obj == null )
			{
				responseBooking.setResponse( -2 );
				android.util.Log.d( "ReservationEngineClient", "NO RESPONSE" );
			}
			else
			{
				if( envelope.bodyIn instanceof SoapFault )
				{
					responseBooking.setResponse( -3 );
					String str= ((SoapFault) envelope.bodyIn).faultstring;
					android.util.Log.d( "ReservationEngineClient", "RESPONSE FAULT: " + str );
				}
				else
				{
					android.util.Log.d( "ReservationEngineClient", "RESPONSE: " + transport.responseDump );

					SoapObject resultsRequestSOAP = (SoapObject) envelope.bodyIn;
					SoapObject resultResponseBooking = (SoapObject)resultsRequestSOAP.getProperty( 0 );

					responseBooking.setCodigoError( resultResponseBooking.getPropertySafelyAsString( "CodigoError" ) );
					responseBooking.setDescError( resultResponseBooking.getPropertySafelyAsString( "DescError" ) );
					responseBooking.setErrorMessage( resultResponseBooking.getPropertySafelyAsString( "ErrorMessage" ) );
					responseBooking.setNumReservacion( resultResponseBooking.getPropertySafelyAsString( "NumReservacion" ) );
					responseBooking.setTotalRate( Double.parseDouble( resultResponseBooking.getPropertySafelyAsString( "TotalRate" ) ) );

					android.util.Log.d( "ReservationEngineClient", "ERROR: " + responseBooking.getCodigoError() );
					android.util.Log.d( "ReservationEngineClient", "RESERVA: " + responseBooking.getNumReservacion() );

					responseBooking.setResponse( 0 );
				}
			}
		}
		catch( Exception e )
		{
			responseBooking.setResponse( -4 );
			android.util.Log.d( "ReservationEngineClient", "Error: " + e.getMessage() );
			e.printStackTrace();
		}

		return responseBooking;
	}

	public GetRoomsAvailablePromoResult GetRoomsAvailablePromo( PromoRequestModel promo )
	{
		GetRoomsAvailablePromoResult result = new GetRoomsAvailablePromoResult();

		SoapObject request = new SoapObject( NAMESPACE, "GetRoomsAvailablePromo" );

		PropertyInfo info = new PropertyInfo();
		info.setName( "promoRequestModelv3" );
		info.setValue( promo );
		info.setType( promo.getClass() );

		request.addProperty( info );

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope( SoapEnvelope.VER11 );
		envelope.dotNet = true;
		envelope.setOutputSoapObject( request );
		envelope.setAddAdornments( false );

		envelope.addMapping( "http://schemas.datacontract.org/2004/07/CityHub", "PromoRequestModel", PromoRequestModel.class );

		HttpTransportSE transport = new HttpTransportSE( APIAddress.RESERVATION_ENGINE_URL );
		transport.debug = true;

		try
		{
			transport.call( "http://tempuri.org/IReservationEngine/GetRoomsAvailablePromo", envelope );

			Object obj = envelope.bodyIn;
			if( obj == null )
			{
				result.setResponse( -2 );
				android.util.Log.d( "ReservationEngineClient", "NO RESPONSE" );
			}
			else
			{
				if( envelope.bodyIn instanceof SoapFault )
				{
					android.util.Log.d( "ReservationEngineClient", transport.requestDump );
					result.setResponse( -3 );
					String str= ((SoapFault) envelope.bodyIn).faultstring;
					android.util.Log.d( "ReservationEngineClient", "RESPONSE FAULT: " + str );
				}
				else
				{
					//android.util.Log.d( "ReservationEngineClient", transport.responseDump );
					SoapObject resultsRequestSOAP = (SoapObject) envelope.bodyIn;
					SoapObject roomsAvailableresult = (SoapObject)resultsRequestSOAP.getProperty( 0 );
					SoapObject disponibilidad = (SoapObject)roomsAvailableresult.getProperty( 0 );

					ArrayList<Available> availables = new ArrayList<Available>();
					for( int i=0; i<disponibilidad.getPropertyCount(); i++ )
					{
						SoapObject aObj = (SoapObject)disponibilidad.getProperty( i );
						Available a = new Available();

						a.setCodigoError( aObj.getPropertySafelyAsString( "CodigoError" ) );
						a.setCodigoTarifa( aObj.getPropertySafelyAsString( "CodigoTarifa" ) );
						a.setDescError( aObj.getPropertySafelyAsString( "DescError" ) );
						a.setDescripcion( aObj.getPropertySafelyAsString( "Descripcion" ) );
						a.setHotel( aObj.getPropertySafelyAsString( "Hotel" ) );

						if( a.getCodigoError().length() == 0 )
						{
							SoapObject promocionesObj = (SoapObject) aObj.getProperty( "Promociones" );
							for( int j = 0; j < promocionesObj.getPropertyCount(); j++ )
							{
								SoapObject hpromotionObj = (SoapObject) promocionesObj.getProperty( j );
								HabPromo hp = new HabPromo();
								hp.setCodRoom( hpromotionObj.getPropertySafelyAsString( "CodRoom" ) );
								hp.setDescRoom( hpromotionObj.getPropertySafelyAsString( "DescRoom" ) );
								SoapObject promotionsObj = (SoapObject) hpromotionObj.getProperty( "Promotions" );
								for( int k = 0; k < promotionsObj.getPropertyCount(); k++ )
								{
									SoapObject promotionObj = (SoapObject) promotionsObj.getProperty( k );
									Promotion p = new Promotion();
									p.setCodPromo( promotionObj.getPropertySafelyAsString( "CodPromo" ) );
									p.setDescPromo( promotionObj.getPropertySafelyAsString( "DescPromo" ) );
									SoapObject nochesObj = (SoapObject) promotionObj.getProperty( "Noches" );
									for( int l = 0; l < nochesObj.getPropertyCount(); l++ )
									{
										SoapObject nocheObj = (SoapObject) nochesObj.getProperty( l );
										Noche n = new Noche();
										n.setCosto( nocheObj.getPropertySafelyAsString( "Costo" ) );
										n.setFecha( nocheObj.getPropertySafelyAsString( "Fecha" ) );
										p.getNoches().add( n );
									}
									hp.getPromotions().add( p );
								}
								a.getPromotions().add( hp );
							}

							SoapObject tarifaBaseObj = (SoapObject) aObj.getProperty( "TarifaBase" );
							for( int j = 0; j < tarifaBaseObj.getPropertyCount(); j++ )
							{
								SoapObject habBaseObj = (SoapObject) tarifaBaseObj.getProperty( j );
								HabBase habBase = new HabBase();
								habBase.setAvailability( Integer.parseInt( habBaseObj.getPropertySafelyAsString( "AVAILABILITY", "-1" ) ) );
								habBase.setCodBase( habBaseObj.getPropertySafelyAsString( "CodBase" ) );
								habBase.setDescBase( habBaseObj.getPropertySafelyAsString( "DescBase" ) );
								SoapObject nochesObj = (SoapObject) habBaseObj.getProperty( "Noches" );
								for( int l = 0; l < nochesObj.getPropertyCount(); l++ )
								{
									SoapObject nocheObj = (SoapObject) nochesObj.getProperty( l );
									Noche n = new Noche();
									n.setCosto( nocheObj.getPropertySafelyAsString( "Costo" ) );
									n.setFecha( nocheObj.getPropertySafelyAsString( "Fecha" ) );
									habBase.getNoches().add( n );
								}
								a.getTarifaBase().add( habBase );
							}
						}

						availables.add( a );
					}

					result.setResponse( 0 );
					result.setAvailables( availables );
				}
			}
		}
		catch( Exception e )
		{
			result.setResponse( -4 );
			android.util.Log.d( "ReservationEngineClient", "Error: " + e.getMessage() );
			e.printStackTrace();
		}

		return result;
	}

	public void Test()
	{
		new Thread(new Runnable()
		{
			public void run()
			{
				SoapObject request = new SoapObject( NAMESPACE, "GetRoomsAvailablePromo" );

				Calendar cal = Calendar.getInstance();
				Date d = cal.getTime();
				PromoRequestModel promo = new PromoRequestModel();
				promo.setHotel( "CEAGU" );
				promo.setCodigoTarifa( "1114" );
				promo.setNumeroDeNoches( 2 );
				promo.setNumeroAdultos( 1 );
				promo.setFechaInicial( d );

				PropertyInfo info = new PropertyInfo();
				info.setName( "promoRequestModelv3" );
				info.setValue( promo );
				info.setType( promo.getClass() );

				request.addProperty( info );

				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope( SoapEnvelope.VER11 );
				envelope.dotNet = true;
				envelope.setOutputSoapObject( request );
				envelope.setAddAdornments( false );

				envelope.addMapping( "http://schemas.datacontract.org/2004/07/CityHub", "PromoRequestModel", PromoRequestModel.class );

				HttpTransportSE transport = new HttpTransportSE( APIAddress.RESERVATION_ENGINE_URL );
				transport.debug = true;

				try
				{
					transport.call( "http://tempuri.org/IReservationEngine/GetRoomsAvailablePromo", envelope );

					Object obj = envelope.bodyIn;
					if( obj == null )
						android.util.Log.d( "ReservationEngineClient", "NO RESPONSE" );
					else
					{
						if( envelope.bodyIn instanceof SoapFault )
						{
							String str= ((SoapFault) envelope.bodyIn).faultstring;
							android.util.Log.d( "ReservationEngineClient", "RESPONSE FAULT: " + str );
						}
						else
						{
							//android.util.Log.d( "ReservationEngineClient", transport.responseDump );
							SoapObject resultsRequestSOAP = (SoapObject) envelope.bodyIn;
							SoapObject roomsAvailableresult = (SoapObject)resultsRequestSOAP.getProperty( 0 );
							SoapObject disponibilidad = (SoapObject)roomsAvailableresult.getProperty( 0 );

							ArrayList<Available> availables = new ArrayList<Available>();
							for( int i=0; i<disponibilidad.getPropertyCount(); i++ )
							{
								SoapObject aObj = (SoapObject)disponibilidad.getProperty( i );
								Available a = new Available();

								a.setCodigoError( aObj.getPropertySafelyAsString( "CodigoError" ) );
								a.setCodigoTarifa( aObj.getPropertySafelyAsString( "CodigoTarifa" ) );
								a.setDescError( aObj.getPropertySafelyAsString( "DescError" ) );
								a.setDescripcion( aObj.getPropertySafelyAsString( "Descripcion" ) );
								a.setHotel( aObj.getPropertySafelyAsString( "Hotel" ) );

								SoapObject promocionesObj = (SoapObject)aObj.getProperty( "Promociones" );
								for( int j=0; j<promocionesObj.getPropertyCount(); j++ )
								{
									SoapObject hpromotionObj = (SoapObject)promocionesObj.getProperty( j );
									HabPromo hp = new HabPromo();
									hp.setCodRoom( hpromotionObj.getPropertySafelyAsString( "CodRoom" ) );
									hp.setDescRoom( hpromotionObj.getPropertySafelyAsString( "DescRoom" ) );
									SoapObject promotionsObj = (SoapObject)hpromotionObj.getProperty( "Promotions" );
									for( int k=0; k<promotionsObj.getPropertyCount(); k++ )
									{
										SoapObject promotionObj = (SoapObject)promotionsObj.getProperty( k );
										Promotion p = new Promotion();
										p.setCodPromo( promotionObj.getPropertySafelyAsString( "CodPromo" ) );
										p.setDescPromo( promotionObj.getPropertySafelyAsString( "DescPromo" ) );
										SoapObject nochesObj = (SoapObject)promotionObj.getProperty( "Noches" );
										for( int l=0; l<nochesObj.getPropertyCount(); l++ )
										{
											SoapObject nocheObj = (SoapObject)nochesObj.getProperty( l );
											Noche n = new Noche();
											n.setCosto( nocheObj.getPropertySafelyAsString( "Costo" ) );
											n.setFecha( nocheObj.getPropertySafelyAsString( "Fecha" ) );
											p.getNoches().add( n );
										}
										hp.getPromotions().add( p );
									}
									a.getPromotions().add( hp );
								}

								SoapObject tarifaBaseObj = (SoapObject)aObj.getProperty( "TarifaBase" );
								for( int j=0; j<tarifaBaseObj.getPropertyCount(); j++ )
								{
									SoapObject habBaseObj = (SoapObject)tarifaBaseObj.getProperty( j );
									HabBase habBase = new HabBase();
									habBase.setAvailability( Integer.parseInt( habBaseObj.getPropertySafelyAsString( "AVAILABILITY", "-1" ) ) );
									habBase.setCodBase( habBaseObj.getPropertySafelyAsString( "CodBase" ) );
									habBase.setDescBase( habBaseObj.getPropertySafelyAsString( "DescBase" ) );
									SoapObject nochesObj = (SoapObject)habBaseObj.getProperty( "Noches" );
									for( int l=0; l<nochesObj.getPropertyCount(); l++ )
									{
										SoapObject nocheObj = (SoapObject)nochesObj.getProperty( l );
										Noche n = new Noche();
										n.setCosto( nocheObj.getPropertySafelyAsString( "Costo" ) );
										n.setFecha( nocheObj.getPropertySafelyAsString( "Fecha" ) );
										habBase.getNoches().add( n );
									}
									a.getTarifaBase().add( habBase );
								}

								availables.add( a );
							}

						}
					}
				}
				catch( Exception e )
				{
					android.util.Log.d( "ReservationEngineClient", "Error: " + e.getMessage() );
					e.printStackTrace();
				}
		/*try
		{

		}
		catch( Exception e )
		{
			e.printStackTrace();
		}*/
			}
		}).start();
	}

	public static class PromoRequestModel implements KvmSerializable
	{
		private static final long serialVersionUID = 0L;

		private String _codigoPromocion;
		private String _codigoTarifa;
		private Date _fechaInicial;
		private String _hotel;
		private Integer _numeroAdultos;
		private Integer _numeroDeNoches;
		private Integer _numeroHabitaciones;
		private String _segmento;
		private String _tipoHabitacion;

		public PromoRequestModel()
		{
			_numeroAdultos = 0;
			_numeroDeNoches = 0;
			_numeroHabitaciones = 0;
		}

		private static Object getSOAPDateString(Date itemValue)
		{
			if( itemValue != null )
			{
				SimpleDateFormat lDateFormat = new SimpleDateFormat( "yyyy-MM-dd'T'hh:mm:ss'Z'" );
				return lDateFormat.format( itemValue );
			}
			else return "";
		}

		private static Date getDateFromSOAPString( String itemValue )
		{
			SimpleDateFormat lDateFormat = new SimpleDateFormat( "yyyy-MM-dd'T'hh:mm:ss'Z'" );
			Date result = null;
			try
			{
				result = lDateFormat.parse( itemValue );
			}
			catch( Exception e )
			{
				android.util.Log.d( "ReservationEngineClient", "Cant parse date: " +e.getMessage() );
			}
			return result;
		}

		@Override
		public Object getProperty( int i )
		{
			if( i == 0 )
				return this.getCodigoPromocion();
			else if( i == 1 )
				return this.getCodigoTarifa();
			else if( i == 2 )
				return getSOAPDateString( this.getFechaInicial() );
			else if( i == 3 )
				return this.getHotel();
			else if( i == 4 )
				return this.getNumeroAdultos();
			else if( i == 5 )
				return this.getNumeroDeNoches();
			else if( i == 6 )
				return this.getNumeroHabitaciones();
			else if( i == 7 )
				return this.getSegmento();
			else if( i == 8 )
				return this.getTipoHabitacion();
			else
				return null;
		}

		@Override
		public int getPropertyCount()
		{
			return 9;
		}

		@Override
		public void setProperty( int i, Object o )
		{
			if( i== 0 )
				this.setCodigoPromocion( (String) o );
			else if( i == 1 )
				this.setCodigoTarifa( (String)o );
			else if( i == 2 )
				this.setFechaInicial( getDateFromSOAPString( (String)o ) );
			else if( i == 3 )
				this.setHotel( (String)o );
			else if( i == 4 )
				this.setNumeroAdultos( (Integer)o );
			else if( i == 5 )
				this.setNumeroDeNoches( (Integer) o );
			else if( i == 6 )
				this.setNumeroHabitaciones( (Integer) o );
			else if( i == 7 )
				this.setSegmento( (String)o );
			else if( i == 8 )
				this.setTipoHabitacion( (String)o );
		}

		@Override
		public void getPropertyInfo( int i, Hashtable hashtable, PropertyInfo propertyInfo )
		{
			if( i == 0 )
			{
				//propertyInfo.type = PropertyInfo.STRING_CLASS;
				propertyInfo.name = "CodigoPromocion";
				propertyInfo.namespace = "http://schemas.datacontract.org/2004/07/CityHub";
			}
			else if( i == 1 )
			{
				//propertyInfo.type = PropertyInfo.STRING_CLASS;
				propertyInfo.name = "CodigoTarifa";
				propertyInfo.namespace = "http://schemas.datacontract.org/2004/07/CityHub";
			}
			else if( i == 2 )
			{
				//propertyInfo.type = PropertyInfo.STRING_CLASS;
				propertyInfo.name = "FechaInicial";
				propertyInfo.namespace = "http://schemas.datacontract.org/2004/07/CityHub";
			}
			else if( i == 3 )
			{
				//propertyInfo.type = PropertyInfo.STRING_CLASS;
				propertyInfo.name = "Hotel";
				propertyInfo.namespace = "http://schemas.datacontract.org/2004/07/CityHub";
			}
			else if( i == 4 )
			{
				//propertyInfo.type = PropertyInfo.INTEGER_CLASS;
				propertyInfo.name = "NumeroAdultos";
				propertyInfo.namespace = "http://schemas.datacontract.org/2004/07/CityHub";
			}
			else if( i == 5 )
			{
				//propertyInfo.type = PropertyInfo.INTEGER_CLASS;
				propertyInfo.name = "NumeroDeNoches";
				propertyInfo.namespace = "http://schemas.datacontract.org/2004/07/CityHub";
			}
			else if( i == 6 )
			{
				//propertyInfo.type = PropertyInfo.INTEGER_CLASS;
				propertyInfo.name = "NumeroHabitaciones";
				propertyInfo.namespace = "http://schemas.datacontract.org/2004/07/CityHub";
			}
			else if( i == 7 )
			{
				//propertyInfo.type = PropertyInfo.STRING_CLASS;
				propertyInfo.name = "Segmento";
				propertyInfo.namespace = "http://schemas.datacontract.org/2004/07/CityHub";
			}
			else if( i == 8 )
			{
				//propertyInfo.type = PropertyInfo.STRING_CLASS;
				propertyInfo.name = "TipoHabitacion";
				propertyInfo.namespace = "http://schemas.datacontract.org/2004/07/CityHub";
			}
		}

		public String getCodigoPromocion()
		{
			return _codigoPromocion;
		}

		public void setCodigoPromocion( String codigoPromocion )
		{
			_codigoPromocion = codigoPromocion;
		}

		public String getCodigoTarifa()
		{
			return _codigoTarifa;
		}

		public void setCodigoTarifa( String codigoTarifa )
		{
			_codigoTarifa = codigoTarifa;
		}

		public Date getFechaInicial()
		{
			return _fechaInicial;
		}

		public void setFechaInicial( Date fechaInicial )
		{
			_fechaInicial = fechaInicial;
		}

		public String getHotel()
		{
			return _hotel;
		}

		public void setHotel( String hotel )
		{
			_hotel = hotel;
		}

		public Integer getNumeroAdultos()
		{
			return _numeroAdultos;
		}

		public void setNumeroAdultos( Integer numeroAdultos )
		{
			_numeroAdultos = numeroAdultos;
		}

		public Integer getNumeroDeNoches()
		{
			return _numeroDeNoches;
		}

		public void setNumeroDeNoches( Integer numeroDeNoches )
		{
			_numeroDeNoches = numeroDeNoches;
		}

		public Integer getNumeroHabitaciones()
		{
			return _numeroHabitaciones;
		}

		public void setNumeroHabitaciones( Integer numeroHabitaciones )
		{
			_numeroHabitaciones = numeroHabitaciones;
		}

		public String getSegmento()
		{
			return _segmento;
		}

		public void setSegmento( String segmento )
		{
			_segmento = segmento;
		}

		public String getTipoHabitacion()
		{
			return _tipoHabitacion;
		}

		public void setTipoHabitacion( String tipoHabitacion )
		{
			_tipoHabitacion = tipoHabitacion;
		}
	}

	public static class GetRoomsAvailablePromoResult
	{
		private int _response;
		private ArrayList<Available> _availables;

		public GetRoomsAvailablePromoResult()
		{
			_response = -1;
		}

		public int getResponse()
		{
			return _response;
		}

		public void setResponse( int response )
		{
			_response = response;
		}

		public ArrayList<Available> getAvailables()
		{
			return _availables;
		}

		public void setAvailables( ArrayList<Available> availables )
		{
			_availables = availables;
		}
	}

	public static class Available
	{
		private String _codigoError;
		private String _codigoTarifa;
		private String _descError;
		private String _descripcion;
		private String _hotel;
		private ArrayList<HabPromo> _promotions;
		private ArrayList<HabBase> _tarifaBase;

		public Available()
		{
			_promotions = new ArrayList<HabPromo>();
			_tarifaBase = new ArrayList<HabBase>();
		}

		public String getCodigoError()
		{
			return _codigoError;
		}

		public void setCodigoError( String codigoError )
		{
			_codigoError = codigoError;
		}

		public String getCodigoTarifa()
		{
			return _codigoTarifa;
		}

		public void setCodigoTarifa( String codigoTarifa )
		{
			_codigoTarifa = codigoTarifa;
		}

		public String getDescError()
		{
			return _descError;
		}

		public void setDescError( String descError )
		{
			_descError = descError;
		}

		public String getDescripcion()
		{
			return _descripcion;
		}

		public void setDescripcion( String descripcion )
		{
			_descripcion = descripcion;
		}

		public String getHotel()
		{
			return _hotel;
		}

		public void setHotel( String hotel )
		{
			_hotel = hotel;
		}

		public ArrayList<HabPromo> getPromotions()
		{
			return _promotions;
		}

		public void setPromotions( ArrayList<HabPromo> promotions )
		{
			_promotions = promotions;
		}

		public ArrayList<HabBase> getTarifaBase()
		{
			return _tarifaBase;
		}

		public void setTarifaBase( ArrayList<HabBase> tarifaBase )
		{
			_tarifaBase = tarifaBase;
		}
	}

	public static class HabBase
	{
		private int _availability;
		private String _codBase;
		private String _descBase;
		private ArrayList<Noche> _noches;

		public HabBase()
		{
			_noches = new ArrayList<Noche>();
		}

		public int getAvailability()
		{
			return _availability;
		}

		public void setAvailability( int availability )
		{
			_availability = availability;
		}

		public String getCodBase()
		{
			return _codBase;
		}

		public void setCodBase( String codBase )
		{
			_codBase = codBase;
		}

		public String getDescBase()
		{
			return _descBase;
		}

		public void setDescBase( String descBase )
		{
			_descBase = descBase;
		}

		public ArrayList<Noche> getNoches()
		{
			return _noches;
		}

		public void setNoches( ArrayList<Noche> noches )
		{
			_noches = noches;
		}
	}

	public static class HabPromo
	{
		private String _codRoom;
		private String _descRoom;
		private ArrayList<Promotion> _promotions;

		public HabPromo()
		{
			_promotions = new ArrayList<Promotion>();
		}

		public String getCodRoom()
		{
			return _codRoom;
		}

		public void setCodRoom( String codRoom )
		{
			_codRoom = codRoom;
		}

		public String getDescRoom()
		{
			return _descRoom;
		}

		public void setDescRoom( String descRoom )
		{
			_descRoom = descRoom;
		}

		public ArrayList<Promotion> getPromotions()
		{
			return _promotions;
		}

		public void setPromotions( ArrayList<Promotion> promotions )
		{
			_promotions = promotions;
		}
	}

	public static class Promotion
	{
		private String _codPromo;
		private String _descPromo;
		private ArrayList<Noche> _noches;

		public Promotion()
		{
			_noches = new ArrayList<Noche>();
		}

		public String getCodPromo()
		{
			return _codPromo;
		}

		public void setCodPromo( String codPromo )
		{
			_codPromo = codPromo;
		}

		public String getDescPromo()
		{
			return _descPromo;
		}

		public void setDescPromo( String descPromo )
		{
			_descPromo = descPromo;
		}

		public ArrayList<Noche> getNoches()
		{
			return _noches;
		}

		public void setNoches( ArrayList<Noche> noches )
		{
			_noches = noches;
		}
	}

	public static class Noche
	{
		private String _costo;
		private String _fecha;

		public Noche()
		{
		}

		public String getCosto()
		{
			return _costo;
		}

		public void setCosto( String costo )
		{
			_costo = costo;
		}

		public String getFecha()
		{
			return _fecha;
		}

		public void setFecha( String fecha )
		{
			_fecha = fecha;
		}
	}

	public static class Deposito_PayPal implements KvmSerializable, Serializable
	{
		private static final long serialVersionUID = 0L;

		private String _comprobante;
		private String _fecha;
		private Float _monto;
		private String _notas;
		private String _notas2;
		private String _tipoMoneda;

		public Deposito_PayPal()
		{
			_comprobante = "";
			_fecha = "";
			_monto = 0.0f;
			_notas = "";
			_notas2 = "";
			_tipoMoneda = "";
		}

		@Override
		public Object getProperty( int i )
		{
			if( i == 0 )
				return this.getComprobante();
			else if( i == 1 )
				return this.getFecha();
			else if( i == 2 )
				return this.getMonto();
			else if( i == 3 )
				return this.getNotas();
			else if( i == 4 )
				return this.getNotas2();
			else if( i == 5 )
				return this.getTipoMoneda();
			else
				return null;
		}

		@Override
		public int getPropertyCount()
		{
			return 6;
		}

		@Override
		public void setProperty( int i, Object o )
		{
			if( i == 0 )
				this.setComprobante( (String)o );
			else if( i == 1 )
				this.setFecha( (String)o );
			else if( i == 2  )
				this.setMonto( (Float)o );
			else if( i == 3 )
				this.setNotas( (String)o );
			else if( i == 4 )
				this.setNotas2( (String)o );
			else if( i == 5 )
				this.setTipoMoneda( (String)o );
		}

		@Override
		public void getPropertyInfo( int i, Hashtable hashtable, PropertyInfo propertyInfo )
		{
			propertyInfo.namespace = "http://schemas.datacontract.org/2004/07/CityHub";
			if( i == 0 )
				propertyInfo.name = "Comprobante";
			else if( i == 1 )
				propertyInfo.name = "Fecha";
			else if( i == 2  )
				propertyInfo.name = "Monto";
			else if( i == 3 )
				propertyInfo.name = "Notas";
			else if( i == 4 )
				propertyInfo.name = "Notas2";
			else if( i == 5 )
				propertyInfo.name = "TipoMoneda";
		}

		public String getComprobante()
		{
			return _comprobante;
		}

		public void setComprobante( String comprobante )
		{
			_comprobante = comprobante;
		}

		public String getFecha()
		{
			return _fecha;
		}

		public void setFecha( String fecha )
		{
			_fecha = fecha;
		}

		public Float getMonto()
		{
			return _monto;
		}

		public void setMonto( Float monto )
		{
			_monto = monto;
		}

		public String getNotas()
		{
			return _notas;
		}

		public void setNotas( String notas )
		{
			_notas = notas;
		}

		public String getNotas2()
		{
			return _notas2;
		}

		public void setNotas2( String notas2 )
		{
			_notas2 = notas2;
		}

		public String getTipoMoneda()
		{
			return _tipoMoneda;
		}

		public void setTipoMoneda( String tipoMoneda )
		{
			_tipoMoneda = tipoMoneda;
		}
	}

	public static class Empresa implements KvmSerializable, Serializable
	{
		private static final long serialVersionUID = 0L;

		private String _iataEmisor;
		private String _rfcEmisor;

		public Empresa()
		{
			_iataEmisor = "";
			_rfcEmisor = "";
		}

		@Override
		public Object getProperty( int i )
		{
			if( i == 0 )
				return this.getIataEmisor();
			else if( i == 1 )
				return this.getRfcEmisor();
			else
				return null;
		}

		@Override
		public int getPropertyCount()
		{
			return 2;
		}

		@Override
		public void setProperty( int i, Object o )
		{
			if( i == 0 )
				this.setIataEmisor( (String)o );
			else if( i == 1 )
				this.setRfcEmisor( (String)o );
		}

		@Override
		public void getPropertyInfo( int i, Hashtable hashtable, PropertyInfo propertyInfo )
		{
			propertyInfo.namespace = "http://schemas.datacontract.org/2004/07/CityHub";
			if( i == 0 )
				propertyInfo.name = "IataEmisor";
			else if( i == 1 )
				propertyInfo.name = "RfcEmisor";
		}

		public String getIataEmisor()
		{
			return _iataEmisor;
		}

		public void setIataEmisor( String iataEmisor )
		{
			_iataEmisor = iataEmisor;
		}

		public String getRfcEmisor()
		{
			return _rfcEmisor;
		}

		public void setRfcEmisor( String rfcEmisor )
		{
			_rfcEmisor = rfcEmisor;
		}
	}

	public static class Estancia implements KvmSerializable, Serializable
	{
		private static final long serialVersionUID = 0L;

		private String _channelRef;
		private String _codigoOperador;
		private String _codigoOrigen;
		private String _codigoSegmento;
		private Date _fechaEntrada;
		private String _formaDePago;
		private String _hotel;
		private String _notasFormaPago;
		private String _notasReservacion;
		private Integer _numeroDeNoches;
		private String _tipoReservacion;

		public Estancia()
		{
			_channelRef = "";
			_codigoOperador = "";
			_codigoOrigen = "";
			_codigoSegmento = "";
			_fechaEntrada = new Date( System.currentTimeMillis() );
			_formaDePago = "";
			_hotel = "";
			_notasFormaPago = "";
			_notasReservacion = "";
			_numeroDeNoches = 0;
			_tipoReservacion = "";
		}

		@Override
		public Object getProperty( int i )
		{
			if( i == 0 )
				return this.getChannelRef();
			else if( i == 1 )
				return this.getCodigoOperador();
			else if( i == 2 )
				return this.getCodigoOrigen();
			else if( i == 3 )
				return this.getCodigoSegmento();
			else if( i == 4 )
				return getSOAPDateString( this.getFechaEntrada() );
			else if( i == 5 )
				return this.getFormaDePago();
			else if( i == 6 )
				return this.getHotel();
			else if( i == 7 )
				return this.getNotasFormaPago();
			else if( i == 8 )
				return this.getNotasReservacion();
			else if( i == 9 )
				return this.getNumeroDeNoches();
			else if( i == 10 )
				return this.getTipoReservacion();
			else
				return null;
		}

		@Override
		public int getPropertyCount()
		{
			return 11;
		}

		@Override
		public void setProperty( int i, Object o )
		{
			if( i == 0 )
				this.setChannelRef( (String) o );
			else if( i == 1 )
				this.setCodigoOperador( (String) o );
			else if( i == 2 )
				this.setCodigoOrigen( (String) o );
			else if( i == 3 )
				this.setCodigoSegmento( (String) o );
			else if( i == 4 )
				this.setFechaEntrada( getDateFromSOAPString( (String) o ) );
			else if( i == 5 )
				this.setFormaDePago( (String) o );
			else if( i == 6 )
				this.setHotel( (String) o );
			else if( i == 7 )
				this.setNotasFormaPago( (String) o );
			else if( i == 8 )
				this.setNotasReservacion( (String) o );
			else if( i == 9 )
				this.setNumeroDeNoches( (Integer) o );
			else if( i == 10 )
				this.setTipoReservacion( (String) o );
		}

		@Override
		public void getPropertyInfo( int i, Hashtable hashtable, PropertyInfo propertyInfo )
		{
			propertyInfo.namespace = "http://schemas.datacontract.org/2004/07/CityHub";
			if( i == 0 )
				propertyInfo.name = "ChannelRef";
			else if( i == 1 )
				propertyInfo.name = "CodigoOperador";
			else if( i == 2 )
				propertyInfo.name = "CodigoOrigen";
			else if( i == 3 )
				propertyInfo.name = "CodigoSegmento";
			else if( i == 4 )
				propertyInfo.name = "FechaEntrada";
			else if( i == 5 )
				propertyInfo.name = "FormaDePago";
			else if( i == 6 )
				propertyInfo.name = "Hotel";
			else if( i == 7 )
				propertyInfo.name = "NotasFormaPago";
			else if( i == 8 )
				propertyInfo.name = "NotasReservacion";
			else if( i == 9 )
				propertyInfo.name = "NumeroDeNoches";
			else if( i == 10 )
				propertyInfo.name = "TipoReservacion";
		}

		private static Object getSOAPDateString(Date itemValue)
		{
			if( itemValue != null )
			{
				SimpleDateFormat lDateFormat = new SimpleDateFormat( "yyyy-MM-dd'T'hh:mm:ss'Z'" );
				return lDateFormat.format( itemValue );
			}
			else return "";
		}

		private static Date getDateFromSOAPString( String itemValue )
		{
			SimpleDateFormat lDateFormat = new SimpleDateFormat( "yyyy-MM-dd'T'hh:mm:ss'Z'" );
			Date result = null;
			try
			{
				result = lDateFormat.parse( itemValue );
			}
			catch( Exception e )
			{
				android.util.Log.d( "ReservationEngineClient", "Cant parse date: " +e.getMessage() );
			}
			return result;
		}

		public String getChannelRef()
		{
			return _channelRef;
		}

		public void setChannelRef( String channelRef )
		{
			_channelRef = channelRef;
		}

		public String getCodigoOperador()
		{
			return _codigoOperador;
		}

		public void setCodigoOperador( String codigoOperador )
		{
			_codigoOperador = codigoOperador;
		}

		public String getCodigoOrigen()
		{
			return _codigoOrigen;
		}

		public void setCodigoOrigen( String codigoOrigen )
		{
			_codigoOrigen = codigoOrigen;
		}

		public String getCodigoSegmento()
		{
			return _codigoSegmento;
		}

		public void setCodigoSegmento( String codigoSegmento )
		{
			_codigoSegmento = codigoSegmento;
		}

		public Date getFechaEntrada()
		{
			return _fechaEntrada;
		}

		public void setFechaEntrada( Date fechaEntrada )
		{
			_fechaEntrada = fechaEntrada;
		}

		public String getFormaDePago()
		{
			return _formaDePago;
		}

		public void setFormaDePago( String formaDePago )
		{
			_formaDePago = formaDePago;
		}

		public String getHotel()
		{
			return _hotel;
		}

		public void setHotel( String hotel )
		{
			_hotel = hotel;
		}

		public String getNotasFormaPago()
		{
			return _notasFormaPago;
		}

		public void setNotasFormaPago( String notasFormaPago )
		{
			_notasFormaPago = notasFormaPago;
		}

		public String getNotasReservacion()
		{
			return _notasReservacion;
		}

		public void setNotasReservacion( String notasReservacion )
		{
			_notasReservacion = notasReservacion;
		}

		public Integer getNumeroDeNoches()
		{
			return _numeroDeNoches;
		}

		public void setNumeroDeNoches( Integer numeroDeNoches )
		{
			_numeroDeNoches = numeroDeNoches;
		}

		public String getTipoReservacion()
		{
			return _tipoReservacion;
		}

		public void setTipoReservacion( String tipoReservacion )
		{
			_tipoReservacion = tipoReservacion;
		}
	}

	public static class Persona implements KvmSerializable, Serializable
	{
		private static final long serialVersionUID = 0L;

		private String _apellidos;
		private Integer _edad;
		private String _nombre;

		public Persona()
		{
			_apellidos = "";
			_edad = 0;
			_nombre = "";
		}

		@Override
		public Object getProperty( int i )
		{
			if( i == 0 )
				return this.getApellidos();
			else if( i == 1 )
				return this.getEdad();
			else if( i == 2 )
				return this.getNombre();
			else
				return null;
		}

		@Override
		public int getPropertyCount()
		{
			return 3;
		}

		@Override
		public void setProperty( int i, Object o )
		{
			if( i == 0 )
				this.setApellidos( (String) o );
			else if( i == 1 )
				this.setEdad( (Integer) o );
			else if( i == 2 )
				this.setNombre( (String) o );
		}

		@Override
		public void getPropertyInfo( int i, Hashtable hashtable, PropertyInfo propertyInfo )
		{
			propertyInfo.namespace = "http://schemas.datacontract.org/2004/07/CityHub";
			if( i == 0 )
				propertyInfo.name = "Apellidos";
			else if( i == 1 )
				propertyInfo.name = "Edad";
			else if( i == 2 )
				propertyInfo.name = "Nombre";
		}

		public String getApellidos()
		{
			return _apellidos;
		}

		public void setApellidos( String apellidos )
		{
			_apellidos = apellidos;
		}

		public Integer getEdad()
		{
			return _edad;
		}

		public void setEdad( Integer edad )
		{
			_edad = edad;
		}

		public String getNombre()
		{
			return _nombre;
		}

		public void setNombre( String nombre )
		{
			_nombre = nombre;
		}
	}

	public static class ArrayOfPersona extends Vector<Persona> implements KvmSerializable, Serializable
	{
		private static final long serialVersionUID = 0L;

		@Override
		public Object getProperty( int i )
		{
			return this.get( i );
		}

		@Override
		public int getPropertyCount()
		{
			return this.size();
		}

		@Override
		public void setProperty( int i, Object o )
		{
			this.add( (Persona)o );
		}

		@Override
		public void getPropertyInfo( int i, Hashtable hashtable, PropertyInfo propertyInfo )
		{
			propertyInfo.namespace = "http://schemas.datacontract.org/2004/07/CityHub";
			propertyInfo.name = "Persona";
		}
	}

	public static class Huesped implements KvmSerializable, Serializable
	{
		private static final long serialVersionUID = 0L;

		private String _apellidos;
		private Integer _edad;
		private String _nombre;
		private ArrayOfPersona _acompanantes_Ar;
		private String _codigoPais;
		private String _correoElectronico;
		private String _indicaciones;
		private String _rwdNumber;
		private String _telefono;
		private Integer _totAcompAdult;
		private Integer _totAcompMenor;

		public Huesped()
		{
			_apellidos = "";
			_edad = 0;
			_nombre = "";
			_acompanantes_Ar = new ArrayOfPersona();
			_codigoPais = "";
			_correoElectronico = "";
			_indicaciones = "";
			_rwdNumber = "";
			_telefono = "";
			_totAcompAdult = 0;
			_totAcompMenor = 0;
		}

		@Override
		public Object getProperty( int i )
		{
			if( i == 0 )
				return this.getApellidos();
			else if( i == 1 )
				return this.getEdad();
			else if( i == 2 )
				return this.getNombre();
			else if( i == 3 )
				return this.getAcompanantes_Ar();
			else if( i == 4 )
				return this.getCodigoPais();
			else if( i == 5 )
				return this.getCorreoElectronico();
			else if( i == 6 )
				return this.getIndicaciones();
			else if( i == 7 )
				return this.getRwdNumber();
			else if( i == 8 )
				return this.getTelefono();
			else if( i == 9 )
				return this.getTotAcompAdult();
			else if( i == 10 )
				return this.getTotAcompMenor();
			else
				return null;
		}

		@Override
		public int getPropertyCount()
		{
			return 11;
		}

		@Override
		public void setProperty( int i, Object o )
		{
			if( i == 0 )
				this.setApellidos( (String) o );
			else if( i == 1 )
				this.setEdad( (Integer) o );
			else if( i == 2 )
				this.setNombre( (String) o );
			else if( i == 3 )
				this.setAcompanantes_Ar( (ArrayOfPersona) o );
			else if( i == 4 )
				this.setCodigoPais( (String) o );
			else if( i == 5 )
				this.setCorreoElectronico( (String) o );
			else if( i == 6 )
				this.setIndicaciones( (String) o );
			else if( i == 7 )
				this.setRwdNumber( (String) o );
			else if( i == 8 )
				this.setTelefono( (String) o );
			else if( i == 9 )
				this.setTotAcompAdult( (Integer) o );
			else if( i == 10 )
				this.setTotAcompMenor( (Integer) o );
		}

		@Override
		public void getPropertyInfo( int i, Hashtable hashtable, PropertyInfo propertyInfo )
		{
			propertyInfo.namespace = "http://schemas.datacontract.org/2004/07/CityHub";
			if( i == 0 )
				propertyInfo.name = "Apellidos";
			else if( i == 1 )
				propertyInfo.name = "Edad";
			else if( i == 2 )
				propertyInfo.name = "Nombre";
			else if( i == 3 )
				propertyInfo.name = "Acompanantes_Ar";
			else if( i == 4 )
				propertyInfo.name = "CodigoPais";
			else if( i == 5 )
				propertyInfo.name = "CorreoElectronico";
			else if( i == 6 )
				propertyInfo.name = "Indicaciones";
			else if( i == 7 )
				propertyInfo.name = "RwdNumber";
			else if( i == 8 )
				propertyInfo.name = "Telefono";
			else if( i == 9 )
				propertyInfo.name = "TotAcompAdult";
			else if( i == 10 )
				propertyInfo.name = "TotAcompMenor";
		}

		public String getApellidos()
		{
			return _apellidos;
		}

		public void setApellidos( String apellidos )
		{
			_apellidos = apellidos;
		}

		public Integer getEdad()
		{
			return _edad;
		}

		public void setEdad( Integer edad )
		{
			_edad = edad;
		}

		public String getNombre()
		{
			return _nombre;
		}

		public void setNombre( String nombre )
		{
			_nombre = nombre;
		}

		public ArrayOfPersona getAcompanantes_Ar()
		{
			return _acompanantes_Ar;
		}

		public void setAcompanantes_Ar( ArrayOfPersona acompanantes_Ar )
		{
			_acompanantes_Ar = acompanantes_Ar;
		}

		public String getCodigoPais()
		{
			return _codigoPais;
		}

		public void setCodigoPais( String codigoPais )
		{
			_codigoPais = codigoPais;
		}

		public String getCorreoElectronico()
		{
			return _correoElectronico;
		}

		public void setCorreoElectronico( String correoElectronico )
		{
			_correoElectronico = correoElectronico;
		}

		public String getIndicaciones()
		{
			return _indicaciones;
		}

		public void setIndicaciones( String indicaciones )
		{
			_indicaciones = indicaciones;
		}

		public String getRwdNumber()
		{
			return _rwdNumber;
		}

		public void setRwdNumber( String rwdNumber )
		{
			_rwdNumber = rwdNumber;
		}

		public String getTelefono()
		{
			return _telefono;
		}

		public void setTelefono( String telefono )
		{
			_telefono = telefono;
		}

		public Integer getTotAcompAdult()
		{
			return _totAcompAdult;
		}

		public void setTotAcompAdult( Integer totAcompAdult )
		{
			_totAcompAdult = totAcompAdult;
		}

		public Integer getTotAcompMenor()
		{
			return _totAcompMenor;
		}

		public void setTotAcompMenor( Integer totAcompMenor )
		{
			_totAcompMenor = totAcompMenor;
		}
	}

	public static class Habitacion implements KvmSerializable, Serializable
	{
		private static final long serialVersionUID = 0L;

		private String _codigoHabitacion;
		private String _codigoPromocion;
		private String _codigoTarifa;
		private Huesped _huespedTitular;
		private Integer _numeroHabitaciones;

		public Habitacion()
		{
			_codigoHabitacion = "";
			_codigoPromocion = "";
			_codigoTarifa = "";
			_huespedTitular = new Huesped();
			_numeroHabitaciones = 0;
		}

		@Override
		public Object getProperty( int i )
		{
			if( i == 0 )
				return this.getCodigoHabitacion();
			else if( i == 1 )
				return this.getCodigoPromocion();
			else if( i == 2 )
				return this.getCodigoTarifa();
			else if( i == 3 )
				return this.getHuespedTitular();
			else if( i == 4 )
				return this.getNumeroHabitaciones();
			else
				return null;
		}

		@Override
		public int getPropertyCount()
		{
			return 5;
		}

		@Override
		public void setProperty( int i, Object o )
		{
			if( i == 0 )
				this.setCodigoHabitacion( (String) o );
			else if( i == 1 )
				this.setCodigoPromocion( (String) o );
			else if( i == 2 )
				this.setCodigoTarifa( (String) o );
			else if( i == 3 )
				this.setHuespedTitular( (Huesped) o );
			else if( i == 4 )
				this.setNumeroHabitaciones( (Integer) o );
		}

		@Override
		public void getPropertyInfo( int i, Hashtable hashtable, PropertyInfo propertyInfo )
		{
			propertyInfo.namespace = "http://schemas.datacontract.org/2004/07/CityHub";
			if( i == 0 )
				propertyInfo.name = "CodigoHabitacion";
			else if( i == 1 )
				propertyInfo.name = "CodigoPromocion";
			else if( i == 2 )
				propertyInfo.name = "CodigoTarifa";
			else if( i == 3 )
				propertyInfo.name = "HuespedTitular";
			else if( i == 4 )
				propertyInfo.name = "NumeroHabitaciones";
		}

		public String getCodigoHabitacion()
		{
			return _codigoHabitacion;
		}

		public void setCodigoHabitacion( String codigoHabitacion )
		{
			_codigoHabitacion = codigoHabitacion;
		}

		public String getCodigoPromocion()
		{
			return _codigoPromocion;
		}

		public void setCodigoPromocion( String codigoPromocion )
		{
			_codigoPromocion = codigoPromocion;
		}

		public String getCodigoTarifa()
		{
			return _codigoTarifa;
		}

		public void setCodigoTarifa( String codigoTarifa )
		{
			_codigoTarifa = codigoTarifa;
		}

		public Huesped getHuespedTitular()
		{
			return _huespedTitular;
		}

		public void setHuespedTitular( Huesped huespedTitular )
		{
			_huespedTitular = huespedTitular;
		}

		public Integer getNumeroHabitaciones()
		{
			return _numeroHabitaciones;
		}

		public void setNumeroHabitaciones( Integer numeroHabitaciones )
		{
			_numeroHabitaciones = numeroHabitaciones;
		}
	}

	public static class Reservante implements KvmSerializable, Serializable
	{
		private static final long serialVersionUID = 0L;

		private String _apellidos;
		private Integer _edad;
		private String _nombre;
		private String _correoElectronico;
		private String _rwdNumber;
		private String _telefonoReservante;

		public Reservante()
		{
			_apellidos = "";
			_edad = 0;
			_nombre = "";
			_correoElectronico = "";
			_rwdNumber = "";
			_telefonoReservante = "";
		}

		@Override
		public Object getProperty( int i )
		{
			if( i == 0 )
				return this.getApellidos();
			else if( i == 1 )
				return this.getEdad();
			else if( i == 2 )
				return this.getNombre();
			else if( i == 3 )
				return this.getCorreoElectronico();
			else if( i == 4 )
				return this.getRwdNumber();
			else if( i == 5 )
				return this.getTelefonoReservante();
			else
				return null;
		}

		@Override
		public int getPropertyCount()
		{
			return 6;
		}

		@Override
		public void setProperty( int i, Object o )
		{
			if( i == 0 )
				this.setApellidos( (String) o );
			else if( i == 1 )
				this.setEdad( (Integer) o );
			else if( i == 2 )
				this.setNombre( (String) o );
			else if( i == 3 )
				this.setCorreoElectronico( (String) o );
			else if( i == 4 )
				this.setRwdNumber( (String) o );
			else if( i == 5 )
				this.setTelefonoReservante( (String) o );
		}

		@Override
		public void getPropertyInfo( int i, Hashtable hashtable, PropertyInfo propertyInfo )
		{
			propertyInfo.namespace = "http://schemas.datacontract.org/2004/07/CityHub";
			if( i == 0 )
				propertyInfo.name = "Apellidos";
			else if( i == 1 )
				propertyInfo.name = "Edad";
			else if( i == 2 )
				propertyInfo.name = "Nombre";
			else if( i == 3 )
				propertyInfo.name = "CorreoElectronico";
			else if( i == 4 )
				propertyInfo.name = "RwdNumber";
			else if( i == 5 )
				propertyInfo.name = "TelefonoReservante";
		}

		public String getApellidos()
		{
			return _apellidos;
		}

		public void setApellidos( String apellidos )
		{
			_apellidos = apellidos;
		}

		public Integer getEdad()
		{
			return _edad;
		}

		public void setEdad( Integer edad )
		{
			_edad = edad;
		}

		public String getNombre()
		{
			return _nombre;
		}

		public void setNombre( String nombre )
		{
			_nombre = nombre;
		}

		public String getCorreoElectronico()
		{
			return _correoElectronico;
		}

		public void setCorreoElectronico( String correoElectronico )
		{
			_correoElectronico = correoElectronico;
		}

		public String getRwdNumber()
		{
			return _rwdNumber;
		}

		public void setRwdNumber( String rwdNumber )
		{
			_rwdNumber = rwdNumber;
		}

		public String getTelefonoReservante()
		{
			return _telefonoReservante;
		}

		public void setTelefonoReservante( String telefonoReservante )
		{
			_telefonoReservante = telefonoReservante;
		}
	}

	public static class TarjetaCredito implements KvmSerializable, Serializable
	{
		private static final long serialVersionUID = 0L;

		private String _anoVencimiento;
		private Integer _codigoSeguridad;
		private String _mesVencimiento;
		private String _nombreTarjeta;
		private String _numeroTarjeta;

		public TarjetaCredito()
		{
			_anoVencimiento = "";
			_codigoSeguridad = 0;
			_mesVencimiento = "";
			_nombreTarjeta = "";
			_numeroTarjeta = "";
		}

		@Override
		public Object getProperty( int i )
		{
			if( i == 0 )
				return this.getAnoVencimiento();
			else if( i == 1 )
				return this.getCodigoSeguridad();
			else if( i == 2 )
				return this.getMesVencimiento();
			else if( i == 3 )
				return this.getNombreTarjeta();
			else if( i == 4 )
				return this.getNumeroTarjeta();
			else
				return null;
		}

		@Override
		public int getPropertyCount()
		{
			return 5;
		}

		@Override
		public void setProperty( int i, Object o )
		{
			if( i == 0 )
				this.setAnoVencimiento( (String) o );
			else if( i == 1 )
				this.setCodigoSeguridad( (Integer) o );
			else if( i == 2 )
				this.setMesVencimiento( (String) o );
			else if( i == 3 )
				this.setNombreTarjeta( (String) o );
			else if( i == 4 )
				this.setNumeroTarjeta( (String) o );
		}

		@Override
		public void getPropertyInfo( int i, Hashtable hashtable, PropertyInfo propertyInfo )
		{
			propertyInfo.namespace = "http://schemas.datacontract.org/2004/07/CityHub";
			if( i == 0 )
				propertyInfo.name = "AnoVencimiento";
			else if( i == 1 )
				propertyInfo.name = "CodigoSeguridad";
			else if( i == 2 )
				propertyInfo.name = "MesVencimiento";
			else if( i == 3 )
				propertyInfo.name = "NombreTarjeta";
			else if( i == 4 )
				propertyInfo.name = "NumeroTarjeta";
		}

		public String getAnoVencimiento()
		{
			return _anoVencimiento;
		}

		public void setAnoVencimiento( String anoVencimiento )
		{
			_anoVencimiento = anoVencimiento;
		}

		public Integer getCodigoSeguridad()
		{
			return _codigoSeguridad;
		}

		public void setCodigoSeguridad( Integer codigoSeguridad )
		{
			_codigoSeguridad = codigoSeguridad;
		}

		public String getMesVencimiento()
		{
			return _mesVencimiento;
		}

		public void setMesVencimiento( String mesVencimiento )
		{
			_mesVencimiento = mesVencimiento;
		}

		public String getNombreTarjeta()
		{
			return _nombreTarjeta;
		}

		public void setNombreTarjeta( String nombreTarjeta )
		{
			_nombreTarjeta = nombreTarjeta;
		}

		public String getNumeroTarjeta()
		{
			return _numeroTarjeta;
		}

		public void setNumeroTarjeta( String numeroTarjeta )
		{
			_numeroTarjeta = numeroTarjeta;
		}
	}

	public static class TarjetaVirtual implements KvmSerializable, Serializable
	{
		private static final long serialVersionUID = 0L;

		private String _anoVencimiento;
		private Integer _codigoSeguridad;
		private String _mesVencimiento;
		private String _nombreTarjeta;
		private String _numeroTarjeta;

		public TarjetaVirtual()
		{
			_anoVencimiento = "";
			_codigoSeguridad = 0;
			_mesVencimiento = "";
			_nombreTarjeta = "";
			_numeroTarjeta = "";
		}

		@Override
		public Object getProperty( int i )
		{
			if( i == 0 )
				return this.getAnoVencimiento();
			else if( i == 1 )
				return this.getCodigoSeguridad();
			else if( i == 2 )
				return this.getMesVencimiento();
			else if( i == 3 )
				return this.getNombreTarjeta();
			else if( i == 4 )
				return this.getNumeroTarjeta();
			else
				return null;
		}

		@Override
		public int getPropertyCount()
		{
			return 5;
		}

		@Override
		public void setProperty( int i, Object o )
		{
			if( i == 0 )
				this.setAnoVencimiento( (String)o );
			else if( i == 1 )
				this.setCodigoSeguridad( (Integer)o );
			else if( i == 2 )
				this.setMesVencimiento( (String)o );
			else if( i == 3 )
				this.setNombreTarjeta( (String)o );
			else if( i == 4 )
				this.setNumeroTarjeta( (String)o );
		}

		@Override
		public void getPropertyInfo( int i, Hashtable hashtable, PropertyInfo propertyInfo )
		{
			propertyInfo.namespace = "http://schemas.datacontract.org/2004/07/CityHub";
			if( i == 0 )
				propertyInfo.name = "AnoVencimiento";
			else if( i == 1 )
				propertyInfo.name = "CodigoSeguridad";
			else if( i == 2 )
				propertyInfo.name = "MesVencimiento";
			else if( i == 3 )
				propertyInfo.name = "NombreTarjeta";
			else if( i == 4 )
				propertyInfo.name = "NumeroTarjeta";
		}

		public String getAnoVencimiento()
		{
			return _anoVencimiento;
		}

		public void setAnoVencimiento( String anoVencimiento )
		{
			_anoVencimiento = anoVencimiento;
		}

		public Integer getCodigoSeguridad()
		{
			return _codigoSeguridad;
		}

		public void setCodigoSeguridad( Integer codigoSeguridad )
		{
			_codigoSeguridad = codigoSeguridad;
		}

		public String getMesVencimiento()
		{
			return _mesVencimiento;
		}

		public void setMesVencimiento( String mesVencimiento )
		{
			_mesVencimiento = mesVencimiento;
		}

		public String getNombreTarjeta()
		{
			return _nombreTarjeta;
		}

		public void setNombreTarjeta( String nombreTarjeta )
		{
			_nombreTarjeta = nombreTarjeta;
		}

		public String getNumeroTarjeta()
		{
			return _numeroTarjeta;
		}

		public void setNumeroTarjeta( String numeroTarjeta )
		{
			_numeroTarjeta = numeroTarjeta;
		}
	}

	public static class ReservationModelv3_01 implements KvmSerializable, Serializable
	{
		private static final long serialVersionUID = 0L;

		private Deposito_PayPal _deposito;
		private Empresa _empresa;
		private Estancia _estancia;
		private Habitacion _habitacion;
		private Reservante _reservante;
		private TarjetaCredito _tarjetaCredito;
		private TarjetaVirtual _tarjetaVirtual;

		public ReservationModelv3_01()
		{
			_deposito = new Deposito_PayPal();
			_empresa = new Empresa();
			_estancia = new Estancia();
			_habitacion = new Habitacion();
			_reservante = new Reservante();
			_tarjetaCredito = new TarjetaCredito();
			_tarjetaVirtual = new TarjetaVirtual();
		}

		@Override
		public Object getProperty( int i )
		{
			if( i == 0 )
				return this.getDeposito();
			else if( i == 1 )
				return this.getEmpresa();
			else if( i == 2 )
				return this.getEstancia();
			else if( i == 3 )
				return this.getHabitacion();
			else if( i == 4 )
				return this.getReservante();
			else if( i == 5 )
				return this.getTarjetaCredito();
			else if( i == 6 )
				return this.getTarjetaVirtual();
			else
				return null;
		}

		@Override
		public int getPropertyCount()
		{
			return 7;
		}

		@Override
		public void setProperty( int i, Object o )
		{
			if( i == 0 )
				this.setDeposito( (Deposito_PayPal) o );
			else if( i == 1 )
				this.setEmpresa( (Empresa) o );
			else if( i == 2 )
				this.setEstancia( (Estancia) o );
			else if( i == 3 )
				this.setHabitacion( (Habitacion) o );
			else if( i == 4 )
				this.setReservante( (Reservante) o );
			else if( i == 5 )
				this.setTarjetaCredito( (TarjetaCredito) o );
			else if( i == 6 )
				this.setTarjetaVirtual( (TarjetaVirtual) o );
		}

		@Override
		public void getPropertyInfo( int i, Hashtable hashtable, PropertyInfo propertyInfo )
		{
			propertyInfo.namespace = "http://schemas.datacontract.org/2004/07/CityHub";
			if( i == 0 )
				propertyInfo.name = "Deposito";
			else if( i == 1 )
				propertyInfo.name = "Empresa";
			else if( i == 2 )
				propertyInfo.name = "Estancia";
			else if( i == 3 )
				propertyInfo.name = "Habitacion";
			else if( i == 4 )
				propertyInfo.name = "Reservante";
			else if( i == 5 )
				propertyInfo.name = "TarjetaCredito";
			else if( i == 6 )
				propertyInfo.name = "TarjetaVirtual";
		}

		public Deposito_PayPal getDeposito()
		{
			return _deposito;
		}

		public void setDeposito( Deposito_PayPal deposito )
		{
			_deposito = deposito;
		}

		public Empresa getEmpresa()
		{
			return _empresa;
		}

		public void setEmpresa( Empresa empresa )
		{
			_empresa = empresa;
		}

		public Estancia getEstancia()
		{
			return _estancia;
		}

		public void setEstancia( Estancia estancia )
		{
			_estancia = estancia;
		}

		public Habitacion getHabitacion()
		{
			return _habitacion;
		}

		public void setHabitacion( Habitacion habitacion )
		{
			_habitacion = habitacion;
		}

		public Reservante getReservante()
		{
			return _reservante;
		}

		public void setReservante( Reservante reservante )
		{
			_reservante = reservante;
		}

		public TarjetaCredito getTarjetaCredito()
		{
			return _tarjetaCredito;
		}

		public void setTarjetaCredito( TarjetaCredito tarjetaCredito )
		{
			_tarjetaCredito = tarjetaCredito;
		}

		public TarjetaVirtual getTarjetaVirtual()
		{
			return _tarjetaVirtual;
		}

		public void setTarjetaVirtual( TarjetaVirtual tarjetaVirtual )
		{
			_tarjetaVirtual = tarjetaVirtual;
		}
	}

	public static class ResponseBooking implements KvmSerializable, Serializable
	{
		private static final long serialVersionUID = 0L;

		private String _codigoError;
		private String _descError;
		private String _errorMessage;
		private String _numReservacion;
		private Double _totalRate;

		private Integer _response;

		public ResponseBooking()
		{
			_response = -1;
		}

		@Override
		public Object getProperty( int i )
		{
			if( i == 0 )
				return this.getCodigoError();
			else if( i == 1 )
				return this.getDescError();
			else if( i == 2 )
				return this.getErrorMessage();
			else if( i == 3 )
				return this.getNumReservacion();
			else if( i == 4 )
				return this.getTotalRate();
			else
				return null;
		}

		@Override
		public int getPropertyCount()
		{
			return 5;
		}

		@Override
		public void setProperty( int i, Object o )
		{
			if( i == 0 )
				this.setCodigoError( (String) o );
			else if( i == 1 )
				this.setDescError( (String) o );
			else if( i == 2 )
				this.setErrorMessage( (String) o );
			else if( i == 3 )
				this.setNumReservacion( (String) o );
			else if( i == 4 )
				this.setTotalRate( (Double) o );
		}

		@Override
		public void getPropertyInfo( int i, Hashtable hashtable, PropertyInfo propertyInfo )
		{
			propertyInfo.namespace = "http://schemas.datacontract.org/2004/07/CityHub";
			if( i == 0 )
				propertyInfo.name = "CodigoError";
			else if( i == 1 )
				propertyInfo.name = "DescErrorField";
			else if( i == 2 )
				propertyInfo.name = "ErrorMessageField";
			else if( i == 3 )
				propertyInfo.name = "NumReservacionField";
			else if( i == 4 )
				propertyInfo.name = "TotalRateField";
		}

		public String getCodigoError()
		{
			return _codigoError;
		}

		public void setCodigoError( String codigoError )
		{
			_codigoError = codigoError;
		}

		public String getDescError()
		{
			return _descError;
		}

		public void setDescError( String descError )
		{
			_descError = descError;
		}

		public String getErrorMessage()
		{
			return _errorMessage;
		}

		public void setErrorMessage( String errorMessage )
		{
			_errorMessage = errorMessage;
		}

		public String getNumReservacion()
		{
			return _numReservacion;
		}

		public void setNumReservacion( String numReservacion )
		{
			_numReservacion = numReservacion;
		}

		public Double getTotalRate()
		{
			return _totalRate;
		}

		public void setTotalRate( Double totalRate )
		{
			_totalRate = totalRate;
		}

		public Integer getResponse()
		{
			return _response;
		}

		public void setResponse( Integer response )
		{
			_response = response;
		}
	}

	// http://seesharpgears.blogspot.mx/2010/11/implementing-ksoap-marshal-interface.html
	public static class MarshalDouble implements Marshal
	{
		public Object readInstance(XmlPullParser parser, String namespace, String name, PropertyInfo expected) throws IOException, XmlPullParserException
		{
			return Double.parseDouble(parser.nextText());
		}

		public void register(SoapSerializationEnvelope cm)
		{
			cm.addMapping(cm.xsd, "double", Double.class, this);
		}

		public void writeInstance(XmlSerializer writer, Object obj) throws IOException
		{
			writer.text(obj.toString());
		}
	}

	public static class MarshalFloat implements Marshal
	{
		public Object readInstance(XmlPullParser parser, String namespace, String name, PropertyInfo expected) throws IOException, XmlPullParserException
		{
			return Float.parseFloat( parser.nextText() );
		}

		public void register(SoapSerializationEnvelope cm)
		{
			cm.addMapping( cm.xsd, "float", Float.class, this );
		}

		public void writeInstance(XmlSerializer writer, Object obj) throws IOException
		{
			writer.text(obj.toString());
		}
	}

	public static class ReservationResult implements Serializable
	{
		private static final long serialVersionUID = 0L;

		private ReservationModelv3_01 _reservation;
		private ResponseBooking _response;

		public ReservationResult( ReservationModelv3_01 reservation, ResponseBooking response )
		{
			_reservation = reservation;
			_response = response;
		}

		public ReservationModelv3_01 getReservation()
		{
			return _reservation;
		}

		public void setReservation( ReservationModelv3_01 reservation )
		{
			_reservation = reservation;
		}

		public ResponseBooking getResponse()
		{
			return _response;
		}

		public void setResponse( ResponseBooking response )
		{
			_response = response;
		}
	}
}
