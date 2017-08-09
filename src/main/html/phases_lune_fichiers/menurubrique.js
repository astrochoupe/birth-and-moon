$("#menu").ready( function () {
    
	// On cache les sous-menus
    // sauf celui qui porte la classe "open_at_load" :
    $(".navigation ul.subMenu:not('.open_at_load')").hide();

	// On modifie l'évènement "click" sur les liens dans les items de liste
    // qui portent la classe "toggleSubMenu" :
    $("#menu .navigation li.toggleSubMenu > a").click( function () {


 
    
        // Si le sous-menu était déjà ouvert, on le referme :
        if ($(this).next("ul.subMenu:visible").length !== 0) {
            $(this).next("ul.subMenu").slideUp("normal", function () { $(this).parent().removeClass("open") });

        }
        // Si le sous-menu est caché, on ferme les autres et on l'affiche :
        else {
            $(".navigation ul.subMenu").slideUp("normal", function () { $(this).parent().removeClass("open") });
            $(this).next("ul.subMenu").slideDown("normal", function () { $(this).parent().addClass("open") });
        }
        // On empêche le navigateur de suivre le lien :
        return false;
    });
} ) ;

$("#nouvellesastro").ready( function () {
    
	// On cache les sous-menus
    // sauf celui qui porte la classe "open_at_load" :
    $("#nouvellesastro .navigation div.subMenu:not('.open_at_load')").hide();
	image = document.getElementById('imagenews');
    // On modifie l'évènement "click" sur les liens dans les items de liste
    // qui portent la classe "toggleSubMenu" :
    $("#nouvellesastro .navigation li.toggleSubMenu > a").click( function () {
        // Si le sous-menu était déjà ouvert, on le referme :
        if ($(this).next("div.subMenu:visible").length !== 0) {
            $(this).next("div.subMenu").slideUp("normal", function () { $(this).parent().removeClass("open") });
			image.src = image.src.substr(0, image.src.indexOf('_')) + '_up.png';
        }
        // Si le sous-menu est caché, on ferme les autres et on l'affiche :
        else {
            //$(".navigation ul.subMenu").slideUp("normal", function () { $(this).parent().removeClass("open") });
            $(this).next("div.subMenu").slideDown("normal", function () { $(this).parent().addClass("open") });
			image.src = image.src.substr(0, image.src.indexOf('_')) + '_down.png';
        }
        // On empêche le navigateur de suivre le lien :
        return false;
    });
} ) ;


/*****************************************************************************************************/
//fonction ajax pour modifier le contenupage et afficher le sommaire de la partie quand on clicke sur le titre//////
//CETTE FONCTION N'EST PLUS UTILISE////////!\\//!\\//!\\
/****************************************************************************************************/

function contenu(lien){

var req = null; 

		document.getElementById("contenupage").innerHTML ="Started...";
 
		if (window.XMLHttpRequest)
		{
 			req = new XMLHttpRequest();
		} 
		else if (window.ActiveXObject) 
		{
			try {
				req = new ActiveXObject("Msxml2.XMLHTTP");
			} catch (e)
			{
				try {
					req = new ActiveXObject("Microsoft.XMLHTTP");
				} catch (e) {}
			}
        	}


		req.onreadystatechange = function()
		{ 
			document.getElementById("contenupage").innerHTML ="Wait server...";
			if(req.readyState == 4)
			{
				if(req.status == 200)
				{
					document.getElementById("contenupage").innerHTML = req.responseText;	               
				}	

			} 
		}; 
		req.open("GET.html", lien, true); 
		req.send(null); 
}    

/**********************************************************************/
/* ZONE NOUVELLES ASTRONOMIQUES                                                                */
/**********************************************************************/
var newsastro = {
	timeout : null,
	changeImage : function() {
		clearTimeout(this.timeout);
		image = document.getElementById('imagenews');
		if($('newsastro').style.display == 'none'){
				image.src = image.src.substr(0, image.src.indexOf('_')) + '_down.png';
				this.timeout = setTimeout(function(){new Effect.BlindDown('newsastro', {duration:.3, fps:40})},400);
			}
			else {
				image.src = image.src.substr(0, image.src.indexOf('_')) + '_up.png';
				this.timeout = setTimeout(function(){new Effect.BlindUp('newsastro', {duration:.3, fps:40})},300);
			}
		}
}
