package com.ns.yc.lifehelper.db.realm;

import android.text.TextUtils;

import com.ns.yc.lifehelper.comment.Constant;
import com.ns.yc.lifehelper.base.app.BaseApplication;
import com.ns.yc.lifehelper.ui.other.workDo.data.DateUtils;
import com.ns.yc.lifehelper.ui.other.workDo.model.TaskDetailEntity;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class RealmWorkDoHelper {

    private static volatile RealmWorkDoHelper mDataDao;
    private Realm realm;

    private RealmWorkDoHelper() {
        initRealm();
    }

    private void initRealm() {
        if(realm==null){
            realm = BaseApplication.getInstance().getRealmHelper();
        }
    }


    public static RealmWorkDoHelper getInstance() {
        if (mDataDao == null) {
            synchronized (RealmWorkDoHelper.class) {
                if (mDataDao == null) {
                    mDataDao = new RealmWorkDoHelper();
                }
            }
        }
        return mDataDao;
    }


    public void close() {
        if(mDataDao!=null){
            mDataDao = null;
        }
    }


    public void insertTask(final TaskDetailEntity taskDetailEntity) {
        realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        realm.copyToRealm(taskDetailEntity);
                    }
                });
    }

    public RealmResults<TaskDetailEntity> findAllTask() {
        return realm.where(TaskDetailEntity.class)
                .findAllSorted("timeStamp");
    }


    public RealmResults<TaskDetailEntity> findAllTask(int dayOfWeek) {
        return realm
                .where(TaskDetailEntity.class)
                .equalTo("dayOfWeek", dayOfWeek)
                .findAllSorted("timeStamp");
    }

    public RealmResults<TaskDetailEntity> findUnFinishedTasks(int dayOfWeek) {
        return realm
                .where(TaskDetailEntity.class)
                .equalTo("dayOfWeek", dayOfWeek)
                .notEqualTo("state", Constant.TaskState.FINISHED)
                .findAllSorted("timeStamp");
    }


    public RealmResults<TaskDetailEntity> findAllTaskOfThisWeekFromSunday() {
        long sundayTimeMillisOfWeek = DateUtils.getFirstSundayTimeMillisOfWeek();
        return realm
                .where(TaskDetailEntity.class)
                .greaterThan("timeStamp", sundayTimeMillisOfWeek)
                .findAll();
    }


    public void editTask(final TaskDetailEntity oldTask, final TaskDetailEntity newTask) {
        realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        oldTask.setTaskDetailEntity(newTask);
                    }
                });
    }


    public void deleteTask(final TaskDetailEntity entity) {
        realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        TaskDetailEntity first = realm.where(TaskDetailEntity.class)
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


    public List<TaskDetailEntity> search(String like) {
        if (TextUtils.isEmpty(like)) {
            throw new IllegalArgumentException("str is null");
        }
        return realm.where(TaskDetailEntity.class)
                .contains("content", like)
                .or()
                .contains("title", like)
                .findAllSorted("timeStamp", Sort.DESCENDING);
    }


    public void switchTaksState(final TaskDetailEntity entity, final int state) {
        realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        entity.setState(state);
                    }
                });
    }


}
