//由于js为动态类型语言，不需要写类型，用function来创建函数
//传入实参个数可以和形参个数不匹配
//如果实参个数小于形参个数，多出来形参就是undefined
//函数中会自动创建一个arguments数组，保存所有实参
function fun(a, b) {
    return a + b;
}
function add() {
    let tmp = 0;
    for(let elem of arguments) {
        tmp += elem;
    }
    return tmp;
}
//匿名函数赋值给一个变量
//这个变量的类型就是function
//js中可以像普通变量一样，把函数赋值给一个变量
//同时也可以把函数作为另一个函数的参数，或者把函数作为另一个函数的返回值
//函数在js中为“一等公民”
let ret = function() {
    let tmp = 0;
    for(let elem of arguments) {
        tmp += elem;
    }
    return tmp;
}
function two() {
    console.log('执行two');
    return 100;
}
function one() {
    //返回two这个函数
    return two;
}
//函数变量接收这个函数
let n = one();
//调用这个函数
n();
console.log(typeof(ret));
console.log(add(10));
console.log(add(10, 20));
console.log(add(10, 20, 30));


let tmp = fun('10');
console.log(tmp);

//js中变量的作用域
//js首先会找当前作用域，如果当前没有，就往上层作用域找。
//一直往上到全局作用域，如果还找不到报错/undefined
let a = 10;
function test() {
    //let a = 20;
    function test2() {
        //let a = 30;
        console.log(a);
    }
    test2();
    console.log(a);
}
test();
console.log(a);