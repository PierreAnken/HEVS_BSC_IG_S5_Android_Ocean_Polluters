package ch.pa.oceanspolluters.app.util;


public interface OnAsyncEventListener {
    void onSuccess();
    void onFailure(Exception e);
}
