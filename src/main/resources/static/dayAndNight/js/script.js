const $ = s => {
  let dom = document.querySelectorAll(s)
  return dom.length == 1 ? dom[0] : dom
}
let mainButton = $(".main-button"); //获取按钮主体
let daytimeBackgrond = $(".daytime-backgrond"); //获取按钮背后的虚影
let cloud = $(".cloud"); //获取云层
let cloudLight = $(".cloud-light"); //获取云层虚影
let components = $(".components"); //获取最外层元素
let moon = $(".moon"); //获取陨石坑
let stars = $(".stars"); //获取所有星星
let isMoved = false; //按钮状态，判断是否白天黑夜,默认为白天
let isClicked = false; // 新变量，用于跟踪是否刚刚发生了鼠标点击事件

mainButton.addEventListener("click", function() {
  if (isMoved) {
    //白天按钮样式
    mainButton.style.transform = "translateX(0)"; //水平平移距离为0px
    mainButton.style.backgroundColor = "rgba(255, 195, 35,1)"; //按钮主体的背景颜色变为黄色(太阳)
    // 盒子阴影
    mainButton.style.boxShadow = "3px 3px 5px rgba(0, 0, 0, 0.5), inset  -3px -5px 3px -3px rgba(0, 0, 0, 0.5), inset  4px 5px 2px -2px rgba(255, 230, 80,1)";
    //云朵上升-云朵显示
    daytimeBackgrond[0].style.transform = "translateX(0)";
    daytimeBackgrond[1].style.transform = "translateX(0)";
    daytimeBackgrond[2].style.transform = "translateX(0)";
    cloud.style.transform = "translateY(10px)";
    cloudLight.style.transform = "translateY(10px)";
    components.style.backgroundColor = "rgba(70, 133, 192,1)"
    //月亮陨石坑完全透明-隐藏
    moon[0].style.opacity = "0";
    moon[1].style.opacity = "0";
    moon[2].style.opacity = "0";
    //星星上升-星星隐藏
    stars.style.transform = "translateY(-125px)";
    stars.style.opacity = "0";
    //网页背景颜色变为浅色
    document.body.style.backgroundColor = "aliceblue";
  } else {
    //黑夜按钮样式
    mainButton.style.transform = "translateX(110px)"; //水平平移距离为110px
    mainButton.style.backgroundColor = "rgba(195, 200,210,1)"; //按钮主体的背景颜色变为黄色(月亮)
    // 盒子阴影
    mainButton.style.boxShadow = "3px 3px 5px rgba(0, 0, 0, 0.5), inset  -3px -5px 3px -3px rgba(0, 0, 0, 0.5), inset  4px 5px 2px -2px rgba(255, 255, 210,1)";
    //云朵下降-云朵隐藏
    daytimeBackgrond[0].style.transform = "translateX(110px)";
    daytimeBackgrond[1].style.transform = "translateX(80px)";
    daytimeBackgrond[2].style.transform = "translateX(50px)";
    cloud.style.transform = "translateY(80px)";
    cloudLight.style.transform = "translateY(80px)";
    components.style.backgroundColor = "rgba(25,30,50,1)"
    //月亮陨石坑完全不透明-显示
    moon[0].style.opacity = "1";
    moon[1].style.opacity = "1";
    moon[2].style.opacity = "1";
    //星星下降-星星显示
    stars.style.transform = "translateY(-62.5px)";
    stars.style.opacity = "1";
    //网页背景颜色变为深色
    document.body.style.backgroundColor = "#424242";
  }
  // 检测鼠标是否点击,默认已经点击
  isClicked = true;
  // 计时器，当0.5秒后，点击状态变成非点击
  setTimeout(function() {
    isClicked = false;
  }, 500);
  isMoved = !isMoved;
});

// 当鼠标挪入按钮时，按钮移动事件
mainButton.addEventListener("mousemove", function() {
  // 当按钮为点击状态时，退出
  if (isClicked) return;

  if (isMoved) {
    // 当黑夜状态时，鼠标挪入按钮
    // 按钮和背后的虚影向左平移10像素 
    mainButton.style.transform = "translateX(100px)";
    daytimeBackgrond[0].style.transform = "translateX(100px)";
    daytimeBackgrond[1].style.transform = "translateX(73px)";
    daytimeBackgrond[2].style.transform = "translateX(46px)";
  } else {
    // 当白天状态时，鼠标挪入按钮
    // 按钮和背后的虚影向右平移10像素 
    mainButton.style.transform = "translateX(10px)";
    daytimeBackgrond[0].style.transform = "translateX(10px)";
    daytimeBackgrond[1].style.transform = "translateX(7px)";
    daytimeBackgrond[2].style.transform = "translateX(4px)";
  }
});

// 当鼠标挪出按钮时，按钮移动事件
mainButton.addEventListener("mouseout", function() {
  // 当按钮为点击状态时，退出
  if (isClicked) { return };
  if (isMoved) {
    // 当黑夜状态时，鼠标挪出按钮
    // 按钮和背后的虚影向右平移10像素 
    mainButton.style.transform = "translateX(110px)";
    daytimeBackgrond[0].style.transform = "translateX(110px)";
    daytimeBackgrond[1].style.transform = "translateX(80px)";
    daytimeBackgrond[2].style.transform = "translateX(50px)";
  } else {
    // 当白天状态时，鼠标挪出按钮
    // 按钮和背后的虚影向左平移10像素 
    mainButton.style.transform = "translateX(0px)";
    daytimeBackgrond[0].style.transform = "translateX(0px)";
    daytimeBackgrond[1].style.transform = "translateX(0px)";
    daytimeBackgrond[2].style.transform = "translateX(0px)";
  }
});

// 星星闪烁js交互部分
// 获取所有星星元素，并随机排序星星数组
let starArray = [...$('.star')].sort(_ => 0.5 - Math.random());

// 定义缩放动画时长和暂停时间
const twinkleDuration = 0.5; // 缩放动画时长（秒）
const pauseDuration = 2; // 暂停时间（秒）

function twinkleStars() {
  starArray.forEach((star, index) => {
    setTimeout(() => {
      star.classList.add('twinkle');
      setTimeout(() => {
        star.classList.remove('twinkle');
        if (index === starArray.length - 1) {
          setTimeout(twinkleStars, pauseDuration * 1000); // 在每次调用之间添加 2 秒的间隔
        }
      }, twinkleDuration * 1000);
    }, (index * (twinkleDuration + pauseDuration)) * 1000);
  });
}

twinkleStars(); // 第一次调用函数开始闪烁

// 云层浮动动画效果
// 定义一个获取随机方向的函数，随机选择移动距离'2px'或'-2px'
const getRandomDirection = () => {
  const directions = ['2px', '-2px'];
  return directions[Math.floor(Math.random() * directions.length)];
}

// 定义一个将元素移动到随机方向的函数
const moveElementRandomly = (element) => {
  const randomDirectionX = getRandomDirection(); // 获取随机的X方向
  const randomDirectionY = getRandomDirection(); // 获取随机的Y方向
  element.style.transform = `translate(${randomDirectionX}, ${randomDirectionY})`; // 将随机方向应用到元素的transform属性
}

// 在文档加载完成后执行以下代码
document.addEventListener('DOMContentLoaded', () => {
  const cloudSons = document.querySelectorAll('.cloud-son'); // 选择所有的.cloud-son元素
  
  // 每秒钟执行一次以下代码
  setInterval(() => {
    cloudSons.forEach(moveElementRandomly); // 将每一个.cloud-son元素移动到随机方向
  }, 1000);
});






