package ch.pa.oceanspolluters.app.database;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import java.util.List;

import ch.pa.oceanspolluters.app.BaseApp;
import ch.pa.oceanspolluters.app.database.entity.BaseEntity;
import ch.pa.oceanspolluters.app.database.entity.ContainerEntity;
import ch.pa.oceanspolluters.app.database.entity.ItemEntity;
import ch.pa.oceanspolluters.app.database.entity.ItemTypeEntity;
import ch.pa.oceanspolluters.app.database.entity.PortEntity;
import ch.pa.oceanspolluters.app.database.entity.ShipEntity;
import ch.pa.oceanspolluters.app.database.entity.UserEntity;
import ch.pa.oceanspolluters.app.util.OnAsyncEventListener;
import ch.pa.oceanspolluters.app.util.OperationMode;

public class AsyncOperationOnEntity extends AsyncTask<BaseEntity, Void, Void> {

    private static final String TAG = "AsyncOperationOnEntity";

    private Application mApplication;
    private OnAsyncEventListener mCallBack;
    private Exception mException;
    private List<ItemTypeEntity> mListEntities = null;

    public AsyncOperationOnEntity(Application application, OnAsyncEventListener callback) {
        mApplication = application;
        mCallBack = callback;
    }

    @Override
    protected Void doInBackground(BaseEntity... params) {
        try {
            BaseEntity entity = params[0];
            OperationMode mode = entity.getOperationMode();

            Log.d(TAG, "PA_Debug asynch: " + entity.getClass().toString()+" "+entity.getOperationMode().toString());

            if (entity.getClass() == ItemTypeEntity.class) {

                if (mode == OperationMode.GetAll) {
                    mListEntities = ((BaseApp) mApplication).getItemTypeRepository().getAll();
                    Log.d(TAG, "PA_Debug getting type from repository: " + mListEntities.size());
                }
            } else if (entity.getClass() == ShipEntity.class) {
                ShipEntity ship = (ShipEntity)entity;

                if(mode == OperationMode.Save){
                    if(ship.getId() == null){
                        ((BaseApp) mApplication).getShipRepository()
                                .insert(ship);
                    } else {
                        ((BaseApp) mApplication).getShipRepository()
                                .update(ship);
                    }
                } else if (mode == OperationMode.Delete) {
                    ((BaseApp) mApplication).getShipRepository()
                            .delete(ship);
                }
            } else if (entity.getClass() == ContainerEntity.class) {
                ContainerEntity container = (ContainerEntity) entity;

                if (mode == OperationMode.Save) {
                    if (container.getId() == null) {
                        ((BaseApp) mApplication).getContainerRepository()
                                .insert(container);
                    } else {
                        ((BaseApp) mApplication).getContainerRepository()
                                .update(container);
                    }
                } else if (mode == OperationMode.Delete) {
                    ((BaseApp) mApplication).getContainerRepository()
                            .delete(container);
                }
            } else if (entity.getClass() == ItemEntity.class) {
                ItemEntity item = (ItemEntity) entity;

                if (mode == OperationMode.Save) {
                    if (item.getId() == null) {
                        ((BaseApp) mApplication).getItemRepository()
                                .insert(item);
                    } else {
                        ((BaseApp) mApplication).getItemRepository()
                                .update(item);
                    }
                } else if (mode == OperationMode.Delete) {
                    ((BaseApp) mApplication).getItemRepository()
                            .delete(item);
                }
            }else if(entity.getClass() == PortEntity.class) {
                PortEntity port = (PortEntity) entity;

                if (mode == OperationMode.Save) {
                    if (port.getId() == null) {
                        ((BaseApp) mApplication).getPortRepository()
                                .insert(port);
                    } else {
                        ((BaseApp) mApplication).getPortRepository()
                                .update(port);
                    }
                } else if (mode == OperationMode.Delete) {
                    ((BaseApp) mApplication).getPortRepository()
                            .delete(port);
                }
            }else if(entity.getClass() == UserEntity.class) {
                UserEntity user = (UserEntity) entity;

                if (mode == OperationMode.Save) {
                    if (user.getId() == null) {
                        ((BaseApp) mApplication).getUserRepository()
                                .insert(user);
                    } else {
                        ((BaseApp) mApplication).getUserRepository()
                                .update(user);
                    }
                } else if (mode == OperationMode.Delete) {
                    ((BaseApp) mApplication).getUserRepository()
                            .delete(user);
                }
            }

        } catch (Exception e) {
            Log.d(TAG, "PA_Debug asynch: " + e.toString());
            mException = e;
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        if (mCallBack != null) {
            if (mException == null) {
                mCallBack.onSuccess(mListEntities);
            } else {
                mCallBack.onFailure(mException);
            }
        }
    }
}
