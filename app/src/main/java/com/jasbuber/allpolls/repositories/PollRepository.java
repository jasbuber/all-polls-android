package com.jasbuber.allpolls.repositories;

import com.jasbuber.allpolls.models.Poll;
import com.jasbuber.allpolls.models.orm.PollORM;

import java.util.List;

import io.realm.Realm;

/**
 * Created by Jasbuber on 16/06/2016.
 */
public class PollRepository {

    public void createOrUpdatePoll(final PollORM poll){
        Realm realm = getRealmInstance();

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(poll);
            }
        });
    }

    public List<PollORM> getMyPolls(){
        Realm realm = getRealmInstance();

        return realm.where(PollORM.class).findAll();
    }

    public PollORM getPoll(long id){
        Realm realm = getRealmInstance();

        return realm.where(PollORM.class).equalTo("id", id).findFirst();
    }

    public boolean pollExists(long id) {

        Realm realm = getRealmInstance();

        return realm.where(PollORM.class).equalTo("id", id).findFirst() != null;
    }

    protected Realm getRealmInstance(){
        return Realm.getDefaultInstance();
    }
}
