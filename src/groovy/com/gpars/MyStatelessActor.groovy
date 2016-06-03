package com.gpars

import groovyx.gpars.actor.DynamicDispatchActor

class MyStatelessActor extends DynamicDispatchActor {

    public void onMessage(final String msg) {
        println("****** Received a string " + msg);
        replyIfExists("Thank you");
    }

    public void onMessage(final Integer msg) {
        println("******* Received a number " + msg);
        replyIfExists("Thank you");
    }

    public void onMessage(final Object msg) {
        println("****** Received an object " + msg);
        replyIfExists("Thank you");
    }
}
