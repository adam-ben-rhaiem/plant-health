// import VanillaRouter from "./router.js";

// const router = new VanillaRouter({
//     type : "history" ,
//     routes : {
//         "/" : "home" ,
//         "/about" : "about"
//     }

// }).listen().on("route" , async e => {
//   console.log(e.detail.route, e.detail.url);
//   console.log("%c "+e.detail.url.search,"color:red;fontWeight:bold");
//   const mainContent = document.getElementById('mainContent');
//   mainContent.innerHTML = await fetch('pages/' + e.detail.route + '.html').then(response=>response.text())
//   document.title = mainContent.getElementsByTagName('title')[0].innerHTML;
// })
