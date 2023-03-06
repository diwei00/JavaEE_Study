//js中的对象，也存在属性。（js不是面向对象语言）
//方法本质上也是属性
let student = {
    name: '吴浩',
    age: 22,
    sex: '男',
    sing: function() {
        console.log('哈哈哈哈');
    },
    dance: function() {
        console.log('哈哈');
    }
};
//访问属性
console.log(student.age);
student.sing();

//js中所有的对象都可以用object表示
//js中属性和方法都是可以随时添加的
let people = new Object();
people.name = '吴浩';
people.age = 22;
people.sex = '男';
people.sing = function() {
    console.log('哈哈哈哈哈');
}
people.age = 55;



console.log(people.name);
console.log(people.age);
people.sing();
people.dance = function() {
    console.log('哈哈哈');
}
people.dance();

while(true) {
    console.log("我好喜欢瑶瑶");
}