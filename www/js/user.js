var user;
var idMobToDelete;
const createPerson = function(){
    let pseudo, name, surname, role;
    function setPseudo(a){ pseudo = a; }
    function setName(a){ name = a; }
    function setSurname(a){ surname = a; }
    function setRole(a){ role = a; }
    const self = {
        getPseudo(){ return pseudo; },
        getName(){ return name; },
        getSurname(){ return surname; },
        getRole(){ return role; },
        setPseudo,
        setName,
        setSurname,
        setRole
    }
    return self;
};

function remplissageInfoUser(infoUser){
    user.setName(infoUser.nom);
    user.setSurname(infoUser.prenom);
    user.setRole(infoUser.role);
    user.setPseudo(infoUser.pseudo);
    let name = user.getSurname() + " " + user.getName();
    $('span[name=userName]').text(name);
}

function cacherPourEtudiant(){
    $('[name=redirectionListeEtudiants]').css('display','none');
    setDernierePage("pageMobilite");
    displayPage($('#pageMobilite'));
}

function creerUser() {
    this.user = createPerson();
    $.ajax({
        url: '/',
        type: 'POST',
        async:false,
        data: {action: "getJsonUser"},
        success: function (reponse) {
            let obj = JSON.parse(reponse);
            remplissageInfoUser(obj);
            if(user.getRole() == 'E'){
                cacherPourEtudiant();   
            }
            else{
                setDernierePage("pageListeEtudiants");
                displayPage($('#pageListeEtudiants'));
            }
        },
        error: function (e) {
            // en cas d'erreur
            console.log(e.message);
        }
    });
}

function chargerScriptUser(){
    $.ajax({
        url: '/',
        type: 'POST',
        data: { action: "verification" },
        beforeSend: function(){
            waitSpinner();
        },
        success: function (reponse) {
            $.ajax({
                url: '/',
                async:false,
                type: 'POST',
                data: { action: "getDernierePage" },
                success: function(reponse){
                    if(reponse == "null"){
                        creerUser();
                    }
                    else{
                        creerUser();
                        displayPage($('#'+reponse));
                    }
                },
                error: function (e) {
                    console.log(e);
                }
            });
        },
        error: function (e) {
            retirerToutInfoDunUtilisateurConnecte();
            retirerTousLesEvenements();
            chargerScriptMainScript();
            chargerScriptLogin();
            chargerScriptRegister();
            displayPage($('#pageDeConnexion'));
        },
        complete: function(){
            $('html').css('cursor','default');
            $('#waitSpinner').remove();
        }
    });
    //pour le futur single page d'office quand log et verifier dans les cookies si undifined -> creer user aussi
    // balises communes à plus d'une page
    $('[name=boutonDeconnexion]').mouseover(function(){ $(this).css("background", "#E0E0E0") });
    $('[name=boutonDeconnexion]').mouseout(function(){ $(this).css("background", "none") });
    $('[name=boutonDeconnexion]').on('click', function(){
        $.ajax({
            url: '/',
            type: 'POST',
            data: { action: "deconnexion" },
            success: function (reponse) {
                setDernierePage("deco");
                //location.reload();
                retirerToutInfoDunUtilisateurConnecte();
                retirerTousLesEvenements();
                //chargementScripts();
                chargementScriptsConnexion();
            },
            error: function (e) {
                // en cas d'erreur
                console.log(e.message);
            }
        });
    });

    $('[name=redirectionMobilites]').on('click', function(){
        chargerPage();
        if(user.getRole()==='E'){
            setDernierePage("pageMobilite");
            displayPage($('#pageMobilite'));
        }else{
            setDernierePage("pageMobiliteProf");
            displayPage($('#pageMobiliteProf'));
        }
    });

    $('[name=redirectionModifUtilisateur]').on('click', function(){
        //$('#pageProfilUtilisateur [name=notification]').empty();
        //chargerProfilUtilisateur();
        /*
        if($('[name=afficherSectionMdp]').attr('hidden') == "hidden"){
            $('[name=afficherSectionMdp]').removeAttr('hidden');
            $('[name=sectionMdp]').attr('hidden','true');
        }
        */
        resetChargementProfilUtilisateur();
        chargerProfilUtilisateur();
        setDernierePage("pageProfilUtilisateur");
        displayPage($('#pageProfilUtilisateur'));
    });

    $('[name=redirectionPartenaire]').on('click', function(){
        setDernierePage("pagePartenaire");
        displayPage($('#pagePartenaire'));
    });

    $('[name=redirectionAccueil]').on('click', function(){
        setDernierePage("pageDAccueil");
        displayPage($('#pageDAccueil'));
    });

    $('[name=redirectionListeEtudiants]').on('click', function(){
        resetChargementListeEtudiants();
        chargementListeEtudiants();
        setDernierePage("pageListeEtudiants");
        displayPage($('#pageListeEtudiants'));
    });
}

//------------------------------------------------------------------------------

chargerScriptUser();