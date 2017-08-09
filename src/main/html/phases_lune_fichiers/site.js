//  Fonctions javascript a associer aux pages du site www.imcce.fr

//*******************************************************//
//*********Nouvelles fonctions basées sur Jquery*********//
//********* Création: Alrick Dias (dias@imcce.fr) *******//
//********* Last edit : 24/03/2011 **********************//
//*******************************************************//

//une fois la page loadée on peut éxecuté les fonction JS
$(function() {
		/**init**/
		//on masque les div infosderoul au chargement de la page
		$(".infosderoul").hide();


	//ensemble de fonction gérant l'affichage de plus d'infos (ex: publication/articles et comunication)
	//le lien doit etre de la forme : <a class="deroul" href="">texte <img class="arrow" src="image"></a>
	// on peut utilisé <h1> au lieu de <a>
	// le contenu a afficher doit ce trouver juste après le lien dans un <div class="infosderoul"></div>
	
	/* change arrow en cliquant sur le h1 */
	$(".deroul").toggle(function(){
		$(this).find(".arrow:first").attr("src","../../public/images/site/arrow_up.png");
	},function(){
		$(this).find(".arrow:first").attr("src","../../public/images/site/arrow_down.png");
	});
	/* En clikant sur la fleche*/
	$(".arrow").toggle(function(){
		$(this).attr("src","../../public/images/site/arrow_up.png");
	},function(){
		$(this).attr("src","../../public/images/site/arrow_down.png");
	});
	
	/* deroule les infos liées au h1 */
	/* au clik sur le h1 */
	$(".deroul").toggle(function(){
		$(this).next(".infosderoul").animate({height: 'show', opacity: 'show'});
	},function(){
		$(this).next(".infosderoul").animate({height: 'hide', opacity: 'hide'});
	});
	
	/* au clik sur la fleche */
	$(".arrow").toggle(function(){
		$(this).parents(".deroul").next(".infosderoul").animate({height: 'show', opacity: 'show'});
	},function(){
		$(this).parents(".deroul").next(".infosderoul").animate({height: 'hide', opacity: 'hide'});
	});
	
	// mise en surbrillance d'élément au survol
	// classe de l'objet à traiter .liste_elem
	$(".liste_elem").hover(function(){
		$(this).addClass("surbrillance");
		},function(){
		$(this).removeClass("surbrillance");
	});

});




//*********Ancienne version : certaines fonctions servent encore mais d'autre doivent etre obsolète***********//
var ns4=document.layers
var ie4=document.all
var ns6=document.getElementById&&!document.all

function topFrame (bando)
{
  if (! bando) {
    bando = '../../fr/nav/top-eclipse.html';
  }
  top.haut.location = bando;  
}

function gone()
{
  location=document.menu.liens.options[document.menu.liens.selectedIndex].value
}

function gone2()
{
  location=document.menu.liens2.options[document.menu.liens2.selectedIndex].value
}

function Affiche (num) {
 if (ie4) { // Internet explorer
    eval('menu'+ num).style.visibility = 'visible';
  }
  else if(ns4) { // Netscape 4.x
    document.eval('menu'+ num).visibility = 'visible';
    
  }
  else if(ns6) { // Netscape 6 (mozilla)
    var divns6 = document.getElementsByTagName("div")
    divns6['menu'+ num].style.visibility = 'visible';
  }
}

function Cache(num) {
  if(ie4) { // Internet explorer
    eval('menu'+ num).style.visibility = 'hidden';
  }
  else if(ns4) { // Netscape 4.x
    document.eval('menu'+ num).visibility = 'hidden';
   }
  else if(ns6) { // Netscape 6 (mozilla)
    var divns6 = document.getElementsByTagName("div")
    divns6['menu'+ num].style.visibility = 'hidden';
   }
}

function Affiche2(name) {

 if(ie4) { // Internet explorer
    eval(name).style.visibility = 'visible';
  }
  else if(ns4) { // Netscape 4.x
    document.eval(name).visibility = 'visible';
  }
  else if(ns6) { // Netscape 6 (mozilla)
    var divns6 = document.getElementsByTagName("div")
    divns6[name].style.visibility = 'visible';
	}
}

function Cache2(name) {
  if(ie4) { // Internet explorer
    eval(name).style.visibility = 'hidden';
  }
  else if(ns4) { // Netscape 4.x
    document.eval(name).visibility = 'hidden';
   }
  else if(ns6) { // Netscape 6 (mozilla)
    var divns6 = document.getElementsByTagName("div")
    divns6[name].style.visibility = 'hidden';
   }
}

function MM_reloadPage(init) {  //reloads the window if Nav4 resized
  if (init==true) with (navigator) {if ((appName=="Netscape")&&(parseInt(appVersion)==4)) {
    document.MM_pgW=innerWidth; document.MM_pgH=innerHeight; onresize=MM_reloadPage; }}
  else if (innerWidth!=document.MM_pgW || innerHeight!=document.MM_pgH) location.reload();
}

function MM_swapImgRestore() { //v3.0
  var i,x,a=document.MM_sr; for(i=0;a&&i<a.length&&(x=a[i])&&x.oSrc;i++) x.src=x.oSrc;
}

function MM_preloadImages() { //v3.0
  var d=document; if(d.images){ if(!d.MM_p) d.MM_p=new Array();
    var i,j=d.MM_p.length,a=MM_preloadImages.arguments; for(i=0; i<a.length; i++)
    if (a[i].indexOf("#")!=0){ d.MM_p[j]=new Image; d.MM_p[j++].src=a[i];}}
}

function MM_findObj(n, d) { //v3.0
  var p,i,x;  if(!d) d=document; if((p=n.indexOf("?"))>0&&parent.frames.length) {
    d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}
  if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];
  for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=MM_findObj(n,d.layers[i].document); return x;
}

function MM_swapImage() { //v3.0
  var i,j=0,x,a=MM_swapImage.arguments; document.MM_sr=new Array; for(i=0;i<(a.length-2);i+=3)
   if ((x=MM_findObj(a[i]))!=null){document.MM_sr[j++]=x; if(!x.oSrc) x.oSrc=x.src; x.src=a[i+2];}
}

// **********************************************************************************************

function MM_openBrWindow(theURL,winName,features) { //v2.0
  window.open(theURL,winName,features);
}

function liste1() {
  MM_openBrWindow(document.menu1.liens1.options[document.menu1.liens1.selectedIndex].value, '', 'scrollbars=yes,resizable=yes,width=585,height=600')
  
}
function liste2() {
  MM_openBrWindow(document.menu2.liens2.options[document.menu2.liens2.selectedIndex].value, '_self', 'scrollbars=yes,width=585,height=600')
}

// **********************************************************************************************

var deja = false;
var NS = (navigator.appName == "Netscape");
var VERSION = parseInt(navigator.appVersion);

function printit()
{  
  if (NS) 
  {
    window.print() ;  
  } 
  else
  {
    var WebBrowser = '<OBJECT ID="WebBrowser1" WIDTH=0 HEIGHT=0 CLASSID="CLSID:8856F961-340A-11D0-A96B-00C04FD705A2"></OBJECT>';
    if (!deja)
      document.body.insertAdjacentHTML('beforeEnd', WebBrowser);
    WebBrowser1.ExecWB(6, 2); //Use a 1 vs. a 2 for a prompting dialog box    WebBrowser1.outerHTML = "";  
    deja = true;
  }
}

// **********************************************************************************************

function currentDate() {
   Todays = new Date();
   TheYear = Todays.getUTCFullYear() ;
   TheMonth = (Todays.getUTCMonth() + 1);
   TheDay = Todays.getUTCDate();
   TheHour = Todays.getUTCHours();
   TheMinu = Todays.getUTCMinutes();
   TheSec = Todays.getUTCSeconds();
   document.ok.an.value = TheYear;
   document.ok.mois.value = TheMonth;
   document.ok.jour.value = TheDay;
   document.ok.heure.value = TheHour;
   document.ok.minute.value = TheMinu;
   document.ok.seconde.value = TheSec;
}

function clearDate() {
   document.ok.an.value = "";
   document.ok.mois.value = "";
   document.ok.jour.value = "";
   document.ok.heure.value = "";
   document.ok.minute.value = "";
   document.ok.seconde.value = "";
}

// **********************************************************************************************

function custom_window(w,h) {

  window.resizeTo(w,h);
  window.menubar.visible=1;
  window.statusbar.visible=0;
  window.personalbar.visible=0;
  window.toolbar.visible=0;
  window.locationbar.visible=0;

}

// **********************************************************************************************
//                LAYER additions

//var vers = navigator.appVersion;
var nver = navigator.appVersion.substring(0,4);
var isNS = (navigator.appName.indexOf("Netscape") !=-1);
var x,y;
var laycolor = "#ffffe0";	// Default = LightYellow
// Change the layer color
function set_laycolor(color) { laycolor = color }
// Addition of text in a layer
function lay(text){
  var t = "<table border='1' cellpadding='3' cellspacing='0' bgcolor='" + 
       laycolor + "'>" + "<tr><td>";
  t = t + text.replace(/&apos;/g, "'");
  t = t + "</td></tr></table>";
  if ((isNS) && (nver<5)) {
    df = document.xplain;
    df.document.write(t);
    df.document.close();
    df.left=x-50;
    df.top =y+20; //document.xplain.top=y+10;
  } else {
    df = document.getElementById('xplain');
    df.innerHTML=t;
    df.style.background=laycolor; // "rgb(255,255,224)"
    df.style.left=x-50;
    df.style.top=y+20;
  }
}
function laz(){	// Out of layer
  if ((isNS) && (nver<5)) { document.xplain.top=-50; }
  else {
    df = document.getElementById('xplain');
    df.innerHTML="";
  }
}
function handlerMM(e){
  x = (isNS) ? e.pageX : event.clientX + document.body.scrollLeft;
  y = (isNS) ? e.pageY : event.clientY + document.body.scrollTop;
}
if (isNS&&(nver<5)){ document.captureEvents(Event.MOUSEMOVE); }
// Install the event handler
document.onmousemove = handlerMM;
if((isNS)&&(nver<5)) {
document.write("<LAYER name=\"xplain\" visibility=\"show\" left=\"25\" top=\"-50\" z-index=\"99\"><br></LAYER>") ;
} else {
document.write("<DIV ID=\"xplain\" style=\"position:absolute; visibility:visible; left:25px; top:-50px; z-index:2\"><br></DIV>") ;
}

// end -->



// **********************************************************************************************
// Fonction javascript anti-spam
function antispam(name) {
    document.write('<a href=\"mailto:' + name + '@imcce.fr\">' + '<font color=\"#3790FF\">' + name + '</a>');
}
function nospam(name) {
    window.location.href='mailto:'+name;
}

function Wnospam(name) {
    document.write('<font color=\"#3790FF\"> ' + name + '</font></a>');
}

