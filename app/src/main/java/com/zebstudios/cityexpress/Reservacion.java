package com.zebstudios.cityexpress;

/**
 * Created by DanyCarreto on 14/12/15.
 */
public class Reservacion {
    String descripcionError;
    String numReservacion;
    String SMART_AutCode;
    String SMART_Error;
    String SMART_NumAfiliacion;
    String SMART_RefSPNum;
    String SMART_Resp_Code;
    String SMART_Resp_Message;
    String TotalRate;


    public Reservacion() {
    }

    public Reservacion(String descripcionError, String numReservacion, String SMART_AutCode, String SMART_Error, String SMART_NumAfiliacion, String SMART_RefSPNum, String SMART_Resp_Code, String SMART_Resp_Message, String totalRate) {
        this.descripcionError = descripcionError;
        this.numReservacion = numReservacion;
        this.SMART_AutCode = SMART_AutCode;
        this.SMART_Error = SMART_Error;
        this.SMART_NumAfiliacion = SMART_NumAfiliacion;
        this.SMART_RefSPNum = SMART_RefSPNum;
        this.SMART_Resp_Code = SMART_Resp_Code;
        this.SMART_Resp_Message = SMART_Resp_Message;
        TotalRate = totalRate;
    }

    public String getDescripcionError() {
        return descripcionError;
    }

    public void setDescripcionError(String descripcionError) {
        this.descripcionError = descripcionError;
    }

    public String getNumReservacion() {
        return numReservacion;
    }

    public void setNumReservacion(String numReservacion) {
        this.numReservacion = numReservacion;
    }

    public String getSMART_AutCode() {
        return SMART_AutCode;
    }

    public void setSMART_AutCode(String SMART_AutCode) {
        this.SMART_AutCode = SMART_AutCode;
    }

    public String getSMART_Error() {
        return SMART_Error;
    }

    public void setSMART_Error(String SMART_Error) {
        this.SMART_Error = SMART_Error;
    }

    public String getSMART_NumAfiliacion() {
        return SMART_NumAfiliacion;
    }

    public void setSMART_NumAfiliacion(String SMART_NumAfiliacion) {
        this.SMART_NumAfiliacion = SMART_NumAfiliacion;
    }

    public String getSMART_RefSPNum() {
        return SMART_RefSPNum;
    }

    public void setSMART_RefSPNum(String SMART_RefSPNum) {
        this.SMART_RefSPNum = SMART_RefSPNum;
    }

    public String getSMART_Resp_Code() {
        return SMART_Resp_Code;
    }

    public void setSMART_Resp_Code(String SMART_Resp_Code) {
        this.SMART_Resp_Code = SMART_Resp_Code;
    }

    public String getSMART_Resp_Message() {
        return SMART_Resp_Message;
    }

    public void setSMART_Resp_Message(String SMART_Resp_Message) {
        this.SMART_Resp_Message = SMART_Resp_Message;
    }

    public String getTotalRate() {
        return TotalRate;
    }

    public void setTotalRate(String totalRate) {
        TotalRate = totalRate;
    }
}
