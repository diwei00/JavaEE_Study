function isDateFormat(dateStr) {
    var count = 0;
    for(var i = 0; i < dateStr.length; i++) {
        if(dateStr[i] == "-") {
            count++;
        }
    }
    if(count != 4) {
        return false;
    }
    return true;
}