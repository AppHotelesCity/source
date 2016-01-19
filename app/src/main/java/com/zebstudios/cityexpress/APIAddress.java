package com.zebstudios.cityexpress;

/**
 * Created by Denumeris Interactive on 2/24/2015.
 */
public class APIAddress
{

	//https://www.cityexpress.com/umbraco/api/MobileAppServices/GetAllHotels
	public static final String HOTELS_WEB_BASE = "https://www.cityexpress.com";
	public static final String HOTELS_API_MOBILE = "https://www.cityexpress.com/umbraco/api/MobileAppServices";

	//TEST
	//public static final String PAYPAL_MERCHANT_API = "https://api-3t.sandbox.paypal.com/nvp";
	//public static final String PAYPAL_MERCHANT_WEB = "https://www.sandbox.paypal.com/cgi-bin/webscr";
	//public static final String RESERVATION_ENGINE_URL = "http://wshc.hotelescity.com:9742/wsMotor2014/ReservationEngine.svc";
	//public static final String CITYPREMIOS_ENGINE_URL = "http://wshc.hotelescity.com:9742/wcfCityPremios_Des/CityPremios/wcfCityPremiosExterno.svc";
	//public static final int PAYPAL_MODE = 2;

	//PRODUCTION
	public static final String PAYPAL_MERCHANT_API = "https://api-3t.paypal.com/nvp";
	public static final String PAYPAL_MERCHANT_WEB = "https://www.paypal.com/cgi-bin/webscr";
	public static final String RESERVATION_ENGINE_URL = "http://wshc.hotelescity.com:9742/wsMotorapp/ReservationEngine.svc";
	public static final String CITYPREMIOS_ENGINE_URL = "http://wshc.hotelescity.com:9742/wcfCityPremios_Des/CityPremios/wcfCityPremiosExterno.svc"; //quitar _Des
	public static final int PAYPAL_MODE = 1;


	//SharedPrefereces
	public static String LOGIN_USUARIO_PREFERENCES = "datosUsuarioPreferences";

	public static String CLIENTE_UNICO = "http://wshc.hotelescity.com:9742/WS_Smart_Des/SmartAdmin.asmx?wsdl";

	public static String URL_RESERVACIONES = "http://wshc.hotelescity.com:9742/wsMotor2014/ReservationEngine.svc";

	public static String URL_CHECKS = "http://wshc.hotelescity.com:9742/WHS_Reservations_Des/WHSReservationEngine.svc?wsdl";
}
