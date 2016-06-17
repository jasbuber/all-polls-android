package com.jasbuber.allpolls;

import com.jasbuber.allpolls.models.orm.PollORM;

/**
 * Created by Jasbuber on 16/06/2016.
 */
public interface OnListMyPollsInteractionListener {
    void onListFragmentInteraction(PollORM poll);
}
