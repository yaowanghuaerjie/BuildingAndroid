// URL模板
http://fy.iciba.com/ajax.php

// URL实例
http://fy.iciba.com/ajax.php?a=fy&f=auto&t=auto&w=hello%20world


// 参数说明：
// a：固定值 fy
// f：原文内容类型，日语取 ja，中文取 zh，英语取 en，韩语取 ko，德语取 de，西班牙语取 es，法语取 fr，自动则取 auto
// t：译文内容类型，日语取 ja，中文取 zh，英语取 en，韩语取 ko，德语取 de，西班牙语取 es，法语取 fr，自动则取 auto
// w：查询内容

//返回值
{
    "status": 1,
    "content": {
        "from": "en",
        "to": "zh",
        "vendor": "ciba",
        "out": " 你好，世界",
        "ciba_use": "以上结果来自机器翻译。",
        "ciba_out": "",
        "err_no": 0
    }
}


