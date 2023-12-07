
function logout() {
    if(confirm("请确认是否要注销！")) {
        jQuery.ajax({
            url: "/user/logout",
            type: "POST",
            data:{},
            success:function(res) {
                if(res.code == 200 && res.data == 1) {
                    // 注销成功
                    location.href = "/blog_list.html";
                }else {
                    alert("操作失败，请重试！" + res.msg);
                }
            }
        });

    }
}