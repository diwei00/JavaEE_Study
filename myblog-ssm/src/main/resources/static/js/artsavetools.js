// 保存草稿，state = 0
function saveDraftByAdd() {
    // 非空校验
    var title = jQuery("#title");
    if (title.val().trim() == "") {
        alert("请先输入标题！");
        title.focus();
        return false;
    }
    // 获取编辑器里面内容
    var content = editor.getValue();
    if (content == "") {
        alert("请先输入正文！");
        return false;
    }
    // 提交数据
    jQuery.ajax({
        url: "/art/save_draft",
        type: "POST",
        data: {
            "title": title.val().trim(),
            "content": content,
            "state": 0
        },
        success:function (res) {
            if(res.code == 200 && res.data == 1) {
                location.href = "myblog_list.html";
            }else {
                alert("操作失败！" + res.msg);
            }
        }
    });

}

function saveDraftByUpdate() {
    // 非空校验
    var title = jQuery("#title");
    if (title.val().trim() == "") {
        alert("请先输入标题！");
        title.focus();
        return false;
    }
    // 获取编辑器里面内容
    var content = editor.getValue();
    if (content == "") {
        alert("请先输入正文！");
        return false;
    }
    var id = getParamByKey("id");
    // 提交数据
    jQuery.ajax({
        url: "/art/update",
        type: "POST",
        data: {
            "id": id,
            "title": title.val().trim(),
            "content": content,
            "state": 0
        },
        success:function (res) {
            if(res.code == 200 && res.data == 1) {
                location.href = "myblog_list.html";
            }else {
                alert("操作失败！" + res.msg);
            }
        }
    });

}