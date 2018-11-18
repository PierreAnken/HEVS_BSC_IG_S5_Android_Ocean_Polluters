package ch.pa.oceanspolluters.app.util;


import java.util.List;

public interface OnAsyncEventListener<T> {
    void onSuccess(List<T> result);
    void onFailure(Exception e);
}
