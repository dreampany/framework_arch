package com.dreampany.framework.data.model;

import com.dreampany.framework.data.enums.NotifyType;

/**
 * Created by nuc on 6/13/2017.
 */

public class NotifyEvent extends Event<NotifyType> {
    public String id;
    public String title;
    public String comment;
}
