package com.zebstudios.cityexpress;

import android.os.AsyncTask;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by rczuart on 1/26/2015.
 */
public class PayPalCaller extends AsyncTask<Void, Void, Void>
{
	private static final int MODE_PRODUCTION = 1;
	private static final int MODE_SANDBOX = 2;
	private static final int PAYPAL_MODE = APIAddress.PAYPAL_MODE;

	public static final int INITIATE_PAYMENT = 100;
	public static final int DO_EXPRESS_CHECKOUT = 101;
	public static final int REFUND_TRANSACTION = 102;

	private int _task;
	private ProgressDialogFragment _progress;
	private android.support.v4.app.FragmentManager _fragmentManager;
	private PayPalCallerInterface _listener;
	private Object _parameters;
	private Object _response;

	public PayPalCaller( PayPalCallerInterface listener, int task, Object parameters, android.support.v4.app.FragmentManager fragmentManager, ProgressDialogFragment progress )
	{
		_listener = listener;
		_task = task;
		_fragmentManager = fragmentManager;
		_progress = progress;
		_parameters = parameters;
	}

	@Override
	protected void onPreExecute()
	{
		super.onPreExecute();

		_progress = ProgressDialogFragment.newInstance();
		_progress.setCancelable( false );
		_progress.show( _fragmentManager, "Dialog" );
	}

	@Override
	protected Void doInBackground( Void... arg0 )
	{
		if( _task == INITIATE_PAYMENT )
		{
			PayPalPayment payment = (PayPalPayment)_parameters;

			ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add( new BasicNameValuePair( "METHOD", "SetExpressCheckout" ) );
			params.add( new BasicNameValuePair( "VERSION", "98" ) );

			if( PAYPAL_MODE == MODE_SANDBOX )
			{
				params.add( new BasicNameValuePair( "USER", "hblancas_api1.gmail.com" ) );
				params.add( new BasicNameValuePair( "PWD", "1405447289" ) );
				params.add( new BasicNameValuePair( "SIGNATURE", "AFcWxV21C7fd0v3bYYYRCpSSRl31Aj0ann9Hr--f3qDoWBOBxT9XRGfb" ) );
				//params.add( new BasicNameValuePair( "USER", "impulsora2_api1.email.com" ) );
				//params.add( new BasicNameValuePair( "PWD", "KNLEG9TEK8V3LVDM" ) );
				//params.add( new BasicNameValuePair( "SIGNATURE", "AFcWxV21C7fd0v3bYYYRCpSSRl31A8s2Lly3K8VklKRTItGj00jJ2mZ9" ) );
			}
			else
			{
				params.add( new BasicNameValuePair( "USER", payment.getUser() ) );
				params.add( new BasicNameValuePair( "PWD", payment.getPassword() ) );
				params.add( new BasicNameValuePair( "SIGNATURE", payment.getSignature() ) );
			}
			params.add( new BasicNameValuePair( "RETURNURL", payment.getReturnURL() ) );
			params.add( new BasicNameValuePair( "CANCELURL", payment.getCancelURL() ) );
			params.add( new BasicNameValuePair( "CARTBORDERCOLOR", payment.getCartBorderColor() ) );
			params.add( new BasicNameValuePair( "LOGOIMG", payment.getLogoImg() ) );
			params.add( new BasicNameValuePair( "NOSHIPPING", "1" ) );
			params.add( new BasicNameValuePair( "LANDINGPAGE", "Login" ) );
			params.add( new BasicNameValuePair( "PAYMENTREQUEST_0_PAYMENTACTION", "Sale" ) );
			params.add( new BasicNameValuePair( "L_PAYMENTREQUEST_0_NAME0", payment.getItemName() ) );
			params.add( new BasicNameValuePair( "L_PAYMENTREQUEST_0_NUMBER0", payment.getItemSKU() ) );
			params.add( new BasicNameValuePair( "L_PAYMENTREQUEST_0_DESC0", payment.getItemDesc() ) );
			params.add( new BasicNameValuePair( "L_PAYMENTREQUEST_0_AMT0", payment.getItemAmount() ) );
			params.add( new BasicNameValuePair( "L_PAYMENTREQUEST_0_QTY0", "1" ) );
			params.add( new BasicNameValuePair( "PAYMENTREQUEST_0_AMT", payment.getAmount() ) );
			params.add( new BasicNameValuePair( "PAYMENTREQUEST_0_ITEMAMT", payment.getAmount() ) );
			params.add( new BasicNameValuePair( "PAYMENTREQUEST_0_CURRENCYCODE", payment.getCurrencyCode() ) );

			ServiceHandler handler = new ServiceHandler();
			String responseStr = handler.makeServiceCall( APIAddress.PAYPAL_MERCHANT_API, ServiceHandler.GET, params );
			android.util.Log.d( "PAYPAL", "RESPONSE: " + responseStr );

			PayPalSECResponse response;
			if( responseStr != null )
			{
				Map<String, String> parameters = getUrlParameters( responseStr );
				response = new PayPalSECResponse( parameters );
			}
			else
			{
				response = new PayPalSECResponse( new HashMap<String, String>() );
				response.setACK( "INET FAIL" );
			}
			_response = response;
		}
		else if( _task == DO_EXPRESS_CHECKOUT )
		{
			PayPalDECParameters paramenters = (PayPalDECParameters)_parameters;

			ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add( new BasicNameValuePair( "METHOD", "DoExpressCheckoutPayment" ) );
			params.add( new BasicNameValuePair( "VERSION", "98" ) );

			if( PAYPAL_MODE == MODE_SANDBOX )
			{
				params.add( new BasicNameValuePair( "USER", "hblancas_api1.gmail.com" ) );
				params.add( new BasicNameValuePair( "PWD", "1405447289" ) );
				params.add( new BasicNameValuePair( "SIGNATURE", "AFcWxV21C7fd0v3bYYYRCpSSRl31Aj0ann9Hr--f3qDoWBOBxT9XRGfb" ) );
				//params.add( new BasicNameValuePair( "USER", "impulsora2_api1.email.com" ) );
				//params.add( new BasicNameValuePair( "PWD", "KNLEG9TEK8V3LVDM" ) );
				//params.add( new BasicNameValuePair( "SIGNATURE", "AFcWxV21C7fd0v3bYYYRCpSSRl31A8s2Lly3K8VklKRTItGj00jJ2mZ9" ) );
			}
			else
			{
				params.add( new BasicNameValuePair( "USER", paramenters.getUser() ) );
				params.add( new BasicNameValuePair( "PWD", paramenters.getPassword() ) );
				params.add( new BasicNameValuePair( "SIGNATURE", paramenters.getSignature() ) );
			}
			params.add( new BasicNameValuePair( "TOKEN", paramenters.getToken() ) );
			params.add( new BasicNameValuePair( "PAYERID", paramenters.getPayerId() ) );
			params.add( new BasicNameValuePair( "PAYMENTREQUEST_0_AMT", paramenters.getAmount() ) );
			params.add( new BasicNameValuePair( "PAYMENTREQUEST_0_CURRENCYCODE", paramenters.getCurrency() ) );
			params.add( new BasicNameValuePair( "PAYMENTREQUEST_0_PAYMENTACTION", "Sale" ) );

			ServiceHandler handler = new ServiceHandler();
			String responseStr = handler.makeServiceCall( APIAddress.PAYPAL_MERCHANT_API, ServiceHandler.GET, params );
			android.util.Log.d( "PAYPAL", "RESPONSE: " + responseStr );

			PayPalDECResponse response;
			if( responseStr != null )
			{
				Map<String, String> parameters = getUrlParameters( responseStr );
				response = new PayPalDECResponse( parameters );
			}
			else
			{
				response = new PayPalDECResponse( new HashMap<String, String>() );
				response.setACK( "INET FAIL" );
			}
			_response = response;
		}
		else if( _task == REFUND_TRANSACTION )
		{
			PayPalRTParameters parameters = (PayPalRTParameters)_parameters;

			ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add( new BasicNameValuePair( "METHOD", "RefundTransaction" ) );
			params.add( new BasicNameValuePair( "VERSION", "98" ) );

			if( PAYPAL_MODE == MODE_SANDBOX )
			{
				params.add( new BasicNameValuePair( "USER", "hblancas_api1.gmail.com" ) );
				params.add( new BasicNameValuePair( "PWD", "1405447289" ) );
				params.add( new BasicNameValuePair( "SIGNATURE", "AFcWxV21C7fd0v3bYYYRCpSSRl31Aj0ann9Hr--f3qDoWBOBxT9XRGfb" ) );
			}
			else
			{
				params.add( new BasicNameValuePair( "USER", parameters.getUser() ) );
				params.add( new BasicNameValuePair( "PWD", parameters.getPassword() ) );
				params.add( new BasicNameValuePair( "SIGNATURE", parameters.getSignature() ) );
			}
			params.add( new BasicNameValuePair( "PAYERID", parameters.getPayerId() ) );
			params.add( new BasicNameValuePair( "REFUNDTYPE", "Full" ) );
			params.add( new BasicNameValuePair( "CURRENCYCODE", parameters.getCurrency() ) );
			params.add( new BasicNameValuePair( "TRANSACTIONID", parameters.getTransactionId() ) );

			ServiceHandler handler = new ServiceHandler();
			String responseStr = handler.makeServiceCall( APIAddress.PAYPAL_MERCHANT_API, ServiceHandler.GET, params );
			android.util.Log.d( "PAYPAL", "RESPONSE: " + responseStr );

			PayPalRTResponse response;
			if( responseStr != null )
			{
				Map<String, String> respParams = getUrlParameters( responseStr );
				response = new PayPalRTResponse( respParams );
			}
			else
			{
				response = new PayPalRTResponse( new HashMap<String, String>() );
				response.setACK( "INET FAIL" );
			}

			_response = response;
		}
		return null;
	}

	@Override
	protected void onPostExecute( Void arg0 )
	{
		super.onPostExecute( arg0 );
		_progress.dismiss();

		if( _task == INITIATE_PAYMENT )
		{
			PayPalSECResponse response = (PayPalSECResponse)_response;
			_listener.onPayPalPaymentInitiated( response );
		}
		else if( _task == DO_EXPRESS_CHECKOUT )
		{
			PayPalDECResponse response = (PayPalDECResponse)_response;
			_listener.onPayPalDoExpressCheckOut( response );
		}
		else if( _task == REFUND_TRANSACTION )
		{
			PayPalRTResponse response = (PayPalRTResponse)_response;
			_listener.onPayPalRefundTransaction( response );
		}
	}

	public static Map<String, String> getUrlParameters( String queryString )
	{
		//http://sevennet.org/2014/12/04/how-to-parsing-query-strings-on-android/
		Map<String, String> params = new HashMap<String, String>();
		try
		{
			for( String param : queryString.split( "&" ) )
			{
				String pair[] = param.split( "=" );
				String key = URLDecoder.decode( pair[0], "UTF-8" ).toUpperCase();
				String value = "";
				if( pair.length > 1 ) { value = URLDecoder.decode( pair[1], "UTF-8" ); }
				params.put( new String( key ), new String( value ) );
			}
		}
		catch( Exception e )
		{
			android.util.Log.d( "PAYPAL", "ERROR PARSING RESULT: " + e.getMessage() );
		}
		return params;
	}

	public interface PayPalCallerInterface
	{
		void onPayPalPaymentInitiated( PayPalSECResponse response );
		void onPayPalDoExpressCheckOut( PayPalDECResponse response );
		void onPayPalRefundTransaction( PayPalRTResponse response );
	}

	public static class PayPalRTResponse
	{
		private Map<String, String> _parameters;
		private String _transactionId;
		private String _ACK;
		private String _timeStamp;
		private String _correlationId;
		private String _version;
		private String _build;
		private String _errorCode;
		private String _shortMessage;
		private String _longMessage;
		private String _severityCode;

		public PayPalRTResponse( Map<String, String> parameters )
		{
			_parameters = parameters;

			_ACK = _parameters.get( "ACK" );
			_timeStamp = _parameters.get( "TIMESTAMP" );
			_correlationId = _parameters.get( "CORRELATIONID" );
			_version = _parameters.get( "VERSION" );
			_build = _parameters.get( "BUILD" );
			_errorCode = _parameters.get( "L_ERRORCODE0" );
			_shortMessage = _parameters.get( "L_SHORTMESSAGE0" );
			_longMessage = _parameters.get( "L_LONGMESSAGE0" );
			_severityCode = _parameters.get( "L_SEVERITYCODE0" );
			_transactionId = _parameters.get( "REFUNDTRANSACTIONID" );
		}

		public String getTransactionId()
		{
			return _transactionId;
		}

		public void setTransactionId( String transactionId )
		{
			_transactionId = transactionId;
		}

		public String getACK()
		{
			return _ACK;
		}

		public void setACK( String ACK )
		{
			_ACK = ACK;
		}

		public String getTimeStamp()
		{
			return _timeStamp;
		}

		public void setTimeStamp( String timeStamp )
		{
			_timeStamp = timeStamp;
		}

		public String getCorrelationId()
		{
			return _correlationId;
		}

		public void setCorrelationId( String correlationId )
		{
			_correlationId = correlationId;
		}

		public String getVersion()
		{
			return _version;
		}

		public void setVersion( String version )
		{
			_version = version;
		}

		public String getBuild()
		{
			return _build;
		}

		public void setBuild( String build )
		{
			_build = build;
		}

		public String getErrorCode()
		{
			return _errorCode;
		}

		public void setErrorCode( String errorCode )
		{
			_errorCode = errorCode;
		}

		public String getShortMessage()
		{
			return _shortMessage;
		}

		public void setShortMessage( String shortMessage )
		{
			_shortMessage = shortMessage;
		}

		public String getLongMessage()
		{
			return _longMessage;
		}

		public void setLongMessage( String longMessage )
		{
			_longMessage = longMessage;
		}

		public String getSeverityCode()
		{
			return _severityCode;
		}

		public void setSeverityCode( String severityCode )
		{
			_severityCode = severityCode;
		}
	}

	public static class PayPalRTParameters
	{
		private String _user;
		private String _password;
		private String _signature;
		private String _payerId;
		private String _currency;
		private String _transactionId;

		public String getUser()
		{
			return _user;
		}

		public void setUser( String user )
		{
			_user = user;
		}

		public String getPassword()
		{
			return _password;
		}

		public void setPassword( String password )
		{
			_password = password;
		}

		public String getSignature()
		{
			return _signature;
		}

		public void setSignature( String signature )
		{
			_signature = signature;
		}

		public String getPayerId()
		{
			return _payerId;
		}

		public void setPayerId( String payerId )
		{
			_payerId = payerId;
		}

		public String getCurrency()
		{
			return _currency;
		}

		public void setCurrency( String currency )
		{
			_currency = currency;
		}

		public String getTransactionId()
		{
			return _transactionId;
		}

		public void setTransactionId( String transactionId )
		{
			_transactionId = transactionId;
		}
	}

	public static class PayPalDECResponse
	{
		private Map<String, String> _parameters;
		private String _ACK;
		private String _token;
		private String _timeStamp;
		private String _correlationId;
		private String _version;
		private String _build;
		private String _errorCode;
		private String _shortMessage;
		private String _longMessage;
		private String _severityCode;
		private String _transactionId;
		private String _transactionType;
		private String _paymentType;
		private String _orderTime;
		private String _amount;
		private String _courrencyCode;
		private String _taxAmount;
		private String _paymentStatus;
		private String _pendingReason;
		private String _reasonCode;

		public PayPalDECResponse( Map<String, String> parameters )
		{
			_parameters = parameters;

			_ACK = _parameters.get( "ACK" );
			_token = _parameters.get( "TOKEN" );
			_timeStamp = _parameters.get( "TIMESTAMP" );
			_correlationId = _parameters.get( "CORRELATIONID" );
			_version = _parameters.get( "VERSION" );
			_build = _parameters.get( "BUILD" );
			_errorCode = _parameters.get( "L_ERRORCODE0" );
			_shortMessage = _parameters.get( "L_SHORTMESSAGE0" );
			_longMessage = _parameters.get( "L_LONGMESSAGE0" );
			_severityCode = _parameters.get( "L_SEVERITYCODE0" );
			_transactionId = _parameters.get( "PAYMENTINFO_0_TRANSACTIONID" );
			_transactionType = _parameters.get( "PAYMENTINFO_0_TRANSACTIONTYPE" );
			_paymentType = _parameters.get( "PAYMENTINFO_0_PAYMENTTYPE" );
			_orderTime = _parameters.get( "PAYMENTINFO_0_ORDERTIME" );
			_amount = _parameters.get( "PAYMENTINFO_0_AMT" );
			_courrencyCode = _parameters.get( "PAYMENTINFO_0_CURRENCYCODE" );
			_taxAmount = _parameters.get( "PAYMENTINFO_0_TAXAMT" );
			_paymentStatus = _parameters.get( "PAYMENTINFO_0_PAYMENTSTATUS" );
			_pendingReason = _parameters.get( "PAYMENTINFO_0_PENDINGREASON" );
			_reasonCode = _parameters.get( "PAYMENTINFO_0_REASONCODE" );
		}

		public Map<String, String> getParameters()
		{
			return _parameters;
		}

		public void setParameters( Map<String, String> parameters )
		{
			_parameters = parameters;
		}

		public String getACK()
		{
			return _ACK;
		}

		public void setACK( String ACK )
		{
			_ACK = ACK;
		}

		public String getToken()
		{
			return _token;
		}

		public void setToken( String token )
		{
			_token = token;
		}

		public String getTimeStamp()
		{
			return _timeStamp;
		}

		public void setTimeStamp( String timeStamp )
		{
			_timeStamp = timeStamp;
		}

		public String getCorrelationId()
		{
			return _correlationId;
		}

		public void setCorrelationId( String correlationId )
		{
			_correlationId = correlationId;
		}

		public String getVersion()
		{
			return _version;
		}

		public void setVersion( String version )
		{
			_version = version;
		}

		public String getBuild()
		{
			return _build;
		}

		public void setBuild( String build )
		{
			_build = build;
		}

		public String getErrorCode()
		{
			return _errorCode;
		}

		public void setErrorCode( String errorCode )
		{
			_errorCode = errorCode;
		}

		public String getShortMessage()
		{
			return _shortMessage;
		}

		public void setShortMessage( String shortMessage )
		{
			_shortMessage = shortMessage;
		}

		public String getLongMessage()
		{
			return _longMessage;
		}

		public void setLongMessage( String longMessage )
		{
			_longMessage = longMessage;
		}

		public String getSeverityCode()
		{
			return _severityCode;
		}

		public void setSeverityCode( String severityCode )
		{
			_severityCode = severityCode;
		}

		public String getTransactionId()
		{
			return _transactionId;
		}

		public void setTransactionId( String transactionId )
		{
			_transactionId = transactionId;
		}

		public String getTransactionType()
		{
			return _transactionType;
		}

		public void setTransactionType( String transactionType )
		{
			_transactionType = transactionType;
		}

		public String getPaymentType()
		{
			return _paymentType;
		}

		public void setPaymentType( String paymentType )
		{
			_paymentType = paymentType;
		}

		public String getOrderTime()
		{
			return _orderTime;
		}

		public void setOrderTime( String orderTime )
		{
			_orderTime = orderTime;
		}

		public String getAmount()
		{
			return _amount;
		}

		public void setAmount( String amount )
		{
			_amount = amount;
		}

		public String getCourrencyCode()
		{
			return _courrencyCode;
		}

		public void setCourrencyCode( String courrencyCode )
		{
			_courrencyCode = courrencyCode;
		}

		public String getTaxAmount()
		{
			return _taxAmount;
		}

		public void setTaxAmount( String taxAmount )
		{
			_taxAmount = taxAmount;
		}

		public String getPaymentStatus()
		{
			return _paymentStatus;
		}

		public void setPaymentStatus( String paymentStatus )
		{
			_paymentStatus = paymentStatus;
		}

		public String getPendingReason()
		{
			return _pendingReason;
		}

		public void setPendingReason( String pendingReason )
		{
			_pendingReason = pendingReason;
		}

		public String getReasonCode()
		{
			return _reasonCode;
		}

		public void setReasonCode( String reasonCode )
		{
			_reasonCode = reasonCode;
		}
	}

	public static class PayPalDECParameters
	{
		private String _user;
		private String _password;
		private String _signature;
		private String _token;
		private String _payerId;
		private String _amount;
		private String _currency;

		public String getUser()
		{
			return _user;
		}

		public void setUser( String user )
		{
			_user = user;
		}

		public String getPassword()
		{
			return _password;
		}

		public void setPassword( String password )
		{
			_password = password;
		}

		public String getSignature()
		{
			return _signature;
		}

		public void setSignature( String signature )
		{
			_signature = signature;
		}

		public String getToken()
		{
			return _token;
		}

		public void setToken( String token )
		{
			_token = token;
		}

		public String getPayerId()
		{
			return _payerId;
		}

		public void setPayerId( String payerId )
		{
			_payerId = payerId;
		}

		public String getAmount()
		{
			return _amount;
		}

		public void setAmount( String amount )
		{
			_amount = amount;
		}

		public String getCurrency()
		{
			return _currency;
		}

		public void setCurrency( String currency )
		{
			_currency = currency;
		}
	}

	public static class PayPalReturnResponse
	{
		private Map<String, String> _parameters;
		private String _token;
		private String _payerId;

		public PayPalReturnResponse( String querySring )
		{
			_parameters = getUrlParameters( querySring );
			_token = _parameters.get( "TOKEN" );
			_payerId = _parameters.get( "PAYERID" );
		}

		public Map<String, String> getParameters()
		{
			return _parameters;
		}

		public void setParameters( Map<String, String> parameters )
		{
			_parameters = parameters;
		}

		public String getToken()
		{
			return _token;
		}

		public void setToken( String token )
		{
			_token = token;
		}

		public String getPayerId()
		{
			return _payerId;
		}

		public void setPayerId( String payerId )
		{
			_payerId = payerId;
		}
	}

	public static class PayPalSECResponse
	{
		private Map<String, String> _parameters;
		private String _ACK;
		private String _token;
		private String _timeStamp;
		private String _correlationId;
		private String _version;
		private String _build;
		private String _errorCode;
		private String _shortMessage;
		private String _longMessage;
		private String _severityCode;

		public PayPalSECResponse( Map<String, String> parameters )
		{
			_parameters = parameters;

			_ACK = _parameters.get( "ACK" );
			_token = _parameters.get( "TOKEN" );
			_timeStamp = _parameters.get( "TIMESTAMP" );
			_correlationId = _parameters.get( "CORRELATIONID" );
			_version = _parameters.get( "VERSION" );
			_build = _parameters.get( "BUILD" );
			_errorCode = _parameters.get( "L_ERRORCODE0" );
			_shortMessage = _parameters.get( "L_SHORTMESSAGE0" );
			_longMessage = _parameters.get( "L_LONGMESSAGE0" );
			_severityCode = _parameters.get( "L_SEVERITYCODE0" );
		}

		public Map<String, String> getParameters()
		{
			return _parameters;
		}

		public void setParameters( Map<String, String> parameters )
		{
			_parameters = parameters;
		}

		public String getACK()
		{
			return _ACK;
		}

		public void setACK( String ACK )
		{
			_ACK = ACK;
		}

		public String getToken()
		{
			return _token;
		}

		public void setToken( String token )
		{
			_token = token;
		}

		public String getTimeStamp()
		{
			return _timeStamp;
		}

		public void setTimeStamp( String timeStamp )
		{
			_timeStamp = timeStamp;
		}

		public String getCorrelationId()
		{
			return _correlationId;
		}

		public void setCorrelationId( String correlationId )
		{
			_correlationId = correlationId;
		}

		public String getVersion()
		{
			return _version;
		}

		public void setVersion( String version )
		{
			_version = version;
		}

		public String getBuild()
		{
			return _build;
		}

		public void setBuild( String build )
		{
			_build = build;
		}

		public String getErrorCode()
		{
			return _errorCode;
		}

		public void setErrorCode( String errorCode )
		{
			_errorCode = errorCode;
		}

		public String getShortMessage()
		{
			return _shortMessage;
		}

		public void setShortMessage( String shortMessage )
		{
			_shortMessage = shortMessage;
		}

		public String getLongMessage()
		{
			return _longMessage;
		}

		public void setLongMessage( String longMessage )
		{
			_longMessage = longMessage;
		}

		public String getSeverityCode()
		{
			return _severityCode;
		}

		public void setSeverityCode( String severityCode )
		{
			_severityCode = severityCode;
		}
	}

	public static class PayPalPayment
	{
		private String _user;
		private String _password;
		private String _signature;
		private String _returnURL;
		private String _cancelURL;
		private String _cartBorderColor;
		private String _logoImg;

		private String _itemName;
		private String _itemSKU;
		private String _itemDesc;
		private String _itemAmount;

		private String _amount;
		private String _currencyCode;

		public String getItemName()
		{
			return _itemName;
		}

		public void setItemName( String itemName )
		{
			_itemName = itemName;
		}

		public String getItemSKU()
		{
			return _itemSKU;
		}

		public void setItemSKU( String itemSKU )
		{
			_itemSKU = itemSKU;
		}

		public String getItemDesc()
		{
			return _itemDesc;
		}

		public void setItemDesc( String itemDesc )
		{
			_itemDesc = itemDesc;
		}

		public String getItemAmount()
		{
			return _itemAmount;
		}

		public void setItemAmount( String itemAmount )
		{
			_itemAmount = itemAmount;
		}

		public String getUser()
		{
			return _user;
		}

		public void setUser( String user )
		{
			_user = user;
		}

		public String getPassword()
		{
			return _password;
		}

		public void setPassword( String password )
		{
			_password = password;
		}

		public String getSignature()
		{
			return _signature;
		}

		public void setSignature( String signature )
		{
			_signature = signature;
		}

		public String getCurrencyCode()
		{
			return _currencyCode;
		}

		public void setCurrencyCode( String currencyCode )
		{
			_currencyCode = currencyCode;
		}

		public String getLogoImg()
		{
			return _logoImg;
		}

		public void setLogoImg( String logoImg )
		{
			_logoImg = logoImg;
		}

		public String getAmount()
		{
			return _amount;
		}

		public void setAmount( String amount )
		{
			_amount = amount;
		}

		public String getReturnURL()
		{
			return _returnURL;
		}

		public void setReturnURL( String returnURL )
		{
			_returnURL = returnURL;
		}

		public String getCancelURL()
		{
			return _cancelURL;
		}

		public void setCancelURL( String cancelURL )
		{
			_cancelURL = cancelURL;
		}

		public String getCartBorderColor()
		{
			return _cartBorderColor;
		}

		public void setCartBorderColor( String cartBorderColor )
		{
			_cartBorderColor = cartBorderColor;
		}
	}
}
