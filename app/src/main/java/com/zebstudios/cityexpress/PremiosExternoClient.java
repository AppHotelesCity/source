package com.zebstudios.cityexpress;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.Hashtable;

/**
 * Created by Denumeris Interactive on 4/23/2015.
 */
public class PremiosExternoClient
{
	String NAMESPACE = "http://tempuri.org/";

	public CanjePremioResponse solCanjePremio( entRedencion redencion )
	{
		CanjePremioResponse response = new CanjePremioResponse();

		SoapObject request = new SoapObject( NAMESPACE, "SolCanjePremio" );

		PropertyInfo info = new PropertyInfo();
		info.setName( "objSwap" );
		info.setValue( redencion );
		info.setType( redencion.getClass() );
		request.addProperty( info );

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope( SoapEnvelope.VER11 );
		envelope.dotNet = true;
		envelope.setOutputSoapObject( request );
		envelope.setAddAdornments( false );

		envelope.addMapping( "http://schemas.datacontract.org/2004/07/wcfCityPremiosCP.Entities.CityPremios", "entRedencion", entRedencion.class );

		HttpTransportSE transport = new HttpTransportSE( APIAddress.CITYPREMIOS_ENGINE_URL );
		transport.debug = true;

		try
		{
			transport.call( "http://tempuri.org/IwcfCityPremiosExterno/SolCanjePremio", envelope );
			android.util.Log.d( "TEST", transport.requestDump );

			Object obj = envelope.bodyIn;
			if( obj == null )
			{
				response.setResponse( -2 );
				android.util.Log.d( "PremiosExternoClient", "NO RESPONSE" );
			}
			else
			{
				if( envelope.bodyIn instanceof SoapFault )
				{
					response.setResponse( -3 );
					String str = ( (SoapFault) envelope.bodyIn ).faultstring;
					android.util.Log.d( "PremiosExternoClient", "RESPONSE FAULT: " + str );
				}
				else
				{
					android.util.Log.d( "PremiosExternoClient", "RESPONSE: " + transport.responseDump );
					SoapObject resultsRequestSOAP = (SoapObject) envelope.bodyIn;
					SoapObject redSoap = (SoapObject)resultsRequestSOAP.getProperty( 0 );

					entRedencion red = new entRedencion();
					red.setentSwap_Add_Usr( redSoap.getPrimitivePropertySafelyAsString( "entSwap_Add_Usr" ) );
					red.setentSwap_Amt( getNulleableIntegerFromSOAPString( redSoap.getPrimitivePropertySafelyAsString( "entSwap_Amt" ) ) );
					red.setentSwap_Cmnt( redSoap.getPrimitivePropertySafelyAsString( "entSwap_Cmnt" ) );
					red.setentSwap_Code( redSoap.getPrimitivePropertySafelyAsString( "entSwap_Code" ) );
					red.setentSwap_CrTy( redSoap.getPrimitivePropertySafelyAsString( "entSwap_CrTy" ) );
					red.setentSwap_CtTy( redSoap.getPrimitivePropertySafelyAsString( "entSwap_CtTy" ) );
					red.setentSwap_Fcbm( getNulleableIntegerFromSOAPString( redSoap.getPrimitivePropertySafelyAsString( "entSwap_Fcbm" ) ) );
					red.setentSwap_FrCs( redSoap.getPrimitivePropertySafelyAsString( "entSwap_FrCs" ) );
					red.setentSwap_Id( getNulleableLongFromSOAPString( redSoap.getPrimitivePropertySafelyAsString( "entSwap_Id" ) ) );
					red.setentSwap_Last_Dte( redSoap.getPrimitivePropertySafelyAsString( "entSwap_Last_Dte" ) );
					red.setentSwap_Last_Usr( redSoap.getPrimitivePropertySafelyAsString( "entSwap_Last_Usr" ) );
					red.setentSwap_Prze( getNulleableIntegerFromSOAPString( redSoap.getPrimitivePropertySafelyAsString( "entSwap_Prze" ) ) );
					red.setentSwap_Pts( getNulleableIntegerFromSOAPString( redSoap.getPrimitivePropertySafelyAsString( "entSwap_Pts" ) ) );
					red.setentSwap_Qtty( getNulleableIntegerFromSOAPString( redSoap.getPrimitivePropertySafelyAsString( "entSwap_Qtty" ) ) );
					red.setentSwap_Src( redSoap.getPrimitivePropertySafelyAsString( "entSwap_Src" ) );
					red.setentSwap_Swst( getNulleableIntegerFromSOAPString( redSoap.getPrimitivePropertySafelyAsString( "entSwap_Swst" ) ) );
					red.setentSwap_Val( getNulleableIntegerFromSOAPString( redSoap.getPrimitivePropertySafelyAsString( "entSwap_Val" ) ) );
					red.setsMensajeRedencion( redSoap.getPrimitivePropertySafelyAsString( "sMensajeRedencion" ) );

					response.setRedencion( red );
				}
			}
		}
		catch( Exception e )
		{
			response.setResponse( -4 );
			android.util.Log.d( "PremiosExternoClient", "Error: " + e.getMessage() );
			e.printStackTrace();
		}

		return response;
	}

	public CategoriesResponse getCatePreXProv( entProveedor proveedor )
	{
		CategoriesResponse response = new CategoriesResponse();

		SoapObject request = new SoapObject( NAMESPACE, "GetCatePreXProv" );

		PropertyInfo info = new PropertyInfo();
		info.setName( "objProveedor" );
		info.setValue( proveedor );
		info.setType( proveedor.getClass() );
		request.addProperty( info );

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope( SoapEnvelope.VER11 );
		envelope.dotNet = true;
		envelope.setOutputSoapObject( request );
		envelope.setAddAdornments( false );

		envelope.addMapping( "http://schemas.datacontract.org/2004/07/wcfCityPremiosCP.Entities.CityPremios", "entProveedor", entProveedor.class );

		HttpTransportSE transport = new HttpTransportSE( APIAddress.CITYPREMIOS_ENGINE_URL );
		transport.debug = true;

		try
		{
			transport.call( "http://tempuri.org/IwcfCityPremiosExterno/GetCatePreXProv", envelope );
			//android.util.Log.d( "TEST", transport.requestDump );

			Object obj = envelope.bodyIn;
			if( obj == null )
			{
				response.setResponse( -2 );
				android.util.Log.d( "PremiosExternoClient", "NO RESPONSE" );
			}
			else
			{
				if( envelope.bodyIn instanceof SoapFault )
				{
					response.setResponse( -3 );
					String str = ( (SoapFault) envelope.bodyIn ).faultstring;
					android.util.Log.d( "PremiosExternoClient", "RESPONSE FAULT: " + str );
				}
				else
				{
					//android.util.Log.d( "PremiosExternoClient", "RESPONSE: " + transport.responseDump );
					SoapObject resultsRequestSOAP = (SoapObject) envelope.bodyIn;
					SoapObject catsSoap = (SoapObject)resultsRequestSOAP.getProperty( 0 );

					for( int i=0; i<catsSoap.getPropertyCount(); i++ )
					{
						entCatePremios cat = new entCatePremios();

						SoapObject catObj = (SoapObject)catsSoap.getProperty( i );
						cat.setClaveCategoria( catObj.getPrimitivePropertySafelyAsString( "ClaveCategoria" ) );
						cat.setDescCategoria( catObj.getPrimitivePropertySafelyAsString( "DescCategoria" ) );
						cat.setLeyendaCategoria( catObj.getPrimitivePropertySafelyAsString( "LeyendaCategoria" ) );
						cat.setUrlLogoCategoria( catObj.getPrimitivePropertySafelyAsString( "UrlLogoCategoria" ) );
						cat.setUrlPaginaCategoria( catObj.getPrimitivePropertySafelyAsString( "UrlPaginaCategoria" ) );
						cat.setsMensajeCategoria( catObj.getPrimitivePropertySafelyAsString( "sMensajeCategoria" ) );

						response.getCategories().add( cat );
					}
				}
			}
		}
		catch( Exception e )
		{
			response.setResponse( -4 );
			android.util.Log.d( "PremiosExternoClient", "Error: " + e.getMessage() );
			e.printStackTrace();
		}

		return response;
	}

	public PremiosResponse getPreXProv( entProveedor proveedor, entCatePremios categoria )
	{
		PremiosResponse response = new PremiosResponse();

		SoapObject request = new SoapObject( NAMESPACE, "GetPreXProv" );

		PropertyInfo info = new PropertyInfo();
		info.setName( "objProveedor" );
		info.setValue( proveedor );
		info.setType( proveedor.getClass() );
		request.addProperty( info );

		info = new PropertyInfo();
		info.setName( "objCatePremios" );
		info.setValue( categoria );
		info.setType( categoria.getClass() );
		request.addProperty( info );

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope( SoapEnvelope.VER11 );
		envelope.dotNet = true;
		envelope.setOutputSoapObject( request );
		envelope.setAddAdornments( false );

		envelope.addMapping( "http://schemas.datacontract.org/2004/07/wcfCityPremiosCP.Entities.CityPremios", "entProveedor", entProveedor.class );
		envelope.addMapping( "http://schemas.datacontract.org/2004/07/wcfCityPremiosCP.Entities.CityPremios", "entCatePremios", entCatePremios.class );

		HttpTransportSE transport = new HttpTransportSE( APIAddress.CITYPREMIOS_ENGINE_URL );
		transport.debug = true;

		try
		{
			transport.call( "http://tempuri.org/IwcfCityPremiosExterno/GetPreXProv", envelope );
			//android.util.Log.d( "TEST", transport.requestDump );

			Object obj = envelope.bodyIn;
			if( obj == null )
			{
				response.setResponse( -2 );
				android.util.Log.d( "PremiosExternoClient", "NO RESPONSE" );
			}
			else
			{
				if( envelope.bodyIn instanceof SoapFault )
				{
					response.setResponse( -3 );
					String str = ( (SoapFault) envelope.bodyIn ).faultstring;
					android.util.Log.d( "PremiosExternoClient", "RESPONSE FAULT: " + str );
				}
				else
				{
					//android.util.Log.d( "PremiosExternoClient", "RESPONSE: " + transport.responseDump );
					SoapObject resultsRequestSOAP = (SoapObject) envelope.bodyIn;
					SoapObject premiosSoap = (SoapObject)resultsRequestSOAP.getProperty( 0 );

					for( int i=0; i<premiosSoap.getPropertyCount(); i++ )
					{
						Prze p = new Prze();
						SoapObject pObj = (SoapObject)premiosSoap.getProperty( i );

						p.setPrze_Actv( getNulleableIntegerFromSOAPString( pObj.getPrimitivePropertySafelyAsString( "Prze_Actv" ) ) );
						p.setPrze_Amt( getNulleableDoubleFromSOAPString( pObj.getPrimitivePropertySafelyAsString( "Prze_Amt" ) ) );
						p.setPrze_BsAs( pObj.getPrimitivePropertySafelyAsString( "Prze_BsAs" ) );
						p.setPrze_CtTy( pObj.getPrimitivePropertySafelyAsString( "Prze_CtTy" ) );
						p.setPrze_Debit( getNulleableBooleanFromSOAPString( pObj.getPrimitivePropertySafelyAsString( "Prze_Debit" ) ) );
						p.setPrze_Dscr( pObj.getPrimitivePropertySafelyAsString( "Prze_Dscr" ) );
						p.setPrze_Hab( getNulleableBooleanFromSOAPString( pObj.getPrimitivePropertySafelyAsString( "Prze_Hab" ) ) );
						p.setPrze_Hgh_Qtty( getNulleableDoubleFromSOAPString( pObj.getPrimitivePropertySafelyAsString( "Prze_Hgh_Qtty" ) ) );
						p.setPrze_Id( getNulleableIntegerFromSOAPString( pObj.getPrimitivePropertySafelyAsString( "Prze_Id" ) ) );
						p.setPrze_Isu_Dte( getDateFromSOAPString( pObj.getPrimitivePropertySafelyAsString( "Prze_Isu_Dte" ) ) );
						p.setPrze_Low_Qtty( getNulleableDoubleFromSOAPString( pObj.getPrimitivePropertySafelyAsString( "Prze_Low_Qtty" ) ) );
						p.setPrze_Mat_Dte( getDateFromSOAPString( pObj.getPrimitivePropertySafelyAsString( "Prze_Mat_Dte" ) ) );
						p.setPrze_Mod_Dte( getDateFromSOAPString( pObj.getPrimitivePropertySafelyAsString( "Prze_Mod_Dte" ) ) );
						p.setPrze_Mod_Usr( pObj.getPrimitivePropertySafelyAsString( "Prze_Mod_Usr" ) );
						p.setPrze_Name( pObj.getPrimitivePropertySafelyAsString( "Prze_Name" ) );
						p.setPrze_Pts( getNulleableDoubleFromSOAPString( pObj.getPrimitivePropertySafelyAsString( "Prze_Pts" ) ) );
						p.setPrze_Rwty( pObj.getPrimitivePropertySafelyAsString( "Prze_Rwty" ) );
						p.setPrze_Shrt_Name( pObj.getPrimitivePropertySafelyAsString( "Prze_Shrt_Name" ) );
						p.setPrze_URL00( pObj.getPrimitivePropertySafelyAsString( "Prze_URL00" ) );
						p.setPrze_URL01( pObj.getPrimitivePropertySafelyAsString( "Prze_URL01" ) );
						p.setPrze_Value( getNulleableDoubleFromSOAPString( pObj.getPrimitivePropertySafelyAsString( "Prze_Value" ) ) );

						response.getPremios().add( p );
					}
				}
			}
		}
		catch( Exception e )
		{
			response.setResponse( -4 );
			android.util.Log.d( "PremiosExternoClient", "Error: " + e.getMessage() );
			e.printStackTrace();
		}

		return response;
	}

	public EdoCtaResponse getEdoCta( String socio, Date inicio, Date fin )
	{
		EdoCtaResponse response = new EdoCtaResponse();

		SoapObject request = new SoapObject( NAMESPACE, "GetEdoCta" );

		SimpleDateFormat sdf = new SimpleDateFormat( "yyyyMMdd" );

		PropertyInfo info = new PropertyInfo();
		info.setName( "numCP" );
		info.setValue( socio );
		info.setType( socio.getClass() );
		request.addProperty( info );

		info = new PropertyInfo();
		info.setName( "sinicioPeriodo" );
		info.setValue( sdf.format( inicio ) );
		info.setType( String.class );
		request.addProperty( info );

		info = new PropertyInfo();
		info.setName( "sfinPeriodo" );
		info.setValue( sdf.format( fin ) );
		info.setType( String.class );
		request.addProperty( info );

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope( SoapEnvelope.VER11 );
		envelope.dotNet = true;
		envelope.setOutputSoapObject( request );
		envelope.setAddAdornments( false );

		HttpTransportSE transport = new HttpTransportSE( APIAddress.CITYPREMIOS_ENGINE_URL );
		transport.debug = true;

		try
		{
			transport.call( "http://tempuri.org/IwcfCityPremiosExterno/GetEdoCta", envelope );
			//android.util.Log.d( "TEST", transport.requestDump );

			Object obj = envelope.bodyIn;
			if( obj == null )
			{
				response.setResponse( -2 );
				android.util.Log.d( "PremiosExternoClient", "NO RESPONSE" );
			}
			else
			{
				if( envelope.bodyIn instanceof SoapFault )
				{
					response.setResponse( -3 );
					String str = ( (SoapFault) envelope.bodyIn ).faultstring;
					android.util.Log.d( "PremiosExternoClient", "RESPONSE FAULT: " + str );
				}
				else
				{
					//android.util.Log.d( "PremiosExternoClient", "RESPONSE: " + transport.responseDump.length() );
					//android.util.Log.d( "PremiosExternoClient", "RESPONSE: " + transport.responseDump );

					PropertyInfo pinfo = new PropertyInfo();

					SoapObject resultsRequestSOAP = (SoapObject) envelope.bodyIn;
					SoapObject responseSOAP = (SoapObject) resultsRequestSOAP.getProperty( 0 );
					SoapObject diffgram = (SoapObject) responseSOAP.getProperty( "diffgram" );
					SoapObject dataset = (SoapObject) diffgram.getProperty( "NewDataSet" );

					for( int i = 0; i < dataset.getPropertyCount(); i++ )
					{
						SoapObject recordObj = (SoapObject) dataset.getProperty( i );
						dataset.getPropertyInfo( i, pinfo );

						//android.util.Log.d( "TEST", "RECORD: " + pinfo.getName() );
						if( pinfo.getName().equalsIgnoreCase( "RESUMEN" ) )
						{
							EdoCtaResponse.ResRecord r = new EdoCtaResponse.ResRecord();
							r.setBalanceAnterior( parseIntForRecord( recordObj.getPrimitivePropertySafelyAsString( "BALANCE_x0020_ANTERIOR" ) ) );
							r.setPuntosAcumulados( parseIntForRecord( recordObj.getPrimitivePropertySafelyAsString( "PUNTOS_x0020_ACUMULADOS" ) ) );
							r.setPuntosCanje( parseIntForRecord( recordObj.getPrimitivePropertySafelyAsString( "PUNTOS_x0020_EN_x0020_PROCESO_x0020_DE_x0020_CANJE" ) ) );
							r.setPuntosRedimidos( parseIntForRecord( recordObj.getPrimitivePropertySafelyAsString( "PUNTOS_x0020_REDIMIDOS" ) ) );
							r.setPuntosVencidos( parseIntForRecord( recordObj.getPrimitivePropertySafelyAsString( "PUNTOS_x0020_VENCIDOS" ) ) );
							r.setSaldoAlCorte( parseIntForRecord( recordObj.getPrimitivePropertySafelyAsString( "SALDO_x0020_AL_x0020_CORTE" ) ) );
							response.getRESUMEN().add( r );
						}
						else
						{
							Date date;
							String description = "";
							int points = 0;

							date = parseDateForRecord( recordObj.getPrimitivePropertySafelyAsString( "FECHA" ) );
							description = recordObj.getPrimitivePropertySafelyAsString( "DESCRIPCIÓN" );
							points = parseIntForRecord( recordObj.getPrimitivePropertySafelyAsString( "PUNTOS" ) );

							if( pinfo.getName().equalsIgnoreCase( "CRB" ) )
								response.getCRB().add( new EdoCtaResponse.Record( date, description, points ) );
							else if( pinfo.getName().equalsIgnoreCase( "PRB" ) )
								response.getPRB().add( new EdoCtaResponse.Record( date, description, points ) );
							else if( pinfo.getName().equalsIgnoreCase( "ACR" ) )
								response.getACR().add( new EdoCtaResponse.Record( date, description, points ) );
							else if( pinfo.getName().equalsIgnoreCase( "RED" ) )
								response.getRED().add( new EdoCtaResponse.Record( date, description, points ) );
							else if( pinfo.getName().equalsIgnoreCase( "SWP" ) )
								response.getSWP().add( new EdoCtaResponse.Record( date, description, points ) );
							else if( pinfo.getName().equalsIgnoreCase( "MAT" ) )
								response.getMAT().add( new EdoCtaResponse.Record( date, description, points ) );
							else if( pinfo.getName().equalsIgnoreCase( "NMT" ) )
								response.getNMT().add( new EdoCtaResponse.Record( date, description, points ) );
						}
					}
				}
			}
		}
		catch( Exception e )
		{
			response.setResponse( -4 );
			android.util.Log.d( "PremiosExternoClient", "Error: " + e.getMessage() );
			e.printStackTrace();
		}

		return response;
	}

	public SendEmailResponse sendTCPEmail( entSocioCP socio )
	{
		SendEmailResponse response = new SendEmailResponse();

		SoapObject request = new SoapObject( NAMESPACE, "SendTCPEmail" );

		PropertyInfo info = new PropertyInfo();
		info.setName( "objSocioCP" );
		info.setValue( socio );
		info.setType( socio.getClass() );
		request.addProperty( info );

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope( SoapEnvelope.VER11 );
		envelope.dotNet = true;
		envelope.setOutputSoapObject( request );
		envelope.setAddAdornments( false );

		envelope.addMapping( "http://schemas.datacontract.org/2004/07/wcfCityPremiosCP.Entities.CityPremios", "entSocioCP", entSocioCP.class );
		envelope.addMapping( "http://schemas.datacontract.org/2004/07/wcfCityPremiosCP.Entities.CityPremios", "entAplicacion", entAplicacion.class );
		envelope.addMapping( "http://schemas.datacontract.org/2004/07/wcfCityPremiosCP.Entities.CityPremios", "entEsfuerzo", entEsfuerzo.class );
		envelope.addMapping( "http://schemas.datacontract.org/2004/07/wcfCityPremiosCP.Entities.CityPremios", "entFrecuenciaViaje", entFrecuenciaViaje.class );
		envelope.addMapping( "http://schemas.datacontract.org/2004/07/wcfCityPremiosCP.Entities.CityPremios", "entMotivoHospedaje", entMotivoHospedaje.class );
		envelope.addMapping( "http://schemas.datacontract.org/2004/07/wcfCityPremiosCP.Entities.CityPremios", "entOrigen", entOrigen.class );
		envelope.addMapping( "http://schemas.datacontract.org/2004/07/wcfCityPremiosCP.Entities.CityPremios", "entPrograma", entPrograma.class );

		HttpTransportSE transport = new HttpTransportSE( APIAddress.CITYPREMIOS_ENGINE_URL );
		transport.debug = true;

		try
		{
			transport.call( "http://tempuri.org/IwcfCityPremiosExterno/SendTCPEmail", envelope );
			//android.util.Log.d( "TEST", transport.requestDump );

			Object obj = envelope.bodyIn;
			if( obj == null )
			{
				response.setResponse( -2 );
				android.util.Log.d( "PremiosExternoClient", "NO RESPONSE" );
			}
			else
			{
				if( envelope.bodyIn instanceof SoapFault )
				{
					response.setResponse( -3 );
					String str = ( (SoapFault) envelope.bodyIn ).faultstring;
					android.util.Log.d( "PremiosExternoClient", "RESPONSE FAULT: " + str );
				}
				else
				{
					//android.util.Log.d( "PremiosExternoClient", "RESPONSE: " + transport.responseDump );
					SoapObject resultsRequestSOAP = (SoapObject) envelope.bodyIn;
					response.setSent( resultsRequestSOAP.getPropertySafelyAsString( "SendTCPEmailResult" ).equalsIgnoreCase( "TRUE" ) );
				}
			}
		}
		catch( Exception e )
		{
			response.setResponse( -4 );
			android.util.Log.d( "PremiosExternoClient", "Error: " + e.getMessage() );
			e.printStackTrace();
		}

		return response;
	}

	public CountriesResponse getCountries()
	{
		CountriesResponse response = new CountriesResponse();

		SoapObject request = new SoapObject( NAMESPACE, "GetCtTy" );

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope( SoapEnvelope.VER11 );
		envelope.dotNet = true;
		envelope.setOutputSoapObject( request );
		envelope.setAddAdornments( false );

		HttpTransportSE transport = new HttpTransportSE( APIAddress.CITYPREMIOS_ENGINE_URL );
		transport.debug = true;
		try
		{
			transport.call( "http://tempuri.org/IwcfCityPremiosExterno/GetCtTy", envelope );

			Object obj = envelope.bodyIn;
			if( obj == null )
			{
				response.setResponse( -2 );
				android.util.Log.d( "PremiosExternoClient", "NO RESPONSE" );
			}
			else
			{
				if( envelope.bodyIn instanceof SoapFault )
				{
					response.setResponse( -3 );
					String str = ( (SoapFault) envelope.bodyIn ).faultstring;
					android.util.Log.d( "PremiosExternoClient", "RESPONSE FAULT: " + str );
				}
				else
				{
					//android.util.Log.d( "PremiosExternoClient", "RESPONSE: " + transport.responseDump );
					SoapObject resultsRequestSOAP = (SoapObject) envelope.bodyIn;
					SoapObject ctTyResponse = (SoapObject) resultsRequestSOAP.getProperty( 0 );

					for( int i = 0; i < ctTyResponse.getPropertyCount(); i++ )
					{
						SoapObject aObj = (SoapObject) ctTyResponse.getProperty( i );
						Country c = new Country();

						c.setCode( aObj.getPropertySafelyAsString( "CtTy_Name" ) );
						c.setISOCode( aObj.getPropertySafelyAsString( "CtTy_ISO_Code" ) );
						c.setName( aObj.getPropertySafelyAsString( "CtTy_Dscr" ) );

						response.getCountries().add( c );
					}
				}
			}
		}
		catch( Exception e )
		{
			response.setResponse( -4 );
			android.util.Log.d( "PremiosExternoClient", "Error: " + e.getMessage() );
			e.printStackTrace();
		}

		return response;
	}

	public SocioResponse afiliacionSocio( entSocioCP socio )
	{
		SocioResponse response = new SocioResponse();

		SoapObject request = new SoapObject( NAMESPACE, "AfiliacionCP" );

		PropertyInfo info = new PropertyInfo();
		info.setName( "objAfiliacionSocio" );
		info.setValue( socio );
		info.setType( socio.getClass() );
		request.addProperty( info );

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope( SoapEnvelope.VER11 );
		envelope.dotNet = true;
		envelope.setOutputSoapObject( request );
		envelope.setAddAdornments( false );

		envelope.addMapping( "http://schemas.datacontract.org/2004/07/wcfCityPremiosCP.Entities.CityPremios", "entSocioCP", entSocioCP.class );
		envelope.addMapping( "http://schemas.datacontract.org/2004/07/wcfCityPremiosCP.Entities.CityPremios", "entAplicacion", entAplicacion.class );
		envelope.addMapping( "http://schemas.datacontract.org/2004/07/wcfCityPremiosCP.Entities.CityPremios", "entEsfuerzo", entEsfuerzo.class );
		envelope.addMapping( "http://schemas.datacontract.org/2004/07/wcfCityPremiosCP.Entities.CityPremios", "entFrecuenciaViaje", entFrecuenciaViaje.class );
		envelope.addMapping( "http://schemas.datacontract.org/2004/07/wcfCityPremiosCP.Entities.CityPremios", "entMotivoHospedaje", entMotivoHospedaje.class );
		envelope.addMapping( "http://schemas.datacontract.org/2004/07/wcfCityPremiosCP.Entities.CityPremios", "entOrigen", entOrigen.class );
		envelope.addMapping( "http://schemas.datacontract.org/2004/07/wcfCityPremiosCP.Entities.CityPremios", "entPrograma", entPrograma.class );

		HttpTransportSE transport = new HttpTransportSE( APIAddress.CITYPREMIOS_ENGINE_URL );
		transport.debug = true;

		try
		{
			transport.call( "http://tempuri.org/IwcfCityPremiosExterno/AfiliacionCP", envelope );
			//android.util.Log.d( "TEST", transport.requestDump );

			Object obj = envelope.bodyIn;
			if( obj == null )
			{
				response.setResponse( -2 );
				android.util.Log.d( "PremiosExternoClient", "NO RESPONSE" );
			}
			else
			{
				if( envelope.bodyIn instanceof SoapFault )
				{
					response.setResponse( -3 );
					String str = ( (SoapFault) envelope.bodyIn ).faultstring;
					android.util.Log.d( "PremiosExternoClient", "RESPONSE FAULT: " + str );
				}
				else
				{
					//android.util.Log.d( "PremiosExternoClient", "RESPONSE: " + transport.responseDump );
					response.setSocio( parseSocio( envelope.bodyIn ) );
				}
			}
		}
		catch( Exception e )
		{
			response.setResponse( -4 );
			android.util.Log.d( "PremiosExternoClient", "Error: " + e.getMessage() );
			e.printStackTrace();
		}

		return response;
	}

	private entSocioCP parseSocio( Object bodyIn )
	{
		entSocioCP socio = new entSocioCP();

		SoapObject resultsRequestSOAP = (SoapObject) bodyIn;
		SoapObject socioResult = (SoapObject) resultsRequestSOAP.getProperty( 0 );

		socio.setApMaternoSocio( socioResult.getPropertySafelyAsString( "ApMaternoSocio" ) );
		socio.setApMaternoSocio( socioResult.getPropertySafelyAsString( "ApPaternoSocio" ) );

		SoapObject appObj = (SoapObject) socioResult.getProperty( "Aplicacion" );
		socio.getAplicacion().setClaveAplicacion( appObj.getPropertySafelyAsString( "ClaveAplicacion" ) );
		socio.getAplicacion().setDescAplicacion( appObj.getPropertySafelyAsString( "DescAplicacion" ) );

		socio.setAplicacionMailSocio( socioResult.getPropertySafelyAsString( "AplicacionMailSocio" ) );
		socio.setCalleEmpresa( socioResult.getPropertySafelyAsString( "CalleEmpresa" ) );
		socio.setCalleSocio( socioResult.getPropertySafelyAsString( "CalleSocio" ) );
		socio.setCelularSocio( socioResult.getPropertySafelyAsString( "CelularSocio" ) );
		socio.setCiudadEmpresa( socioResult.getPropertySafelyAsString( "CiudadEmpresa" ) );
		socio.setCiudadSocio( socioResult.getPropertySafelyAsString( "CiudadSocio" ) );
		socio.setClaveIATAEmpresa( socioResult.getPropertySafelyAsString( "ClaveIATAEmpresa" ) );
		socio.setCodigoPostalEmpresa( socioResult.getPropertySafelyAsString( "CodigoPostalEmpresa" ) );
		socio.setCodigoPostalSocio( socioResult.getPropertySafelyAsString( "CodigoPostalSocio" ) );
		socio.setColoniaEmpresa( socioResult.getPropertySafelyAsString( "ColoniaEmpresa" ) );
		socio.setColoniaSocio( socioResult.getPropertySafelyAsString( "ColoniaSocio" ) );
		socio.setComentariosSocio( socioResult.getPropertySafelyAsString( "ComentariosSocio" ) );
		socio.setCorreoDestinoSocio( socioResult.getPropertySafelyAsString( "CorreoDestinoSocio" ) );
		socio.setCorreoEmailSocio( socioResult.getPropertySafelyAsString( "CorreoEmailSocio" ) );
		socio.setEmailEmpresa( socioResult.getPropertySafelyAsString( "EmailEmpresa" ) );

		SoapObject esfuerzoObj = (SoapObject) socioResult.getProperty( "EsfuerzoSocio" );
		socio.getEsfuerzoSocio().setClaveEsfuerzo( esfuerzoObj.getPropertySafelyAsString( "ClaveEsfuerzo" ) );
		socio.getEsfuerzoSocio().setDescEsfuerzo( esfuerzoObj.getPropertySafelyAsString( "DescEsfuerzo" ) );
		socio.getEsfuerzoSocio().setsMensajeEsfuerzo( esfuerzoObj.getPropertySafelyAsString( "sMensajeEsfuerzo" ) );

		socio.setEstadoCivil( socioResult.getPropertySafelyAsString( "EstadoCivil" ) );
		socio.setEstadoEmpresa( socioResult.getPropertySafelyAsString( "EstadoEmpresa" ) );
		socio.setEstadoSocio( socioResult.getPropertySafelyAsString( "EstadoSocio" ) );
		socio.setFaxEmpresa( socioResult.getPropertySafelyAsString( "FaxEmpresa" ) );

		socio.setFechaAltaSocio( getDateFromSOAPString( socioResult.getPropertySafelyAsString( "FechaAltaSocio" ) ) );
		socio.setFechaModificaSocio( getDateFromSOAPString( socioResult.getPropertySafelyAsString( "FechaModificaSocio" ) ) );
		socio.setFechaNacSocio( getDateFromSOAPString( socioResult.getPropertySafelyAsString( "FechaNacSocio" ) ) );

		SoapObject frecuenciaObj = (SoapObject) socioResult.getProperty( "FrecuenciaViaje" );
		socio.getFrecuenciaViaje().setClaveFrecVia( frecuenciaObj.getPropertySafelyAsString( "ClaveFrecVia" ) );
		socio.getFrecuenciaViaje().setDescFrecViaj( frecuenciaObj.getPropertySafelyAsString( "DescFrecViaj" ) );

		socio.setIsMemberGold( socioResult.getPropertySafelyAsString( "IsMemberGold" ) );

		SoapObject motivosObj = (SoapObject) socioResult.getProperty( "MotivoHospedaje" );
		socio.getMotivoHospedaje().setClaveMotHos( motivosObj.getPropertySafelyAsString( "ClaveMotHos" ) );
		socio.getMotivoHospedaje().setDescMotHos( motivosObj.getPropertySafelyAsString( "DescMotHos" ) );

		socio.setNoExtEmpresa( socioResult.getPropertySafelyAsString( "NoExtEmpresa" ) );
		socio.setNoExteriorSocio( socioResult.getPropertySafelyAsString( "NoExteriorSocio" ) );
		socio.setNoIntEmpresa( socioResult.getPropertySafelyAsString( "NoIntEmpresa" ) );
		socio.setNoInteriorSocio( socioResult.getPropertySafelyAsString( "NoInteriorSocio" ) );
		socio.setNoSocioCP( socioResult.getPropertySafelyAsString( "NoSocioCP" ) );
		socio.setNombreSocio( socioResult.getPropertySafelyAsString( "NombreSocio" ) );

		SoapObject origenObj = (SoapObject) socioResult.getProperty( "OrigenSocio" );
		socio.getOrigenSocio().setClaveOrigen( origenObj.getPropertySafelyAsString( "ClaveOrigen" ) );
		socio.getOrigenSocio().setDescOrigen( origenObj.getPropertySafelyAsString( "DescOrigen" ) );
		socio.getOrigenSocio().setsMensajeOrigen( origenObj.getPropertySafelyAsString( "sMensajeOrigen" ) );

		socio.setPaisEmpresa( socioResult.getPropertySafelyAsString( "PaisEmpresa" ) );
		socio.setPaisNacSocio( socioResult.getPropertySafelyAsString( "PaisNacSocio" ) );
		socio.setPaisSocio( socioResult.getPropertySafelyAsString( "PaisSocio" ) );
		socio.setPassword( socioResult.getPropertySafelyAsString( "Password" ) );

		SoapObject programaObj = (SoapObject) socioResult.getProperty( "ProgramaSocio" );
		socio.getProgramaSocio().setClaveProg( programaObj.getPropertySafelyAsString( "ClaveProg" ) );
		socio.getProgramaSocio().setDescripcionProg( programaObj.getPropertySafelyAsString( "DescripcionProg" ) );
		socio.getProgramaSocio().setsMensajePrograma( programaObj.getPropertySafelyAsString( "sMensajePrograma" ) );

		socio.setPuestoEmpresa( socioResult.getPropertySafelyAsString( "PuestoEmpresa" ) );
		socio.setRFCEmpresa( socioResult.getPropertySafelyAsString( "RFCEmpresa" ) );
		socio.setRazonSocialEmpresa( socioResult.getPropertySafelyAsString( "RazonSocialEmpresa" ) );
		socio.setRubroEmpresa( socioResult.getPropertySafelyAsString( "RubroEmpresa" ) );
		socio.setSexoSocio( socioResult.getPropertySafelyAsString( "SexoSocio" ) );
		socio.setTelefonoEmpresa( socioResult.getPropertySafelyAsString( "TelefonoEmpresa" ) );
		socio.setTelefonoSocio( socioResult.getPropertySafelyAsString( "TelefonoSocio" ) );
		socio.setTituloSocio( socioResult.getPropertySafelyAsString( "TituloSocio" ) );
		socio.setUsuarioAltaSocio( socioResult.getPropertySafelyAsString( "UsuarioAltaSocio" ) );
		socio.setUsuarioModificaSocio( socioResult.getPropertySafelyAsString( "UsuarioModificaSocio" ) );
		socio.setsMensajeAfiliacion( socioResult.getPropertySafelyAsString( "sMensajeAfiliacion" ) );

		return socio;
	}

	public SocioResponse loginSocio( entSocioCP socio )
	{
		SocioResponse response = new SocioResponse();

		SoapObject request = new SoapObject( NAMESPACE, "loginSocio" );

		PropertyInfo info = new PropertyInfo();
		info.setName( "objLoginSocio" );
		info.setValue( socio );
		info.setType( socio.getClass() );
		request.addProperty( info );

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope( SoapEnvelope.VER11 );
		envelope.dotNet = true;
		envelope.setOutputSoapObject( request );
		envelope.setAddAdornments( false );

		envelope.addMapping( "http://schemas.datacontract.org/2004/07/wcfCityPremiosCP.Entities.CityPremios", "entSocioCP", entSocioCP.class );
		envelope.addMapping( "http://schemas.datacontract.org/2004/07/wcfCityPremiosCP.Entities.CityPremios", "entAplicacion", entAplicacion.class );
		envelope.addMapping( "http://schemas.datacontract.org/2004/07/wcfCityPremiosCP.Entities.CityPremios", "entEsfuerzo", entEsfuerzo.class );
		envelope.addMapping( "http://schemas.datacontract.org/2004/07/wcfCityPremiosCP.Entities.CityPremios", "entFrecuenciaViaje", entFrecuenciaViaje.class );
		envelope.addMapping( "http://schemas.datacontract.org/2004/07/wcfCityPremiosCP.Entities.CityPremios", "entMotivoHospedaje", entMotivoHospedaje.class );
		envelope.addMapping( "http://schemas.datacontract.org/2004/07/wcfCityPremiosCP.Entities.CityPremios", "entOrigen", entOrigen.class );
		envelope.addMapping( "http://schemas.datacontract.org/2004/07/wcfCityPremiosCP.Entities.CityPremios", "entPrograma", entPrograma.class );

		HttpTransportSE transport = new HttpTransportSE( APIAddress.CITYPREMIOS_ENGINE_URL );
		transport.debug = true;

		try
		{
			transport.call( "http://tempuri.org/IwcfCityPremiosExterno/loginSocio", envelope );
			//android.util.Log.d( "TEST", transport.requestDump );

			Object obj = envelope.bodyIn;
			if( obj == null )
			{
				response.setResponse( -2 );
				android.util.Log.d( "PremiosExternoClient", "NO RESPONSE" );
			}
			else
			{
				if( envelope.bodyIn instanceof SoapFault )
				{
					response.setResponse( -3 );
					String str = ( (SoapFault) envelope.bodyIn ).faultstring;
					android.util.Log.d( "PremiosExternoClient", "RESPONSE FAULT: " + str );
				}
				else
				{
					//android.util.Log.d( "PremiosExternoClient", "RESPONSE: " + transport.responseDump );
					response.setSocio( parseSocio( envelope.bodyIn ) );
				}
			}
		}
		catch( Exception e )
		{
			response.setResponse( -4 );
			android.util.Log.d( "PremiosExternoClient", "Error: " + e.getMessage() );
			e.printStackTrace();
		}

		return response;
	}

	public static class CanjePremioResponse
	{
		private int _response;
		private entRedencion _redencion;

		public CanjePremioResponse()
		{
			_response = 0;
		}

		public int getResponse()
		{
			return _response;
		}

		public void setResponse( int response )
		{
			_response = response;
		}

		public entRedencion getRedencion()
		{
			return _redencion;
		}

		public void setRedencion( entRedencion redencion )
		{
			_redencion = redencion;
		}
	}

	public static class EdoCtaResponse
	{
		private int _response;
		private ArrayList<Record> _CRB;
		private ArrayList<Record> _PRB;
		private ArrayList<Record> _ACR;
		private ArrayList<Record> _RED;
		private ArrayList<Record> _SWP;
		private ArrayList<Record> _MAT;
		private ArrayList<Record> _NMT;
		private ArrayList<ResRecord> _RESUMEN;

		public EdoCtaResponse()
		{
			_CRB = new ArrayList<Record>();
			_PRB = new ArrayList<Record>();
			_ACR = new ArrayList<Record>();
			_RED = new ArrayList<Record>();
			_SWP = new ArrayList<Record>();
			_MAT = new ArrayList<Record>();
			_NMT = new ArrayList<Record>();
			_RESUMEN = new ArrayList<ResRecord>();
		}

		public ArrayList<ResRecord> getRESUMEN()
		{
			return _RESUMEN;
		}

		public void setRESUMEN( ArrayList<ResRecord> RESUMEN )
		{
			_RESUMEN = RESUMEN;
		}

		public int getResponse()
		{
			return _response;
		}

		public void setResponse( int response )
		{
			_response = response;
		}

		public ArrayList<Record> getCRB()
		{
			return _CRB;
		}

		public void setCRB( ArrayList<Record> CRB )
		{
			_CRB = CRB;
		}

		public ArrayList<Record> getPRB()
		{
			return _PRB;
		}

		public void setPRB( ArrayList<Record> PRB )
		{
			_PRB = PRB;
		}

		public ArrayList<Record> getACR()
		{
			return _ACR;
		}

		public void setACR( ArrayList<Record> ACR )
		{
			_ACR = ACR;
		}

		public ArrayList<Record> getRED()
		{
			return _RED;
		}

		public void setRED( ArrayList<Record> RED )
		{
			_RED = RED;
		}

		public ArrayList<Record> getSWP()
		{
			return _SWP;
		}

		public void setSWP( ArrayList<Record> SWP )
		{
			_SWP = SWP;
		}

		public ArrayList<Record> getMAT()
		{
			return _MAT;
		}

		public void setMAT( ArrayList<Record> MAT )
		{
			_MAT = MAT;
		}

		public ArrayList<Record> getNMT()
		{
			return _NMT;
		}

		public void setNMT( ArrayList<Record> NMT )
		{
			_NMT = NMT;
		}

		public static class ResRecord
		{
			private int _balanceAnterior;
			private int _puntosAcumulados;
			private int _puntosCanje;
			private int _puntosRedimidos;
			private int _puntosVencidos;
			private int _saldoAlCorte;

			public int getBalanceAnterior()
			{
				return _balanceAnterior;
			}

			public void setBalanceAnterior( int balanceAnterior )
			{
				_balanceAnterior = balanceAnterior;
			}

			public int getPuntosAcumulados()
			{
				return _puntosAcumulados;
			}

			public void setPuntosAcumulados( int puntosAcumulados )
			{
				_puntosAcumulados = puntosAcumulados;
			}

			public int getPuntosCanje()
			{
				return _puntosCanje;
			}

			public void setPuntosCanje( int puntosCanje )
			{
				_puntosCanje = puntosCanje;
			}

			public int getPuntosRedimidos()
			{
				return _puntosRedimidos;
			}

			public void setPuntosRedimidos( int puntosRedimidos )
			{
				_puntosRedimidos = puntosRedimidos;
			}

			public int getPuntosVencidos()
			{
				return _puntosVencidos;
			}

			public void setPuntosVencidos( int puntosVencidos )
			{
				_puntosVencidos = puntosVencidos;
			}

			public int getSaldoAlCorte()
			{
				return _saldoAlCorte;
			}

			public void setSaldoAlCorte( int saldoAlCorte )
			{
				_saldoAlCorte = saldoAlCorte;
			}
		}

		public static class Record
		{
			private Date _date;
			private String _description;
			private int _points;

			public Record( Date date, String desc, int points )
			{
				_date = date;
				_description = desc;
				_points = points;
			}

			public Date getDate()
			{
				return _date;
			}

			public void setDate( Date date )
			{
				_date = date;
			}

			public String getDescription()
			{
				return _description;
			}

			public void setDescription( String description )
			{
				_description = description;
			}

			public int getPoints()
			{
				return _points;
			}

			public void setPoints( int points )
			{
				_points = points;
			}

			public static class Comparator implements java.util.Comparator<Record>
			{
				public int compare( Record c1, Record c2 )
				{
					if( c1.getDate() == null && c2.getDate() == null )
						return 0;
					else if( c1.getDate() == null )
						return -1;
					else if( c2.getDate() == null )
						return 1;
					else
						return c1.getDate().compareTo( c2.getDate() );
				}
			}
		}
	}

	public static class SendEmailResponse
	{
		private int _response;
		private Boolean _sent;

		public int getResponse()
		{
			return _response;
		}

		public void setResponse( int response )
		{
			_response = response;
		}

		public Boolean isSent()
		{
			return _sent;
		}

		public void setSent( Boolean sent )
		{
			_sent = sent;
		}
	}

	public static class CountriesResponse
	{
		private int _response;
		private ArrayList<Country> _countries;

		public CountriesResponse()
		{
			_countries = new ArrayList<Country>();
		}

		public int getResponse()
		{
			return _response;
		}

		public void setResponse( int response )
		{
			_response = response;
		}

		public ArrayList<Country> getCountries()
		{
			return _countries;
		}

		public void setCountries( ArrayList<Country> countries )
		{
			_countries = countries;
		}
	}

	public static class SocioResponse
	{
		private entSocioCP _socio;
		private int _response;

		public int getResponse()
		{
			return _response;
		}

		public void setResponse( int response )
		{
			_response = response;
		}

		public entSocioCP getSocio()
		{
			return _socio;
		}

		public void setSocio( entSocioCP socio )
		{
			_socio = socio;
		}
	}

	public static class CategoriesResponse
	{
		private int _response;
		private ArrayList<entCatePremios> _categories;

		public CategoriesResponse()
		{
			_response = 0;
			_categories = new ArrayList<entCatePremios>();
		}

		public int getResponse()
		{
			return _response;
		}

		public void setResponse( int response )
		{
			_response = response;
		}

		public ArrayList<entCatePremios> getCategories()
		{
			return _categories;
		}

		public void setCategories( ArrayList<entCatePremios> categories )
		{
			_categories = categories;
		}
	}

	public static class PremiosResponse
	{
		private int _response;
		private ArrayList<Prze> _premios;

		public PremiosResponse()
		{
			_response = 0;
			_premios = new ArrayList<Prze>();
		}

		public int getResponse()
		{
			return _response;
		}

		public void setResponse( int response )
		{
			_response = response;
		}

		public ArrayList<Prze> getPremios()
		{
			return _premios;
		}

		public void setPremios( ArrayList<Prze> premios )
		{
			_premios = premios;
		}
	}

	public static class entAplicacion implements KvmSerializable
	{
		private static final long serialVersionUID = 0L;

		private String _claveAplicacion;
		private String _descAplicacion;

		@Override
		public int getPropertyCount()
		{
			return 2;
		}

		@Override
		public Object getProperty( int i )
		{
			if( i == 0 )
			{
				return this.getClaveAplicacion();
			}
			else if( i == 1 )
			{
				return this.getDescAplicacion();
			}
			else
			{
				return null;
			}
		}

		@Override
		public void setProperty( int i, Object o )
		{
			if( i == 0 )
			{
				this.setClaveAplicacion( (String) o );
			}
			else if( i == 1 )
			{
				this.setDescAplicacion( (String) o );
			}
		}

		@Override
		public void getPropertyInfo( int i, Hashtable hashtable, PropertyInfo propertyInfo )
		{
			if( i == 0 )
			{
				propertyInfo.name = "ClaveAplicacion";
				propertyInfo.namespace = "http://schemas.datacontract.org/2004/07/wcfCityPremiosCP.Entities.CityPremios";
			}
			else if( i == 0 )
			{
				propertyInfo.name = "DescAplicacion";
				propertyInfo.namespace = "http://schemas.datacontract.org/2004/07/wcfCityPremiosCP.Entities.CityPremios";
			}
		}

		public String getClaveAplicacion()
		{
			return _claveAplicacion;
		}

		public void setClaveAplicacion( String claveAplicacion )
		{
			_claveAplicacion = claveAplicacion;
		}

		public String getDescAplicacion()
		{
			return _descAplicacion;
		}

		public void setDescAplicacion( String descAplicacion )
		{
			_descAplicacion = descAplicacion;
		}
	}

	public static class entEsfuerzo implements KvmSerializable
	{
		private static final long serialVersionUID = 0L;
		private String _claveEsfuerzo;
		private String _descEsfuerzo;
		private String _sMensajeEsfuerzo;

		@Override
		public int getPropertyCount()
		{
			return 3;
		}

		@Override
		public Object getProperty( int i )
		{
			if( i == 0 )
			{
				return this.getClaveEsfuerzo();
			}
			else if( i == 1 )
			{
				return this.getDescEsfuerzo();
			}
			else if( i == 2 )
			{
				return this.getsMensajeEsfuerzo();
			}
			else
			{
				return null;
			}
		}

		@Override
		public void setProperty( int i, Object o )
		{
			if( i == 0 )
			{
				this.setClaveEsfuerzo( (String) o );
			}
			else if( i == 1 )
			{
				this.setDescEsfuerzo( (String) o );
			}
			else if( i == 2 )
			{
				this.setsMensajeEsfuerzo( (String) o );
			}
		}

		@Override
		public void getPropertyInfo( int i, Hashtable hashtable, PropertyInfo propertyInfo )
		{
			if( i == 0 )
			{
				propertyInfo.name = "ClaveEsfuerzo";
				propertyInfo.namespace = "http://schemas.datacontract.org/2004/07/wcfCityPremiosCP.Entities.CityPremios";
			}
			else if( i == 1 )
			{
				propertyInfo.name = "DescEsfuerzo";
				propertyInfo.namespace = "http://schemas.datacontract.org/2004/07/wcfCityPremiosCP.Entities.CityPremios";
			}
			else if( i == 2 )
			{
				propertyInfo.name = "sMensajeEsfuerzo";
				propertyInfo.namespace = "http://schemas.datacontract.org/2004/07/wcfCityPremiosCP.Entities.CityPremios";
			}
		}

		public String getClaveEsfuerzo()
		{
			return _claveEsfuerzo;
		}

		public void setClaveEsfuerzo( String claveEsfuerzo )
		{
			_claveEsfuerzo = claveEsfuerzo;
		}

		public String getDescEsfuerzo()
		{
			return _descEsfuerzo;
		}

		public void setDescEsfuerzo( String descEsfuerzo )
		{
			_descEsfuerzo = descEsfuerzo;
		}

		public String getsMensajeEsfuerzo()
		{
			return _sMensajeEsfuerzo;
		}

		public void setsMensajeEsfuerzo( String sMensajeEsfuerzo )
		{
			_sMensajeEsfuerzo = sMensajeEsfuerzo;
		}
	}

	public static class entFrecuenciaViaje implements KvmSerializable
	{
		private static final long serialVersionUID = 0L;
		private String _claveFrecVia;
		private String _descFrecViaj;

		@Override
		public int getPropertyCount()
		{
			return 2;
		}

		@Override
		public Object getProperty( int i )
		{
			if( i == 0 )
			{
				return this.getClaveFrecVia();
			}
			else if( i == 1 )
			{
				return this.getDescFrecViaj();
			}
			else
			{
				return null;
			}
		}

		@Override
		public void setProperty( int i, Object o )
		{
			if( i == 0 )
			{
				this.setClaveFrecVia( (String) o );
			}
			else if( i == 1 )
			{
				this.setDescFrecViaj( (String) o );
			}
		}

		@Override
		public void getPropertyInfo( int i, Hashtable hashtable, PropertyInfo propertyInfo )
		{
			if( i == 0 )
			{
				propertyInfo.name = "ClaveFrecVia";
				propertyInfo.namespace = "http://schemas.datacontract.org/2004/07/wcfCityPremiosCP.Entities.CityPremios";
			}
			else if( i == 1 )
			{
				propertyInfo.name = "DescFrecViaj";
				propertyInfo.namespace = "http://schemas.datacontract.org/2004/07/wcfCityPremiosCP.Entities.CityPremios";
			}
		}

		public String getClaveFrecVia()
		{
			return _claveFrecVia;
		}

		public void setClaveFrecVia( String claveFrecVia )
		{
			_claveFrecVia = claveFrecVia;
		}

		public String getDescFrecViaj()
		{
			return _descFrecViaj;
		}

		public void setDescFrecViaj( String descFrecViaj )
		{
			_descFrecViaj = descFrecViaj;
		}
	}

	public static class entMotivoHospedaje implements KvmSerializable
	{
		private static final long serialVersionUID = 0L;
		private String _claveMotHos;
		private String _descMotHos;

		@Override
		public int getPropertyCount()
		{
			return 2;
		}

		@Override
		public Object getProperty( int i )
		{
			if( i == 0 )
			{
				return this.getClaveMotHos();
			}
			else if( i == 1 )
			{
				return this.getDescMotHos();
			}
			else
			{
				return null;
			}
		}

		@Override
		public void setProperty( int i, Object o )
		{
			if( i == 0 )
			{
				this.setClaveMotHos( (String) o );
			}
			else if( i == 1 )
			{
				this.setDescMotHos( (String) o );
			}
		}

		@Override
		public void getPropertyInfo( int i, Hashtable hashtable, PropertyInfo propertyInfo )
		{
			if( i == 0 )
			{
				propertyInfo.name = "ClaveMotHos";
				propertyInfo.namespace = "http://schemas.datacontract.org/2004/07/wcfCityPremiosCP.Entities.CityPremios";
			}
			else if( i == 1 )
			{
				propertyInfo.name = "DescMotHos";
				propertyInfo.namespace = "http://schemas.datacontract.org/2004/07/wcfCityPremiosCP.Entities.CityPremios";
			}
		}

		public String getClaveMotHos()
		{
			return _claveMotHos;
		}

		public void setClaveMotHos( String claveMotHos )
		{
			_claveMotHos = claveMotHos;
		}

		public String getDescMotHos()
		{
			return _descMotHos;
		}

		public void setDescMotHos( String descMotHos )
		{
			_descMotHos = descMotHos;
		}
	}

	public static class entOrigen implements KvmSerializable
	{
		private static final long serialVersionUID = 0L;
		private String _claveOrigen;
		private String _descOrigen;
		private String _sMensajeOrigen;

		@Override
		public int getPropertyCount()
		{
			return 3;
		}

		@Override
		public Object getProperty( int i )
		{
			if( i == 0 )
			{
				return this.getClaveOrigen();
			}
			else if( i == 1 )
			{
				return this.getDescOrigen();
			}
			else if( i == 2 )
			{
				return this.getsMensajeOrigen();
			}
			else
			{
				return null;
			}
		}

		@Override
		public void setProperty( int i, Object o )
		{
			if( i == 0 )
			{
				this.setClaveOrigen( (String) o );
			}
			else if( i == 1 )
			{
				this.setDescOrigen( (String) o );
			}
			else if( i == 2 )
			{
				this.setsMensajeOrigen( (String) o );
			}
		}

		@Override
		public void getPropertyInfo( int i, Hashtable hashtable, PropertyInfo propertyInfo )
		{
			if( i == 0 )
			{
				propertyInfo.name = "ClaveOrigen";
				propertyInfo.namespace = "http://schemas.datacontract.org/2004/07/wcfCityPremiosCP.Entities.CityPremios";
			}
			else if( i == 1 )
			{
				propertyInfo.name = "DescOrigen";
				propertyInfo.namespace = "http://schemas.datacontract.org/2004/07/wcfCityPremiosCP.Entities.CityPremios";
			}
			else if( i == 2 )
			{
				propertyInfo.name = "sMensajeOrigen";
				propertyInfo.namespace = "http://schemas.datacontract.org/2004/07/wcfCityPremiosCP.Entities.CityPremios";
			}
		}

		public String getClaveOrigen()
		{
			return _claveOrigen;
		}

		public void setClaveOrigen( String claveOrigen )
		{
			_claveOrigen = claveOrigen;
		}

		public String getDescOrigen()
		{
			return _descOrigen;
		}

		public void setDescOrigen( String descOrigen )
		{
			_descOrigen = descOrigen;
		}

		public String getsMensajeOrigen()
		{
			return _sMensajeOrigen;
		}

		public void setsMensajeOrigen( String sMensajeOrigen )
		{
			_sMensajeOrigen = sMensajeOrigen;
		}
	}

	public static class entPrograma implements KvmSerializable
	{
		private static final long serialVersionUID = 0L;
		private String _claveProg;
		private String _descripcionProg;
		private String _sMensajePrograma;

		@Override
		public int getPropertyCount()
		{
			return 3;
		}

		@Override
		public Object getProperty( int i )
		{
			if( i == 0 )
			{
				return this.getClaveProg();
			}
			else if( i == 1 )
			{
				return this.getDescripcionProg();
			}
			else if( i == 2 )
			{
				return this.getsMensajePrograma();
			}
			else
			{
				return null;
			}
		}

		@Override
		public void setProperty( int i, Object o )
		{
			if( i == 0 )
			{
				this.setClaveProg( (String) o );
			}
			else if( i == 1 )
			{
				this.setDescripcionProg( (String) o );
			}
			else if( i == 2 )
			{
				this.setsMensajePrograma( (String) o );
			}
		}

		@Override
		public void getPropertyInfo( int i, Hashtable hashtable, PropertyInfo propertyInfo )
		{
			if( i == 0 )
			{
				propertyInfo.name = "ClaveProg";
				propertyInfo.namespace = "http://schemas.datacontract.org/2004/07/wcfCityPremiosCP.Entities.CityPremios";
			}
			else if( i == 1 )
			{
				propertyInfo.name = "DescripcionProg";
				propertyInfo.namespace = "http://schemas.datacontract.org/2004/07/wcfCityPremiosCP.Entities.CityPremios";
			}
			else if( i == 2 )
			{
				propertyInfo.name = "sMensajePrograma";
				propertyInfo.namespace = "http://schemas.datacontract.org/2004/07/wcfCityPremiosCP.Entities.CityPremios";
			}
		}

		public String getClaveProg()
		{
			return _claveProg;
		}

		public void setClaveProg( String claveProg )
		{
			_claveProg = claveProg;
		}

		public String getDescripcionProg()
		{
			return _descripcionProg;
		}

		public void setDescripcionProg( String descripcionProg )
		{
			_descripcionProg = descripcionProg;
		}

		public String getsMensajePrograma()
		{
			return _sMensajePrograma;
		}

		public void setsMensajePrograma( String sMensajePrograma )
		{
			_sMensajePrograma = sMensajePrograma;
		}
	}

	public static class entSocioCP implements KvmSerializable
	{
		private static final long serialVersionUID = 0L;
		private String _apMaternoSocio;
		private String _apPaternoSocio;
		private entAplicacion _aplicacion;
		private String _aplicacionMailSocio;
		private String _calleEmpresa;
		private String _calleSocio;
		private String _celularSocio;
		private String _ciudadEmpresa;
		private String _ciudadSocio;
		private String _claveIATAEmpresa;
		private String _codigoPostalEmpresa;
		private String _codigoPostalSocio;
		private String _coloniaEmpresa;
		private String _coloniaSocio;
		private String _comentariosSocio;
		private String _correoDestinoSocio;
		private String _correoEmailSocio;
		private String _emailEmpresa;
		private entEsfuerzo _esfuerzoSocio;
		private String _estadoCivil;
		private String _estadoEmpresa;
		private String _estadoSocio;
		private String _faxEmpresa;
		private Date _fechaAltaSocio;
		private Date _fechaModificaSocio;
		private Date _fechaNacSocio;
		private entFrecuenciaViaje _frecuenciaViaje;
		private String _isMemberGold;
		private entMotivoHospedaje _motivoHospedaje;
		private String _noExtEmpresa;
		private String _noExteriorSocio;
		private String _noIntEmpresa;
		private String _noInteriorSocio;
		private String _noSocioCP;
		private String _nombreSocio;
		private entOrigen _origenSocio;
		private String _paisEmpresa;
		private String _paisNacSocio;
		private String _paisSocio;
		private String _password;
		private entPrograma _programaSocio;
		private String _puestoEmpresa;
		private String _rFCEmpresa;
		private String _razonSocialEmpresa;
		private String _rubroEmpresa;
		private String _sexoSocio;
		private String _telefonoEmpresa;
		private String _telefonoSocio;
		private String _tituloSocio;
		private String _usuarioAltaSocio;
		private String _usuarioModificaSocio;
		private String _sMensajeAfiliacion;

		public entSocioCP()
		{
			_aplicacion = new entAplicacion();
			_esfuerzoSocio = new entEsfuerzo();
			_frecuenciaViaje = new entFrecuenciaViaje();
			_motivoHospedaje = new entMotivoHospedaje();
			_origenSocio = new entOrigen();
			_programaSocio = new entPrograma();

			_fechaAltaSocio = new Date( System.currentTimeMillis() );
			_fechaModificaSocio = new Date( System.currentTimeMillis() );
			_fechaNacSocio = new Date( System.currentTimeMillis() );

			_isMemberGold = "false";
		}

		@Override
		public int getPropertyCount()
		{
			return 52;
		}

		@Override
		public Object getProperty( int i )
		{
			if( i == 0 )
			{
				return this.getApMaternoSocio();
			}
			else if( i == 1 )
			{
				return this.getApPaternoSocio();
			}
			else if( i == 2 )
			{
				return this.getAplicacion();
			}
			else if( i == 3 )
			{
				return this.getAplicacionMailSocio();
			}
			else if( i == 4 )
			{
				return this.getCalleEmpresa();
			}
			else if( i == 5 )
			{
				return this.getCalleSocio();
			}
			else if( i == 6 )
			{
				return this.getCelularSocio();
			}
			else if( i == 7 )
			{
				return this.getCiudadEmpresa();
			}
			else if( i == 8 )
			{
				return this.getCiudadSocio();
			}
			else if( i == 9 )
			{
				return this.getClaveIATAEmpresa();
			}
			else if( i == 10 )
			{
				return this.getCodigoPostalEmpresa();
			}
			else if( i == 11 )
			{
				return this.getCodigoPostalSocio();
			}
			else if( i == 12 )
			{
				return this.getColoniaEmpresa();
			}
			else if( i == 13 )
			{
				return this.getColoniaSocio();
			}
			else if( i == 14 )
			{
				return this.getComentariosSocio();
			}
			else if( i == 15 )
			{
				return this.getCorreoDestinoSocio();
			}
			else if( i == 16 )
			{
				return this.getCorreoEmailSocio();
			}
			else if( i == 17 )
			{
				return this.getEmailEmpresa();
			}
			else if( i == 18 )
			{
				return this.getEsfuerzoSocio();
			}
			else if( i == 19 )
			{
				return this.getEstadoCivil();
			}
			else if( i == 20 )
			{
				return this.getEstadoEmpresa();
			}
			else if( i == 21 )
			{
				return this.getEstadoSocio();
			}
			else if( i == 22 )
			{
				return this.getFaxEmpresa();
			}
			else if( i == 23 )
			{
				return getSOAPDateString( this.getFechaAltaSocio() );
			}
			else if( i == 24 )
			{
				return getSOAPDateString( this.getFechaModificaSocio() );
			}
			else if( i == 25 )
			{
				return getSOAPDateString( this.getFechaNacSocio() );
			}
			else if( i == 26 )
			{
				return this.getFrecuenciaViaje();
			}
			else if( i == 27 )
			{
				return this.getIsMemberGold();
			}
			else if( i == 28 )
			{
				return this.getMotivoHospedaje();
			}
			else if( i == 29 )
			{
				return this.getNoExtEmpresa();
			}
			else if( i == 30 )
			{
				return this.getNoExteriorSocio();
			}
			else if( i == 31 )
			{
				return this.getNoIntEmpresa();
			}
			else if( i == 32 )
			{
				return this.getNoInteriorSocio();
			}
			else if( i == 33 )
			{
				return this.getNoSocioCP();
			}
			else if( i == 34 )
			{
				return this.getNombreSocio();
			}
			else if( i == 35 )
			{
				return this.getOrigenSocio();
			}
			else if( i == 36 )
			{
				return this.getPaisEmpresa();
			}
			else if( i == 37 )
			{
				return this.getPaisNacSocio();
			}
			else if( i == 38 )
			{
				return this.getPaisSocio();
			}
			else if( i == 39 )
			{
				return this.getPassword();
			}
			else if( i == 40 )
			{
				return this.getProgramaSocio();
			}
			else if( i == 41 )
			{
				return this.getPuestoEmpresa();
			}
			else if( i == 42 )
			{
				return this.getRFCEmpresa();
			}
			else if( i == 43 )
			{
				return this.getRazonSocialEmpresa();
			}
			else if( i == 44 )
			{
				return this.getRubroEmpresa();
			}
			else if( i == 45 )
			{
				return this.getSexoSocio();
			}
			else if( i == 46 )
			{
				return this.getTelefonoEmpresa();
			}
			else if( i == 47 )
			{
				return this.getTelefonoSocio();
			}
			else if( i == 48 )
			{
				return this.getTituloSocio();
			}
			else if( i == 49 )
			{
				return this.getUsuarioAltaSocio();
			}
			else if( i == 50 )
			{
				return this.getUsuarioModificaSocio();
			}
			else if( i == 51 )
			{
				return this.getsMensajeAfiliacion();
			}
			else
			{
				return null;
			}
		}

		@Override
		public void setProperty( int i, Object o )
		{
			if( i == 0 )
			{
				this.setApMaternoSocio( (String) o );
			}
			else if( i == 1 )
			{
				this.setApPaternoSocio( (String) o );
			}
			else if( i == 2 )
			{
				this.setAplicacion( (entAplicacion) o );
			}
			else if( i == 3 )
			{
				this.setAplicacionMailSocio( (String) o );
			}
			else if( i == 4 )
			{
				this.setCalleEmpresa( (String) o );
			}
			else if( i == 5 )
			{
				this.setCalleSocio( (String) o );
			}
			else if( i == 6 )
			{
				this.setCelularSocio( (String) o );
			}
			else if( i == 7 )
			{
				this.setCiudadEmpresa( (String) o );
			}
			else if( i == 8 )
			{
				this.setCiudadSocio( (String) o );
			}
			else if( i == 9 )
			{
				this.setClaveIATAEmpresa( (String) o );
			}
			else if( i == 10 )
			{
				this.setCodigoPostalEmpresa( (String) o );
			}
			else if( i == 11 )
			{
				this.setCodigoPostalSocio( (String) o );
			}
			else if( i == 12 )
			{
				this.setColoniaEmpresa( (String) o );
			}
			else if( i == 13 )
			{
				this.setColoniaSocio( (String) o );
			}
			else if( i == 14 )
			{
				this.setComentariosSocio( (String) o );
			}
			else if( i == 15 )
			{
				this.setCorreoDestinoSocio( (String) o );
			}
			else if( i == 16 )
			{
				this.setCorreoEmailSocio( (String) o );
			}
			else if( i == 17 )
			{
				this.setEmailEmpresa( (String) o );
			}
			else if( i == 18 )
			{
				this.setEsfuerzoSocio( (entEsfuerzo) o );
			}
			else if( i == 19 )
			{
				this.setEstadoCivil( (String) o );
			}
			else if( i == 20 )
			{
				this.setEstadoEmpresa( (String) o );
			}
			else if( i == 21 )
			{
				this.setEstadoSocio( (String) o );
			}
			else if( i == 22 )
			{
				this.setFaxEmpresa( (String) o );
			}
			else if( i == 23 )
			{
				this.setFechaAltaSocio( getDateFromSOAPString( (String) o ) );
			}
			else if( i == 24 )
			{
				this.setFechaModificaSocio( getDateFromSOAPString( (String) o ) );
			}
			else if( i == 25 )
			{
				this.setFechaNacSocio( getDateFromSOAPString( (String) o ) );
			}
			else if( i == 26 )
			{
				this.setFrecuenciaViaje( (entFrecuenciaViaje) o );
			}
			else if( i == 27 )
			{
				this.setIsMemberGold( (String) o );
			}
			else if( i == 28 )
			{
				this.setMotivoHospedaje( (entMotivoHospedaje) o );
			}
			else if( i == 29 )
			{
				this.setNoExtEmpresa( (String) o );
			}
			else if( i == 30 )
			{
				this.setNoExteriorSocio( (String) o );
			}
			else if( i == 31 )
			{
				this.setNoIntEmpresa( (String) o );
			}
			else if( i == 32 )
			{
				this.setNoInteriorSocio( (String) o );
			}
			else if( i == 33 )
			{
				this.setNoSocioCP( (String) o );
			}
			else if( i == 34 )
			{
				this.setNombreSocio( (String) o );
			}
			else if( i == 35 )
			{
				this.setOrigenSocio( (entOrigen) o );
			}
			else if( i == 36 )
			{
				this.setPaisEmpresa( (String) o );
			}
			else if( i == 37 )
			{
				this.setPaisNacSocio( (String) o );
			}
			else if( i == 38 )
			{
				this.setPaisSocio( (String) o );
			}
			else if( i == 39 )
			{
				this.setPassword( (String) o );
			}
			else if( i == 40 )
			{
				this.setProgramaSocio( (entPrograma) o );
			}
			else if( i == 41 )
			{
				this.setPuestoEmpresa( (String) o );
			}
			else if( i == 42 )
			{
				this.setRFCEmpresa( (String) o );
			}
			else if( i == 43 )
			{
				this.setRazonSocialEmpresa( (String) o );
			}
			else if( i == 44 )
			{
				this.setRubroEmpresa( (String) o );
			}
			else if( i == 45 )
			{
				this.setSexoSocio( (String) o );
			}
			else if( i == 46 )
			{
				this.setTelefonoEmpresa( (String) o );
			}
			else if( i == 47 )
			{
				this.setTelefonoSocio( (String) o );
			}
			else if( i == 48 )
			{
				this.setTituloSocio( (String) o );
			}
			else if( i == 49 )
			{
				this.setUsuarioAltaSocio( (String) o );
			}
			else if( i == 50 )
			{
				this.setUsuarioModificaSocio( (String) o );
			}
			else if( i == 51 )
			{
				this.setsMensajeAfiliacion( (String) o );
			}
		}

		@Override
		public void getPropertyInfo( int i, Hashtable hashtable, PropertyInfo propertyInfo )
		{
			if( i == 0 )
			{
				propertyInfo.name = "ApMaternoSocioField";
				propertyInfo.namespace = "http://schemas.datacontract.org/2004/07/wcfCityPremiosCP.Entities.CityPremios";
			}
			else if( i == 1 )
			{
				propertyInfo.name = "ApPaternoSocio";
				propertyInfo.namespace = "http://schemas.datacontract.org/2004/07/wcfCityPremiosCP.Entities.CityPremios";
			}
			else if( i == 2 )
			{
				propertyInfo.name = "Aplicacion";
				propertyInfo.namespace = "http://schemas.datacontract.org/2004/07/wcfCityPremiosCP.Entities.CityPremios";
			}
			else if( i == 3 )
			{
				propertyInfo.name = "AplicacionMailSocio";
				propertyInfo.namespace = "http://schemas.datacontract.org/2004/07/wcfCityPremiosCP.Entities.CityPremios";
			}
			else if( i == 4 )
			{
				propertyInfo.name = "CalleEmpresa";
				propertyInfo.namespace = "http://schemas.datacontract.org/2004/07/wcfCityPremiosCP.Entities.CityPremios";
			}
			else if( i == 5 )
			{
				propertyInfo.name = "CalleSocio";
				propertyInfo.namespace = "http://schemas.datacontract.org/2004/07/wcfCityPremiosCP.Entities.CityPremios";
			}
			else if( i == 6 )
			{
				propertyInfo.name = "CelularSocio";
				propertyInfo.namespace = "http://schemas.datacontract.org/2004/07/wcfCityPremiosCP.Entities.CityPremios";
			}
			else if( i == 7 )
			{
				propertyInfo.name = "CiudadEmpresa";
				propertyInfo.namespace = "http://schemas.datacontract.org/2004/07/wcfCityPremiosCP.Entities.CityPremios";
			}
			else if( i == 8 )
			{
				propertyInfo.name = "CiudadSocio";
				propertyInfo.namespace = "http://schemas.datacontract.org/2004/07/wcfCityPremiosCP.Entities.CityPremios";
			}
			else if( i == 9 )
			{
				propertyInfo.name = "ClaveIATAEmpresa";
				propertyInfo.namespace = "http://schemas.datacontract.org/2004/07/wcfCityPremiosCP.Entities.CityPremios";
			}
			else if( i == 10 )
			{
				propertyInfo.name = "CodigoPostalEmpresa";
				propertyInfo.namespace = "http://schemas.datacontract.org/2004/07/wcfCityPremiosCP.Entities.CityPremios";
			}
			else if( i == 11 )
			{
				propertyInfo.name = "CodigoPostalSocio";
				propertyInfo.namespace = "http://schemas.datacontract.org/2004/07/wcfCityPremiosCP.Entities.CityPremios";
			}
			else if( i == 12 )
			{
				propertyInfo.name = "ColoniaEmpresa";
				propertyInfo.namespace = "http://schemas.datacontract.org/2004/07/wcfCityPremiosCP.Entities.CityPremios";
			}
			else if( i == 13 )
			{
				propertyInfo.name = "ColoniaSocio";
				propertyInfo.namespace = "http://schemas.datacontract.org/2004/07/wcfCityPremiosCP.Entities.CityPremios";
			}
			else if( i == 14 )
			{
				propertyInfo.name = "ComentariosSocio";
				propertyInfo.namespace = "http://schemas.datacontract.org/2004/07/wcfCityPremiosCP.Entities.CityPremios";
			}
			else if( i == 15 )
			{
				propertyInfo.name = "CorreoDestinoSocio";
				propertyInfo.namespace = "http://schemas.datacontract.org/2004/07/wcfCityPremiosCP.Entities.CityPremios";
			}
			else if( i == 16 )
			{
				propertyInfo.name = "CorreoEmailSocio";
				propertyInfo.namespace = "http://schemas.datacontract.org/2004/07/wcfCityPremiosCP.Entities.CityPremios";
			}
			else if( i == 17 )
			{
				propertyInfo.name = "EmailEmpresa";
				propertyInfo.namespace = "http://schemas.datacontract.org/2004/07/wcfCityPremiosCP.Entities.CityPremios";
			}
			else if( i == 18 )
			{
				propertyInfo.name = "EsfuerzoSocio";
				propertyInfo.namespace = "http://schemas.datacontract.org/2004/07/wcfCityPremiosCP.Entities.CityPremios";
			}
			else if( i == 19 )
			{
				propertyInfo.name = "EstadoCivil";
				propertyInfo.namespace = "http://schemas.datacontract.org/2004/07/wcfCityPremiosCP.Entities.CityPremios";
			}
			else if( i == 20 )
			{
				propertyInfo.name = "EstadoEmpresa";
				propertyInfo.namespace = "http://schemas.datacontract.org/2004/07/wcfCityPremiosCP.Entities.CityPremios";
			}
			else if( i == 21 )
			{
				propertyInfo.name = "EstadoSocio";
				propertyInfo.namespace = "http://schemas.datacontract.org/2004/07/wcfCityPremiosCP.Entities.CityPremios";
			}
			else if( i == 22 )
			{
				propertyInfo.name = "FaxEmpresa";
				propertyInfo.namespace = "http://schemas.datacontract.org/2004/07/wcfCityPremiosCP.Entities.CityPremios";
			}
			else if( i == 23 )
			{
				propertyInfo.name = "FechaAltaSocio";
				propertyInfo.namespace = "http://schemas.datacontract.org/2004/07/wcfCityPremiosCP.Entities.CityPremios";
			}
			else if( i == 24 )
			{
				propertyInfo.name = "FechaModificaSocio";
				propertyInfo.namespace = "http://schemas.datacontract.org/2004/07/wcfCityPremiosCP.Entities.CityPremios";
			}
			else if( i == 25 )
			{
				propertyInfo.name = "FechaNacSocio";
				propertyInfo.namespace = "http://schemas.datacontract.org/2004/07/wcfCityPremiosCP.Entities.CityPremios";
			}
			else if( i == 26 )
			{
				propertyInfo.name = "FrecuenciaViaje";
				propertyInfo.namespace = "http://schemas.datacontract.org/2004/07/wcfCityPremiosCP.Entities.CityPremios";
			}
			else if( i == 27 )
			{
				propertyInfo.name = "IsMemberGold";
				propertyInfo.namespace = "http://schemas.datacontract.org/2004/07/wcfCityPremiosCP.Entities.CityPremios";
			}
			else if( i == 28 )
			{
				propertyInfo.name = "MotivoHospedaje";
				propertyInfo.namespace = "http://schemas.datacontract.org/2004/07/wcfCityPremiosCP.Entities.CityPremios";
			}
			else if( i == 29 )
			{
				propertyInfo.name = "NoExtEmpresa";
				propertyInfo.namespace = "http://schemas.datacontract.org/2004/07/wcfCityPremiosCP.Entities.CityPremios";
			}
			else if( i == 30 )
			{
				propertyInfo.name = "NoExteriorSocio";
				propertyInfo.namespace = "http://schemas.datacontract.org/2004/07/wcfCityPremiosCP.Entities.CityPremios";
			}
			else if( i == 31 )
			{
				propertyInfo.name = "NoIntEmpresa";
				propertyInfo.namespace = "http://schemas.datacontract.org/2004/07/wcfCityPremiosCP.Entities.CityPremios";
			}
			else if( i == 32 )
			{
				propertyInfo.name = "NoInteriorSocio";
				propertyInfo.namespace = "http://schemas.datacontract.org/2004/07/wcfCityPremiosCP.Entities.CityPremios";
			}
			else if( i == 33 )
			{
				propertyInfo.name = "NoSocioCP";
				propertyInfo.namespace = "http://schemas.datacontract.org/2004/07/wcfCityPremiosCP.Entities.CityPremios";
			}
			else if( i == 34 )
			{
				propertyInfo.name = "NombreSocio";
				propertyInfo.namespace = "http://schemas.datacontract.org/2004/07/wcfCityPremiosCP.Entities.CityPremios";
			}
			else if( i == 35 )
			{
				propertyInfo.name = "OrigenSocio";
				propertyInfo.namespace = "http://schemas.datacontract.org/2004/07/wcfCityPremiosCP.Entities.CityPremios";
			}
			else if( i == 36 )
			{
				propertyInfo.name = "PaisEmpresa";
				propertyInfo.namespace = "http://schemas.datacontract.org/2004/07/wcfCityPremiosCP.Entities.CityPremios";
			}
			else if( i == 37 )
			{
				propertyInfo.name = "PaisNacSocio";
				propertyInfo.namespace = "http://schemas.datacontract.org/2004/07/wcfCityPremiosCP.Entities.CityPremios";
			}
			else if( i == 38 )
			{
				propertyInfo.name = "PaisSocio";
				propertyInfo.namespace = "http://schemas.datacontract.org/2004/07/wcfCityPremiosCP.Entities.CityPremios";
			}
			else if( i == 39 )
			{
				propertyInfo.name = "Password";
				propertyInfo.namespace = "http://schemas.datacontract.org/2004/07/wcfCityPremiosCP.Entities.CityPremios";
			}
			else if( i == 40 )
			{
				propertyInfo.name = "ProgramaSocio";
				propertyInfo.namespace = "http://schemas.datacontract.org/2004/07/wcfCityPremiosCP.Entities.CityPremios";
			}
			else if( i == 41 )
			{
				propertyInfo.name = "PuestoEmpresa";
				propertyInfo.namespace = "http://schemas.datacontract.org/2004/07/wcfCityPremiosCP.Entities.CityPremios";
			}
			else if( i == 42 )
			{
				propertyInfo.name = "RFCEmpresa";
				propertyInfo.namespace = "http://schemas.datacontract.org/2004/07/wcfCityPremiosCP.Entities.CityPremios";
			}
			else if( i == 43 )
			{
				propertyInfo.name = "RazonSocialEmpresa";
				propertyInfo.namespace = "http://schemas.datacontract.org/2004/07/wcfCityPremiosCP.Entities.CityPremios";
			}
			else if( i == 44 )
			{
				propertyInfo.name = "RubroEmpresa";
				propertyInfo.namespace = "http://schemas.datacontract.org/2004/07/wcfCityPremiosCP.Entities.CityPremios";
			}
			else if( i == 45 )
			{
				propertyInfo.name = "SexoSocio";
				propertyInfo.namespace = "http://schemas.datacontract.org/2004/07/wcfCityPremiosCP.Entities.CityPremios";
			}
			else if( i == 46 )
			{
				propertyInfo.name = "TelefonoEmpresa";
				propertyInfo.namespace = "http://schemas.datacontract.org/2004/07/wcfCityPremiosCP.Entities.CityPremios";
			}
			else if( i == 47 )
			{
				propertyInfo.name = "TelefonoSocio";
				propertyInfo.namespace = "http://schemas.datacontract.org/2004/07/wcfCityPremiosCP.Entities.CityPremios";
			}
			else if( i == 48 )
			{
				propertyInfo.name = "TituloSocio";
				propertyInfo.namespace = "http://schemas.datacontract.org/2004/07/wcfCityPremiosCP.Entities.CityPremios";
			}
			else if( i == 49 )
			{
				propertyInfo.name = "UsuarioAltaSocio";
				propertyInfo.namespace = "http://schemas.datacontract.org/2004/07/wcfCityPremiosCP.Entities.CityPremios";
			}
			else if( i == 50 )
			{
				propertyInfo.name = "UsuarioModificaSocio";
				propertyInfo.namespace = "http://schemas.datacontract.org/2004/07/wcfCityPremiosCP.Entities.CityPremios";
			}
			else if( i == 51 )
			{
				propertyInfo.name = "sMensajeAfiliacion";
				propertyInfo.namespace = "http://schemas.datacontract.org/2004/07/wcfCityPremiosCP.Entities.CityPremios";
			}
		}

		public String getApMaternoSocio()
		{
			return _apMaternoSocio;
		}

		public void setApMaternoSocio( String apMaternoSocio )
		{
			_apMaternoSocio = apMaternoSocio;
		}

		public String getApPaternoSocio()
		{
			return _apPaternoSocio;
		}

		public void setApPaternoSocio( String apPaternoSocio )
		{
			_apPaternoSocio = apPaternoSocio;
		}

		public entAplicacion getAplicacion()
		{
			return _aplicacion;
		}

		public void setAplicacion( entAplicacion aplicacion )
		{
			_aplicacion = aplicacion;
		}

		public String getAplicacionMailSocio()
		{
			return _aplicacionMailSocio;
		}

		public void setAplicacionMailSocio( String aplicacionMailSocio )
		{
			_aplicacionMailSocio = aplicacionMailSocio;
		}

		public String getCalleEmpresa()
		{
			return _calleEmpresa;
		}

		public void setCalleEmpresa( String calleEmpresa )
		{
			_calleEmpresa = calleEmpresa;
		}

		public String getCalleSocio()
		{
			return _calleSocio;
		}

		public void setCalleSocio( String calleSocio )
		{
			_calleSocio = calleSocio;
		}

		public String getCelularSocio()
		{
			return _celularSocio;
		}

		public void setCelularSocio( String celularSocio )
		{
			_celularSocio = celularSocio;
		}

		public String getCiudadEmpresa()
		{
			return _ciudadEmpresa;
		}

		public void setCiudadEmpresa( String ciudadEmpresa )
		{
			_ciudadEmpresa = ciudadEmpresa;
		}

		public String getCiudadSocio()
		{
			return _ciudadSocio;
		}

		public void setCiudadSocio( String ciudadSocio )
		{
			_ciudadSocio = ciudadSocio;
		}

		public String getClaveIATAEmpresa()
		{
			return _claveIATAEmpresa;
		}

		public void setClaveIATAEmpresa( String claveIATAEmpresa )
		{
			_claveIATAEmpresa = claveIATAEmpresa;
		}

		public String getCodigoPostalEmpresa()
		{
			return _codigoPostalEmpresa;
		}

		public void setCodigoPostalEmpresa( String codigoPostalEmpresa )
		{
			_codigoPostalEmpresa = codigoPostalEmpresa;
		}

		public String getCodigoPostalSocio()
		{
			return _codigoPostalSocio;
		}

		public void setCodigoPostalSocio( String codigoPostalSocio )
		{
			_codigoPostalSocio = codigoPostalSocio;
		}

		public String getColoniaEmpresa()
		{
			return _coloniaEmpresa;
		}

		public void setColoniaEmpresa( String coloniaEmpresa )
		{
			_coloniaEmpresa = coloniaEmpresa;
		}

		public String getColoniaSocio()
		{
			return _coloniaSocio;
		}

		public void setColoniaSocio( String coloniaSocio )
		{
			_coloniaSocio = coloniaSocio;
		}

		public String getComentariosSocio()
		{
			return _comentariosSocio;
		}

		public void setComentariosSocio( String comentariosSocio )
		{
			_comentariosSocio = comentariosSocio;
		}

		public String getCorreoDestinoSocio()
		{
			return _correoDestinoSocio;
		}

		public void setCorreoDestinoSocio( String correoDestinoSocio )
		{
			_correoDestinoSocio = correoDestinoSocio;
		}

		public String getCorreoEmailSocio()
		{
			return _correoEmailSocio;
		}

		public void setCorreoEmailSocio( String correoEmailSocio )
		{
			_correoEmailSocio = correoEmailSocio;
		}

		public String getEmailEmpresa()
		{
			return _emailEmpresa;
		}

		public void setEmailEmpresa( String emailEmpresa )
		{
			_emailEmpresa = emailEmpresa;
		}

		public entEsfuerzo getEsfuerzoSocio()
		{
			return _esfuerzoSocio;
		}

		public void setEsfuerzoSocio( entEsfuerzo esfuerzoSocio )
		{
			_esfuerzoSocio = esfuerzoSocio;
		}

		public String getEstadoCivil()
		{
			return _estadoCivil;
		}

		public void setEstadoCivil( String estadoCivil )
		{
			_estadoCivil = estadoCivil;
		}

		public String getEstadoEmpresa()
		{
			return _estadoEmpresa;
		}

		public void setEstadoEmpresa( String estadoEmpresa )
		{
			_estadoEmpresa = estadoEmpresa;
		}

		public String getEstadoSocio()
		{
			return _estadoSocio;
		}

		public void setEstadoSocio( String estadoSocio )
		{
			_estadoSocio = estadoSocio;
		}

		public String getFaxEmpresa()
		{
			return _faxEmpresa;
		}

		public void setFaxEmpresa( String faxEmpresa )
		{
			_faxEmpresa = faxEmpresa;
		}

		public Date getFechaAltaSocio()
		{
			return _fechaAltaSocio;
		}

		public void setFechaAltaSocio( Date fechaAltaSocio )
		{
			_fechaAltaSocio = fechaAltaSocio;
		}

		public Date getFechaModificaSocio()
		{
			return _fechaModificaSocio;
		}

		public void setFechaModificaSocio( Date fechaModificaSocio )
		{
			_fechaModificaSocio = fechaModificaSocio;
		}

		public Date getFechaNacSocio()
		{
			return _fechaNacSocio;
		}

		public void setFechaNacSocio( Date fechaNacSocio )
		{
			_fechaNacSocio = fechaNacSocio;
		}

		public entFrecuenciaViaje getFrecuenciaViaje()
		{
			return _frecuenciaViaje;
		}

		public void setFrecuenciaViaje( entFrecuenciaViaje frecuenciaViaje )
		{
			_frecuenciaViaje = frecuenciaViaje;
		}

		public String getIsMemberGold()
		{
			return _isMemberGold;
		}

		public void setIsMemberGold( String isMemberGold )
		{
			_isMemberGold = isMemberGold;
		}

		public entMotivoHospedaje getMotivoHospedaje()
		{
			return _motivoHospedaje;
		}

		public void setMotivoHospedaje( entMotivoHospedaje motivoHospedaje )
		{
			_motivoHospedaje = motivoHospedaje;
		}

		public String getNoExtEmpresa()
		{
			return _noExtEmpresa;
		}

		public void setNoExtEmpresa( String noExtEmpresa )
		{
			_noExtEmpresa = noExtEmpresa;
		}

		public String getNoExteriorSocio()
		{
			return _noExteriorSocio;
		}

		public void setNoExteriorSocio( String noExteriorSocio )
		{
			_noExteriorSocio = noExteriorSocio;
		}

		public String getNoIntEmpresa()
		{
			return _noIntEmpresa;
		}

		public void setNoIntEmpresa( String noIntEmpresa )
		{
			_noIntEmpresa = noIntEmpresa;
		}

		public String getNoInteriorSocio()
		{
			return _noInteriorSocio;
		}

		public void setNoInteriorSocio( String noInteriorSocio )
		{
			_noInteriorSocio = noInteriorSocio;
		}

		public String getNoSocioCP()
		{
			return _noSocioCP;
		}

		public void setNoSocioCP( String noSocioCP )
		{
			_noSocioCP = noSocioCP;
		}

		public String getNombreSocio()
		{
			return _nombreSocio;
		}

		public void setNombreSocio( String nombreSocio )
		{
			_nombreSocio = nombreSocio;
		}

		public entOrigen getOrigenSocio()
		{
			return _origenSocio;
		}

		public void setOrigenSocio( entOrigen origenSocio )
		{
			_origenSocio = origenSocio;
		}

		public String getPaisEmpresa()
		{
			return _paisEmpresa;
		}

		public void setPaisEmpresa( String paisEmpresa )
		{
			_paisEmpresa = paisEmpresa;
		}

		public String getPaisNacSocio()
		{
			return _paisNacSocio;
		}

		public void setPaisNacSocio( String paisNacSocio )
		{
			_paisNacSocio = paisNacSocio;
		}

		public String getPaisSocio()
		{
			return _paisSocio;
		}

		public void setPaisSocio( String paisSocio )
		{
			_paisSocio = paisSocio;
		}

		public String getPassword()
		{
			return _password;
		}

		public void setPassword( String password )
		{
			_password = password;
		}

		public entPrograma getProgramaSocio()
		{
			return _programaSocio;
		}

		public void setProgramaSocio( entPrograma programaSocio )
		{
			_programaSocio = programaSocio;
		}

		public String getPuestoEmpresa()
		{
			return _puestoEmpresa;
		}

		public void setPuestoEmpresa( String puestoEmpresa )
		{
			_puestoEmpresa = puestoEmpresa;
		}

		public String getRFCEmpresa()
		{
			return _rFCEmpresa;
		}

		public void setRFCEmpresa( String rFCEmpresa )
		{
			_rFCEmpresa = rFCEmpresa;
		}

		public String getRazonSocialEmpresa()
		{
			return _razonSocialEmpresa;
		}

		public void setRazonSocialEmpresa( String razonSocialEmpresa )
		{
			_razonSocialEmpresa = razonSocialEmpresa;
		}

		public String getRubroEmpresa()
		{
			return _rubroEmpresa;
		}

		public void setRubroEmpresa( String rubroEmpresa )
		{
			_rubroEmpresa = rubroEmpresa;
		}

		public String getSexoSocio()
		{
			return _sexoSocio;
		}

		public void setSexoSocio( String sexoSocio )
		{
			_sexoSocio = sexoSocio;
		}

		public String getTelefonoEmpresa()
		{
			return _telefonoEmpresa;
		}

		public void setTelefonoEmpresa( String telefonoEmpresa )
		{
			_telefonoEmpresa = telefonoEmpresa;
		}

		public String getTelefonoSocio()
		{
			return _telefonoSocio;
		}

		public void setTelefonoSocio( String telefonoSocio )
		{
			_telefonoSocio = telefonoSocio;
		}

		public String getTituloSocio()
		{
			return _tituloSocio;
		}

		public void setTituloSocio( String tituloSocio )
		{
			_tituloSocio = tituloSocio;
		}

		public String getUsuarioAltaSocio()
		{
			return _usuarioAltaSocio;
		}

		public void setUsuarioAltaSocio( String usuarioAltaSocio )
		{
			_usuarioAltaSocio = usuarioAltaSocio;
		}

		public String getUsuarioModificaSocio()
		{
			return _usuarioModificaSocio;
		}

		public void setUsuarioModificaSocio( String usuarioModificaSocio )
		{
			_usuarioModificaSocio = usuarioModificaSocio;
		}

		public String getsMensajeAfiliacion()
		{
			return _sMensajeAfiliacion;
		}

		public void setsMensajeAfiliacion( String sMensajeAfiliacion )
		{
			_sMensajeAfiliacion = sMensajeAfiliacion;
		}


	}

	public static class entCatePremios implements KvmSerializable, Serializable
	{
		private static final long serialVersionUID = 0L;
		private String _claveCategoria;
		private String _descCategoria;
		private String _leyendaCategoria;
		private String _urlLogoCategoria;
		private String _urlPaginaCategoria;
		private String _sMensajeCategoria;

		@Override
		public int getPropertyCount()
		{
			return 6;
		}

		@Override
		public Object getProperty( int i )
		{
			if( i == 0 )
				return this.getClaveCategoria();
			else if( i == 1 )
				return this.getDescCategoria();
			else if( i == 2 )
				return this.getLeyendaCategoria();
			else if( i == 3 )
				return this.getUrlLogoCategoria();
			else if( i == 4 )
				return this.getUrlPaginaCategoria();
			else if( i == 5 )
				return this.getsMensajeCategoria();
			else return null;
		}

		@Override
		public void setProperty( int i, Object o )
		{
			if( i == 0 )
				this.setClaveCategoria( (String)o );
			else if( i == 1 )
				this.setDescCategoria( (String)o );
			else if( i == 2 )
				this.setLeyendaCategoria( (String)o );
			else if( i == 3 )
				this.setUrlLogoCategoria( (String)o );
			else if( i == 4 )
				this.setUrlPaginaCategoria( (String)o );
			else if( i == 5 )
				this.setsMensajeCategoria( (String)o );
		}

		@Override
		public void getPropertyInfo( int i, Hashtable hashtable, PropertyInfo propertyInfo )
		{
			if( i == 0 )
			{
				propertyInfo.name = "ClaveCategoria";
				propertyInfo.namespace = "http://schemas.datacontract.org/2004/07/wcfCityPremiosCP.Entities.CityPremios";
			}
			else if( i == 1 )
			{
				propertyInfo.name = "DescCategoria";
				propertyInfo.namespace = "http://schemas.datacontract.org/2004/07/wcfCityPremiosCP.Entities.CityPremios";
			}
			else if( i == 2 )
			{
				propertyInfo.name = "LeyendaCategoria";
				propertyInfo.namespace = "http://schemas.datacontract.org/2004/07/wcfCityPremiosCP.Entities.CityPremios";
			}
			else if( i == 3 )
			{
				propertyInfo.name = "UrlLogoCategoria";
				propertyInfo.namespace = "http://schemas.datacontract.org/2004/07/wcfCityPremiosCP.Entities.CityPremios";
			}
			else if( i == 4 )
			{
				propertyInfo.name = "UrlPaginaCategoria";
				propertyInfo.namespace = "http://schemas.datacontract.org/2004/07/wcfCityPremiosCP.Entities.CityPremios";
			}
			else if( i == 5 )
			{
				propertyInfo.name = "sMensajeCategoria";
				propertyInfo.namespace = "http://schemas.datacontract.org/2004/07/wcfCityPremiosCP.Entities.CityPremios";
			}
		}

		public String getClaveCategoria()
		{
			return _claveCategoria;
		}

		public void setClaveCategoria( String claveCategoria )
		{
			_claveCategoria = claveCategoria;
		}

		public String getDescCategoria()
		{
			return _descCategoria;
		}

		public void setDescCategoria( String descCategoria )
		{
			_descCategoria = descCategoria;
		}

		public String getLeyendaCategoria()
		{
			return _leyendaCategoria;
		}

		public void setLeyendaCategoria( String leyendaCategoria )
		{
			_leyendaCategoria = leyendaCategoria;
		}

		public String getUrlLogoCategoria()
		{
			return _urlLogoCategoria;
		}

		public void setUrlLogoCategoria( String urlLogoCategoria )
		{
			_urlLogoCategoria = urlLogoCategoria;
		}

		public String getUrlPaginaCategoria()
		{
			return _urlPaginaCategoria;
		}

		public void setUrlPaginaCategoria( String urlPaginaCategoria )
		{
			_urlPaginaCategoria = urlPaginaCategoria;
		}

		public String getsMensajeCategoria()
		{
			return _sMensajeCategoria;
		}

		public void setsMensajeCategoria( String sMensajeCategoria )
		{
			_sMensajeCategoria = sMensajeCategoria;
		}

		public static class Comparator implements java.util.Comparator<entCatePremios>
		{
			public int compare( entCatePremios c1, entCatePremios c2 )
			{
				return c1.getDescCategoria().compareToIgnoreCase( c2.getDescCategoria() );
			}
		}
	}

	public static class entProveedor implements KvmSerializable
	{
		private static final long serialVersionUID = 0L;
		private String _claveProveedor;
		private String _contactoProveedor;
		private String _descProveedor;
		private String _emailContactProveedor;
		private String _faxContactProveedor;
		private String _telConactProveedor;
		private String _sMensajeProveedor;

		@Override
		public int getPropertyCount()
		{
			return 7;
		}

		@Override
		public Object getProperty( int i )
		{
			if( i == 0 )
				return this.getClaveProveedor();
			else if( i == 1 )
				return this.getContactoProveedor();
			else if( i == 2 )
				return this.getDescProveedor();
			else if( i == 3 )
				return this.getEmailContactProveedor();
			else if( i == 4 )
				return this.getFaxContactProveedor();
			else if( i == 5 )
				return this.getTelConactProveedor();
			else if( i == 6 )
				return this.getsMensajeProveedor();
			else return null;
		}

		@Override
		public void setProperty( int i, Object o )
		{
			if( i == 0 )
				this.setClaveProveedor( (String)o );
			else if( i == 1 )
				this.setContactoProveedor( (String)o );
			else if( i == 2 )
				this.setDescProveedor( (String)o );
			else if( i == 3 )
				this.setEmailContactProveedor( (String)o );
			else if( i == 4 )
				this.setFaxContactProveedor( (String)o );
			else if( i == 5 )
				this.setTelConactProveedor( (String)o );
			else if( i == 6 )
				this.setsMensajeProveedor( (String)o );
		}

		@Override
		public void getPropertyInfo( int i, Hashtable hashtable, PropertyInfo propertyInfo )
		{
			if( i == 0 )
			{
				propertyInfo.name = "ClaveProveedor";
				propertyInfo.namespace = "http://schemas.datacontract.org/2004/07/wcfCityPremiosCP.Entities.CityPremios";
			}
			else if( i == 1 )
			{
				propertyInfo.name = "ContactoProveedor";
				propertyInfo.namespace = "http://schemas.datacontract.org/2004/07/wcfCityPremiosCP.Entities.CityPremios";
			}
			else if( i == 2 )
			{
				propertyInfo.name = "DescProveedor";
				propertyInfo.namespace = "http://schemas.datacontract.org/2004/07/wcfCityPremiosCP.Entities.CityPremios";
			}
			else if( i == 3 )
			{
				propertyInfo.name = "EmailContactProveedor";
				propertyInfo.namespace = "http://schemas.datacontract.org/2004/07/wcfCityPremiosCP.Entities.CityPremios";
			}
			else if( i == 4 )
			{
				propertyInfo.name = "FaxContactProveedor";
				propertyInfo.namespace = "http://schemas.datacontract.org/2004/07/wcfCityPremiosCP.Entities.CityPremios";
			}
			else if( i == 5 )
			{
				propertyInfo.name = "TelConactProveedor";
				propertyInfo.namespace = "http://schemas.datacontract.org/2004/07/wcfCityPremiosCP.Entities.CityPremios";
			}
			else if( i == 6 )
			{
				propertyInfo.name = "sMensajeProveedor";
				propertyInfo.namespace = "http://schemas.datacontract.org/2004/07/wcfCityPremiosCP.Entities.CityPremios";
			}
		}

		public String getClaveProveedor()
		{
			return _claveProveedor;
		}

		public void setClaveProveedor( String claveProveedor )
		{
			_claveProveedor = claveProveedor;
		}

		public String getContactoProveedor()
		{
			return _contactoProveedor;
		}

		public void setContactoProveedor( String contactoProveedor )
		{
			_contactoProveedor = contactoProveedor;
		}

		public String getDescProveedor()
		{
			return _descProveedor;
		}

		public void setDescProveedor( String descProveedor )
		{
			_descProveedor = descProveedor;
		}

		public String getEmailContactProveedor()
		{
			return _emailContactProveedor;
		}

		public void setEmailContactProveedor( String emailContactProveedor )
		{
			_emailContactProveedor = emailContactProveedor;
		}

		public String getFaxContactProveedor()
		{
			return _faxContactProveedor;
		}

		public void setFaxContactProveedor( String faxContactProveedor )
		{
			_faxContactProveedor = faxContactProveedor;
		}

		public String getTelConactProveedor()
		{
			return _telConactProveedor;
		}

		public void setTelConactProveedor( String telConactProveedor )
		{
			_telConactProveedor = telConactProveedor;
		}

		public String getsMensajeProveedor()
		{
			return _sMensajeProveedor;
		}

		public void setsMensajeProveedor( String sMensajeProveedor )
		{
			_sMensajeProveedor = sMensajeProveedor;
		}
	}

	public static class entRedencion implements KvmSerializable
	{
		private static final long serialVersionUID = 0L;
		private String _entSwap_Add_Usr;
		private Integer _entSwap_Amt;
		private String _entSwap_BsAs;
		private String _entSwap_Cmnt;
		private String _entSwap_Code;
		private String _entSwap_CrTy;
		private String _entSwap_CtTy;
		private Integer _entSwap_Fcbm;
		private String _entSwap_FrCs;
		private Long _entSwap_Id;
		private String _entSwap_Last_Dte;
		private String _entSwap_Last_Usr;
		private Integer _entSwap_Prze;
		private Integer _entSwap_Pts;
		private Integer _entSwap_Qtty;
		private String _entSwap_Src;
		private Integer _entSwap_Swst;
		private Integer _entSwap_Val;
		private String _sMensajeRedencion;

		@Override
		public int getPropertyCount()
		{
			return 19;
		}

		@Override
		public Object getProperty( int i )
		{
			if( i == 0 )
				return this.getentSwap_Add_Usr();
			else if( i == 1 )
				return this.getentSwap_Amt();
			else if( i == 2 )
				return this.getentSwap_BsAs();
			else if( i == 3 )
				return this.getentSwap_Cmnt();
			else if( i == 4 )
				return this.getentSwap_Code();
			else if( i == 5 )
				return this.getentSwap_CrTy();
			else if( i == 6 )
				return this.getentSwap_CtTy();
			else if( i == 7 )
				return this.getentSwap_Fcbm();
			else if( i == 8 )
				return this.getentSwap_FrCs();
			else if( i == 9 )
				return this.getentSwap_Id();
			else if( i == 10 )
				return this.getentSwap_Last_Dte();
			else if( i == 11 )
				return this.getentSwap_Last_Usr();
			else if( i == 12 )
				return this.getentSwap_Prze();
			else if( i == 13 )
				return this.getentSwap_Pts();
			else if( i == 14 )
				return this.getentSwap_Qtty();
			else if( i == 15 )
				return this.getentSwap_Src();
			else if( i == 16 )
				return this.getentSwap_Swst();
			else if( i == 17 )
				return this.getentSwap_Val();
			else if( i == 18 )
				return this.getsMensajeRedencion();
			else return null;
		}

		@Override
		public void setProperty( int i, Object o )
		{
			if( i == 0 )
				this.setentSwap_Add_Usr( (String)o );
			else if( i == 1 )
				this.setentSwap_Amt( (Integer)o );
			else if( i == 2 )
				this.setentSwap_BsAs( (String)o );
			else if( i == 3 )
				this.setentSwap_Cmnt( (String)o );
			else if( i == 4 )
				this.setentSwap_Code( (String)o );
			else if( i == 5 )
				this.setentSwap_CrTy( (String)o );
			else if( i == 6 )
				this.setentSwap_CtTy( (String)o );
			else if( i == 7 )
				this.setentSwap_Fcbm( (Integer)o );
			else if( i == 8 )
				this.setentSwap_FrCs( (String)o );
			else if( i == 9 )
				this.setentSwap_Id( (Long)o );
			else if( i == 10 )
				this.setentSwap_Last_Dte( (String)o );
			else if( i == 11 )
				this.setentSwap_Last_Usr( (String)o );
			else if( i == 12 )
				this.setentSwap_Prze( (Integer)o );
			else if( i == 13 )
				this.setentSwap_Pts( (Integer)o );
			else if( i == 14 )
				this.setentSwap_Qtty( (Integer)o );
			else if( i == 15 )
				this.setentSwap_Src( (String)o );
			else if( i == 16 )
				this.setentSwap_Swst( (Integer)o );
			else if( i == 17 )
				this.setentSwap_Val( (Integer)o );
			else if( i == 18 )
				this.setsMensajeRedencion( (String)o );
		}

		@Override
		public void getPropertyInfo( int i, Hashtable hashtable, PropertyInfo propertyInfo )
		{
			if( i == 0 )
			{
				propertyInfo.name = "entSwap_Add_Usr";
				propertyInfo.namespace = "http://schemas.datacontract.org/2004/07/wcfCityPremiosCP.Entities.CityPremios";
			}
			else if( i == 1 )
			{
				propertyInfo.name = "entSwap_Amt";
				propertyInfo.namespace = "http://schemas.datacontract.org/2004/07/wcfCityPremiosCP.Entities.CityPremios";
			}
			else if( i == 2 )
			{
				propertyInfo.name = "entSwap_BsAs";
				propertyInfo.namespace = "http://schemas.datacontract.org/2004/07/wcfCityPremiosCP.Entities.CityPremios";
			}
			else if( i == 3 )
			{
				propertyInfo.name = "entSwap_Cmnt";
				propertyInfo.namespace = "http://schemas.datacontract.org/2004/07/wcfCityPremiosCP.Entities.CityPremios";
			}
			else if( i == 4 )
			{
				propertyInfo.name = "entSwap_Code";
				propertyInfo.namespace = "http://schemas.datacontract.org/2004/07/wcfCityPremiosCP.Entities.CityPremios";
			}
			else if( i == 5 )
			{
				propertyInfo.name = "entSwap_CrTy";
				propertyInfo.namespace = "http://schemas.datacontract.org/2004/07/wcfCityPremiosCP.Entities.CityPremios";
			}
			else if( i == 6 )
			{
				propertyInfo.name = "entSwap_CtTy";
				propertyInfo.namespace = "http://schemas.datacontract.org/2004/07/wcfCityPremiosCP.Entities.CityPremios";
			}
			else if( i == 7 )
			{
				propertyInfo.name = "entSwap_Fcbm";
				propertyInfo.namespace = "http://schemas.datacontract.org/2004/07/wcfCityPremiosCP.Entities.CityPremios";
			}
			else if( i == 8 )
			{
				propertyInfo.name = "entSwap_FrCs";
				propertyInfo.namespace = "http://schemas.datacontract.org/2004/07/wcfCityPremiosCP.Entities.CityPremios";
			}
			else if( i == 9 )
			{
				propertyInfo.name = "entSwap_Id";
				propertyInfo.namespace = "http://schemas.datacontract.org/2004/07/wcfCityPremiosCP.Entities.CityPremios";
			}
			else if( i == 10 )
			{
				propertyInfo.name = "entSwap_Last_Dte";
				propertyInfo.namespace = "http://schemas.datacontract.org/2004/07/wcfCityPremiosCP.Entities.CityPremios";
			}
			else if( i == 11 )
			{
				propertyInfo.name = "entSwap_Last_Usr";
				propertyInfo.namespace = "http://schemas.datacontract.org/2004/07/wcfCityPremiosCP.Entities.CityPremios";
			}
			else if( i == 12 )
			{
				propertyInfo.name = "entSwap_Prze";
				propertyInfo.namespace = "http://schemas.datacontract.org/2004/07/wcfCityPremiosCP.Entities.CityPremios";
			}
			else if( i == 13 )
			{
				propertyInfo.name = "entSwap_Pts";
				propertyInfo.namespace = "http://schemas.datacontract.org/2004/07/wcfCityPremiosCP.Entities.CityPremios";
			}
			else if( i == 14 )
			{
				propertyInfo.name = "entSwap_Qtty";
				propertyInfo.namespace = "http://schemas.datacontract.org/2004/07/wcfCityPremiosCP.Entities.CityPremios";
			}
			else if( i == 15 )
			{
				propertyInfo.name = "entSwap_Src";
				propertyInfo.namespace = "http://schemas.datacontract.org/2004/07/wcfCityPremiosCP.Entities.CityPremios";
			}
			else if( i == 16 )
			{
				propertyInfo.name = "entSwap_Swst";
				propertyInfo.namespace = "http://schemas.datacontract.org/2004/07/wcfCityPremiosCP.Entities.CityPremios";
			}
			else if( i == 17 )
			{
				propertyInfo.name = "entSwap_Val";
				propertyInfo.namespace = "http://schemas.datacontract.org/2004/07/wcfCityPremiosCP.Entities.CityPremios";
			}
			else if( i == 18 )
			{
				propertyInfo.name = "sMensajeRedencion";
				propertyInfo.namespace = "http://schemas.datacontract.org/2004/07/wcfCityPremiosCP.Entities.CityPremios";
			}
		}

		public String getentSwap_Add_Usr()
		{
			return _entSwap_Add_Usr;
		}

		public void setentSwap_Add_Usr( String entSwap_Add_Usr )
		{
			_entSwap_Add_Usr = entSwap_Add_Usr;
		}

		public Integer getentSwap_Amt()
		{
			return _entSwap_Amt;
		}

		public void setentSwap_Amt( Integer entSwap_Amt )
		{
			_entSwap_Amt = entSwap_Amt;
		}

		public String getentSwap_BsAs()
		{
			return _entSwap_BsAs;
		}

		public void setentSwap_BsAs( String entSwap_BsAs )
		{
			_entSwap_BsAs = entSwap_BsAs;
		}

		public String getentSwap_Cmnt()
		{
			return _entSwap_Cmnt;
		}

		public void setentSwap_Cmnt( String entSwap_Cmnt )
		{
			_entSwap_Cmnt = entSwap_Cmnt;
		}

		public String getentSwap_Code()
		{
			return _entSwap_Code;
		}

		public void setentSwap_Code( String entSwap_Code )
		{
			_entSwap_Code = entSwap_Code;
		}

		public String getentSwap_CrTy()
		{
			return _entSwap_CrTy;
		}

		public void setentSwap_CrTy( String entSwap_CrTy )
		{
			_entSwap_CrTy = entSwap_CrTy;
		}

		public String getentSwap_CtTy()
		{
			return _entSwap_CtTy;
		}

		public void setentSwap_CtTy( String entSwap_CtTy )
		{
			_entSwap_CtTy = entSwap_CtTy;
		}

		public Integer getentSwap_Fcbm()
		{
			return _entSwap_Fcbm;
		}

		public void setentSwap_Fcbm( Integer entSwap_Fcbm )
		{
			_entSwap_Fcbm = entSwap_Fcbm;
		}

		public String getentSwap_FrCs()
		{
			return _entSwap_FrCs;
		}

		public void setentSwap_FrCs( String entSwap_FrCs )
		{
			_entSwap_FrCs = entSwap_FrCs;
		}

		public Long getentSwap_Id()
		{
			return _entSwap_Id;
		}

		public void setentSwap_Id( Long entSwap_Id )
		{
			_entSwap_Id = entSwap_Id;
		}

		public String getentSwap_Last_Dte()
		{
			return _entSwap_Last_Dte;
		}

		public void setentSwap_Last_Dte( String entSwap_Last_Dte )
		{
			_entSwap_Last_Dte = entSwap_Last_Dte;
		}

		public String getentSwap_Last_Usr()
		{
			return _entSwap_Last_Usr;
		}

		public void setentSwap_Last_Usr( String entSwap_Last_Usr )
		{
			_entSwap_Last_Usr = entSwap_Last_Usr;
		}

		public Integer getentSwap_Prze()
		{
			return _entSwap_Prze;
		}

		public void setentSwap_Prze( Integer entSwap_Prze )
		{
			_entSwap_Prze = entSwap_Prze;
		}

		public Integer getentSwap_Pts()
		{
			return _entSwap_Pts;
		}

		public void setentSwap_Pts( Integer entSwap_Pts )
		{
			_entSwap_Pts = entSwap_Pts;
		}

		public Integer getentSwap_Qtty()
		{
			return _entSwap_Qtty;
		}

		public void setentSwap_Qtty( Integer entSwap_Qtty )
		{
			_entSwap_Qtty = entSwap_Qtty;
		}

		public String getentSwap_Src()
		{
			return _entSwap_Src;
		}

		public void setentSwap_Src( String entSwap_Src )
		{
			_entSwap_Src = entSwap_Src;
		}

		public Integer getentSwap_Swst()
		{
			return _entSwap_Swst;
		}

		public void setentSwap_Swst( Integer entSwap_Swst )
		{
			_entSwap_Swst = entSwap_Swst;
		}

		public Integer getentSwap_Val()
		{
			return _entSwap_Val;
		}

		public void setentSwap_Val( Integer entSwap_Val )
		{
			_entSwap_Val = entSwap_Val;
		}

		public String getsMensajeRedencion()
		{
			return _sMensajeRedencion;
		}

		public void setsMensajeRedencion( String sMensajeRedencion )
		{
			_sMensajeRedencion = sMensajeRedencion;
		}
	}

	public static class Prze implements KvmSerializable, Serializable
	{
		private static final long serialVersionUID = 0L;
		private Integer _prze_Actv;
		private Double _prze_Amt;
		private String _prze_BsAs;
		private String _prze_CtTy;
		private Boolean _prze_Debit;
		private String _prze_Dscr;
		private Boolean _prze_Hab;
		private Double _prze_Hgh_Qtty;
		private Integer _prze_Id;
		private Date _prze_Isu_Dte;
		private Double _prze_Low_Qtty;
		private Date _prze_Mat_Dte;
		private Date _prze_Mod_Dte;
		private String _prze_Mod_Usr;
		private String _prze_Name;
		private Double _prze_Pts;
		private String _prze_Rwty;
		private String _prze_Shrt_Name;
		private String _prze_URL00;
		private String _prze_URL01;
		private Double _prze_Value;

		@Override
		public int getPropertyCount()
		{
			return 21;
		}

		@Override
		public Object getProperty( int i )
		{
			if( i == 0 )
				return this.getPrze_Actv();
			else if( i == 1 )
				return this.getPrze_Amt();
			else if( i == 2 )
				return this.getPrze_BsAs();
			else if( i == 3 )
				return this.getPrze_CtTy();
			else if( i == 4 )
				return this.getPrze_Debit();
			else if( i == 5 )
				return this.getPrze_Dscr();
			else if( i == 6 )
				return this.getPrze_Hab();
			else if( i == 7 )
				return this.getPrze_Hgh_Qtty();
			else if( i == 8 )
				return this.getPrze_Id();
			else if( i == 9 )
				return getSOAPDateString( this.getPrze_Isu_Dte() );
			else if( i == 10 )
				return this.getPrze_Low_Qtty();
			else if( i == 11 )
				return getSOAPDateString( this.getPrze_Mat_Dte() );
			else if( i == 12 )
				return getSOAPDateString( this.getPrze_Mod_Dte() );
			else if( i == 13 )
				return this.getPrze_Mod_Usr();
			else if( i == 14 )
				return this.getPrze_Name();
			else if( i == 15 )
				return this.getPrze_Pts();
			else if( i == 16 )
				return this.getPrze_Rwty();
			else if( i == 17 )
				return this.getPrze_Shrt_Name();
			else if( i == 18 )
				return this.getPrze_URL00();
			else if( i == 19 )
				return this.getPrze_URL01();
			else if( i == 20 )
				return this.getPrze_Value();
			else return null;
		}

		@Override
		public void setProperty( int i, Object o )
		{
			if( i == 0 )
				this.setPrze_Actv( (Integer)o );
			else if( i == 1 )
				this.setPrze_Amt( (Double)o );
			else if( i == 2 )
				this.setPrze_BsAs( (String)o );
			else if( i == 3 )
				this.setPrze_CtTy( (String)o );
			else if( i == 4 )
				this.setPrze_Debit( (Boolean)o );
			else if( i == 5 )
				this.setPrze_Dscr( (String)o );
			else if( i == 6 )
				this.setPrze_Hab( (Boolean)o );
			else if( i == 7 )
				this.setPrze_Hgh_Qtty( (Double)o );
			else if( i == 8 )
				this.setPrze_Id( (Integer)o );
			else if( i == 9 )
				this.setPrze_Isu_Dte( getDateFromSOAPString( (String)o ) );
			else if( i == 10 )
				this.setPrze_Low_Qtty( (Double)o );
			else if( i == 11 )
				this.setPrze_Mat_Dte( getDateFromSOAPString( (String)o ) );
			else if( i == 12 )
				this.setPrze_Mod_Dte( getDateFromSOAPString( (String)o ) );
			else if( i == 13 )
				this.setPrze_Mod_Usr( (String)o );
			else if( i == 14 )
				this.setPrze_Name( (String)o );
			else if( i == 15 )
				this.setPrze_Pts( (Double)o );
			else if( i == 16 )
				this.setPrze_Rwty( (String)o );
			else if( i == 17 )
				this.setPrze_Shrt_Name( (String)o );
			else if( i == 18 )
				this.setPrze_URL00( (String)o );
			else if( i == 19 )
				this.setPrze_URL01( (String)o );
			else if( i == 20 )
				this.setPrze_Value( (Double)o );
		}

		@Override
		public void getPropertyInfo( int i, Hashtable hashtable, PropertyInfo propertyInfo )
		{
			if( i == 0 )
			{
				propertyInfo.name = "Prze_Actv";
				propertyInfo.namespace = "http://schemas.datacontract.org/2004/07/wcfCityPremiosCP.Entities.CityPremios";
			}
			else if( i == 1 )
			{
				propertyInfo.name = "Prze_Amt";
				propertyInfo.namespace = "http://schemas.datacontract.org/2004/07/wcfCityPremiosCP.Entities.CityPremios";
			}
			else if( i == 2 )
			{
				propertyInfo.name = "Prze_BsAs";
				propertyInfo.namespace = "http://schemas.datacontract.org/2004/07/wcfCityPremiosCP.Entities.CityPremios";
			}
			else if( i == 3 )
			{
				propertyInfo.name = "Prze_CtTy";
				propertyInfo.namespace = "http://schemas.datacontract.org/2004/07/wcfCityPremiosCP.Entities.CityPremios";
			}
			else if( i == 4 )
			{
				propertyInfo.name = "Prze_Debit";
				propertyInfo.namespace = "http://schemas.datacontract.org/2004/07/wcfCityPremiosCP.Entities.CityPremios";
			}
			else if( i == 5 )
			{
				propertyInfo.name = "Prze_Dscr";
				propertyInfo.namespace = "http://schemas.datacontract.org/2004/07/wcfCityPremiosCP.Entities.CityPremios";
			}
			else if( i == 6 )
			{
				propertyInfo.name = "Prze_Hab";
				propertyInfo.namespace = "http://schemas.datacontract.org/2004/07/wcfCityPremiosCP.Entities.CityPremios";
			}
			else if( i == 7 )
			{
				propertyInfo.name = "Prze_Hgh_Qtty";
				propertyInfo.namespace = "http://schemas.datacontract.org/2004/07/wcfCityPremiosCP.Entities.CityPremios";
			}
			else if( i == 8 )
			{
				propertyInfo.name = "Prze_Id";
				propertyInfo.namespace = "http://schemas.datacontract.org/2004/07/wcfCityPremiosCP.Entities.CityPremios";
			}
			else if( i == 9 )
			{
				propertyInfo.name = "Prze_Isu_Dte";
				propertyInfo.namespace = "http://schemas.datacontract.org/2004/07/wcfCityPremiosCP.Entities.CityPremios";
			}
			else if( i == 10 )
			{
				propertyInfo.name = "Prze_Low_Qtty";
				propertyInfo.namespace = "http://schemas.datacontract.org/2004/07/wcfCityPremiosCP.Entities.CityPremios";
			}
			else if( i == 11 )
			{
				propertyInfo.name = "Prze_Mat_Dte";
				propertyInfo.namespace = "http://schemas.datacontract.org/2004/07/wcfCityPremiosCP.Entities.CityPremios";
			}
			else if( i == 12 )
			{
				propertyInfo.name = "Prze_Mod_Dte";
				propertyInfo.namespace = "http://schemas.datacontract.org/2004/07/wcfCityPremiosCP.Entities.CityPremios";
			}
			else if( i == 13 )
			{
				propertyInfo.name = "Prze_Mod_Usr";
				propertyInfo.namespace = "http://schemas.datacontract.org/2004/07/wcfCityPremiosCP.Entities.CityPremios";
			}
			else if( i == 14 )
			{
				propertyInfo.name = "Prze_Name";
				propertyInfo.namespace = "http://schemas.datacontract.org/2004/07/wcfCityPremiosCP.Entities.CityPremios";
			}
			else if( i == 15 )
			{
				propertyInfo.name = "Prze_Pts";
				propertyInfo.namespace = "http://schemas.datacontract.org/2004/07/wcfCityPremiosCP.Entities.CityPremios";
			}
			else if( i == 16 )
			{
				propertyInfo.name = "Prze_Rwty";
				propertyInfo.namespace = "http://schemas.datacontract.org/2004/07/wcfCityPremiosCP.Entities.CityPremios";
			}
			else if( i == 17 )
			{
				propertyInfo.name = "Prze_Shrt_Name";
				propertyInfo.namespace = "http://schemas.datacontract.org/2004/07/wcfCityPremiosCP.Entities.CityPremios";
			}
			else if( i == 18 )
			{
				propertyInfo.name = "Prze_URL00";
				propertyInfo.namespace = "http://schemas.datacontract.org/2004/07/wcfCityPremiosCP.Entities.CityPremios";
			}
			else if( i == 19 )
			{
				propertyInfo.name = "Prze_URL01";
				propertyInfo.namespace = "http://schemas.datacontract.org/2004/07/wcfCityPremiosCP.Entities.CityPremios";
			}
			else if( i == 20 )
			{
				propertyInfo.name = "Prze_Value";
				propertyInfo.namespace = "http://schemas.datacontract.org/2004/07/wcfCityPremiosCP.Entities.CityPremios";
			}
		}

		public Integer getPrze_Actv()
		{
			return _prze_Actv;
		}

		public void setPrze_Actv( Integer prze_Actv )
		{
			_prze_Actv = prze_Actv;
		}

		public Double getPrze_Amt()
		{
			return _prze_Amt;
		}

		public void setPrze_Amt( Double prze_Amt )
		{
			_prze_Amt = prze_Amt;
		}

		public String getPrze_BsAs()
		{
			return _prze_BsAs;
		}

		public void setPrze_BsAs( String prze_BsAs )
		{
			_prze_BsAs = prze_BsAs;
		}

		public String getPrze_CtTy()
		{
			return _prze_CtTy;
		}

		public void setPrze_CtTy( String prze_CtTy )
		{
			_prze_CtTy = prze_CtTy;
		}

		public Boolean getPrze_Debit()
		{
			return _prze_Debit;
		}

		public void setPrze_Debit( Boolean prze_Debit )
		{
			_prze_Debit = prze_Debit;
		}

		public String getPrze_Dscr()
		{
			return _prze_Dscr;
		}

		public void setPrze_Dscr( String prze_Dscr )
		{
			_prze_Dscr = prze_Dscr;
		}

		public Boolean getPrze_Hab()
		{
			return _prze_Hab;
		}

		public void setPrze_Hab( Boolean prze_Hab )
		{
			_prze_Hab = prze_Hab;
		}

		public Double getPrze_Hgh_Qtty()
		{
			return _prze_Hgh_Qtty;
		}

		public void setPrze_Hgh_Qtty( Double prze_Hgh_Qtty )
		{
			_prze_Hgh_Qtty = prze_Hgh_Qtty;
		}

		public Integer getPrze_Id()
		{
			return _prze_Id;
		}

		public void setPrze_Id( Integer prze_Id )
		{
			_prze_Id = prze_Id;
		}

		public Date getPrze_Isu_Dte()
		{
			return _prze_Isu_Dte;
		}

		public void setPrze_Isu_Dte( Date prze_Isu_Dte )
		{
			_prze_Isu_Dte = prze_Isu_Dte;
		}

		public Double getPrze_Low_Qtty()
		{
			return _prze_Low_Qtty;
		}

		public void setPrze_Low_Qtty( Double prze_Low_Qtty )
		{
			_prze_Low_Qtty = prze_Low_Qtty;
		}

		public Date getPrze_Mat_Dte()
		{
			return _prze_Mat_Dte;
		}

		public void setPrze_Mat_Dte( Date prze_Mat_Dte )
		{
			_prze_Mat_Dte = prze_Mat_Dte;
		}

		public Date getPrze_Mod_Dte()
		{
			return _prze_Mod_Dte;
		}

		public void setPrze_Mod_Dte( Date prze_Mod_Dte )
		{
			_prze_Mod_Dte = prze_Mod_Dte;
		}

		public String getPrze_Mod_Usr()
		{
			return _prze_Mod_Usr;
		}

		public void setPrze_Mod_Usr( String prze_Mod_Usr )
		{
			_prze_Mod_Usr = prze_Mod_Usr;
		}

		public String getPrze_Name()
		{
			return _prze_Name;
		}

		public void setPrze_Name( String prze_Name )
		{
			_prze_Name = prze_Name;
		}

		public Double getPrze_Pts()
		{
			return _prze_Pts;
		}

		public void setPrze_Pts( Double prze_Pts )
		{
			_prze_Pts = prze_Pts;
		}

		public String getPrze_Rwty()
		{
			return _prze_Rwty;
		}

		public void setPrze_Rwty( String prze_Rwty )
		{
			_prze_Rwty = prze_Rwty;
		}

		public String getPrze_Shrt_Name()
		{
			return _prze_Shrt_Name;
		}

		public void setPrze_Shrt_Name( String prze_Shrt_Name )
		{
			_prze_Shrt_Name = prze_Shrt_Name;
		}

		public String getPrze_URL00()
		{
			return _prze_URL00;
		}

		public void setPrze_URL00( String prze_URL00 )
		{
			_prze_URL00 = prze_URL00;
		}

		public String getPrze_URL01()
		{
			return _prze_URL01;
		}

		public void setPrze_URL01( String prze_URL01 )
		{
			_prze_URL01 = prze_URL01;
		}

		public Double getPrze_Value()
		{
			return _prze_Value;
		}

		public void setPrze_Value( Double prze_Value )
		{
			_prze_Value = prze_Value;
		}

		public static class Comparator implements java.util.Comparator<Prze>
		{
			public int compare( Prze c1, Prze c2 )
			{
				return c1.getPrze_Name().compareToIgnoreCase( c2.getPrze_Name() );
			}
		}
	}

	private static Object getSOAPDateString( Date itemValue )
	{
		if( itemValue != null )
		{
			SimpleDateFormat lDateFormat = new SimpleDateFormat( "yyyy-MM-dd'T'hh:mm:ss'Z'" );
			return lDateFormat.format( itemValue );
		}
		else
		{
			return "";
		}
	}

	private static Date getDateFromSOAPString( String itemValue )
	{
		SimpleDateFormat lDateFormat;
		if( itemValue.toUpperCase().contains( "Z" ) )
			lDateFormat = new SimpleDateFormat( "yyyy-MM-dd'T'hh:mm:ss'Z'" );
		else
			lDateFormat = new SimpleDateFormat( "yyyy-MM-dd'T'hh:mm:ss" );
		Date result = null;
		try
		{
			result = lDateFormat.parse( itemValue );
		}
		catch( Exception e )
		{
			android.util.Log.d( "ReservationEngineClient", "Cant parse date: " + e.getMessage() );
		}
		return result;
	}

	private static Integer getNulleableIntegerFromSOAPString( String value )
	{
		if( value.length() == 0 )
			return null;
		else
		{
			try
			{
				return Integer.parseInt( value );
			}
			catch( Exception e )
			{
				e.printStackTrace();
				return null;
			}
		}
	}

	private static Long getNulleableLongFromSOAPString( String value )
	{
		if( value.length() == 0 )
			return null;
		else
		{
			try
			{
				return Long.parseLong( value );
			}
			catch( Exception e )
			{
				e.printStackTrace();
				return null;
			}
		}
	}

	private static Double getNulleableDoubleFromSOAPString( String value )
	{
		if( value.length() == 0 )
			return null;
		else
		{
			try
			{
				return Double.parseDouble( value );
			}
			catch( Exception e )
			{
				e.printStackTrace();
				return null;
			}
		}
	}

	private static Boolean getNulleableBooleanFromSOAPString( String value )
	{
		if( value.length() == 0 )
			return null;
		else
		{
			try
			{
				return value.equalsIgnoreCase( "TRUE" );
			}
			catch( Exception e )
			{
				e.printStackTrace();
				return null;
			}
		}
	}

	private int parseIntForRecord( String value )
	{
		if( value.length() == 0 )
			return 0;

		try
		{
			return Integer.parseInt( value.replaceAll( ",", "" ) );
		}
		catch( Exception e )
		{
			e.printStackTrace();
			return 0;
		}
	}

	private Date parseDateForRecord( String value )
	{
		if( value.length() == 0 )
			return null;

		if( value.contains( "/" ) )
		{
			SimpleDateFormat sdf = new SimpleDateFormat( "yyyy'/'MM'/'dd" );
			try
			{
				return sdf.parse( value );
			}
			catch( ParseException e )
			{
				e.printStackTrace();
				return null;
			}
		}
		else
		{
			SimpleDateFormat sdf = new SimpleDateFormat( "yyyyMMdd" );
			try
			{
				return sdf.parse( value );
			}
			catch( ParseException e )
			{
				e.printStackTrace();
				return null;
			}
		}
	}
}
