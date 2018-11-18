package ch.pa.oceanspolluters.app.util;


import java.util.List;

import ch.pa.oceanspolluters.app.database.entity.ItemTypeEntity;

public interface OnAsyncEventListener {
    void onSuccess(List<ItemTypeEntity> result);
    void onFailure(Exception e);
}
