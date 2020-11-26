package com.george.hook;

public class ViewState {
    private long onclickTime;
    private boolean isReqest;

    public ViewState(long onclickTime) {
        this(onclickTime, false);
    }

    public ViewState(long onclickTime, boolean isReqest) {
        this.onclickTime = onclickTime;
        this.isReqest = isReqest;
    }

    public long getOnclickTime() {
        return onclickTime;
    }

    public void setOnclickTime(long onclickTime) {
        this.onclickTime = onclickTime;
    }

    public boolean isReqest() {
        return isReqest;
    }
}
