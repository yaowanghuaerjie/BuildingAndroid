package com.george.rxjava.demo16;

import android.util.Log;

public class Translation {

    /**
     * status : 1
     * content : {"from":"en","to":"zh","vendor":"ciba","out":" 你好，世界","ciba_use":"以上结果来自机器翻译。","ciba_out":"","err_no":0}
     */

    private int status;
    private ContentBean content;

    public int getStatus() {
        return status;
    }

    public ContentBean getContent() {
        return content;
    }

    public void show() {
        Log.d("RxJavaRetrift", content.out);
    }

    public static class ContentBean {
        /**
         * from : en
         * to : zh
         * vendor : ciba
         * out :  你好，世界
         * ciba_use : 以上结果来自机器翻译。
         * ciba_out :
         * err_no : 0
         */

        private String from;
        private String to;
        private String vendor;
        private String out;
        private String ciba_use;
        private String ciba_out;
        private int err_no;

        public String getFrom() {
            return from;
        }

        public String getTo() {
            return to;
        }

        public String getVendor() {
            return vendor;
        }

        public String getOut() {
            return out;
        }

        public String getCiba_use() {
            return ciba_use;
        }

        public String getCiba_out() {
            return ciba_out;
        }

        public int getErr_no() {
            return err_no;
        }
    }
}
