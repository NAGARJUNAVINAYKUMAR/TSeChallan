package com.tspolice.echallan.network;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.tspolice.echallan.R;
import com.tspolice.echallan.utils.UiHelper;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

public class SoapController extends AsyncTask<Void, Void, String> {

    private SoapObject mSoapObject;
    private SoapResponseCallbackListener delegate;
    @SuppressLint("StaticFieldLeak")
    private Context mContext;
    private UiHelper mUiHelper;
    private String flag;

    public SoapController(Context context, SoapObject soapObject, SoapResponseCallbackListener listener) {
        this.mContext = context;
        this.mSoapObject = soapObject;
        this.delegate = listener;
        this.mUiHelper = new UiHelper(mContext);
        mUiHelper.showProgressDialog("" + mContext.getString(R.string.please_wait), false);
    }

    public SoapController(Context context, SoapObject soapObject, SoapResponseCallbackListener listener, String flag) {
        this.mContext = context;
        this.mSoapObject = soapObject;
        this.delegate = listener;
        this.mUiHelper = new UiHelper(mContext);
        this.flag = flag;
        mUiHelper.showProgressDialog("" + mContext.getString(R.string.please_wait), false);
    }

    @Override
    protected String doInBackground(Void... voids) {
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(mSoapObject);
        HttpTransportSE transport = new HttpTransportSE(URLs.url, 10);
        String response = null;
        try {
            transport.call(URLs.soapAction, envelope);
        } catch (IOException e) {
            e.printStackTrace();
            Log.i("SoapController-->", ", IOException");
            if (mUiHelper != null) {
                mUiHelper.dismissProgressDialog();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
            Log.i("SoapController-->", ", XmlPullParserException");
            if (mUiHelper != null) {
                mUiHelper.dismissProgressDialog();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("SoapController-->", ", Exception");
            if (mUiHelper != null) {
                mUiHelper.dismissProgressDialog();
            }
        }
        //Object result = null;
        try {
            response = envelope.getResponse().toString();
        } catch (SoapFault fault) {
            /*result = "SOAP_FAULT";
            fault.printStackTrace();
            if (this.delegate != null) {
                this.delegate.onSoapResponse(result.toString(), flag);
                if (mUiHelper != null) {
                    mUiHelper.dismissProgressDialog();
                }
            }*/
            //response = "";
            fault.printStackTrace();
            Log.i("SoapController-->", ", SoapFault");
            if (mUiHelper != null) {
                mUiHelper.dismissProgressDialog();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("SoapController-->", ", Exception");
            if (mUiHelper != null) {
                mUiHelper.dismissProgressDialog();
            }
        }

        try {
            //assert result != null;
            response = new PidSecEncrypt().decrypt(response);
        } catch (Exception e) {
            Log.i("SoapController-->", ", Exception");
            e.printStackTrace();
            if (mUiHelper != null) {
                mUiHelper.dismissProgressDialog();
            }
        }
        return response;
    }

    @Override
    protected void onPostExecute(String response) {
        super.onPostExecute(response);
        if (delegate != null) {
            if (response != null) {
                this.delegate.onSoapResponse(response, flag);
                if (mUiHelper != null) {
                    mUiHelper.dismissProgressDialog();
                }
            }
        }
    }
}