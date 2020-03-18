/* VARIABLES GLOBALES */
var FrontEnd = null;
var notif = $("[name=notification]").empty();
notif.css("color", "#b91e1e");$


function retirerTousLesEvenements(){
    $('#pageDAccueil').find("*").off();
    $('#pageAjoutPartenaire').find("*").off();
    $('#pageListeEtudiants').find("*").off();
    $('#pageDeConnexion').find("*").off();
    $('#pageMobilite').find("*").off();
    $('#pageMobiliteProf').find("*").off();
    $('#pagePartenaire').find("*").off();
    $('#pageProfilUtilisateur').find("*").off();
    $('#pageInscription').find("*").off();
    $('#pageDocuments').find("*").off();
}

function retirerToutInfoDunUtilisateurConnecte(){
    $('form').trigger("reset");
    $('[name=form-partenaire] input').attr('value','');
    $('tbody').empty();
    //$('[name=redirectionAjoutPartenaire]').off();
    $('[name=userName]').text("");
    $('[name=notification]').text("");
    sensTriChampsEtudiants = null;
    sensTriChampsMobilites = null;
}

function chargementScripts(){
    // dans l'odre de chargement du footer.html
    chargerScriptMainScript();
    chargerScriptUser();
    chargerScriptLogin();
    chargerScriptRegister();
    // tous les élements deaccueil ont été déplacés dans mainscript donc plus aucun chargement nécaissaire
    //chargerScriptAccueil();
    chargerScriptProfil();
    //chargerScriptMobilite();
    chargerScriptMobiliteProf();
    chargerScriptPartenaire();
    chargerScriptListeEtudiants();
    chargerScriptAjoutPartenaire();
    chargerScriptVueDocuments();
}

function chargementScriptsConnexion(){
    chargerScriptMainScript();
    chargerScriptUser();
    chargerScriptLogin();
    chargerScriptRegister();
}

function displayPage(cible) {
    // chercher un display:block
    var long = $('div[id^=page]').length;
    for (var i = 0; i < long; i++) {
        $($('div[id^=page]')[i]).css('display', 'none');
    }
    if (FrontEnd == null) {
        FrontEnd = cible;
        FrontEnd.css('display', 'block');
    }
    else {
        //FrontEnd.css('display', 'none');
        FrontEnd = cible;
        FrontEnd.css('display', 'block');
    }
}

function setDernierePage(cible){
   $.ajax({
        url: '/',
        type: 'POST',
        data: { action: "setDernierePage", currentPage: cible},
        success: function(reponse){     
        },
        error: function (e) {
            // en cas d'erreur
            console.log(e.message);
        }
    }); 
}

function form2Json(form){
    var o = {};
    let error = false;
    var inputs = $(form).find('input');
    inputs.each(function (i, el) {
        el = $(el);
        o[el.attr('name')] = el.val();
    });
    inputs = form.find('input[type=radio]:checked');
    inputs.each(function (i, el) {
        el = $(el);
        o[el.attr('name')] = el.val();
    });
    inputs = form.find('input[type=checkbox]');
    inputs.each(function (i, el) {
        el = $(el);
        o[el.attr('name')] = el.prop('checked');
    });
    inputs = form.find('select');
    inputs.each(function (i, el) {
        el = $(el);
        o[el.attr('name')] = el.find('option:selected').val();
    });
    if(error){
        return false;
    }
    return JSON.stringify(o);
}

function JSONToForm(json, form){
    //ForEach : pour chaque "clé" dans mon tableau "jsonContent"
    for (var key in json) {
        var value = json[key];
        //Trouver l'élément (composant) dont le "name" est "key"
        // el = 2 car deja vu 2 fois

        var el = form.find('[name="' + key + '"]');
        //console.info(el);
        if (el.is('input')) {
            switch (el.attr('type')) {
                case "text":
                    el.val(value);
                    break;
                case "password":
                    el.val(value);
                    break;
                case "number":
                    el.val(value);
                    break;
                case "date":
                    el.val(value);
                    break;
                case "radio":
                    // radio ont le meme attribut name
                    for (var i = 0; i < el.length; i++) {
                        if (el[i].value == value) {
                            el[i].checked = true;
                        }
                        else {
                            el[i].checked = false;
                        }
                    }
                    break;
                case "checkbox":
                    el.prop('checked', value);
                    break;
            }

        }
        else if(el.is('select')){
            //Identifier le type de select : Multiple ou simple
            if(el.is('[multiple]')){
                if(value.length === 0){
                    el.val(null);
                }
                else{
                    el.val(value);
                }
            }
            else{
                if (value === null) {
                    el.val("");
                }
                else{
                    el.val(value);
                }
            }
        }
        else if(el.is('textarea')){
            el.val(value);
        }
    }
}

function waitSpinner(){
    $('html').css('cursor','none');
    var spinner = $('<img src="images/waitSpinner.svg" id="waitSpinner" hidden>');   
    $('body').append(spinner);
    $('#waitSpinner').removeAttr('hidden');
    $('#waitSpinner').css('position','absolute');
    $('#waitSpinner').css('height','75px');
    $('#waitSpinner').css('width','75px');
    $('#waitSpinner').css({left:'42%', top:'30%'});
    
    $('html').mousemove(function(e){
        //$('#waitSpinner').removeAttr('hidden');
        $('#waitSpinner').css('position','absolute');
        $('#waitSpinner').css('height','75px');
        $('#waitSpinner').css('width','75px');
        $('#waitSpinner').css({left:e.pageX-28, top:e.pageY-50});
    }); 
}

function opaciteDeCouleur(codeCouleur){
    var color = codeCouleur;
    var rgbaCol = 'rgba(' + parseInt(color.slice(-6,-4),16)
        + ',' + parseInt(color.slice(-4,-2),16)
        + ',' + parseInt(color.slice(-2),16)
        +',0.5)';
    return rgbaCol;
}

function erreurChamps(message, page){
    $('[name=notification]').empty();
    var mauvaisChampAlert = $('<div class="alert alert-warning" role="alert"></div>');
    mauvaisChampAlert.text(message);
    $('[name=notification]').append(mauvaisChampAlert);
}

function sleep(milliseconds){
    var start = new Date().getTime();
    for(var i = 0; i < 1e7; i++){
        if((new Date().getTime() - start) > milliseconds){
            break;
        }
    }
}

function chargerScriptMainScript(){
    $(function(){
        $(".smartSearch").autocomplete({
            source: function(request, response){
                $.ajax({ // Aller récupérer les infos dans la base de données
                    url: '/',
                    type: 'POST',
                    data: { action: "recupererInfosAutocomplete", term : request.term, role : user.getRole()},
                    dataType : "json",
                    success: function(data){ 
                    	console.log("samrt");
                        response(data);
                    }
                });
            },
            minLength: 1,//search after two characters
            select: function(event, ui){
                console.log(ui);
            },
        });
    });

    $(".smartSearchButton").on("click", (event)=>{
        var recherche = $(event.currentTarget).parent().parent().children("input:first").val();
        var sorte = recherche.split("[")[1];
        sorte = sorte.substring(0,sorte.length-1);
        switch(sorte){
        case "étudiant":
        case "professeur":
            var pseudo = recherche.split("#")[1].split(" ")[0];
            console.log(pseudo);
            resetChargementProfilUtilisateur();
            chargerProfilUtilisateur(pseudo);  
            displayPage($('#pageProfilUtilisateur'));
            break;
        case "partenaire":
            displayPage($('#pagePartenaire'));
            var nom = recherche.split("pays")[0].replace(/\s/g,'');
            $('html,body').animate({scrollTop: $("#"+nom).parent().parent().offset().top}, 'slow');
            for(let i=0; i<3; i++){
                $("#"+nom).fadeOut(400).fadeIn(400); 
            }
            break;
        case "mobilité":
            displayPage($('#pageMobiliteProf'));
            var tab = recherche.split(" ");
            var search = tab[0] + " " +tab[1] + " " + tab[2] + " " + tab[4].split("°")[1] + " " + tab[7];
            console.log(search);
            $('#tableMobiliteProf').DataTable().search(search).draw();
            break;
        default:
            console.log("What the switch");
            break;
        }
        $("div[class=ui-helper-hidden-accessible]").remove();
    	$(".smartSearch").empty();
    });
}

//------------------------------------------------------------------------------

chargerScriptMainScript();