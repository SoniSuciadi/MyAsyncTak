package com.sonisuciadi.si52.myasynctak;

public interface MyAsyncTaksCallBack  {
    void onPreExecute();
    void onUpdateProgres(Long value);
    void onPostExecute(String text);
}
