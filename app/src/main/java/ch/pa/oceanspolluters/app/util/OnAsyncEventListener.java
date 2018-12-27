package ch.pa.oceanspolluters.app.util;


import java.util.ArrayList;
import java.util.List;

import ch.pa.oceanspolluters.app.database.entity.ItemTypeEntity;

public interface OnAsyncEventListener {
    void onSuccess(ArrayList resultData);
    void onFailure(Exception e);
}
