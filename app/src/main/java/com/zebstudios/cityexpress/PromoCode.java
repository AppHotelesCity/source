package com.zebstudios.cityexpress;

import android.util.Log;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.Comparator;

/**
 * *Created by Denumeris Interactive on 23/10/2014.
 */
public class PromoCode implements Serializable
{
    private static final long serialVersionUID = 0L;

    private String _titulo_promo;
    private String _numpromocode;
    private boolean _publica;
    private String _codigotarifa;
    private boolean _parsedOk;

    public PromoCode( JSONObject json )
    {
        try
        {

            _titulo_promo = json.getString("Titulo");
            _numpromocode = json.getString("NumPromoCode");
            _publica = json.getBoolean("promoPublica");
            _codigotarifa = json.getString("codigoTarifa");

            Log.e("PromoCode", "Nombre de Promo - " + _titulo_promo + " - Codigo Tarifa ->" +_codigotarifa );

            _parsedOk = true;
        }
        catch( Exception e )
        {
            _parsedOk = false;
            android.util.Log.e( "PromoCode", "Cant parse estado: " + e.getMessage() );
        }
    }

    public PromoCode( String codigotarifa, String titulo_promo )
    {
        _codigotarifa = codigotarifa;
        _titulo_promo = titulo_promo;
    }

    public String gettitulo_promo(){return  _titulo_promo;}

    public String getnumpromocode(){ return  _numpromocode;}

    public Boolean getPublica(){return _publica;}

    public String getCodigoTarifa(){ return _codigotarifa; }

    public boolean isParsedOk()
    {
        return _parsedOk;
    }

    public static class PromoCodeComparator implements Comparator<PromoCode>
    {
        public int compare( PromoCode c1, PromoCode c2 )
        {
            return c1.gettitulo_promo().compareToIgnoreCase( c2.gettitulo_promo() );
        }
    }
}