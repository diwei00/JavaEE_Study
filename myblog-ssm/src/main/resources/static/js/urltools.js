function getParamByKey(key) {
    // 得到url，包含后面的“？”
    var params = location.search;
    params = params.substring(1); // 去掉？
    var paramArr = params.split("&");
    if(paramArr != null && paramArr.length > 0) {
        for(var i = 0; i < paramArr.length; i++) {
            var item = paramArr[i];
            var itemArr = item.split("=");
            if(itemArr.length == 2 && itemArr[0] == key) {
                return itemArr[1]; // 返回key对应value
            }
        }
    }
    return null;
}