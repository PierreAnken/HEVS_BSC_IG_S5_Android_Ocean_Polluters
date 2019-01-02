package ch.pa.oceanspolluters.app.util;


import java.util.ArrayList;

public interface OnAsyncEventListener {
    void onSuccess(ArrayList resultData);
    void onFailure(Exception e);
}
