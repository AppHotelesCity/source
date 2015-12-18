package com.zebstudios.cityexpress;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by usuario on 18/12/15.
 */
public class SmartCard {

    public String CardNumber;
    public String OwnerName;
    public String Alias;
    public String IdCard;
    public String ccv;

    public void loadinfo(JSONObject tarjeta){
        try {

            CardNumber = tarjeta.getString("CardNumber");
            OwnerName = tarjeta.getString("OwnerName");
            Alias = tarjeta.getString("Alias");
            IdCard = tarjeta.getString("IdCard");


        } catch (JSONException e) {
            Log.e("JSON exception", e.getMessage());
            e.printStackTrace();
        }

    }
}
