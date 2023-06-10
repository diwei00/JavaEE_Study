function isLogin() {
    jQuery.ajax({
        url: "/user/islogin",
        type: "POST",
        data: {},
        success:function(res) {
            if(res.code == 200 && res.data == 1) {
                // 用户登录
                jQuery("#userElement").show(); // 显示控件
                jQuery("#loginElement").hide(); // 隐藏控件
            }else {
                jQuery("#userElement").hide(); // 隐藏控件
                jQuery("#loginElement").show(); // 显示控件
            }
        }
    });
}