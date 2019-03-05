package com.ycbjie.library.db.realm;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.ycbjie.library.base.config.AppConfig;
import com.ycbjie.library.constant.Constant;
import com.ycbjie.library.db.cache.CacheTaskDetailEntity;
import com.ycbjie.library.utils.time.DateUtils;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class RealmWorkDoHelper {

    private static volatile RealmWorkDoHelper mDataDao;
    private Realm realm;

    private RealmWorkDoHelper() {
        initRealm();
    }

    private void initRealm() {
        if(realm==null){
            realm = AppConfig.INSTANCE.getRealmHelper();
        }
    }


    public static RealmWorkDoHelper getInstance() {
        if (mDataDao == null) {
            mDataDao = new RealmWorkDoHelper();
        }
        return mDataDao;
    }


    public void close() {
        if(mDataDao!=null){
            mDataDao = null;
        }
    }


    public void insertTask(final CacheTaskDetailEntity taskDetailEntity) {
        realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        realm.copyToRealm(taskDetailEntity);
                    }
                });
    }

    public RealmResults<CacheTaskDetailEntity> findAllTask() {
        return realm.where(CacheTaskDetailEntity.class)
                .findAll();
    }


    public RealmResults<CacheTaskDetailEntity> findAllTask(int dayOfWeek) {
        return realm
                .where(CacheTaskDetailEntity.class)
                .equalTo("dayOfWeek", dayOfWeek)
                .findAll();
    }

    public RealmResults<CacheTaskDetailEntity> findUnFinishedTasks(int dayOfWeek) {
        return realm
                .where(CacheTaskDetailEntity.class)
                .equalTo("dayOfWeek", dayOfWeek)
                .notEqualTo("state", Constant.TaskState.FINISHED)
                .findAll();
    }


    public RealmResults<CacheTaskDetailEntity> findAllTaskOfThisWeekFromSunday() {
        long sundayTimeMillisOfWeek = DateUtils.getFirstSundayTimeMillisOfWeek();
        return realm
                .where(CacheTaskDetailEntity.class)
                .greaterThan("timeStamp", sundayTimeMillisOfWeek)
                .findAll();
    }


    public void editTask(final CacheTaskDetailEntity oldTask, final CacheTaskDetailEntity newTask) {
        realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(@NonNull Realm realm) {
                        oldTask.setTaskDetailEntity(newTask);
                    }
                });
    }


    public void deleteTask(final CacheTaskDetailEntity entity) {
        realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        CacheTaskDetailEntity first = realm.where(CacheTaskDetailEntity.class)
                                .equalTo("dayOfWeek", entity.getDayOfWeek())
                                .equalTo("title", entity.getTitle())
                                .equalTo("content", entity.getContent())
                                .equalTo("icon", entity.getIcon())
                                .equalTo("priority", entity.getPriority())
                                .equalTo("state", entity.getState())
                                .equalTo("timeStamp", entity.getTimeStamp())
                                .findFirst();
                        if (first != null){
                            first.deleteFromRealm();
                        }
                    }
                });
    }


    public List<CacheTaskDetailEntity> search(String like) {
        if (TextUtils.isEmpty(like)) {
            throw new IllegalArgumentException("str is null");
        }
        return realm.where(CacheTaskDetailEntity.class)
                .contains("content", like)
                .or()
                .contains("title", like)
                .findAll();
    }


    public void switchTaksState(final CacheTaskDetailEntity entity, final int state) {
        realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        entity.setState(state);
                    }
                });
    }


}
