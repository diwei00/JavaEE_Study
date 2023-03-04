let arr = [1, 'hello', true, 5];
arr[0] = 8;
//扩容。多余的元素为undefined
arr.length = 6;
//扩容，直接填充这个位置元素，且扩容数组到这么大
arr[50] = 9;
//数组末尾新增元素（尾插）
arr.push(999);
//js中的数组不是传统意义上的数组，而是带有键值对的数组  
arr[-1] = 10;
arr['hello'] = 20;
console.log(arr);
let arr2 = new Array();

for (let i = 0; i < arr.length; i++) {
    console.log(arr[i]);
}
for(let i in arr) {
    //此处i是数组下标
    console.log(arr[i]);
}
for(let elem of arr) {
    //此处elem是数组元素
    console.log(elem);
}
//数组越界显示undefined
console.log(arr[8]);
console.log(arr[-1]);
console.log(arr[9]);

let arr3 = [1, 2, 3, 4, 5, 6];
//从4下标开始，删除后面两个元素，第三个变长参数，替换前面的区间
//（index, length, 变长参数）
//没有变长参数，就是删除这个区间的元素
//变长参数长度和前面区间长度一样，就是修改这个区间元素
//变长参数长度大于区间长度，就是新增
//变长参数长度小于前面区间长度，填充完，把多余的区间删除
arr3.splice(4, 2, 8);
console.log(arr3);